package com.kuit.afternote.feature.dailyrecord.domain.usecase

import com.kuit.afternote.feature.dailyrecord.data.dto.CreateMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordResponse
import com.kuit.afternote.feature.dailyrecord.data.repository.MindRecordRepositoryImpl
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetter
import javax.inject.Inject
/**
 * 마음의 기록 게시물 생성 UseCase. (임시저장 DRAFT / 정식등록 SCHEDULED)
 *
 * @param title 제목
 * @param content 내용
 * @param status 상태 (DRAFT / SCHEDULED)
 * @param mediaList 미디어 목록 (타입, URL 쌍)ㅇ
 * @return [Result] of [TimeLetter]
 */
class CreateMindRecordUseCase @Inject constructor(
    private val repository: MindRecordRepositoryImpl
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
            val request = CreateMindRecordRequest(
                type = type,
                title = title,
                content = content ?: "",
                date = date,
                isDraft = isDraft,
                questionId = questionId,
                category = category
            )
            val response = repository.createMindRecord(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
