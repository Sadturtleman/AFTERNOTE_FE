package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.sd.lib.compose.wheel_picker.FVerticalWheelPicker
import com.sd.lib.compose.wheel_picker.rememberFWheelPickerState
import java.time.LocalDate

/**
 * 발송 날짜 Wheel Picker
 */
@Composable
fun DateWheelPicker(
    modifier: Modifier = Modifier,
    initialYear: Int = LocalDate.now().year,
    initialMonth: Int = LocalDate.now().monthValue,
    initialDay: Int = LocalDate.now().dayOfMonth,
    onDateChanged: (year: Int, month: Int, day: Int) -> Unit
) {
    val currentYear = LocalDate.now().year
    val years = (currentYear..currentYear + 10).toList()
    val months = (1..12).toList()

    var selectedYear by remember { mutableIntStateOf(initialYear) }
    var selectedMonth by remember { mutableIntStateOf(initialMonth) }
    var selectedDay by remember { mutableIntStateOf(initialDay) }

    // 월에 따른 일수 계산
    val daysInMonth = remember(selectedYear, selectedMonth) {
        LocalDate.of(selectedYear, selectedMonth, 1).lengthOfMonth()
    }
    val days = (1..daysInMonth).toList()

    // 일이 월의 일수를 초과하면 조정
    LaunchedEffect(daysInMonth) {
        if (selectedDay > daysInMonth) {
            selectedDay = daysInMonth
        }
    }

    val yearState = rememberFWheelPickerState(initialIndex = years.indexOf(initialYear))
    val monthState = rememberFWheelPickerState(initialIndex = initialMonth - 1)
    val dayState = rememberFWheelPickerState(initialIndex = initialDay - 1)

    // 선택 변경 감지
    LaunchedEffect(yearState.currentIndex) {
        selectedYear = years.getOrElse(yearState.currentIndex) { currentYear }
        onDateChanged(selectedYear, selectedMonth, selectedDay)
    }
    LaunchedEffect(monthState.currentIndex) {
        selectedMonth = months.getOrElse(monthState.currentIndex) { 1 }
        onDateChanged(selectedYear, selectedMonth, selectedDay)
    }
    LaunchedEffect(dayState.currentIndex) {
        selectedDay = days.getOrElse(dayState.currentIndex) { 1 }
        onDateChanged(selectedYear, selectedMonth, selectedDay)
    }

    Box(modifier = modifier
        .width(228.dp)
        .height(152.dp)) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 년도
            FVerticalWheelPicker(
                count = years.size,
                state = yearState,
                modifier = Modifier.width(80.dp),
                focus = {} // 구분선 제거
            ) { index ->
                PickerText(
                    text = "${years[index]}",
                    isSelected = index == yearState.currentIndex
                )
            }

            Divider()

            // 월
            FVerticalWheelPicker(
                count = months.size,
                state = monthState,
                modifier = Modifier.width(60.dp),
                focus = {} // 구분선 제거
            ) { index ->
                PickerText(
                    text = "${months[index]}",
                    isSelected = index == monthState.currentIndex
                )
            }

            Divider()

            // 일
            FVerticalWheelPicker(
                count = days.size,
                state = dayState,
                modifier = Modifier.width(60.dp),
                focus = {} // 구분선 제거
            ) { index ->
                PickerText(
                    text = "${days[index]}",
                    isSelected = index == dayState.currentIndex
                )
            }
        }

        // 선택 영역 테두리
        Box(
            modifier = Modifier
                .width(220.dp)
                .height(40.dp)
                .border(1.dp, Color(0xFF6B8FF8), RoundedCornerShape(8.dp))
        )
    }
}

@Composable
private fun PickerText(
    text: String,
    isSelected: Boolean
) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight(400),
        color = if (isSelected) Color(0xFF212121) else Color(0xFFBDBDBD),
        fontFamily = FontFamily(Font(R.font.sansneoregular))
    )
}

@Composable
private fun Divider() {
    Text(
        text = "|",
        color = Color(0xFFE0E0E0),
        fontSize = 20.sp,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 260)
@Composable
private fun DateWheelPickerPreview() {
    Box(
        modifier = Modifier
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        DateWheelPicker(
            initialYear = 2025,
            initialMonth = 11,
            initialDay = 26,
            onDateChanged = { _, _, _ -> }
        )
    }
}
