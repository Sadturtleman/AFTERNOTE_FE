package com.kuit.afternote.feature.receiver.presentation.screen.mindrecord

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.receiver.presentation.component.CalendarGrid
import com.kuit.afternote.feature.receiver.presentation.component.ReceiverWheelDatePickerDialog
import com.kuit.afternote.feature.receiver.presentation.component.RecordListItem
import com.kuit.afternote.feature.receiver.presentation.component.TodayRecordCard
import com.kuit.afternote.feature.receiver.presentation.uimodel.MindRecordItemUiModel
import com.kuit.afternote.feature.receiver.presentation.uimodel.MindRecordListUiState
import com.kuit.afternote.feature.receiver.presentation.viewmodel.MindRecordViewModel
import com.kuit.afternote.feature.receiver.presentation.viewmodel.MindRecordViewModelContract
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
@Suppress("AssignedValueIsNeverRead")
fun MindRecordScreen(
    showBottomBar: Boolean = true,
    receiverId: String = "1",
    onBackClick: () -> Unit = {},
    onNavigateToDetail: (LocalDate) -> Unit = {},
    viewModel: MindRecordViewModelContract = hiltViewModel<MindRecordViewModel>()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.RECORD) }

    LaunchedEffect(receiverId) {
        receiverId.toLongOrNull()?.let { viewModel.loadMindRecords(it) }
    }

    if (uiState.showDatePicker) {
        ReceiverWheelDatePickerDialog(
            initialDate = uiState.selectedDate,
            onDismiss = { viewModel.setShowDatePicker(false) },
            onConfirm = { date ->
                viewModel.setSelectedDate(date)
                viewModel.setShowDatePicker(false)
            }
        )
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                TopBar(
                    title = stringResource(R.string.receiver_mindrecord_title),
                    onBackClick = onBackClick
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    selectedItem = selectedBottomNavItem,
                    onItemSelected = { selectedBottomNavItem = it }
                )
            }
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(20.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                if (uiState.errorMessage != null) {
                    item {
                        Text(
                            text = uiState.errorMessage!!,
                            color = Gray9,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }

                item {
                    Column(modifier = Modifier.padding(vertical = 10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.receiver_mindrecord_today_section),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                fontFamily = Sansneo
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = B2
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        TodayRecordCard(todayRecord = uiState.todayRecord)
                    }
                }

                item {
                    Column {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(R.string.receiver_mindrecord_date_section),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = Sansneo,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(vertical = 20.dp)) {
                                val yearMonthText = stringResource(
                                    R.string.receiver_mindrecord_year_month_format,
                                    uiState.selectedDate.year,
                                    uiState.selectedDate.monthValue
                                )
                                OutlinedButton(
                                    onClick = { viewModel.setShowDatePicker(true) },
                                    shape = RoundedCornerShape(20.dp),
                                    border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                                        width = 1.dp,
                                        brush = SolidColor(B3)
                                    ),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text(
                                        text = yearMonthText,
                                        fontSize = 12.sp,
                                        color = Gray9,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                CalendarGrid(
                                    displayYearMonth = YearMonth.from(uiState.selectedDate),
                                    selectedDay = uiState.selectedDate.dayOfMonth,
                                    daysWithRecords = uiState.daysWithRecords,
                                    onDaySelected = { day ->
                                        viewModel.setSelectedDate(
                                            uiState.selectedDate.withDayOfMonth(day)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    Column(modifier = Modifier.padding(vertical = 20.dp)) {
                        if (uiState.selectedDateRecords.isEmpty()) {
                            val dateDisplay =
                                uiState.selectedDate.format(
                                    DateTimeFormatter.ofPattern("yyyy년 M월 d일")
                                )
                            Text(
                                text = dateDisplay,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Gray9,
                                fontFamily = Sansneo
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.receiver_mindrecord_no_record_yet),
                                fontSize = 16.sp,
                                color = Gray9,
                                fontFamily = Sansneo
                            )
                        } else {
                            uiState.selectedDateRecords.forEach { item ->
                                RecordListItem(
                                    date = item.date,
                                    content = item.question.ifBlank { item.content }
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        ClickButton(
                            color = B3,
                            title = stringResource(R.string.receiver_mindrecord_confirm),
                            onButtonClick = { onNavigateToDetail(uiState.selectedDate) }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMindRecord() {
    MindRecordScreen(
        viewModel = remember { FakeMindRecordViewModel() }
    )
}

private class FakeMindRecordViewModel : MindRecordViewModelContract {
    private val _uiState = MutableStateFlow(
        MindRecordListUiState(
            selectedDate = LocalDate.of(2026, 11, 10),
            daysWithRecords = setOf(10, 11),
            selectedDateRecords = listOf(
                MindRecordItemUiModel(
                    mindRecordId = 1L,
                    date = "2026년 11월 10일",
                    tags = "#감사 #가족",
                    question = "오늘 하루, 누구에게 가장 고마웠나요?",
                    content = "아무 말 없이 내 옆을 지켜주는 남편이 너무 고맙다.",
                    hasImage = false
                ),
                MindRecordItemUiModel(
                    mindRecordId = 2L,
                    date = "2026년 11월 10일",
                    tags = "#일상",
                    question = "오늘의 일기",
                    content = "오늘은 병원에서 검사 결과가 안좋게 나와서...",
                    hasImage = false
                )
            ),
            todayRecord = MindRecordItemUiModel(
                mindRecordId = 1L,
                date = "2026년 11월 10일",
                tags = "#감사 #가족",
                question = "오늘 하루, 누구에게 가장 고마웠나요?",
                content = "",
                hasImage = false
            )
        )
    )
    override val uiState: StateFlow<MindRecordListUiState> = _uiState.asStateFlow()

    override fun loadMindRecords(receiverId: Long) {}
    override fun setSelectedDate(date: LocalDate) {}
    override fun setShowDatePicker(show: Boolean) {}
    override fun clearError() {}
}
