package com.kuit.afternote.feature.user.data.mapper

import com.kuit.afternote.feature.user.data.dto.DailyQuestionAnswerItemDto
import com.kuit.afternote.feature.user.data.dto.DeliveryConditionRequestDto
import com.kuit.afternote.feature.user.data.dto.DeliveryConditionResponseDto
import com.kuit.afternote.feature.user.data.dto.DeliveryConditionTypeDto
import com.kuit.afternote.feature.user.data.dto.ReceiverDetailResponseDto
import com.kuit.afternote.feature.user.data.dto.ReceiverItemDto
import com.kuit.afternote.feature.user.data.dto.UserPushSettingResponse
import com.kuit.afternote.feature.user.data.dto.UserResponse
import com.kuit.afternote.feature.user.domain.model.DailyQuestionAnswerItem
import com.kuit.afternote.feature.user.domain.model.DeliveryCondition
import com.kuit.afternote.feature.user.domain.model.DeliveryConditionType
import com.kuit.afternote.feature.user.domain.model.PushSettings
import com.kuit.afternote.feature.user.domain.model.ReceiverDailyQuestionsResult
import com.kuit.afternote.feature.user.domain.model.ReceiverDetail
import com.kuit.afternote.feature.user.domain.model.ReceiverListItem
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

    fun toDeliveryCondition(dto: DeliveryConditionResponseDto): DeliveryCondition =
        DeliveryCondition(
            conditionType = dto.conditionType.toDomain(),
            inactivityPeriodDays = dto.inactivityPeriodDays,
            specificDate = dto.specificDate,
            conditionFulfilled = dto.conditionFulfilled,
            conditionMet = dto.conditionMet
        )

    fun toDeliveryConditionRequestDto(
        conditionType: DeliveryConditionType,
        inactivityPeriodDays: Int?,
        specificDate: String?
    ): DeliveryConditionRequestDto =
        DeliveryConditionRequestDto(
            conditionType = conditionType.toDto(),
            inactivityPeriodDays = inactivityPeriodDays,
            specificDate = specificDate
        )

    private fun DeliveryConditionTypeDto.toDomain(): DeliveryConditionType =
        when (this) {
            DeliveryConditionTypeDto.NONE -> DeliveryConditionType.NONE
            DeliveryConditionTypeDto.DEATH_CERTIFICATE -> DeliveryConditionType.DEATH_CERTIFICATE
            DeliveryConditionTypeDto.INACTIVITY -> DeliveryConditionType.INACTIVITY
            DeliveryConditionTypeDto.SPECIFIC_DATE -> DeliveryConditionType.SPECIFIC_DATE
        }

    private fun DeliveryConditionType.toDto(): DeliveryConditionTypeDto =
        when (this) {
            DeliveryConditionType.NONE -> DeliveryConditionTypeDto.NONE
            DeliveryConditionType.DEATH_CERTIFICATE -> DeliveryConditionTypeDto.DEATH_CERTIFICATE
            DeliveryConditionType.INACTIVITY -> DeliveryConditionTypeDto.INACTIVITY
            DeliveryConditionType.SPECIFIC_DATE -> DeliveryConditionTypeDto.SPECIFIC_DATE
        }
}
