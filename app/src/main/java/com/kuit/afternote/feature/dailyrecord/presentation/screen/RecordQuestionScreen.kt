package com.kuit.afternote.feature.dailyrecord.presentation.screen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordDiaryQuestionContentItem
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordSubTopbar

@Composable
fun RecordQuestionScreen(
    modifier: Modifier = Modifier,
    onLeftClick: () -> Unit
    ) {
    Scaffold(

    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)//상태바 만큼 패딩을 줘서 겹치지 않도록

        ) {
            item {
                RecordSubTopbar(
                    text = "데일리 질문 답변",
                    onLeftClock = onLeftClick,
                    onRightClick = {}
                )
            }

            item {
                RecordDiaryQuestionContentItem()
            }
        }

    }
}

