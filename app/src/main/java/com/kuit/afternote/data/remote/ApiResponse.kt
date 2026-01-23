package com.kuit.afternote.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * API 공통 응답 래퍼.
 *
 * 문서화된 형식: `{ "status", "code", "message", "data" }`
 *
 * @param T `data` 필드의 타입. null인 경우 [Unit] 또는 [Nothing] 사용.
 */
@Serializable
data class ApiResponse<T>(
    val status: Int,
    val code: Int,
    val message: String,
    @SerialName("data") val data: T? = null
)
