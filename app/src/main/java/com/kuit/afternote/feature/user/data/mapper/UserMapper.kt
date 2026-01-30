package com.kuit.afternote.feature.user.data.mapper

import com.kuit.afternote.feature.user.data.dto.UserPushSettingResponse
import com.kuit.afternote.feature.user.data.dto.UserResponse
import com.kuit.afternote.feature.user.domain.model.PushSettings
import com.kuit.afternote.feature.user.domain.model.UserProfile

/**
 * User DTO를 Domain 모델로 변환. (스웨거 기준)
 */
object UserMapper {
    fun toUserProfile(dto: UserResponse): UserProfile =
        UserProfile(
            name = dto.name,
            email = dto.email,
            phone = dto.phone,
            profileImageUrl = dto.profileImageUrl
        )

    fun toPushSettings(dto: UserPushSettingResponse): PushSettings =
        PushSettings(
            timeLetter = dto.timeLetter,
            mindRecord = dto.mindRecord,
            afterNote = dto.afterNote
        )
}
