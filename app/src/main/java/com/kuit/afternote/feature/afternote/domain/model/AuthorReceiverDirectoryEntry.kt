package com.kuit.afternote.feature.afternote.domain.model

/**
 * 작성자 계정의 수신자 디렉터리 한 행 (GET /users/receivers 등).
 * Afternote feature는 user feature 모델에 의존하지 않기 위한 최소 필드만 둡니다.
 */
data class AuthorReceiverDirectoryEntry(
    val receiverId: Long,
    val name: String,
    val relation: String,
)
