package com.kuit.afternote.feature.dailyrecord.presentation.component

import android.os.Build
import android.widget.Space
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.Sansneo
import java.time.LocalDate
import java.time.YearMonth

/**
 * 캘린더 정렬 시 필요
 * 초기 설정값 : 현재 시각을 기준으로
 * 선택 값에 따라 다이어리가 뜨도록
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordCalendar(modifier: Modifier = Modifier) {
    val today = LocalDate.now()
    var selectedYear by remember { mutableStateOf(today.year) }
    var selectedMonth by remember { mutableStateOf(today.monthValue) }

    val daysInMonth = YearMonth.of(selectedYear, selectedMonth).lengthOfMonth()
    val firstDayOfWeek = LocalDate.of(selectedYear, selectedMonth, 1).dayOfWeek.value // 1 = 월요일

    //누르면 정렬 가능하도록
    var showSelector by remember { mutableStateOf(false) }

    val calendarDates = buildList {
        repeat(firstDayOfWeek % 7) { add(null) } // 빈칸 채우기
        for (day in 1..daysInMonth) {
            add(day)
        }
    }

    Column(
        modifier = Modifier
            .size(350.dp,370.dp)
            .background(color = Color.White)
            .clip(RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 24.dp)

    ) {
//        Row {
//            DropdownSelector("년", (2020..2030).toList(), selectedYear) { selectedYear = it }
//            DropdownSelector("월", (1..12).toList(), selectedMonth) { selectedMonth = it }
//        }

        Box(
            modifier = Modifier
                .size(120.dp,34.dp)
                .border(
                    width = 1.dp,
                    color = Color(0xFF328BFF),
                    shape = RoundedCornerShape(20.dp)
                    ),
            contentAlignment = Alignment.Center

        ){
            Row {
                Text(text = "${selectedYear}년 ${selectedMonth}월",
                    fontSize = 12.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.padding(start = 8.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_under),
                    contentDescription = "밑 화살표",
                    modifier = Modifier.size(16.dp)
                        .clickable{ showSelector = true },
                    colorFilter = ColorFilter.tint(Color(0xFF89C2FF))

                )
            }

        }

        LazyVerticalGrid(columns = GridCells.Fixed(7)) {
            items(calendarDates.size) { index ->
                val day = calendarDates[index]
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (day != null) {
                        Text(
                            text = day.toString(),
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun CalendarPrev() {
    RecordCalendar()
}
