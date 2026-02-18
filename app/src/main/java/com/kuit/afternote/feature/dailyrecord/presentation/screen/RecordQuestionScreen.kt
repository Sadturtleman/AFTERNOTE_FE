package com.kuit.afternote.feature.dailyrecord.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordDiaryQuestionContentItem
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordSubTopbar
import com.kuit.afternote.feature.dailyrecord.presentation.uimodel.MindRecordUiModel
import com.kuit.afternote.feature.dailyrecord.presentation.viewmodel.CreateRecordParams
import com.kuit.afternote.feature.dailyrecord.presentation.viewmodel.EditRecordParams
import com.kuit.afternote.feature.dailyrecord.presentation.viewmodel.MindRecordViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordQuestionScreen(
    record: MindRecordUiModel?,
    modifier: Modifier = Modifier,
    onLeftClick: () -> Unit,
    viewModel: MindRecordViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val questionText = uiState.dailyQuestionText ?: ""
    var content by remember { mutableStateOf(record?.content ?: "") }

    LaunchedEffect(record) {
        if (record == null) {
            viewModel.loadDailyQuestion()
        } else {
            content = record.content ?: ""
        }
    }

    fun onSaveClick() {
        if (record != null) {
            viewModel.editRecord(
                params = EditRecordParams(
                    recordId = record.id,
                    title = record.title ?: questionText,
                    content = content,
                    date = record.originalDate,
                    type = record.type ?: "DAILY_QUESTION",
                    category = record.category,
                    isDraft = false
                ),
                onSuccess = onLeftClick
            )
        } else {
            viewModel.onCreateRecord(
                params = CreateRecordParams(
                    type = "DAILY_QUESTION",
                    title = questionText,
                    content = content,
                    date = LocalDate.now().toString(),
                    isDraft = false
                ),
                onSuccess = onLeftClick
            )
        }
    }

    Scaffold { _ ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            item {
                RecordSubTopbar(
                    text = "데일리 질문 답변",
                    onLeftClock = onLeftClick,
                    onRightClick = { onSaveClick() }
                )
            }

            item {
                RecordDiaryQuestionContentItem(
                    questionText = questionText,
                    content = content,
                    onContentChange = { content = it }
                )
            }
        }
    }
}
