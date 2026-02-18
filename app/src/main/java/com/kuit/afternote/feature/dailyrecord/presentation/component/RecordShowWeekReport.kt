package com.kuit.afternote.feature.dailyrecord.presentation.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.feature.dailyrecord.presentation.viewmodel.MindRecordViewModel
import com.kuit.afternote.feature.home.presentation.component.CalendarDayStyle
import com.kuit.afternote.ui.expand.dropShadow
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@Composable
fun RecordShowWeekReport(
    titlePrefix: String,
    summary: MindRecordViewModel.WeeklySummaryUiState
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize() // 확장 시 애니메이션 추가
            .dropShadow(
                shape = RoundedCornerShape(16.dp),
                color = Color.Black.copy(alpha = 0.05f),
                blur = 5.dp,
                offsetY = 2.dp
            )
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { isExpanded = !isExpanded }
    ) {
        Column(modifier = Modifier.padding(17.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$titlePrefix ${summary.totalWeeklyCount}번 작성했어요.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Sansneo
                )
                Image(
                    painter = painterResource(R.drawable.ic_under_direct_foreground),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(if (isExpanded) 180f else 0f) // 화살표 회전
                )
            }

            if (isExpanded) {
                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    summary.calendarDays.forEach { dayState ->
                        SubWeekCheck(
                            modifier = Modifier.weight(1f),
                            dayLabel = dayState.dayLabel,
                            dateLabel = dayState.date.toString(),
                            isRecorded = dayState.style == CalendarDayStyle.FILLED,
                            isToday = dayState.style == CalendarDayStyle.TODAY
                        )
                    }
                }
            }
        }
    }
}
