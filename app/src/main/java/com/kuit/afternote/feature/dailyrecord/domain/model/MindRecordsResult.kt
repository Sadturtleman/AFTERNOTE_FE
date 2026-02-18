package com.kuit.afternote.feature.dailyrecord.domain.model

import com.kuit.afternote.feature.dailyrecord.data.dto.MindRecordSummary

/**
 * 마음의 기록 목록 조회 결과 (records + markedDates)
 */
data class MindRecordsResult(
    val records: List<MindRecordSummary>,
    val markedDates: List<String> = emptyList()
)
