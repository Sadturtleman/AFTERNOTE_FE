package com.kuit.afternote.feature.dailyrecord.presentation.screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteListViewModel
import com.kuit.afternote.feature.dailyrecord.presentation.component.EmotionBubbleReport
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordAllSeeReport
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordCurrentWeek
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordListItem
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordTextComponent
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordWeekTotal
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordWeekendReview
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordweekendMindKeyword
import com.kuit.afternote.feature.dailyrecord.presentation.viewmodel.MindRecordViewModel
import com.kuit.afternote.ui.theme.Gray1
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordWeekendReportScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onBottomNavTabSelected: (BottomNavItem) -> Unit = {},
    mindRecordViewModel: MindRecordViewModel = hiltViewModel()
) {
    val weekState = mindRecordViewModel.totalSummary.collectAsStateWithLifecycle()
    val dailyState = mindRecordViewModel.dailyQuestionSummary.collectAsStateWithLifecycle()
    val afternoteState = mindRecordViewModel.afternoteSummary.collectAsStateWithLifecycle()
    val emotionResponse by mindRecordViewModel.emotions.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        mindRecordViewModel.loadWeeklyReportData()
    }
    val records by mindRecordViewModel.records.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        mindRecordViewModel.loadRecords("DAILY_QUESTION")
    }

    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.RECORD) }
    val today = LocalDate.now()

    val weekFields = WeekFields.of(Locale.KOREA)
    val weekOfMonth = today[weekFields.weekOfMonth()]

    // 이번 주의 월요일
    val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    // 이번 주의 일요일
    val endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(color = Gray1),
        containerColor = Gray1,
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { item ->
                    selectedBottomNavItem = item
                    onBottomNavTabSelected(item)
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            item {
                TopBar(
                    title = "주간 리포트",
                    onBackClick = onBackClick,
                )
            }
            item {
                RecordCurrentWeek(
                    today = today,
                    week = weekOfMonth,
                    startDate = startOfWeek,
                    endDate = endOfWeek
                )
            }
            item {
                RecordWeekTotal(
                    weeklySummaryUiState = weekState.value
                )
            }
            item {
                RecordAllSeeReport(
                    dailySummary = dailyState.value,
                    afterNoteSummary = afternoteState.value
                )
            }
            item {
                Spacer(Modifier.height(20.dp))
                RecordTextComponent(title = "나의 감정 키워드")
                EmotionBubbleReport(emotionResponse = emotionResponse)
            }
            item {
                RecordTextComponent(title = "나의 기록 다시 읽기")
            }
            items(records) { record ->
                RecordListItem(
                    record = record,
                    onDeleteClick = {
                        mindRecordViewModel.deleteRecord(
                            recordId = record.id,
                            recordType = record.type ?: "DAILY_QUESTION",
                            onReload = { mindRecordViewModel.loadRecords("DAILY_QUESTION") }
                        )
                    },
                    onEditClick = { recordId ->

                    }
                ) // 이제 UIModel을 그대로 넘김
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun RecordWeekendReportScreenPreview(){
    RecordWeekendReportScreen(
        onBackClick = {},
        onBottomNavTabSelected = {}
    )
}
