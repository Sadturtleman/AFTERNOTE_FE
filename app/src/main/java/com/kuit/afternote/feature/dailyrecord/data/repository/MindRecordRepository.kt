package com.kuit.afternote.feature.dailyrecord.data.repository

import com.kuit.afternote.feature.dailyrecord.data.dto.CreateMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordDetailResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordListResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordResponse

interface MindRecordRepository {
    suspend fun getMindRecords(): MindRecordListResponse

    suspend fun createMindRecord(request: CreateMindRecordRequest): PostMindRecordResponse

    suspend fun deleteMindRecord(recordId: Long)
    suspend fun getMindRecord(recordId: Long): MindRecordDetailResponse

    suspend fun editMindRecord(recordId: Long, request: PostMindRecordRequest): MindRecordDetailResponse


}
