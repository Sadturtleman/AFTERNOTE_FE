package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.feature.user.domain.model.DailyQuestionAnswerItem
import com.kuit.afternote.feature.user.domain.repository.UserRepository
import javax.inject.Inject

/**
 * 수신인별 데일리 질문 답변 목록 조회 UseCase.
 * GET /users/receivers/{receiverId}/daily-questions
 */
class GetReceiverDailyQuestionsUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository
    ) {
        suspend operator fun invoke(receiverId: Long): Result<List<DailyQuestionAnswerItem>> =
            userRepository.getReceiverDailyQuestions(receiverId = receiverId)
    }
