package com.kuit.afternote.util

import android.util.Base64
import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

/**
 * JWT 토큰 디코딩 유틸리티.
 *
 * JWT 토큰에서 userId를 추출합니다.
 * JWT는 Base64로 인코딩된 JSON이므로 별도 라이브러리 없이 디코딩 가능합니다.
 */
object JwtDecoder {
    private const val TAG = "JwtDecoder"

    /**
     * JWT 토큰에서 userId를 추출합니다.
     *
     * @param token JWT 토큰 문자열
     * @return userId (Long) 또는 null (토큰이 유효하지 않거나 userId를 찾을 수 없는 경우)
     */
    fun getUserId(token: String): Long? {
        return runCatching {
            val parts = token.split(".")
            if (parts.size != 3) {
                Log.w(TAG, "Invalid JWT token format: expected 3 parts, got ${parts.size}")
                return null
            }

            val payload = parts[1]
            val decoded = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
            val jsonString = String(decoded, Charsets.UTF_8)

            val json = Json { ignoreUnknownKeys = true }
            val jsonObject = json.parseToJsonElement(jsonString).jsonObject

            // 여러 가능한 필드명에서 userId 찾기
            val userId = jsonObject["userId"]?.toString()?.toLongOrNull()
                ?: jsonObject["sub"]?.toString()?.toLongOrNull()
                ?: jsonObject["id"]?.toString()?.toLongOrNull()
                ?: jsonObject["user_id"]?.toString()?.toLongOrNull()

            if (userId == null) {
                Log.w(TAG, "userId not found in JWT payload. Available keys: ${jsonObject.keys}")
            }

            userId
        }.getOrElse { e ->
            Log.e(TAG, "Failed to decode JWT token: ${e.message}", e)
            null
        }
    }

    /**
     * JWT 토큰의 payload를 JSON 객체로 반환합니다 (디버깅용).
     *
     * @param token JWT 토큰 문자열
     * @return JSON 객체 또는 null
     */
    fun getPayload(token: String): JsonObject? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null

            val payload = parts[1]
            val decoded = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
            val jsonString = String(decoded, Charsets.UTF_8)

            val json = Json { ignoreUnknownKeys = true }
            json.parseToJsonElement(jsonString).jsonObject
        } catch (e: Exception) {
            Log.e(TAG, "Failed to decode JWT payload: ${e.message}", e)
            null
        }
    }
}
