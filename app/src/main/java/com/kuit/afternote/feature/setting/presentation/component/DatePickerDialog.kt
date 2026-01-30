package com.kuit.afternote.feature.setting.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kuit.afternote.core.ui.component.DateWheelPicker
import com.kuit.afternote.ui.expand.dropShadow
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import java.time.LocalDate

@Composable
fun DatePickerDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onDateChanged: (LocalDate) -> Unit,
    initialDate: LocalDate = LocalDate.now()
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        DatePickerDialogContent(
            modifier = modifier,
            initialYear = initialDate.year,
            initialMonth = initialDate.monthValue,
            initialDay = initialDate.dayOfMonth,
            onConfirm = onDateChanged
        )
    }
}

@Composable
fun DatePickerDialogContent(
    modifier: Modifier = Modifier,
    initialYear: Int = LocalDate.now().year,
    initialMonth: Int = LocalDate.now().monthValue,
    initialDay: Int = LocalDate.now().dayOfMonth,
    onConfirm: (LocalDate) -> Unit = {}
) {
    val containerShape = RoundedCornerShape(16.dp)
    val buttonShape = RoundedCornerShape(8.dp)

    var selectedYear by remember { mutableIntStateOf(initialYear) }
    var selectedMonth by remember { mutableIntStateOf(initialMonth) }
    var selectedDay by remember { mutableIntStateOf(initialDay) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .dropShadow(
                shape = containerShape,
                color = Color.Black.copy(alpha = 0.15f),
                blur = 10.dp,
                offsetX = 0.dp,
                offsetY = 2.dp,
                spread = 0.dp
            ).clip(containerShape)
            .background(Color.White)
            .padding(
                horizontal = 24.dp,
                vertical = 32.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        Text(
            text = "날짜 선택하기",
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Bold,
                color = Gray9,
                textAlign = TextAlign.Center
            )
        )

        // DateWheelPicker
        DateWheelPicker(
            initialYear = initialYear,
            initialMonth = initialMonth,
            initialDay = initialDay,
            onDateChanged = { year, month, day ->
                selectedYear = year
                selectedMonth = month
                selectedDay = day
            }
        )

        // Confirm Button
        Text(
            text = "선택하기",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray9,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .fillMaxWidth()
                .dropShadow(
                    shape = buttonShape,
                    color = Color.Black.copy(alpha = 0.05f),
                    blur = 5.dp,
                    offsetX = 0.dp,
                    offsetY = 2.dp,
                    spread = 0.dp
                ).clip(buttonShape)
                .background(B3)
                .clickable {
                    onConfirm(LocalDate.of(selectedYear, selectedMonth, selectedDay))
                }.padding(
                    horizontal = 24.dp,
                    vertical = 16.dp
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DatePickerDialogContentPreview() {
    AfternoteTheme {
        DatePickerDialogContent(
            initialYear = 2025,
            initialMonth = 11,
            initialDay = 26,
            onConfirm = {}
        )
    }
}
