package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.feature.user.domain.model.ReceiverDailyQuestionsResult
import com.kuit.afternote.feature.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 수신인별 데일리 질문 답변 목록 조회 UseCase (페이지네이션).
 * GET /users/receivers/{receiverId}/daily-questions
 */
class GetReceiverDailyQuestionsUseCase
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
        ): Result<ReceiverDailyQuestionsResult> =
            userRepository.getReceiverDailyQuestions(
                receiverId = receiverId,
                page = page,
                size = size
            )
    }
