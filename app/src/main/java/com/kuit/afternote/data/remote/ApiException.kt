package com.kuit.afternote.data.remote

/**
 * API 오류 시 사용하는 예외.
 *
 * HTTP 4xx/5xx 또는 공통 응답의 code/message를 담을 수 있다.
 *
 * @param status HTTP 상태 코드 (선택)
 * @param code API 내부 코드 (선택)
 * @param message 오류 메시지
 */
class ApiException(
    val status: Int? = null,
    val code: Int? = null,
    override val message: String
) : Exception(message)
