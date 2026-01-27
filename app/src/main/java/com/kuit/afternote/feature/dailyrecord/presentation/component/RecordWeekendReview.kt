package com.kuit.afternote.feature.dailyrecord.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp

@Composable
fun RecordWeekendReview(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        RecordTextComponent(
            title = "나의 기록 다시 읽기"
        )
    }
}
