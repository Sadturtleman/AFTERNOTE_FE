package com.kuit.afternote.feature.user.domain.repository

import com.kuit.afternote.feature.user.domain.model.DailyQuestionAnswerItem
import com.kuit.afternote.feature.user.domain.model.PushSettings
import com.kuit.afternote.feature.user.domain.model.ReceiverAfterNoteSourceItem
import com.kuit.afternote.feature.user.domain.model.ReceiverDetail
import com.kuit.afternote.feature.user.domain.model.ReceiverListItem
import com.kuit.afternote.feature.user.domain.model.ReceiverTimeLetterItem
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

    suspend fun getReceivers(): Result<List<ReceiverListItem>>

    suspend fun registerReceiver(
        name: String,
        relation: String,
        phone: String?,
        email: String?
    ): Result<Long>

    suspend fun getReceiverDetail(receiverId: Long): Result<ReceiverDetail>

    suspend fun getReceiverDailyQuestions(receiverId: Long): Result<List<DailyQuestionAnswerItem>>

    suspend fun getReceiverTimeLetters(receiverId: Long): Result<List<ReceiverTimeLetterItem>>

    suspend fun getReceiverAfterNotes(receiverId: Long): Result<List<ReceiverAfterNoteSourceItem>>
}
