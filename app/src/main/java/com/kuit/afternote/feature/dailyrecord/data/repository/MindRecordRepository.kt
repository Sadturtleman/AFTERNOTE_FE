package com.kuit.afternote.feature.dailyrecord.data.repository

import com.kuit.afternote.feature.dailyrecord.data.api.DailyRecordApiService
import com.kuit.afternote.feature.dailyrecord.data.dto.CreateMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordDetailResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordListResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.UpdateMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.UpdateMindRecordResponse
import retrofit2.Response
import javax.inject.Inject

class MindRecordRepository @Inject constructor(
    private val apiService: DailyRecordApiService
) {
    suspend fun getMindRecords(): MindRecordListResponse {
        return apiService.getMindRecords()
    }

    suspend fun createMindRecord(request: CreateMindRecordRequest): PostMindRecordResponse {
        return apiService.createMindRecord(request)
    }

    suspend fun deleteMindRecord(recordId: Long) {
        apiService.deleteMindRecord(recordId)
    }
    suspend fun getMindRecord(recordId: Long): MindRecordDetailResponse {
        val response = apiService.getMindRecord(recordId)
        return response.body() ?: throw IllegalStateException("Response body is null")
    }

    suspend fun editMindRecord(recordId: Long, request: PostMindRecordRequest): MindRecordDetailResponse {
        val response = apiService.editMindRecord(recordId, request)
        return response.body() ?: throw IllegalStateException("Response body is null")
    }


}

