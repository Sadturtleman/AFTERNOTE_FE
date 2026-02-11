package com.kuit.afternote.feature.dailyrecord.domain.usecase

import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordSummary
import com.kuit.afternote.feature.dailyrecord.data.repository.MindRecordRepository
import javax.inject.Inject

class GetMindRecordsUseCase @Inject constructor(
    private val repository: MindRecordRepository
) {
    suspend operator fun invoke(): List<MindRecordSummary> {
        val response = repository.getMindRecords()
        return response.data?.records ?: emptyList()
    }
}

