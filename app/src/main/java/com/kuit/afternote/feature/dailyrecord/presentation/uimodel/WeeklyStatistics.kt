package com.kuit.afternote.feature.dailyrecord.presentation.uimodel

import java.time.LocalDate

data class WeeklyStatistics(
    val totalCount: Int = 0,
    val dayCounts: Map<LocalDate, Int> = emptyMap() // 날짜별 작성 건수
)
