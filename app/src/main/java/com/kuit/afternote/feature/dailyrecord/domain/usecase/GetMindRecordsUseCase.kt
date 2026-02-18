package com.kuit.afternote.feature.dailyrecord.domain.usecase

import com.kuit.afternote.feature.dailyrecord.data.repository.MindRecordRepositoryImpl
import com.kuit.afternote.feature.dailyrecord.domain.model.MindRecordsResult
import javax.inject.Inject

class GetMindRecordsUseCase @Inject constructor(
    private val repository: MindRecordRepositoryImpl
) {
    suspend operator fun invoke(
        type: String,
        view: String,
        year: Int?,
        month: Int?
    ): MindRecordsResult {
        val response = repository.getMindRecords(type, view, year, month)
        val data = response.data
        return MindRecordsResult(
            records = data?.records ?: emptyList(),
            markedDates = data?.markedDates ?: emptyList()
        )
    }
}

