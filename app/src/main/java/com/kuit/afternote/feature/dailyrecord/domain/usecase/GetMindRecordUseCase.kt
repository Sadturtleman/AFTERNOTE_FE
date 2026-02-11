package com.kuit.afternote.feature.dailyrecord.domain.usecase

import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordDetailResponse
import com.kuit.afternote.feature.dailyrecord.data.repository.MindRecordRepository
import javax.inject.Inject

class GetMindRecordUseCase @Inject constructor(
    private val repository: MindRecordRepository
) {
    suspend operator fun invoke(recordId: Long): Result<MindRecordDetailResponse> {
        return try {
            val record = repository.getMindRecord(recordId)
            Result.success(record)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

