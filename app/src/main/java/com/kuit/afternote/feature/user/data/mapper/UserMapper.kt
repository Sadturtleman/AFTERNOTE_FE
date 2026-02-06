package com.kuit.afternote.feature.user.data.mapper

import com.kuit.afternote.feature.user.data.dto.DailyQuestionAnswerItemDto
import com.kuit.afternote.feature.user.data.dto.ReceiverAfterNoteSourceItemDto
import com.kuit.afternote.feature.user.data.dto.ReceiverDetailResponseDto
import com.kuit.afternote.feature.user.data.dto.ReceiverItemDto
import com.kuit.afternote.feature.user.data.dto.ReceiverTimeLetterItemDto
import com.kuit.afternote.feature.user.data.dto.UserPushSettingResponse
import com.kuit.afternote.feature.user.data.dto.UserResponse
import com.kuit.afternote.feature.user.domain.model.DailyQuestionAnswerItem
import com.kuit.afternote.feature.user.domain.model.ReceiverDailyQuestionsResult
import com.kuit.afternote.feature.user.domain.model.PushSettings
import com.kuit.afternote.feature.user.domain.model.ReceiverAfterNoteSourceItem
import com.kuit.afternote.feature.user.domain.model.ReceiverDetail
import com.kuit.afternote.feature.user.domain.model.ReceiverListItem
import com.kuit.afternote.feature.user.domain.model.ReceiverTimeLetterItem
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

    fun toReceiverListItem(dto: ReceiverItemDto): ReceiverListItem =
        ReceiverListItem(
            receiverId = dto.receiverId,
            name = dto.name,
            relation = dto.relation
        )

    fun toReceiverDetail(dto: ReceiverDetailResponseDto): ReceiverDetail =
        ReceiverDetail(
            receiverId = dto.receiverId,
            name = dto.name,
            relation = dto.relation,
            phone = dto.phone,
            email = dto.email,
            dailyQuestionCount = dto.dailyQuestionCount,
            timeLetterCount = dto.timeLetterCount,
            afterNoteCount = dto.afterNoteCount
        )

    fun toDailyQuestionAnswerItem(dto: DailyQuestionAnswerItemDto): DailyQuestionAnswerItem =
        DailyQuestionAnswerItem(
            dailyQuestionAnswerId = dto.dailyQuestionAnswerId,
            question = dto.question,
            answer = dto.answer,
            recordDate = dto.recordDate
        )

    fun toReceiverDailyQuestionsResult(
        items: List<DailyQuestionAnswerItem>,
        hasNext: Boolean
    ): ReceiverDailyQuestionsResult =
        ReceiverDailyQuestionsResult(items = items, hasNext = hasNext)

    fun toReceiverTimeLetterItem(dto: ReceiverTimeLetterItemDto): ReceiverTimeLetterItem =
        ReceiverTimeLetterItem(
            timeLetterId = dto.timeLetterId,
            receiverName = dto.receiverName,
            sendAt = dto.sendAt,
            title = dto.title,
            content = dto.content
        )

    fun toReceiverAfterNoteSourceItem(dto: ReceiverAfterNoteSourceItemDto): ReceiverAfterNoteSourceItem =
        ReceiverAfterNoteSourceItem(
            sourceType = dto.sourceType,
            lastUpdatedAt = dto.lastUpdatedAt
        )
}
