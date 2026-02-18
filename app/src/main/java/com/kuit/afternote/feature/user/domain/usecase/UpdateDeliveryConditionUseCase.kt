package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.feature.user.domain.model.DeliveryCondition
import com.kuit.afternote.feature.user.domain.model.DeliveryConditionType
import com.kuit.afternote.feature.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 전달 조건 수정 UseCase.
 * PATCH /users/delivery-condition — 로그인한 사용자의 전달 조건을 설정하거나 변경합니다.
 */
class UpdateDeliveryConditionUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository
    ) {
    /**
     * @param conditionType 전달 조건 타입
     * @param inactivityPeriodDays 비활동 기간(일), INACTIVITY일 때 사용
     * @param specificDate 특정 날짜(yyyy-MM-dd), SPECIFIC_DATE일 때 사용
     * @param leaveMessage 마지막 인사말 (수신자에게 전달되는 메시지)
     * @return 업데이트된 [DeliveryCondition]
     */
    suspend operator fun invoke(
        conditionType: DeliveryConditionType,
        inactivityPeriodDays: Int?,
        specificDate: String?,
        leaveMessage: String? = null
    ): Result<DeliveryCondition> =
        userRepository.updateDeliveryCondition(
            conditionType = conditionType,
            inactivityPeriodDays = inactivityPeriodDays,
            specificDate = specificDate,
            leaveMessage = leaveMessage
        )
}
