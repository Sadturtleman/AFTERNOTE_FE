package com.kuit.afternote.feature.receiver.presentation.screen.mindrecord

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.receiver.presentation.component.ExpandableRecordItem
import com.kuit.afternote.feature.receiver.presentation.component.ReceiverWheelDatePickerDialog
import com.kuit.afternote.feature.receiver.presentation.uimodel.MindRecordDetailUiState
import com.kuit.afternote.feature.receiver.presentation.viewmodel.MindRecordDetailViewModel
import com.kuit.afternote.feature.receiver.presentation.viewmodel.MindRecordDetailViewModelContract
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9

@Composable
@Suppress("AssignedValueIsNeverRead")
fun MindRecordDetailScreen(
    receiverId: String,
    initialSelectedDate: LocalDate? = null,
    onBackClick: () -> Unit,
    viewModel: MindRecordDetailViewModelContract = hiltViewModel<MindRecordDetailViewModel>()
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedDate = uiState.selectedDate

    LaunchedEffect(receiverId) {
        receiverId.toLongOrNull()?.let { viewModel.loadMindRecords(it) }
    }
    LaunchedEffect(initialSelectedDate) {
        initialSelectedDate?.let { viewModel.setSelectedDate(it) }
    }

    if (showDatePicker) {
        ReceiverWheelDatePickerDialog(
            initialDate = selectedDate,
            onDismiss = { showDatePicker = false },
            onConfirm = { date ->
                viewModel.setSelectedDate(date)
                showDatePicker = false
            }
        )
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                TopBar(
                    title = "마음의 기록",
                    onBackClick = { onBackClick() }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage!!,
                    modifier = Modifier.padding(20.dp),
                    color = Gray9
                )
            }
            // 상단 날짜 선택 버튼 영역
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                val dateButtonText = stringResource(
                    R.string.receiver_mindrecord_date_button_format,
                    selectedDate.monthValue,
                    selectedDate.dayOfMonth
                )
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    shape = RoundedCornerShape(20.dp),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                        width = 1.dp,
                        brush = SolidColor(B3)
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(
                        text = dateButtonText,
                        fontSize = 13.sp,
                        color = Gray9,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = B3,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // 리스트 영역
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(
                        items = uiState.mindRecordItems,
                        key = { it.mindRecordId }
                    ) { item ->
                        ExpandableRecordItem(
                            date = item.date,
                            tags = item.tags,
                            question = item.question,
                            content = item.content,
                            hasImage = item.hasImage
                        )
                        HorizontalDivider(
                            color = Gray6,
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMindRecordDetail() {
    MaterialTheme {
        MindRecordDetailScreen(
            receiverId = "1",
            onBackClick = {},
            viewModel = remember { FakeMindRecordDetailViewModel() }
        )
    }
}

/**
 * Preview용 Fake ViewModel. Hilt 없이 렌더합니다.
 */
private class FakeMindRecordDetailViewModel : MindRecordDetailViewModelContract {
    private val _uiState = MutableStateFlow(MindRecordDetailUiState())
    override val uiState: StateFlow<MindRecordDetailUiState> = _uiState.asStateFlow()

    override fun loadMindRecords(receiverId: Long) {}
    override fun setSelectedDate(date: LocalDate) {}
    override fun clearError() {}
}
