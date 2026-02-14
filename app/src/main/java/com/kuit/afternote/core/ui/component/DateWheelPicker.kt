package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.sd.lib.compose.wheel_picker.FVerticalWheelPicker
import com.sd.lib.compose.wheel_picker.FWheelPickerState
import com.sd.lib.compose.wheel_picker.rememberFWheelPickerState
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.LocalDate

private val PickerContainerHeight = 152.dp
private val SelectionBorderHeight = 40.dp
private val SelectionBorderHorizontalInset = 4.dp
private val DividerWidth = 14.dp

private data class DateWheelPickerColors(
    val selectedTextColor: Color,
    val unselectedTextColor: Color,
    val selectionBorderColor: Color,
    val dividerColor: Color
)

/**
 * Default values for [DateWheelPicker].
 */
object DateWheelPickerDefaults {
    val ContainerWidth = 228.dp
    val SelectedTextColor = Color(0xFF212121)
    val UnselectedTextColor = Color(0xFFBDBDBD)
    val SelectionBorderColor = Color(0xFF6B8FF8)
    val DividerColor = Color(0xFFE0E0E0)
}

/**
 * 발송 날짜 Wheel Picker
 *
 * @param modifier Modifier
 * @param currentDate 현재 선택된 날짜 (State Hoisting)
 * @param onDateChanged 날짜 변경 콜백
 * @param minDate 최소 선택 가능 날짜. null이면 제한 없음(과거·미래 모두 선택 가능).
 * @param selectedTextColor 선택된 텍스트 색상
 * @param unselectedTextColor 선택되지 않은 텍스트 색상
 * @param selectionBorderColor 선택 영역 테두리 색상
 * @param dividerColor 구분선 색상
 */
@Composable
fun DateWheelPicker(
    modifier: Modifier = Modifier,
    currentDate: LocalDate = LocalDate.now(),
    onDateChanged: (LocalDate) -> Unit,
    minDate: LocalDate? = null,
    selectedTextColor: Color = DateWheelPickerDefaults.SelectedTextColor,
    unselectedTextColor: Color = DateWheelPickerDefaults.UnselectedTextColor,
    selectionBorderColor: Color = DateWheelPickerDefaults.SelectionBorderColor,
    dividerColor: Color = DateWheelPickerDefaults.DividerColor
) {
    val colors = DateWheelPickerColors(
        selectedTextColor = selectedTextColor,
        unselectedTextColor = unselectedTextColor,
        selectionBorderColor = selectionBorderColor,
        dividerColor = dividerColor
    )

    val model = rememberDateWheelPickerModel(currentDate = currentDate, minDate = minDate)
    val yearState = rememberFWheelPickerState(initialIndex = model.yearIndex)
    val monthState = rememberFWheelPickerState(initialIndex = model.monthIndex)

    SyncWheelIndex(state = yearState, targetIndex = model.yearIndex)
    SyncWheelIndex(state = monthState, targetIndex = model.monthIndex)

    ObserveYearWheel(
        state = yearState,
        years = model.years,
        currentYearFallback = model.currentYear,
        currentDate = currentDate,
        minDate = minDate,
        onDateChanged = onDateChanged
    )
    ObserveMonthWheel(
        state = monthState,
        months = model.months,
        currentDate = currentDate,
        minDate = minDate,
        onDateChanged = onDateChanged
    )

    DateWheelPickerContent(
        modifier = modifier,
        model = model,
        currentDate = currentDate,
        yearState = yearState,
        monthState = monthState,
        minDate = minDate,
        onDateChanged = onDateChanged,
        colors = colors
    )
}

private data class DateWheelPickerModel(
    val currentYear: Int,
    val years: List<Int>,
    val months: List<Int>,
    val daysInMonth: Int,
    val days: List<Int>,
    val yearIndex: Int,
    val monthIndex: Int,
    val dayIndex: Int,
    val dateDescription: String
)

@Composable
private fun rememberDateWheelPickerModel(
    currentDate: LocalDate,
    minDate: LocalDate?
): DateWheelPickerModel {
    val currentYear = LocalDate.now().year
    val years = remember(currentYear) { (currentYear..currentYear + 10).toList() }
    val months = remember { (1..12).toList() }

    val effectiveDate = remember(currentDate, minDate) {
        if (minDate != null) currentDate.coerceAtLeast(minDate) else currentDate
    }

    val yearIndex = remember(effectiveDate.year, years) {
        years.indexOf(effectiveDate.year).coerceIn(0, years.lastIndex)
    }
    val monthIndex = remember(effectiveDate.monthValue, months) {
        (effectiveDate.monthValue - 1).coerceIn(0, months.lastIndex)
    }

    val daysInMonth = remember(effectiveDate.year, effectiveDate.monthValue) {
        LocalDate.of(effectiveDate.year, effectiveDate.monthValue, 1).lengthOfMonth()
    }
    val days = remember(daysInMonth, minDate, effectiveDate.year, effectiveDate.monthValue) {
        if (minDate != null &&
            effectiveDate.year == minDate.year &&
            effectiveDate.monthValue == minDate.monthValue
        ) {
            (minDate.dayOfMonth..daysInMonth).toList()
        } else {
            (1..daysInMonth).toList()
        }
    }
    val dayIndex = remember(effectiveDate.dayOfMonth, days) {
        val idx = days.indexOf(effectiveDate.dayOfMonth)
        idx.coerceIn(0, days.lastIndex)
    }

    val dateDescription = "${effectiveDate.year}년 ${effectiveDate.monthValue}월 ${effectiveDate.dayOfMonth}일 선택됨"

    return DateWheelPickerModel(
        currentYear = currentYear,
        years = years,
        months = months,
        daysInMonth = daysInMonth,
        days = days,
        yearIndex = yearIndex,
        monthIndex = monthIndex,
        dayIndex = dayIndex,
        dateDescription = dateDescription
    )
}

@Composable
private fun DateWheelPickerContent(
    modifier: Modifier,
    model: DateWheelPickerModel,
    currentDate: LocalDate,
    yearState: FWheelPickerState,
    monthState: FWheelPickerState,
    minDate: LocalDate?,
    onDateChanged: (LocalDate) -> Unit,
    colors: DateWheelPickerColors
) {
    Box(
        modifier = modifier.semantics {
            contentDescription = model.dateDescription
        }
    ) {
        // 테두리는 뒤에 그려서 휠 터치가 가려지지 않도록 함
        SelectionBorder(
            modifier = Modifier.align(Alignment.Center),
            selectionBorderColor = colors.selectionBorderColor
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(PickerContainerHeight),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FVerticalWheelPicker(
                count = model.years.size,
                state = yearState,
                modifier = Modifier.weight(4f),
                itemHeight = SelectionBorderHeight,
                unfocusedCount = 1,
                focus = {}
            ) { index ->
                PickerText(
                    text = "${model.years[index]}",
                    isSelected = index == yearState.currentIndex,
                    selectedTextColor = colors.selectedTextColor,
                    unselectedTextColor = colors.unselectedTextColor
                )
            }

            Divider(
                modifier = Modifier.width(DividerWidth),
                color = colors.dividerColor
            )

            FVerticalWheelPicker(
                count = model.months.size,
                state = monthState,
                modifier = Modifier.weight(3f),
                itemHeight = SelectionBorderHeight,
                unfocusedCount = 1,
                focus = {}
            ) { index ->
                PickerText(
                    text = "${model.months[index]}",
                    isSelected = index == monthState.currentIndex,
                    selectedTextColor = colors.selectedTextColor,
                    unselectedTextColor = colors.unselectedTextColor
                )
            }

            Divider(
                modifier = Modifier.width(DividerWidth),
                color = colors.dividerColor
            )

            DayWheel(
                modifier = Modifier.weight(3f),
                model = DateWheelPickerDayModel(
                    daysInMonth = model.daysInMonth,
                    days = model.days,
                    dayIndex = model.dayIndex
                ),
                currentDate = currentDate,
                minDate = minDate,
                onDateChanged = onDateChanged,
                colors = colors
            )
        }
    }
}

private data class DateWheelPickerDayModel(
    val daysInMonth: Int,
    val days: List<Int>,
    val dayIndex: Int
)

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
private fun DayWheel(
    modifier: Modifier,
    model: DateWheelPickerDayModel,
    currentDate: LocalDate,
    minDate: LocalDate?,
    onDateChanged: (LocalDate) -> Unit,
    colors: DateWheelPickerColors
) {
    key(model.daysInMonth, model.days.size) {
        val dayState = rememberFWheelPickerState(initialIndex = model.dayIndex)

        SyncWheelIndex(state = dayState, targetIndex = model.dayIndex)
        ObserveDayWheel(
            state = dayState,
            days = model.days,
            currentDate = currentDate,
            minDate = minDate,
            onDateChanged = onDateChanged
        )

        FVerticalWheelPicker(
            count = model.days.size,
            state = dayState,
            modifier = modifier,
            itemHeight = SelectionBorderHeight,
            unfocusedCount = 1,
            focus = {}
        ) { index ->
            PickerText(
                text = "${model.days[index]}",
                isSelected = index == dayState.currentIndex,
                selectedTextColor = colors.selectedTextColor,
                unselectedTextColor = colors.unselectedTextColor
            )
        }
    }
}

@Composable
private fun SyncWheelIndex(
    state: FWheelPickerState,
    targetIndex: Int
) {
    LaunchedEffect(targetIndex) {
        if (state.currentIndex != targetIndex) {
            state.scrollToIndex(targetIndex)
        }
    }
}

@Composable
private fun ObserveYearWheel(
    state: FWheelPickerState,
    years: List<Int>,
    currentYearFallback: Int,
    currentDate: LocalDate,
    minDate: LocalDate?,
    onDateChanged: (LocalDate) -> Unit
) {
    LaunchedEffect(state, currentDate, minDate) {
        snapshotFlow { state.currentIndex }
            .distinctUntilChanged()
            .collect { index ->
                val newYear = years.getOrElse(index) { currentYearFallback }
                var newDate = currentDate.withYearClamped(newYear)
                if (minDate != null) newDate = newDate.coerceAtLeast(minDate)
                newDate.takeIf { it != currentDate }?.let(onDateChanged)
            }
    }
}

@Composable
private fun ObserveMonthWheel(
    state: FWheelPickerState,
    months: List<Int>,
    currentDate: LocalDate,
    minDate: LocalDate?,
    onDateChanged: (LocalDate) -> Unit
) {
    LaunchedEffect(state, currentDate, minDate) {
        snapshotFlow { state.currentIndex }
            .distinctUntilChanged()
            .collect { index ->
                val newMonth = months.getOrElse(index) { 1 }
                var newDate = currentDate.withMonthClamped(newMonth)
                if (minDate != null) newDate = newDate.coerceAtLeast(minDate)
                newDate.takeIf { it != currentDate }?.let(onDateChanged)
            }
    }
}

@Composable
private fun ObserveDayWheel(
    state: FWheelPickerState,
    days: List<Int>,
    currentDate: LocalDate,
    minDate: LocalDate?,
    onDateChanged: (LocalDate) -> Unit
) {
    LaunchedEffect(state, currentDate, minDate) {
        snapshotFlow { state.currentIndex }
            .distinctUntilChanged()
            .collect { index ->
                val newDay = days.getOrElse(index) { 1 }
                var newDate = currentDate.withDayClamped(newDay)
                if (minDate != null) newDate = newDate.coerceAtLeast(minDate)
                newDate.takeIf { it != currentDate }?.let(onDateChanged)
            }
    }
}

private fun LocalDate.withYearClamped(newYear: Int): LocalDate {
    val daysInMonth = LocalDate.of(newYear, monthValue, 1).lengthOfMonth()
    val newDay = dayOfMonth.coerceAtMost(daysInMonth)
    return LocalDate.of(newYear, monthValue, newDay)
}

private fun LocalDate.withMonthClamped(newMonth: Int): LocalDate {
    val safeMonth = newMonth.coerceIn(1, 12)
    val daysInMonth = LocalDate.of(year, safeMonth, 1).lengthOfMonth()
    val newDay = dayOfMonth.coerceAtMost(daysInMonth)
    return LocalDate.of(year, safeMonth, newDay)
}

private fun LocalDate.withDayClamped(newDay: Int): LocalDate {
    val daysInMonth = LocalDate.of(year, monthValue, 1).lengthOfMonth()
    val safeDay = newDay.coerceIn(1, daysInMonth)
    return LocalDate.of(year, monthValue, safeDay)
}

@Composable
private fun PickerText(
    text: String,
    isSelected: Boolean,
    selectedTextColor: Color,
    unselectedTextColor: Color
) {
    Text(
        text = text,
        fontSize = if (isSelected) 18.sp else 14.sp,
        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
        color = if (isSelected) selectedTextColor else unselectedTextColor,
        fontFamily = FontFamily(Font(R.font.sansneomedium))
    )
}

@Composable
private fun Divider(
    modifier: Modifier = Modifier,
    color: Color
) {
    Text(
        text = "|",
        color = color,
        fontSize = 20.sp,
        modifier = modifier.padding(horizontal = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun DateWheelPickerPreview() {
    DateWheelPicker(
        modifier = Modifier.width(DateWheelPickerDefaults.ContainerWidth),
        currentDate = LocalDate.of(2025, 11, 26),
        onDateChanged = {}
    )
}
