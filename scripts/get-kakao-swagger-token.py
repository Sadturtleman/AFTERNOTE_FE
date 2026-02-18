#!/usr/bin/env python3
"""
Get a backend JWT for Swagger by applying the Kakao token to POST /auth/social/login.

Flow:
1. Starts a local server on http://localhost:3000 to catch the OAuth redirect.
2. Opens the Kakao authorize URL in the default browser.
3. You log in with Kakao if prompted; redirect brings the code to this script.
4. Script exchanges the code for a Kakao access_token.
5. Applies that token to POST /auth/social/login with body:
   { "provider": "KAKAO", "accessToken": "<kakao_access_token>" }
6. Reads the response accessToken (and optionally refreshToken, newUser).
7. Prints the service JWT accessToken for pasting into Swagger Authorize.

API: POST /auth/social/login
  Request:  { "provider": "KAKAO", "accessToken": "..." }
  Response: { "status", "code", "message", "data": { "accessToken", "refreshToken", "newUser" } }

Usage:
  python3 scripts/get-kakao-swagger-token.py

Requires: Python 3 (stdlib only). Port 3000 must be free.
"""

import json
import urllib.parse
import urllib.request
import urllib.error
from http.server import HTTPServer, BaseHTTPRequestHandler
from threading import Thread
from typing import Tuple
import webbrowser

# OAuth / backend config (from your flow)
KAKAO_AUTHORIZE_URL = (
    "https://kauth.kakao.com/oauth/authorize"
    "?response_type=code"
    "&client_id=f631cb629a27146986a0337232138f16"
    "&redirect_uri=http://localhost:3000"
)
KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token"
KAKAO_CLIENT_ID = "f631cb629a27146986a0337232138f16"
KAKAO_REDIRECT_URI = "http://localhost:3000"
KAKAO_CLIENT_SECRET = "Kt0EMss5t6LA7BnHKCvH0JEd7lujoW7c"
BACKEND_LOGIN_URL = "https://afternote.kro.kr/auth/social/login"

# Shared state for the one request we care about
captured_code = None
server_ready = None


class OAuthHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        global captured_code
        parsed = urllib.parse.urlparse(self.path)
        if parsed.path in ("/", "") and parsed.query:
            params = urllib.parse.parse_qs(parsed.query)
            codes = params.get("code")
            if codes:
                captured_code = codes[0]
        self.send_response(200)
        self.send_header("Content-Type", "text/html; charset=utf-8")
        self.end_headers()
        self.wfile.write(
            b"<html><body><p>Code received. You can close this tab.</p></body></html>"
        )

    def log_message(self, format, *args):
        pass  # Suppress default request logging


def run_server():
    global server_ready
    server = HTTPServer(("127.0.0.1", 3000), OAuthHandler)
    server_ready = True
    server.handle_request()
    server.server_close()


def exchange_code_for_token(code: str) -> str:
    body = urllib.parse.urlencode({
        "grant_type": "authorization_code",
        "client_id": KAKAO_CLIENT_ID,
        "redirect_uri": KAKAO_REDIRECT_URI,
        "code": code,
        "client_secret": KAKAO_CLIENT_SECRET,
    }).encode("utf-8")
    req = urllib.request.Request(
        KAKAO_TOKEN_URL,
        data=body,
        method="POST",
        headers={"Content-Type": "application/x-www-form-urlencoded"},
    )
    with urllib.request.urlopen(req) as resp:
        data = json.loads(resp.read().decode())
    return data.get("access_token", "")


def backend_social_login(kakao_access_token: str) -> Tuple[dict, int]:
    """
    POST /auth/social/login with provider KAKAO and the Kakao accessToken.
    Returns (parsed_response_json, http_status_code).
    """
    body = json.dumps({
        "provider": "KAKAO",
        "accessToken": kakao_access_token,
    }).encode("utf-8")
    req = urllib.request.Request(
        BACKEND_LOGIN_URL,
        data=body,
        method="POST",
        headers={"Content-Type": "application/json"},
    )
    try:
        with urllib.request.urlopen(req) as resp:
            return json.loads(resp.read().decode()), resp.status
    except urllib.error.HTTPError as e:
        body = e.read().decode() if e.fp else ""
        try:
            return json.loads(body), e.code
        except json.JSONDecodeError:
            return {"message": body, "status": e.code}, e.code


def main():
    global captured_code, server_ready
    server_ready = False
    print("Starting local server on http://localhost:3000 ...")
    thread = Thread(target=run_server)
    thread.start()
    while not server_ready:
        pass
    print("Opening Kakao authorize URL in your browser. Log in if asked.")
    webbrowser.open(KAKAO_AUTHORIZE_URL)
    thread.join()
    if not captured_code:
        print("No authorization code was received. Check that you completed login.")
        return 1
    print("Code received. Exchanging for Kakao access token...")
    kakao_token = exchange_code_for_token(captured_code)
    if not kakao_token:
        print("Failed to get Kakao access token.")
        return 1
    print("Calling backend POST /auth/social/login with Kakao accessToken...")
    try:
        backend, status = backend_social_login(kakao_token)
    except Exception as e:
        print(f"Backend login request failed: {e}")
        print("Kakao access_token (for manual use):")
        print(kakao_token)
        return 1
    if status != 200:
        print(f"Backend returned HTTP {status}:")
        print(json.dumps(backend, indent=2))
        return 1
    # Response: { "status", "code", "message", "data": { "accessToken", "refreshToken", "newUser" } }
    data = backend.get("data") or {}
    access_token = (
        data.get("accessToken")
        or data.get("access_token")
        or backend.get("accessToken")
        or backend.get("access_token")
    )
    if access_token:
        print("\n--- Service JWT accessToken (for Swagger Authorize) ---")
        print(access_token)
        print("---")
        refresh_token = data.get("refreshToken") or data.get("refresh_token")
        new_user = data.get("newUser")
        if refresh_token or new_user is not None:
            print("(refreshToken and newUser are in the response; use accessToken above in Swagger.)")
        print("In Swagger UI: Authorize → paste the token (or Bearer <token> if required).")
    else:
        print("Backend response (no accessToken in data):")
        print(json.dumps(backend, indent=2))
        return 1
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
