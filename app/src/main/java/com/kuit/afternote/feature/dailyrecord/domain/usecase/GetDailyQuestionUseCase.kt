package com.kuit.afternote.feature.dailyrecord.domain.usecase

import com.kuit.afternote.feature.dailyrecord.data.repository.MindRecordRepositoryImpl
import javax.inject.Inject

/**
 * 오늘의 데일리 질문 ID를 조회합니다.
 * DAILY_QUESTION 타입 기록 작성 시 questionId가 필수이므로 등록 전에 호출합니다.
 *
 * @return 오늘의 질문 ID, 실패 시 null
 */
class GetDailyQuestionUseCase
@Inject
constructor(
    private val repository: MindRecordRepositoryImpl
) {
    suspend operator fun invoke(): Long? = repository.getDailyQuestion()
}
