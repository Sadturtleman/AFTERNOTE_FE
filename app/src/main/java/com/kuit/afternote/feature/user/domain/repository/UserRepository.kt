package com.kuit.afternote.feature.user.domain.repository

import com.kuit.afternote.feature.user.domain.model.PushSettings
import com.kuit.afternote.feature.user.domain.model.UserProfile

/**
 * User 도메인 Repository 인터페이스. (스웨거 기준)
 */
interface UserRepository {

    suspend fun getMyProfile(userId: Long): Result<UserProfile>

    suspend fun updateMyProfile(
        userId: Long,
        name: String?,
        phone: String?,
        profileImageUrl: String?
    ): Result<UserProfile>

    suspend fun getMyPushSettings(userId: Long): Result<PushSettings>

    suspend fun updateMyPushSettings(
        userId: Long,
        timeLetter: Boolean?,
        mindRecord: Boolean?,
        afterNote: Boolean?
    ): Result<PushSettings>
}
