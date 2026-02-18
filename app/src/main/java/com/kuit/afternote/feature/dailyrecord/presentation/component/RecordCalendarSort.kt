package com.kuit.afternote.feature.dailyrecord.presentation.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.White
import java.time.LocalDate
import java.time.YearMonth

private fun parseMarkedDaysInMonth(markedDates: Set<String>, year: Int, month: Int): Set<Int> =
    markedDates.mapNotNull { dateStr ->
        runCatching {
            val parts = dateStr.split("-")
            if (parts.size == 3 &&
                parts[0].toIntOrNull() == year &&
                parts[1].toIntOrNull() == month
            ) {
                parts[2].toIntOrNull()
            } else null
        }.getOrNull()
    }.filterNotNull().toSet()

@Composable
private fun CalendarDayCell(
    modifier: Modifier,
    day: Int,
    isSelected: Boolean,
    isMarked: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .background(
                if (isSelected) Color(0xFF328BFF) else Color.Transparent
            )
            .then(
                if (isMarked) {
                    Modifier.border(
                        width = 2.dp,
                        color = Color(0xFF328BFF),
                        shape = CircleShape
                    )
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            fontSize = 18.sp,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

@Composable
private fun CalendarGrid(
    year: Int,
    month: Int,
    markedDaysInMonth: Set<Int>,
    selectedDate: Int,
    onDateSelected: (Int) -> Unit
) {
    val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
    val startOffset = LocalDate.of(year, month, 1).dayOfWeek.value
    val totalCells = startOffset + daysInMonth
    val rows = (totalCells / 7) + if (totalCells % 7 != 0) 1 else 0

    for (row in 0 until rows) {
        Row {
            for (col in 0..6) {
                val day = row * 7 + col - startOffset + 1
                if (day in 1..daysInMonth) {
                    CalendarDayCell(
                        modifier = Modifier.weight(1f),
                        day = day,
                        isSelected = day == selectedDate,
                        isMarked = day in markedDaysInMonth,
                        onClick = { onDateSelected(day) }
                    )
                } else {
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                    )
                }
            }
        }
    }
}

/**
 * 기록들 정렬할 때 쓰이는 컴포넌트
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordCalendarSort(
    modifier: Modifier = Modifier,
    today: LocalDate,
    markedDates: Set<String> = emptySet()
) {
    val year = today.year
    val month = today.monthValue
    val markedDaysInMonth = parseMarkedDaysInMonth(markedDates, year, month)
    var selectedDate by remember { mutableStateOf(today.dayOfMonth) }

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .size(350.dp, 370.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, top = 24.dp)
                    .size(120.dp, 34.dp)
                    .background(color = White)
                    .border(
                        width = 1.dp,
                        color = Color(0xFF328BFF),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = " ${year}년 ${month}월",
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.width(4.dp))
                    Image(
                        painter = painterResource(R.drawable.ic_under),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(Color(0xFF328BFF))
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
            ) {
                CalendarGrid(
                    year = year,
                    month = month,
                    markedDaysInMonth = markedDaysInMonth,
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it }
                )
            }
        }
    }
}
