package com.kuit.afternote.data.remote

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
    val data: T? = null
)

/**
 * 응답 status가 200이 아니면 [ApiException]을 던지고, data가 null이면 [ApiException]을 던진다.
 *
 * @return non-null data
 * @throws ApiException 서버가 에러 응답을 반환한 경우
 */
fun <T> ApiResponse<T>.requireData(): T & Any {
    if (status != 200) {
        throw ApiException(status = status, code = code, message = message)
    }
    return data ?: throw ApiException(
        status = status,
        code = code,
        message = message.ifBlank { "data is null" }
    )
}

/**
 * 응답 status가 200이 아니면 [ApiException]을 던진다.
 *
 * data가 없는(또는 의미 없는) API에서도 성공 여부만 확인할 때 사용한다.
 *
 * @throws ApiException 서버가 에러 응답을 반환한 경우
 */
fun ApiResponse<*>.requireSuccess() {
    if (status != 200) {
        throw ApiException(status = status, code = code, message = message)
    }
}
