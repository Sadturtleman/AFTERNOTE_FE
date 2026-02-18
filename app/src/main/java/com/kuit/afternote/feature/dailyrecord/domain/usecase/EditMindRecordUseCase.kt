package com.kuit.afternote.feature.dailyrecord.domain.usecase

import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.repository.MindRecordRepositoryImpl
import javax.inject.Inject

class EditMindRecordUseCase @Inject constructor(
    private val repository: MindRecordRepositoryImpl
) {
    suspend operator fun invoke(recordId: Long, request: PostMindRecordRequest): Result<Unit> {
        return try {
            repository.editMindRecord(recordId, request)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
