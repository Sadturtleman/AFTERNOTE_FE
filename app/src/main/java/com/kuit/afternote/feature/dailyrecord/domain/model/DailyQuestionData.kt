package com.kuit.afternote.feature.dailyrecord.domain.model

/**
 * 오늘의 데일리 질문 정보.
 * GET /daily-question API 응답을 도메인 모델로 변환한 결과.
 *
 * @param questionId 데일리 질문 ID (POST /mind-records 시 필수)
 * @param content 질문 내용 (UI 표시용)
 */
data class DailyQuestionData(
    val questionId: Long,
    val content: String
)
