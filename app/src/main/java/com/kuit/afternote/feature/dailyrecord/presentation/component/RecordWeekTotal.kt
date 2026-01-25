package com.kuit.afternote.feature.dailyrecord.presentation.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.expand.dropShadow
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Sansneo
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

/**
 * 주간리포트 컴포넌트
 * 주에 몇번 기록했는 지를 보여줌
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordWeekTotal(
    modifier: Modifier = Modifier,
    today: LocalDate,

    ) {
    val month = today.monthValue
    val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top=32.dp)
    ) {
        Text(
            text = "이번주 박서연님은 3번의 마음을 기록하셨네요.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Sansneo,
        )
        Box(
            modifier = Modifier
                .height(100.dp)
                .dropShadow(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Black.copy(alpha = 0.05f),
                    blur = 5.dp,
                    offsetY = 2.dp,
                    offsetX = 0.dp,
                    spread = 0.dp
                )
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)

        ){
            val formatter = DateTimeFormatter.ofPattern("d", Locale.KOREA) // 일(day)만
            val allItems = (0..6).map { offset ->
                val date = startOfWeek.plusDays(offset.toLong())
                val dayLabel = date.format(formatter) // "10", "11", ...
                listOf("월","화","수","목","금","토","일")[offset] to dayLabel
            }
            Row {
                allItems.forEach { (today, day) ->
                    SubWeekCheck(
                        today = today,
                        day = day
                    )
                }
            }

        }
    }
}
