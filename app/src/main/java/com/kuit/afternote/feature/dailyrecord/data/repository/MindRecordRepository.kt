package com.kuit.afternote.feature.dailyrecord.data.repository

import com.kuit.afternote.feature.dailyrecord.data.dto.CreateMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.EmotionResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordDetailResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordListResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordResponse
import com.kuit.afternote.feature.dailyrecord.domain.model.DailyQuestionData

interface MindRecordRepository {
    suspend fun getDailyQuestion(): DailyQuestionData?

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

    suspend fun getEmotions(): EmotionResponse

    /**
     * PATCH mind-records/{recordId}/receivers/{receiverId}
     * 해당 마음의 기록을 해당 수신인에게 전달할지 여부를 설정합니다.
     */
    suspend fun setMindRecordReceiverEnabled(
        recordId: Long,
        receiverId: Long,
        enabled: Boolean
    )
}
