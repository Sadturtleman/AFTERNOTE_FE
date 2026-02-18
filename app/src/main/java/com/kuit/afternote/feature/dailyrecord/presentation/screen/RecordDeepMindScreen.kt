package com.kuit.afternote.feature.dailyrecord.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordDiaryContentItem
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordSubTopbar
import com.kuit.afternote.feature.dailyrecord.presentation.viewmodel.MindRecordWriterViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordDeepMindScreen(
    modifier: Modifier = Modifier,
    onLeftClick: () -> Unit,
    onRegisterSuccess: () -> Unit = {},
    viewModel: MindRecordWriterViewModel,
    recordId: Long?,
    onDateClick: () -> Unit,
    sendDate: String,
    showDatePicker: Boolean,
    onDatePickerDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars) // 상태바 만큼 패딩을 줘서 겹치지 않도록
        ) {
            item {
                RecordSubTopbar(
                    text = "깊은 생각 기록하기",
                    onLeftClock = onLeftClick,
                    onRightClick = {
                        viewModel.registerWithPopUpThenSave(type = "DEEP_THOUGHT") {
                            onRegisterSuccess()
                        }
                    },
                    isRegisterEnabled = !uiState.isLoading
                )
            }

            item {
                var showCategoryDropdown by remember { mutableStateOf(false) }
                RecordDiaryContentItem(
                    standard = "깊은 생각 기록하기",
                    onDateSelected = { year, month, day ->
                        val selectedDate = LocalDate.of(year, month, day)
                        viewModel.updateSendDate(selectedDate.format(formatter))
                        viewModel.hideDatePicker()
                    },
                    title = uiState.title,
                    onTitleChange = viewModel::updateTitle,
                    content = uiState.content,
                    onContentChange = viewModel::updateContent,
                    onDateClick = onDateClick,
                    sendDate = sendDate,
                    showDatePicker = showDatePicker,
                    onDatePickerDismiss = onDatePickerDismiss,
                    selectedCategory = uiState.category,
                    showCategoryDropdown = showCategoryDropdown,
                    onCategoryClick = { showCategoryDropdown = true },
                    onCategorySelected = viewModel::updateCategory,
                    onCategoryDropdownDismiss = { showCategoryDropdown = false }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun RecordDeepMindPrev() {
    // RecordDeepMindScreen()
}
