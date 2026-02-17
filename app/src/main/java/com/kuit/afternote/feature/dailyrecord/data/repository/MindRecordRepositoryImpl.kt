package com.kuit.afternote.feature.dailyrecord.data.repository

import com.kuit.afternote.feature.dailyrecord.data.api.DailyRecordApiService
import com.kuit.afternote.feature.dailyrecord.data.dto.CreateMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordDetailResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordListResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordResponse
import javax.inject.Inject

class MindRecordRepositoryImpl @Inject constructor(
    private val apiService: DailyRecordApiService
) : MindRecordRepository {
    override suspend fun getDailyQuestion(): Long? = try {
        apiService.getDailyQuestion().questionId
    } catch (e: Exception) {
        null
    }

    override suspend fun getMindRecords(
        type: String,
        view: String,
        year: Int?,
        month: Int?
    ): MindRecordListResponse {
        return apiService.getMindRecords(type = type, view = view, year = year, month = month)
    }

    override suspend fun createMindRecord(request: CreateMindRecordRequest): PostMindRecordResponse {
        return apiService.createMindRecord(request)
    }

    override suspend fun deleteMindRecord(recordId: Long) {
        apiService.deleteMindRecord(recordId)
    }
    override suspend fun getMindRecord(recordId: Long): MindRecordDetailResponse {
        val response = apiService.getMindRecord(recordId)
        return response.body() ?: throw IllegalStateException("Response body is null")
    }

    override suspend fun editMindRecord(recordId: Long, request: PostMindRecordRequest): MindRecordDetailResponse {
        val response = apiService.editMindRecord(recordId, request)
        return response.body() ?: throw IllegalStateException("Response body is null")
    }


}

