package com.kuit.afternote.feature.dailyrecord.presentation.screen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordDiaryQuestionContentItem
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordSubTopbar
import com.kuit.afternote.feature.dailyrecord.presentation.uimodel.MindRecordUiModel
import com.kuit.afternote.feature.dailyrecord.presentation.viewmodel.MindRecordViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordQuestionScreen(
    record: MindRecordUiModel?,
    modifier: Modifier = Modifier,
    onLeftClick: () -> Unit,
    onRegisterSuccess: () -> Unit = {},
    viewModel: MindRecordViewModel
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(record) {
        record?.let {
            title = it.title
            content = it.content ?: ""
        }
    }

    LaunchedEffect(uiState.createErrorMessage) {
        uiState.createErrorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.clearCreateError()
        }
    }

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            item {
                RecordSubTopbar(
                    text = "데일리 질문 답변",
                    onLeftClock = onLeftClick,
                    onRightClick = {
                        if (record == null) {
                            viewModel.onCreateRecord(
                                type = "DAILY_QUESTION",
                                title = title.ifBlank { "" },
                                content = content,
                                date = LocalDate.now().toString(),
                                isDraft = false,
                                questionId = null,
                                onSuccess = onRegisterSuccess
                            )
                        } else {
                            viewModel.editRecord(
                                recordId = record.id,
                                title = title,
                                content = content,
                                date = record.originalDate,
                                type = record.type ?: "DAILY_QUESTION",
                                category = record.category,
                                isDraft = false,
                                onSuccess = onRegisterSuccess
                            )
                        }
                    }
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
