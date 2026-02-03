package com.kuit.afternote.feature.user.data.repository

import android.util.Log
import com.kuit.afternote.data.remote.ApiException
import com.kuit.afternote.data.remote.requireData
import com.kuit.afternote.feature.user.data.api.UserApiService
import com.kuit.afternote.feature.user.data.dto.RegisterReceiverRequestDto
import com.kuit.afternote.feature.user.data.dto.UserUpdateProfileRequest
import com.kuit.afternote.feature.user.data.dto.UserUpdatePushSettingRequest
import com.kuit.afternote.feature.user.data.mapper.UserMapper
import com.kuit.afternote.feature.user.domain.model.DailyQuestionAnswerItem
import com.kuit.afternote.feature.user.domain.model.PushSettings
import com.kuit.afternote.feature.user.domain.model.ReceiverAfterNoteSourceItem
import com.kuit.afternote.feature.user.domain.model.ReceiverDetail
import com.kuit.afternote.feature.user.domain.model.ReceiverListItem
import com.kuit.afternote.feature.user.domain.model.ReceiverTimeLetterItem
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

        override suspend fun getReceivers(): Result<List<ReceiverListItem>> =
            runCatching {
                Log.d(TAG, "getReceivers")
                val response = api.getReceivers()
                Log.d(TAG, "getReceivers: response=$response")
                val body = response.requireData()
                body.receivers.map(UserMapper::toReceiverListItem)
            }

        override suspend fun registerReceiver(
            name: String,
            relation: String,
            phone: String?,
            email: String?
        ): Result<Long> =
            runCatching {
                Log.d(TAG, "registerReceiver: name=$name, relation=$relation")
                val response = api.registerReceiver(
                    RegisterReceiverRequestDto(
                        name = name,
                        relation = relation,
                        phone = phone,
                        email = email
                    )
                )
                Log.d(TAG, "registerReceiver: response=$response")
                if (response.status != 200 && response.status != 201) {
                    throw ApiException(
                        status = response.status,
                        code = response.code,
                        message = response.message
                    )
                }
                val data = response.data ?: throw ApiException(
                    status = response.status,
                    code = response.code,
                    message = response.message.ifBlank { "data is null" }
                )
                data.receiverId
            }

        override suspend fun getReceiverDetail(receiverId: Long): Result<ReceiverDetail> =
            runCatching {
                Log.d(TAG, "getReceiverDetail: receiverId=$receiverId")
                val response = api.getReceiverDetail(receiverId = receiverId)
                Log.d(TAG, "getReceiverDetail: response=$response")
                UserMapper.toReceiverDetail(response.requireData())
            }

        override suspend fun getReceiverDailyQuestions(receiverId: Long): Result<List<DailyQuestionAnswerItem>> =
            runCatching {
                Log.d(TAG, "getReceiverDailyQuestions: receiverId=$receiverId")
                val response = api.getReceiverDailyQuestions(receiverId = receiverId)
                Log.d(TAG, "getReceiverDailyQuestions: response=$response")
                val body = response.requireData()
                body.items.map(UserMapper::toDailyQuestionAnswerItem)
            }

        override suspend fun getReceiverTimeLetters(receiverId: Long): Result<List<ReceiverTimeLetterItem>> =
            runCatching {
                Log.d(TAG, "getReceiverTimeLetters: receiverId=$receiverId")
                val response = api.getReceiverTimeLetters(receiverId = receiverId)
                Log.d(TAG, "getReceiverTimeLetters: response=$response")
                val body = response.requireData()
                body.items.map(UserMapper::toReceiverTimeLetterItem)
            }

        override suspend fun getReceiverAfterNotes(receiverId: Long): Result<List<ReceiverAfterNoteSourceItem>> =
            runCatching {
                Log.d(TAG, "getReceiverAfterNotes: receiverId=$receiverId")
                val response = api.getReceiverAfterNotes(receiverId = receiverId)
                Log.d(TAG, "getReceiverAfterNotes: response=$response")
                val body = response.requireData()
                body.items.map(UserMapper::toReceiverAfterNoteSourceItem)
            }

        companion object {
            private const val TAG = "UserRepositoryImpl"
        }
    }
