package com.kuit.afternote.feature.dailyrecord.presentation.component

import android.icu.util.LocaleData
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.White
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * 기록들 정렬할 때 쓰이는 컴포넌트
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordCalendarSort(
    modifier: Modifier = Modifier,
    today: LocalDate
    ) {
    val year = today.year
    val month = today.monthValue
    val formatter = DateTimeFormatter.ofPattern("yyyy년 mm월", Locale.KOREA)
    val markedDates = listOf(1, 6, 8, 11, 13, 15, 16, 17, 25)
    var selectedDate = today.dayOfMonth


    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .size(350.dp, 370.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = White)
    ){
        Column {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, top = 24.dp)
                    .size(120.dp, 34.dp)
                    .background(color = White)
                    .border(
                        width = 1.dp,                // 두께
                        color = Color(0xFF328BFF),          // 색상
                        shape = RoundedCornerShape(20.dp) // 모양 (선택)
                    ),
                contentAlignment = Alignment.Center // 가운데 정렬

            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text =" ${year}년 ${month}월",
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.width(4.dp))
                    Image(
                        painter = painterResource(R.drawable.ic_under),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp),
                        colorFilter = ColorFilter.tint(Color(0xFF328BFF))
                    )
                }

            }
            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
            ) {

                // 요일 헤더
//                Row(horizontalArrangement = Arrangement.SpaceBetween) {
//                    listOf("일", "월", "화", "수", "목", "금", "토").forEach {
//                        Text(text = it, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
//                    }
//                }

                // 날짜 그리드 (예: 1일부터 30일까지)
                val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
                val startOffset = LocalDate.of(year, month, 1).dayOfWeek.value
                val totalCells = startOffset + daysInMonth
                val rows = (totalCells / 7) + if (totalCells % 7 != 0) 1 else 0

                for (row in 0 until rows) {
                    Row {
                        for (col in 0..6) {
                            val day = row * 7 + col - startOffset + 1
                            if (day in 1..daysInMonth) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(4.dp)
                                        .clip(CircleShape)
                                        .clickable(onClick = {selectedDate = day })
                                        .background(
                                            when {
                                                day == selectedDate -> Color(0xFF328BFF)
                                                else -> Color.Transparent
                                            }
                                        )
                                        .then(
                                            if (day in markedDates) {
                                                Modifier.border(
                                                    width = 1.dp,
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
                                        color = if (day == selectedDate) Color.White else Color.Black
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}
