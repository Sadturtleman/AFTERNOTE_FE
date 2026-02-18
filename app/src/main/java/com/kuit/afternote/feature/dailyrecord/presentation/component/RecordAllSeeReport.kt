package com.kuit.afternote.feature.dailyrecord.presentation.component

import android.R.attr.top
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kuit.afternote.feature.dailyrecord.presentation.viewmodel.MindRecordViewModel
import java.time.LocalDate

/**
 * 활동 한 눈에 보기
 */
@Composable
fun RecordAllSeeReport(
    modifier: Modifier = Modifier,
    dailySummary: MindRecordViewModel.WeeklySummaryUiState,
    afterNoteSummary: MindRecordViewModel.WeeklySummaryUiState
) {
    Column(
        modifier = modifier
            .padding(top = 32.dp, start = 20.dp, end = 20.dp)
    ) {
        RecordTextComponent(title = "활동 한 눈에 보기")
        Spacer(Modifier.height(16.dp))

        // 1. 데일리 질문 리포트
        RecordShowWeekReport(
            titlePrefix = "데일리 질문 답변을",
            summary = dailySummary
        )

        Spacer(Modifier.height(8.dp))

        // 2. 애프터노트 전체 리포트
        RecordShowWeekReport(
            titlePrefix = "애프터노트에",
            summary = afterNoteSummary
        )
    }
}
