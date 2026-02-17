package com.kuit.afternote.feature.dailyrecord.data.repository

import com.kuit.afternote.feature.dailyrecord.data.dto.CreateMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordDetailResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordListResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordResponse
interface MindRecordRepository {
    suspend fun getDailyQuestion(): Long?

    suspend fun getMindRecords(
        type: String,
        view: String = "LIST",
        year: Int? = null,
        month: Int? = null
    ): MindRecordListResponse

    suspend fun createMindRecord(request: CreateMindRecordRequest): PostMindRecordResponse

    suspend fun deleteMindRecord(recordId: Long)
    suspend fun getMindRecord(recordId: Long): MindRecordDetailResponse

    suspend fun editMindRecord(recordId: Long, request: PostMindRecordRequest): MindRecordDetailResponse

    /**
     * Sets whether a specific mind record is delivered to a specific receiver.
     * PATCH /mind-records/{recordId}/receivers/{receiverId}
     *
     * @param recordId 마음의 기록 ID
     * @param receiverId 수신인 ID
     * @param enabled true → 해당 수신인에게 전달, false → 전달 중단
     */
    suspend fun setMindRecordReceiverEnabled(
        recordId: Long,
        receiverId: Long,
        enabled: Boolean
    )
}
