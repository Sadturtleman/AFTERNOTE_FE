package com.kuit.afternote.feature.dailyrecord.domain.usecase

import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordDetail
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.repository.MindRecordRepositoryImpl
import javax.inject.Inject

class EditMindRecordUseCase @Inject constructor(
    private val repository: MindRecordRepositoryImpl
) {
    suspend operator fun invoke(recordId: Long, request: PostMindRecordRequest): Result<MindRecordDetail> {
        return try {
            val response = repository.editMindRecord(recordId, request)
            response.data?.let { Result.success(it) } ?: Result.failure(Exception("No data"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
