package com.kuit.afternote.feature.dailyrecord.domain.usecase

import com.kuit.afternote.feature.dailyrecord.data.dto.CreateMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordResponse
import com.kuit.afternote.feature.dailyrecord.data.repository.MindRecordRepositoryImpl
import javax.inject.Inject

/**
 * 마음의 기록 게시물 생성 UseCase.
 * enabled 수신자(mindRecordDeliveryEnabled=true)에게만 전달되도록 receiverIds를 포함합니다.
 *
 * @param type DAILY_QUESTION / DIARY / DEEP_THOUGHT
 * @param title 기록 제목
 * @param content 기록 내용
 * @param date 기록 날짜 (yyyy-MM-dd)
 * @param isDraft 임시저장 여부
 * @param questionId 데일리 질문 ID (DAILY_QUESTION일 때)
 * @param category 깊은 생각 카테고리 (DEEP_THOUGHT일 때)
 */
class CreateMindRecordUseCase @Inject constructor(
    private val repository: MindRecordRepositoryImpl,
    private val getEnabledReceiversForMindRecordUseCase: GetEnabledReceiversForMindRecordUseCase
) {
    suspend operator fun invoke(
        type: String,
        title: String?,
        content: String?,
        date: String?,
        isDraft: Boolean,
        questionId: Long? = null,
        category: String? = null
    ): Result<PostMindRecordResponse> {
        return try {
            val receiverIds = getEnabledReceiversForMindRecordUseCase()
            val request = CreateMindRecordRequest(
                type = type,
                title = title,
                content = content ?: "",
                date = date,
                isDraft = isDraft,
                questionId = questionId,
                category = category,
                receiverIds = receiverIds.ifEmpty { null }
            )
            val response = repository.createMindRecord(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
