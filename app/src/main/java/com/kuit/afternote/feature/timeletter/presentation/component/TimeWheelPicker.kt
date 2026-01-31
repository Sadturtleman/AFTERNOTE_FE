package com.kuit.afternote.feature.timeletter.presentation.component

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import java.time.LocalTime

/**
 * 발송 시간 Wheel Picker (오전/오후 | 시 | 분)
 */
@Composable
fun TimeWheelPicker(
    initialHour: Int = LocalTime.now().hour,
    initialMinute: Int = LocalTime.now().minute,
    onTimeChanged: (hour: Int, minute: Int) -> Unit, // 24시간 형식으로 반환
    modifier: Modifier = Modifier
) {
    val amPmList = listOf("오전", "오후")
    val hours = (1..12).toList()
    val minutes = (0..59).toList()

    // 24시간 -> 12시간 변환
    val initialAmPm = if (initialHour < 12) 0 else 1
    val initialHour12 = when {
        initialHour == 0 -> 12
        initialHour > 12 -> initialHour - 12
        else -> initialHour
    }

    var selectedAmPm by remember { mutableStateOf(amPmList[initialAmPm]) }
    var selectedHour by remember { mutableIntStateOf(initialHour12) }
    var selectedMinute by remember { mutableIntStateOf(initialMinute) }

    val amPmState = rememberFWheelPickerState(initialIndex = initialAmPm)
    val hourState = rememberFWheelPickerState(initialIndex = initialHour12 - 1)
    val minuteState = rememberFWheelPickerState(initialIndex = initialMinute)

    // 선택 변경 감지
    LaunchedEffect(amPmState.currentIndex) {
        selectedAmPm = amPmList.getOrElse(amPmState.currentIndex) { "오전" }
        val hour24 = convertTo24Hour(selectedAmPm, selectedHour)
        onTimeChanged(hour24, selectedMinute)
    }
    LaunchedEffect(hourState.currentIndex) {
        selectedHour = hours.getOrElse(hourState.currentIndex) { 1 }
        val hour24 = convertTo24Hour(selectedAmPm, selectedHour)
        onTimeChanged(hour24, selectedMinute)
    }
    LaunchedEffect(minuteState.currentIndex) {
        selectedMinute = minutes.getOrElse(minuteState.currentIndex) { 0 }
        val hour24 = convertTo24Hour(selectedAmPm, selectedHour)
        onTimeChanged(hour24, selectedMinute)
    }

    Box(
        modifier = modifier
            .width(204.dp)
            .height(152.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(40.dp)
                .border(1.dp, Color(0xFF6B8FF8), RoundedCornerShape(8.dp))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 21.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FVerticalWheelPicker(
                count = amPmList.size,
                state = amPmState,
                modifier = Modifier.width(60.dp),
                focus = {}
            ) { index ->
                TimePickerText(
                    text = amPmList[index],
                    isSelected = index == amPmState.currentIndex
                )
            }

            TimeDivider()

            FVerticalWheelPicker(
                count = hours.size,
                state = hourState,
                modifier = Modifier.width(50.dp),
                focus = {}
            ) { index ->
                TimePickerText(
                    text = "${hours[index]}",
                    isSelected = index == hourState.currentIndex
                )
            }

            TimeDivider()

            FVerticalWheelPicker(
                count = minutes.size,
                state = minuteState,
                modifier = Modifier.width(50.dp),
                focus = {}
            ) { index ->
                TimePickerText(
                    text = minutes[index].toString().padStart(2, '0'),
                    isSelected = index == minuteState.currentIndex
                )
            }
        }
    }

}

private fun convertTo24Hour(
    amPm: String,
    hour12: Int
): Int =
    when {
        amPm == "오전" && hour12 == 12 -> 0
        amPm == "오전" -> hour12
        amPm == "오후" && hour12 == 12 -> 12
        else -> hour12 + 12
    }
@Composable
private fun TimePickerText(
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
private fun TimeDivider() {
    Text(
        text = "|",
        color = Color(0xFFE0E0E0),
        fontSize = 20.sp,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun TimeWheelPickerPreview() {
    TimeWheelPicker(
        initialHour = 15, // 오후 3시
        initialMinute = 22,
        onTimeChanged = { _, _ -> }
    )
}
