package com.kuit.afternote.feature.dailyrecord.presentation.screen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordDiaryQuestionContentItem
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordSubTopbar

@Composable
fun RecordQuestionScreen(
    modifier: Modifier = Modifier,
    onLeftClick: () -> Unit,
    onCreateRight: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars) // 상태바 만큼 패딩을 줘서 겹치지 않도록
        ) {
            item {
                RecordSubTopbar(
                    text = "데일리 질문 답변",
                    onLeftClock = onLeftClick,
                    onRightClick = { onCreateRight }
                )
            }

            item {
                RecordDiaryQuestionContentItem(
                    title = title,
                    onTitleChange = { title = it },
                    content = content,
                    onContentChange = { content = it }
                )
            }
        }
    }
}
