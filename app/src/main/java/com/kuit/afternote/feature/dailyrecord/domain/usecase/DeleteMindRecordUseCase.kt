package com.kuit.afternote.feature.dailyrecord.domain.usecase

import com.kuit.afternote.feature.dailyrecord.data.repository.MindRecordRepository
import javax.inject.Inject

class DeleteMindRecordUseCase @Inject constructor(
    private val repository: MindRecordRepository
) {
    suspend operator fun invoke(recordId: Long): Result<Unit> {
        return try {
            repository.deleteMindRecord(recordId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
