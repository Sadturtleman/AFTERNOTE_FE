package com.kuit.afternote.feature.dailyrecord.domain.usecase

import com.kuit.afternote.feature.dailyrecord.data.repository.MindRecordRepositoryImpl
import com.kuit.afternote.feature.dailyrecord.domain.model.DailyQuestionData
import javax.inject.Inject

/**
 * 오늘의 데일리 질문을 조회합니다.
 * DAILY_QUESTION 타입 기록 작성 시 questionId가 필수이므로 등록 전에 호출합니다.
 * content는 UI에 질문 텍스트를 표시할 때 사용합니다.
 *
 * @return 오늘의 질문 (questionId, content), 실패 시 null
 */
class GetDailyQuestionUseCase
@Inject
constructor(
    private val repository: MindRecordRepositoryImpl
) {
    suspend operator fun invoke(): DailyQuestionData? = repository.getDailyQuestion()
}
