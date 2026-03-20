package com.kuit.afternote.feature.afternote.presentation.mapper

import com.kuit.afternote.feature.afternote.domain.model.Item
import com.kuit.afternote.feature.dailyrecord.presentation.uimodel.MindRecordUiModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Item.toMindRecordUiModel(): MindRecordUiModel {
    // 1. 날짜 포맷팅 로직 (안정성을 위해 java.time 사용)
    val inputFormatter =
        DateTimeFormatter
            .ofPattern("yyyy.MM.dd")
    val isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE // yyyy-MM-dd

    val parsedDate =
        runCatching {
            LocalDate.parse(this.date, inputFormatter)
        }.getOrElse {
            LocalDate.now() // 파싱 실패 시 오늘 날짜로 폴백 (시스템 안정성)
        }

    val originalDateStr = parsedDate.format(isoFormatter) // "2026-02-06"
    val formattedDateStr = "${parsedDate.monthValue}월 ${parsedDate.dayOfMonth}일"

    return MindRecordUiModel(
        // String ID를 Long으로 변환 (UI용 unique key 확보)
        id = this.id.hashCode().toLong(),
        title = this.serviceName,
        formattedDate = formattedDateStr,
        draftLabel = "완료", // 애프터노트는 도메인상 완료된 데이터로 간주
        content = this.processing.message,
        type = this.type.name,
        category = null, // 필요 시 mapping 로직 추가
        originalDate = originalDateStr,
    )
}
