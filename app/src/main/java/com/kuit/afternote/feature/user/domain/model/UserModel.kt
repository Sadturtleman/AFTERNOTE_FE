package com.kuit.afternote.feature.user.domain.model

/**
 * User 도메인 모델. (스웨거 기준)
 */

data class UserProfile(
    val name: String,
    val email: String,
    val phone: String?,
    val profileImageUrl: String?
)

data class PushSettings(
    val timeLetter: Boolean,
    val mindRecord: Boolean,
    val afterNote: Boolean
)
