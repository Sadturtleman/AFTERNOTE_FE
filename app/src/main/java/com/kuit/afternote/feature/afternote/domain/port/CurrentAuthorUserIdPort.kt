package com.kuit.afternote.feature.afternote.domain.port

/** 현재 로그인한 작성자 사용자 ID (없으면 null). */
fun interface CurrentAuthorUserIdPort {
    suspend operator fun invoke(): Long?
}
