package com.kuit.afternote.feature.dailyrecord.domain.usecase

import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordSummary
import com.kuit.afternote.feature.dailyrecord.data.repository.MindRecordRepositoryImpl
import javax.inject.Inject

class GetMindRecordsUseCase @Inject constructor(
    private val repository: MindRecordRepositoryImpl
) {
    suspend operator fun invoke(
        type: String,
        view: String = "LIST",
        year: Int? = null,
        month: Int? = null
    ): List<MindRecordSummary> {
        val response = repository.getMindRecords(type, view, year, month)
        return response.data?.records ?: emptyList()
    }
}

