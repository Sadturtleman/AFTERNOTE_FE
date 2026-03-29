package com.kuit.afternote.core.data.model
import com.kuit.afternote.core.data.service.ApiException
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
data class BaseResponse<T>(
    @SerialName("status") val status: Int,
    @SerialName("message") val message: String?,
    @SerialName("data") val data: T? = null,
    @SerialName("code") val code: Int,
)

/**
 * 응답 status가 200이 아니면 [ApiException]을 던지고, data가 null이면 [ApiException]을 던진다.
 *
 * @return non-null data
 * @throws ApiException 서버가 에러 응답을 반환한 경우
 */
fun <T> BaseResponse<T>.requireData(): T & Any {
    requireStatus()
    return data ?: throw ApiException(
        status = status,
        code = code,
        message = message ?: "데이터 필드 없음",
    )
}

/**
 * 응답 status가 200이 아니면 [ApiException]을 던진다.
 *
 * data가 없는(또는 의미 없는) API에서도 성공 여부만 확인할 때 사용한다.
 *
 * @throws ApiException 서버가 에러 응답을 반환한 경우
 */
fun BaseResponse<*>.requireStatus() {
    if (status != 200) {
        throw ApiException(status = status, code = code, message = message ?: "Status가 200이 아님")
    }
}
