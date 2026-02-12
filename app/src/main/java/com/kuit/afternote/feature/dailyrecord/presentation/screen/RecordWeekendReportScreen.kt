package com.kuit.afternote.feature.dailyrecord.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordAllSeeReport
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordCurrentWeek
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordWeekTotal
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordWeekendReview
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordweekendMindKeyword
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
    onBottomNavTabSelected: (BottomNavItem) -> Unit = {}
) {
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                modifier = Modifier.size(56.dp),
                containerColor = Color.Transparent,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFBDE0FF), // B3
                                    Color(0xFFFFE1CC) // 주황색 계열
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "새 애프터노트 추가",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
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
                    today = today
                )
            }
            item {
                RecordAllSeeReport(
                    today = today
                )
            }
            item {
                RecordweekendMindKeyword()
            }
            item {
                RecordWeekendReview()
            }
        }
    }
}
