package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.feature.user.domain.model.DeliveryCondition
import com.kuit.afternote.feature.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 전달 조건 조회 UseCase.
 * GET /users/delivery-condition — 로그인한 사용자의 전달 조건 설정을 조회합니다.
 */
class GetDeliveryConditionUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository
    ) {
    /**
     * @return [DeliveryCondition] (conditionType, inactivityPeriodDays, specificDate, etc.)
     */
    suspend operator fun invoke(): Result<DeliveryCondition> =
        userRepository.getDeliveryCondition()
}
