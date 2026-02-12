package com.kuit.afternote.feature.dailyrecord.domain.usecase

import com.kuit.afternote.feature.dailyrecord.data.dto.CreateMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordResponse
import com.kuit.afternote.feature.dailyrecord.data.repository.MindRecordRepositoryImpl
import javax.inject.Inject

class CreateMindRecordUseCase @Inject constructor(
    private val repository: MindRecordRepositoryImpl
) {
    suspend operator fun invoke(
        type: String,
        title: String,
        content: String,
        date: String,
        isDraft: Boolean,
        questionId: Long? = null,
        category: String? = null
    ): PostMindRecordResponse {
        val request = CreateMindRecordRequest(type, title, content, date, isDraft, questionId, category)
        return repository.createMindRecord(request)
    }
}
