package com.kuit.afternote.data.remote

import android.util.Log
import com.kuit.afternote.data.local.TokenManager
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * 인증 토큰을 요청 헤더에 자동으로 추가하는 Interceptor.
 *
 * Authorization 헤더가 필요한 API 요청에 Bearer 토큰을 추가합니다.
 * 401 응답 시 자동으로 토큰을 재발급하고 요청을 재시도합니다.
 */
class AuthInterceptor
    @Inject
    constructor(
        private val tokenManager: TokenManager
    ) : Interceptor {
        companion object {
            private const val TAG = "AuthInterceptor"
            private const val AUTHORIZATION_HEADER = "Authorization"
            private const val BEARER_PREFIX = "Bearer "
            private const val BASE_URL = "https://afternote.kro.kr/"
            private const val REISSUE_ENDPOINT = "auth/reissue"

            /**
             * 인증이 필요 없는 경로 목록.
             */
            private val NO_AUTH_PATHS = listOf(
                "/auth/email/send",
                "/auth/email/verify",
                "/auth/sign-up",
                "/auth/login",
                "/auth/social/login",
                "/auth/reissue"
            )
        }

        private val json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            isLenient = true
        }

        // 토큰 재발급용 별도 OkHttpClient (인터셉터 없이)
        private val refreshClient: OkHttpClient by lazy {
            OkHttpClient
                .Builder()
                .connectTimeout(15L, TimeUnit.SECONDS)
                .readTimeout(15L, TimeUnit.SECONDS)
                .build()
        }

        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val url = originalRequest.url
            val path = url.encodedPath
            val method = originalRequest.method

            // 강제로 눈에 띄도록 INFO 레벨 로그 추가 (필터: tag=AuthInterceptor, level=Info)
            Log.i(TAG, "========== AUTH REQUEST ==========")
            Log.i(TAG, "Method: $method")
            Log.i(TAG, "URL: $url")
            Log.i(TAG, "Path: $path")

            // 인증이 필요 없는 경로는 그대로 진행
            if (NO_AUTH_PATHS.any { path.endsWith(it) }) {
                Log.d(TAG, "Auth: SKIP (public endpoint)")
                return proceedAndLog(chain, originalRequest)
            }

            // 이미 Authorization 헤더가 있으면 그대로 진행
            if (originalRequest.header(AUTHORIZATION_HEADER) != null) {
                Log.d(TAG, "Auth: ALREADY SET")
                return proceedAndLog(chain, originalRequest)
            }

            // 토큰을 가져와서 헤더에 추가
            val accessToken = runBlocking { tokenManager.getAccessToken() }
            Log.i(TAG, "Access Token (first 20 chars): ${accessToken?.take(n = 20)}...")

            if (accessToken.isNullOrEmpty()) {
                Log.w(TAG, "Auth: NO TOKEN AVAILABLE")
                return proceedAndLog(chain, originalRequest)
            }

            val authenticatedRequest = originalRequest
                .newBuilder()
                .header(AUTHORIZATION_HEADER, "$BEARER_PREFIX$accessToken")
                .build()
            Log.d(TAG, "Auth: TOKEN ADDED")

            val response = proceedAndLog(chain, authenticatedRequest)

            // 401 응답 시 토큰 재발급 시도
            if (response.code == 401) {
                Log.w(TAG, "Auth: 401 Unauthorized - Attempting token refresh")
                return handleTokenRefresh(chain, originalRequest, response)
            }

            return response
        }

        /**
         * 401 응답 시 토큰 재발급을 시도하고 요청을 재시도합니다.
         */
        private fun handleTokenRefresh(
            chain: Interceptor.Chain,
            originalRequest: Request,
            originalResponse: Response
        ): Response {
            val refreshToken = runBlocking { tokenManager.getRefreshToken() }

            if (refreshToken.isNullOrEmpty()) {
                Log.e(TAG, "TokenRefresh: No refresh token available")
                // 토큰이 전혀 없는 상태에서는 추가 조치를 하지 않고 401을 그대로 반환한다.
                // (UI 레이어에서 isLoggedInFlow를 보고 로그인 화면으로 유도)
                return originalResponse
            }

            Log.d(TAG, "TokenRefresh: Attempting with refreshToken=${refreshToken.take(n = 20)}...")

            return try {
                val newTokens = refreshAccessToken(refreshToken)

                if (newTokens != null) {
                    Log.d(TAG, "TokenRefresh: SUCCESS - New tokens received")

                    // 새 토큰 저장
                    runBlocking {
                        tokenManager.updateTokens(
                            accessToken = newTokens.accessToken ?: "",
                            refreshToken = newTokens.refreshToken ?: refreshToken
                        )
                    }

                    // 원래 응답 닫기
                    originalResponse.close()

                    // 새 토큰으로 원래 요청 재시도
                    val newRequest = originalRequest
                        .newBuilder()
                        .header(AUTHORIZATION_HEADER, "$BEARER_PREFIX${newTokens.accessToken}")
                        .build()

                    Log.d(TAG, "TokenRefresh: Retrying original request with new token")
                    proceedAndLog(chain, newRequest)
                } else {
                    Log.e(TAG, "TokenRefresh: FAILED - keeping existing tokens")
                    // 재발급 실패 시 기존 토큰을 삭제하지 않고 401을 그대로 반환한다.
                    // 이렇게 해야 사용자가 이미 로그인했다고 생각하는 상태에서
                    // 토큰이 갑자기 null로 사라지는 문제를 방지할 수 있다.
                    originalResponse
                }
            } catch (e: Exception) {
                Log.e(TAG, "TokenRefresh: Exception - ${e.message}", e)
                // 예외 발생 시에도 토큰을 강제로 삭제하지 않고 401 응답을 그대로 반환한다.
                originalResponse
            }
        }

        /**
         * Refresh token을 사용하여 새 access token을 발급받습니다.
         */
        private fun refreshAccessToken(refreshToken: String): ReissueResponseData? {
            val requestBody = json
                .encodeToString(
                    ReissueRequestBody.serializer(),
                    ReissueRequestBody(refreshToken)
                ).toRequestBody("application/json".toMediaType())

            val request = Request
                .Builder()
                .url("$BASE_URL$REISSUE_ENDPOINT")
                .post(requestBody)
                .build()

            return try {
                val response = refreshClient.newCall(request).execute()
                Log.d(TAG, "TokenRefresh API: ${response.code} ${response.message}")

                if (response.isSuccessful) {
                    val responseBody = response.body.string()
                    val apiResponse = json.decodeFromString<ReissueApiResponse>(responseBody)
                    apiResponse.data
                } else {
                    Log.e(TAG, "TokenRefresh API failed: ${response.code}")
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "TokenRefresh API exception: ${e.message}", e)
                null
            }
        }

        private fun proceedAndLog(
            chain: Interceptor.Chain,
            request: Request
        ): Response {
            val response = chain.proceed(request)
            Log.d(TAG, "========== RESPONSE ==========")
            Log.d(TAG, "Status: ${response.code} ${response.message}")
            if (response.code == 404) {
                Log.e(TAG, "404 NOT FOUND - Check if endpoint exists: ${request.url}")
            }
            return response
        }
    }

/**
 * 토큰 재발급 요청 Body.
 */
@Serializable
private data class ReissueRequestBody(
    @SerialName("refreshToken") val refreshToken: String
)

/**
 * 토큰 재발급 API 응답.
 */
@Serializable
private data class ReissueApiResponse(
    @SerialName("status") val status: Int? = null,
    @SerialName("code") val code: Int? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("data") val data: ReissueResponseData? = null
)

/**
 * 토큰 재발급 응답 데이터.
 */
@Serializable
private data class ReissueResponseData(
    @SerialName("accessToken") val accessToken: String? = null,
    @SerialName("refreshToken") val refreshToken: String? = null
)
