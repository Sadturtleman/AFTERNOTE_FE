package com.kuit.afternote.feature.user.data.repository

import android.util.Log
import com.kuit.afternote.data.remote.requireData
import com.kuit.afternote.feature.user.data.api.UserApiService
import com.kuit.afternote.feature.user.data.dto.UserUpdateProfileRequest
import com.kuit.afternote.feature.user.data.dto.UserUpdatePushSettingRequest
import com.kuit.afternote.feature.user.data.mapper.UserMapper
import com.kuit.afternote.feature.user.domain.model.PushSettings
import com.kuit.afternote.feature.user.domain.model.UserProfile
import com.kuit.afternote.feature.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * UserRepository 구현체. (스웨거 기준)
 */
class UserRepositoryImpl
    @Inject
    constructor(
        private val api: UserApiService
    ) : UserRepository {
        override suspend fun getMyProfile(userId: Long): Result<UserProfile> =
            runCatching {
                Log.d(TAG, "getMyProfile: userId=$userId")
                val response = api.getMyProfile(userId = userId)
                Log.d(TAG, "getMyProfile: response=$response")
                UserMapper.toUserProfile(response.requireData())
            }

        override suspend fun updateMyProfile(
            userId: Long,
            name: String?,
            phone: String?,
            profileImageUrl: String?
        ): Result<UserProfile> =
            runCatching {
                Log.d(TAG, "updateMyProfile: userId=$userId, name=$name, phone=$phone")
                val response = api.updateMyProfile(
                    userId = userId,
                    body = UserUpdateProfileRequest(
                        name = name,
                        phone = phone,
                        profileImageUrl = profileImageUrl
                    )
                )
                Log.d(TAG, "updateMyProfile: response=$response")
                UserMapper.toUserProfile(response.requireData())
            }

        override suspend fun getMyPushSettings(userId: Long): Result<PushSettings> =
            runCatching {
                Log.d(TAG, "getMyPushSettings: userId=$userId")
                val response = api.getMyPushSettings(userId = userId)
                Log.d(TAG, "getMyPushSettings: response=$response")
                UserMapper.toPushSettings(response.requireData())
            }

        override suspend fun updateMyPushSettings(
            userId: Long,
            timeLetter: Boolean?,
            mindRecord: Boolean?,
            afterNote: Boolean?
        ): Result<PushSettings> =
            runCatching {
                Log.d(TAG, "updateMyPushSettings: userId=$userId, timeLetter=$timeLetter, mindRecord=$mindRecord, afterNote=$afterNote")
                val response = api.updateMyPushSettings(
                    userId = userId,
                    body = UserUpdatePushSettingRequest(
                        timeLetter = timeLetter,
                        mindRecord = mindRecord,
                        afterNote = afterNote
                    )
                )
                Log.d(TAG, "updateMyPushSettings: response=$response")
                UserMapper.toPushSettings(response.requireData())
            }

        companion object {
            private const val TAG = "UserRepositoryImpl"
        }
    }
