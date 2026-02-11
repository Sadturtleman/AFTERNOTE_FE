package com.kuit.afternote.feature.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

enum class CalendarDayStyle {
    OUTLINED,
    FILLED,
    TODAY,
    DEFAULT
}

data class CalendarDay(
    val dayLabel: String,
    val date: Int,
    val style: CalendarDayStyle = CalendarDayStyle.DEFAULT
)

@Composable
fun WeeklyCalendarStrip(
    modifier: Modifier = Modifier,
    days: List<CalendarDay>
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        days.forEach { day ->
            CalendarDayColumn(day = day)
        }
    }
}

@Composable
private fun CalendarDayColumn(
    day: CalendarDay
) {
    val isToday = day.style == CalendarDayStyle.TODAY
    val columnBackground = if (isToday) B3 else Color.Transparent

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(40.dp))
            .background(columnBackground)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Day name
        Box(
            modifier = Modifier.size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.dayLabel,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                color = when (day.style) {
                    CalendarDayStyle.TODAY -> Gray9
                    CalendarDayStyle.DEFAULT -> Gray5
                    else -> Gray6
                },
                textAlign = TextAlign.Center
            )
        }

        // Date circle
        val dateModifier = Modifier.size(32.dp)
        val dateBackgroundColor = when (day.style) {
            CalendarDayStyle.FILLED -> B3
            CalendarDayStyle.TODAY -> Color.White
            CalendarDayStyle.OUTLINED,
            CalendarDayStyle.DEFAULT -> Color.Transparent
        }
        val dateCircleModifier = when (day.style) {
            CalendarDayStyle.OUTLINED,
            CalendarDayStyle.DEFAULT -> dateModifier
                .border(width = 1.dp, color = B2, shape = CircleShape)
            CalendarDayStyle.FILLED,
            CalendarDayStyle.TODAY -> dateModifier
                .background(color = dateBackgroundColor, shape = CircleShape)
        }

        Box(
            modifier = dateCircleModifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = day.date.toString(),
                fontFamily = Sansneo,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                color = when (day.style) {
                    CalendarDayStyle.OUTLINED,
                    CalendarDayStyle.DEFAULT -> B2
                    CalendarDayStyle.FILLED -> Gray6
                    CalendarDayStyle.TODAY -> Gray9
                },
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WeeklyCalendarStripPreview() {
    AfternoteTheme {
        WeeklyCalendarStrip(
            days = listOf(
                CalendarDay("월", 10, CalendarDayStyle.OUTLINED),
                CalendarDay("화", 11, CalendarDayStyle.OUTLINED),
                CalendarDay("수", 12, CalendarDayStyle.OUTLINED),
                CalendarDay("목", 13, CalendarDayStyle.FILLED),
                CalendarDay("금", 14, CalendarDayStyle.FILLED),
                CalendarDay("토", 15, CalendarDayStyle.TODAY),
                CalendarDay("일", 16, CalendarDayStyle.DEFAULT)
            )
        )
    }
}

