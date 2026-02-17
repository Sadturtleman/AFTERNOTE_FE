package com.kuit.afternote.feature.receiver.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 모든 기록 내려받기 시 JSON 파일로 저장하기 위한 직렬화 전용 DTO.
 *
 * 도메인 [com.kuit.afternote.feature.receiver.domain.entity.DownloadAllResult]와
 * 동일한 구조를 가지며, kotlinx.serialization으로 직렬화합니다.
 */
@Serializable
data class DownloadAllResultExportDto(
    @SerialName("timeLetters") val timeLetters: List<ReceivedTimeLetterExportDto> = emptyList(),
    @SerialName("mindRecords") val mindRecords: List<ReceivedMindRecordExportDto> = emptyList(),
    @SerialName("afternotes") val afternotes: List<ReceivedAfternoteExportDto> = emptyList()
)

@Serializable
data class ReceivedTimeLetterExportDto(
    @SerialName("timeLetterId") val timeLetterId: Long,
    @SerialName("timeLetterReceiverId") val timeLetterReceiverId: Long,
    @SerialName("title") val title: String? = null,
    @SerialName("content") val content: String? = null,
    @SerialName("sendAt") val sendAt: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("senderName") val senderName: String? = null,
    @SerialName("deliveredAt") val deliveredAt: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("mediaList") val mediaList: List<ReceivedTimeLetterMediaExportDto> = emptyList(),
    @SerialName("isRead") val isRead: Boolean = false
)

@Serializable
data class ReceivedTimeLetterMediaExportDto(
    @SerialName("id") val id: Long,
    @SerialName("mediaType") val mediaType: String? = null,
    @SerialName("mediaUrl") val mediaUrl: String
)

@Serializable
data class ReceivedMindRecordExportDto(
    @SerialName("mindRecordId") val mindRecordId: Long,
    @SerialName("sourceType") val sourceType: String? = null,
    @SerialName("content") val content: String? = null,
    @SerialName("recordDate") val recordDate: String? = null
)

@Serializable
data class ReceivedAfternoteExportDto(
    @SerialName("sourceType") val sourceType: String? = null,
    @SerialName("lastUpdatedAt") val lastUpdatedAt: String? = null,
    @SerialName("leaveMessage") val leaveMessage: String? = null
)
