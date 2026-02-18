package com.kuit.afternote.feature.dailyrecord.presentation.component

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.feature.dailyrecord.presentation.viewmodel.MindRecordViewModel
import com.kuit.afternote.feature.home.presentation.component.CalendarDayStyle
import com.kuit.afternote.ui.expand.dropShadow
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Sansneo
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

/**
 * 주간리포트 컴포넌트
 * 주에 몇번 기록했는 지를 보여줌
 */
@Composable
fun RecordWeekTotal(
    modifier: Modifier = Modifier,
    // today는 상징적 의미 외에 UI 로직에서는 ViewModel의 calendarDays가 우선입니다.
    weeklySummaryUiState: MindRecordViewModel.WeeklySummaryUiState
) {
    Log.d("state", weeklySummaryUiState.calendarDays.toString())
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            // 총 횟수를 보여주는 것이 사용자 경험(UX) 측면에서 더 명확합니다.
            text = "이번 주 박서연님은 ${weeklySummaryUiState.totalWeeklyCount}번의 마음을 기록하셨네요.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Sansneo,
            color = Color(0xFF222222)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(90.dp)
                .dropShadow(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Black.copy(alpha = 0.05f),
                    blur = 5.dp,
                    offsetY = 2.dp
                )
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                // ViewModel에서 계산된 7일간의 데이터를 기반으로 UI 렌더링
                weeklySummaryUiState.calendarDays.forEach { dayState ->
                    SubWeekCheck(
                        modifier = Modifier.weight(1f),
                        dayLabel = dayState.dayLabel, // "월", "화" 등
                        dateLabel = dayState.date.toString(), // "10", "11" 등
                        // FILLED 상태일 때 하늘색 동그라미를 활성화합니다.
                        isRecorded = dayState.style == CalendarDayStyle.FILLED,
                        isToday = dayState.style == CalendarDayStyle.TODAY
                    )
                }
            }
        }
    }
}

@Composable
fun SubWeekCheck(
    modifier: Modifier = Modifier,
    dayLabel: String,
    dateLabel: String,
    isRecorded: Boolean,
    isToday: Boolean
) {
    Column(
        modifier = modifier,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(
            text = dayLabel,
            fontSize = 12.sp,
            color = if (isToday) Color(0xFF222222) else Color(0xFFAAAAAA),
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
        )

        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(32.dp) // 동그라미 크기 고정
                .clip(androidx.compose.foundation.shape.CircleShape)
                // 기록됨(isRecorded) 상태일 때 하늘색 배경 적용
                .background(
                    when {
                        isRecorded -> B3// 밝은 하늘색
                        else -> Color.Transparent
                    }
                ),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(
                text = dateLabel,
                fontSize = 14.sp,
                fontWeight = if (isRecorded || isToday) FontWeight.Bold else FontWeight.Medium,
                color = if (isRecorded) Color(0xFF007AFF) else Color(0xFF444444) // 기록 시 텍스트는 진한 파랑
            )
        }
    }
}
