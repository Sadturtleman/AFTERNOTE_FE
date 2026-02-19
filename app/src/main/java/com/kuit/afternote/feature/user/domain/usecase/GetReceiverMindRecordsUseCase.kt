package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.feature.user.domain.model.ReceiverMindRecordsResult
import com.kuit.afternote.feature.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 수신인별 마음의 기록 전체 조회 UseCase (일기, 깊은 생각, 데일리 질문 답변).
 * GET /users/receivers/{receiverId}/mind-records
 * API 미구현 시 daily-questions로 fallback.
 */
class GetReceiverMindRecordsUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository
    ) {
        /**
         * @param receiverId 수신인 식별자
         * @param page 페이지 번호 (0부터 시작)
         * @param size 페이지 당 조회 개수
         */
        suspend operator fun invoke(
            receiverId: Long,
            page: Int,
            size: Int
        ): Result<ReceiverMindRecordsResult> =
            userRepository.getReceiverMindRecords(
                receiverId = receiverId,
                page = page,
                size = size
            )
    }
