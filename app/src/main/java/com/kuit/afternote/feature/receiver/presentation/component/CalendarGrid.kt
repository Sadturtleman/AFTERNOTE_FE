package com.kuit.afternote.feature.receiver.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import java.time.LocalDate
import java.time.YearMonth

private val RECORD_DAY_BORDER_COLOR = Color(0xFF328BFF)
private val FUTURE_DAY_TEXT_COLOR = Color(0xFFBDBDBD)

/**
 * 특정 연·월의 일자를 7열 그리드로 표시하는 캘린더.
 *
 * @param displayYearMonth 표시할 연·월 (해당 월 일수·첫날 요일 계산에 사용)
 * @param selectedDay 선택된 일 (1 ~ lengthOfMonth)
 * @param daysWithRecords 기록이 있는 일의 집합 (1 ~ lengthOfMonth)
 * @param onDaySelected 일 선택 시 콜백 (1 ~ lengthOfMonth)
 */
@Composable
fun CalendarGrid(
    displayYearMonth: YearMonth,
    selectedDay: Int,
    daysWithRecords: Set<Int> = emptySet(),
    onDaySelected: (Int) -> Unit
) {
    val daysInMonth = displayYearMonth.lengthOfMonth()
    val days = (1..daysInMonth).toList()
    val firstDayOfWeek = displayYearMonth.atDay(1).dayOfWeek
    val paddingDays = firstDayOfWeek.value % 7
    val totalSlots = paddingDays + days.size
    val rows = (totalSlots + 6) / 7
    val today = LocalDate.now()
    val isDisplayedMonthCurrent = displayYearMonth.equals(YearMonth.from(today))

    Column {
        for (row in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (col in 0 until 7) {
                    val index = row * 7 + col
                    val day = index - paddingDays + 1

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (day in 1..daysInMonth) {
                            val isSelected = day == selectedDay
                            val hasRecords = day in daysWithRecords
                            val isFuture = isDisplayedMonthCurrent && day > today.dayOfMonth
                            val borderColor = when {
                                hasRecords -> RECORD_DAY_BORDER_COLOR
                                isSelected -> B1
                                else -> Color.White
                            }
                            val shape = if (hasRecords) RoundedCornerShape(40.dp) else CircleShape
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(shape)
                                    .border(
                                        width = 1.dp,
                                        color = borderColor,
                                        shape = shape
                                    )
                                    .background(Color.Transparent)
                                    .clickable { onDaySelected(day) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    color = if (isFuture) FUTURE_DAY_TEXT_COLOR else Gray9,
                                    fontSize = 18.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontFamily = Sansneo
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
