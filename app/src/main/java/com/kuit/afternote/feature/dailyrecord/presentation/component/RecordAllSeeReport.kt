package com.kuit.afternote.feature.dailyrecord.presentation.component

import android.R.attr.top
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate

/**
 * 활동 한 눈에 보기
 */
@Composable
fun RecordAllSeeReport(
    modifier: Modifier = Modifier,
    today: LocalDate
) {
    Column(
        modifier = Modifier
            .padding(top = 32.dp)
            .padding(horizontal = 20.dp)
    ) {
        RecordTextComponent(
            title = "활동 한 눈에 보기"
        )
        Spacer(Modifier.height(16.dp))
        RecordShowWeekReport(
            standard = "데일리 질문 답변을 3번 작성했어요.",
            today = today
        )
        Spacer(Modifier.height(8.dp))
        RecordShowWeekReport(
            standard = "애프터노트에 3번 등록했어요.",
            today = today
        )
    }
}
