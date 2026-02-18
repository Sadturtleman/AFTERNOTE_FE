package com.kuit.afternote.feature.user.domain.repository

import com.kuit.afternote.feature.user.domain.model.DeliveryCondition
import com.kuit.afternote.feature.user.domain.model.DeliveryConditionType
import com.kuit.afternote.feature.user.domain.model.PushSettings
import com.kuit.afternote.feature.user.domain.model.ReceiverDetail
import com.kuit.afternote.feature.user.domain.model.ReceiverDailyQuestionsResult
import com.kuit.afternote.feature.user.domain.model.ReceiverListItem
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

    /** GET /users/push-settings — 푸시 알림 설정 조회. */
    suspend fun getMyPushSettings(userId: Long): Result<PushSettings>

    suspend fun updateMyPushSettings(
        userId: Long,
        timeLetter: Boolean?,
        mindRecord: Boolean?,
        afterNote: Boolean?
    ): Result<PushSettings>

    /** GET /users/receivers — 수신인 목록 조회. 로그인한 사용자가 등록한 수신인 목록을 조회합니다. */
    suspend fun getReceivers(userId: Long): Result<List<ReceiverListItem>>

    suspend fun registerReceiver(
        name: String,
        relation: String,
        phone: String?,
        email: String?
    ): Result<Long>

    suspend fun getReceiverDetail(receiverId: Long): Result<ReceiverDetail>

    suspend fun updateReceiver(
        receiverId: Long,
        name: String,
        relation: String,
        phone: String?,
        email: String?
    ): Result<Unit>

    suspend fun getReceiverDailyQuestions(
        receiverId: Long,
        page: Int,
        size: Int
    ): Result<ReceiverDailyQuestionsResult>

    /**
     * GET /users/delivery-condition — 로그인한 사용자의 전달 조건 설정 조회.
     */
    suspend fun getDeliveryCondition(): Result<DeliveryCondition>

    /**
     * PATCH /users/delivery-condition — 로그인한 사용자의 전달 조건 설정/변경.
     *
     * @param conditionType 전달 조건 타입
     * @param inactivityPeriodDays 비활동 기간(일), INACTIVITY일 때 사용
     * @param specificDate 특정 날짜(yyyy-MM-dd), SPECIFIC_DATE일 때 사용
     * @param leaveMessage 마지막 인사말 (수신자에게 전달되는 메시지)
     */
    suspend fun updateDeliveryCondition(
        conditionType: DeliveryConditionType,
        inactivityPeriodDays: Int?,
        specificDate: String?,
        leaveMessage: String? = null
    ): Result<DeliveryCondition>
}
