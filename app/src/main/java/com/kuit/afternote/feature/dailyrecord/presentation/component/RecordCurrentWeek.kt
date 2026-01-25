package com.kuit.afternote.feature.dailyrecord.presentation.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Sansneo
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordCurrentWeek(
    modifier: Modifier = Modifier,
    today: LocalDate,
    week: Int,
    startDate: LocalDate,
    endDate: LocalDate
    ) {
    val year = today.year
    //val month = today.month
    val month = today.monthValue
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.", Locale.KOREA)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top =23.dp, start = 20.dp)
    ) {
        Text(
            text = "${month}월 ${week}주차 리포트",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Sansneo
        )
        Text(
            text = "${startDate.format(formatter)} - ${endDate.format(formatter)}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = Sansneo,
            color = Gray6
        )
    }
}
