package com.kuit.afternote.feature.receiver.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9
import com.sd.lib.compose.wheel_picker.FVerticalWheelPicker
import com.sd.lib.compose.wheel_picker.FWheelPickerState
import com.sd.lib.compose.wheel_picker.rememberFWheelPickerState
import java.time.YearMonth
import java.time.LocalDate

private const val WHEEL_PICKER_MIN_YEAR = 2026
private const val WHEEL_PICKER_YEAR_COUNT = 11
private val PickerContainerHeight = 152.dp
private val SelectionBorderHeight = 40.dp
private val SelectionBorderHorizontalInset = 4.dp

/**
 * 연·월만 선택하는 휠 데이트 피커 다이얼로그 (피그마 스타일).
 *
 * @param initialDate 다이얼로그 표시 시 초기 선택 날짜
 * @param onDismiss 다이얼로그 닫기 콜백
 * @param onConfirm 선택 완료 시 선택된 날짜 전달 (일은 initialDate의 일 또는 해당 월의 유효 일로 보정)
 */
@Composable
fun ReceiverWheelDatePickerDialog(
    initialDate: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val years = remember {
        (WHEEL_PICKER_MIN_YEAR until WHEEL_PICKER_MIN_YEAR + WHEEL_PICKER_YEAR_COUNT).toList()
    }
    val months = remember { (1..12).toList() }

    val yearIndex = remember(initialDate.year, years) {
        val year = initialDate.year.coerceAtLeast(WHEEL_PICKER_MIN_YEAR)
        years.indexOf(year).coerceIn(0, years.lastIndex)
    }
    val monthIndex = remember(initialDate.monthValue, months) {
        (initialDate.monthValue - 1).coerceIn(0, months.lastIndex)
    }

    val yearState = rememberFWheelPickerState(initialIndex = yearIndex)
    val monthState = rememberFWheelPickerState(initialIndex = monthIndex)

    LaunchedEffect(yearIndex) {
        if (yearState.currentIndex != yearIndex) {
            yearState.scrollToIndex(yearIndex)
        }
    }
    LaunchedEffect(monthIndex) {
        if (monthState.currentIndex != monthIndex) {
            monthState.scrollToIndex(monthIndex)
        }
    }

    val selectedYear = years.getOrElse(yearState.currentIndex) { WHEEL_PICKER_MIN_YEAR }
    val selectedMonth = months.getOrElse(monthState.currentIndex) { 1 }
    val maxDay = YearMonth.of(selectedYear, selectedMonth).lengthOfMonth()
    val selectedDay = initialDate.dayOfMonth.coerceIn(1, maxDay)

    val dateDescription = context.getString(
        R.string.receiver_mindrecord_date_picker_content_description,
        selectedYear,
        selectedMonth
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(280.dp)
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(PickerContainerHeight)
                        .semantics { contentDescription = dateDescription }
                ) {
                    SelectionBorder(
                        modifier = Modifier.align(Alignment.Center),
                        selectionBorderColor = B3
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(PickerContainerHeight),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FVerticalWheelPicker(
                            count = years.size,
                            state = yearState,
                            modifier = Modifier.weight(1f),
                            itemHeight = SelectionBorderHeight,
                            unfocusedCount = 1,
                            focus = {}
                        ) { index ->
                            YearMonthPickerText(
                                text = "${years[index]}",
                                isSelected = index == yearState.currentIndex
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(Gray6)
                        )
                        FVerticalWheelPicker(
                            count = months.size,
                            state = monthState,
                            modifier = Modifier.weight(1f),
                            itemHeight = SelectionBorderHeight,
                            unfocusedCount = 1,
                            focus = {}
                        ) { index ->
                            YearMonthPickerText(
                                text = "${months[index]}",
                                isSelected = index == monthState.currentIndex
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(B3, RoundedCornerShape(8.dp))
                        .clickable {
                            val date = LocalDate.of(selectedYear, selectedMonth, selectedDay)
                            onConfirm(date)
                            onDismiss()
                        }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.receiver_mindrecord_date_picker_confirm),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Gray9
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectionBorder(
    modifier: Modifier,
    selectionBorderColor: Color
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SelectionBorderHorizontalInset)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(SelectionBorderHeight)
                .border(1.dp, selectionBorderColor, RoundedCornerShape(8.dp))
        )
    }
}

@Composable
private fun YearMonthPickerText(
    text: String,
    isSelected: Boolean
) {
    Text(
        text = text,
        fontSize = if (isSelected) 18.sp else 14.sp,
        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
        color = if (isSelected) Color(0xFF212121) else Color(0xFFBDBDBD)
    )
}
