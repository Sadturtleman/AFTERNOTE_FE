package com.kuit.afternote.feature.afternote.domain.model

/**
 * 갤러리 카테고리의 수신자 정보.
 * receiverId: from API; name/relation may be resolved from GET /users/receivers when API returns only IDs.
 */
data class DetailReceiver(
    val receiverId: Long? = null,
    val name: String,
    val relation: String,
    val phone: String,
)
