package com.kuit.afternote.feature.dailyrecord.data.repository

import com.kuit.afternote.feature.dailyrecord.data.api.DailyRecordApiService
import com.kuit.afternote.feature.dailyrecord.data.dto.CreateMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordDetailResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordListResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.ReceiverEnabledRequest
import retrofit2.HttpException
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
        val response = apiService.deleteMindRecord(recordId)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }
    override suspend fun getMindRecord(recordId: Long): MindRecordDetailResponse {
        val response = apiService.getMindRecord(recordId)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        return response.body() ?: throw IllegalStateException("Response body is null")
    }

    override suspend fun editMindRecord(recordId: Long, request: PostMindRecordRequest): MindRecordDetailResponse {
        val response = apiService.editMindRecord(recordId, request)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        return response.body() ?: throw IllegalStateException("Response body is null")
    }

    override suspend fun setMindRecordReceiverEnabled(
        recordId: Long,
        receiverId: Long,
        enabled: Boolean
    ) {
        val response = apiService.setMindRecordReceiverEnabled(
            recordId,
            receiverId,
            ReceiverEnabledRequest(enabled = enabled)
        )
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }
}

