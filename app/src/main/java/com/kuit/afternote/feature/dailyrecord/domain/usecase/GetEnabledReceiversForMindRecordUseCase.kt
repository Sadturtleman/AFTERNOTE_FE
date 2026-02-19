package com.kuit.afternote.feature.dailyrecord.domain.usecase

import com.kuit.afternote.feature.user.domain.usecase.GetReceiversUseCase
import com.kuit.afternote.feature.user.domain.usecase.GetUserIdUseCase
import javax.inject.Inject

/**
 * 마음의 기록 전달이 enabled인 수신자 ID 목록을 반환합니다.
 * GET /users/receivers 응답의 mindRecordDeliveryEnabled가 true인 수신자만 포함합니다.
 *
 * @return enabled 수신자 ID 목록 (빈 목록이면 전달 대상 없음)
 */
class GetEnabledReceiversForMindRecordUseCase
    @Inject
    constructor(
        private val getUserIdUseCase: GetUserIdUseCase,
        private val getReceiversUseCase: GetReceiversUseCase
    ) {
    suspend operator fun invoke(): List<Long> {
        val userId = getUserIdUseCase() ?: return emptyList()
        return getReceiversUseCase(userId)
            .getOrNull()
            ?.filter { it.mindRecordDeliveryEnabled }
            ?.map { it.receiverId }
            ?: emptyList()
    }
}
