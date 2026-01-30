package com.kuit.afternote.feature.dailyrecord.presentation.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.expand.dropShadow
import com.kuit.afternote.ui.theme.White
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordShowWeekReport(
    modifier: Modifier = Modifier,
    standard: String,
    today: LocalDate
) {
    var isExpanded by remember { mutableStateOf(false) }
    val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    if (!isExpanded) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 56.dp)
                .dropShadow(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Black.copy(alpha = 0.05f),
                    blur = 5.dp,
                    offsetY = 2.dp,
                    offsetX = 0.dp,
                    spread = 0.dp
                ).clip(RoundedCornerShape(16.dp))
                .background(color = White)
                .clickable { isExpanded = !isExpanded }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 17.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = standard,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Box(
                    modifier = Modifier
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_under_direct_foreground),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 144.dp)
                .dropShadow(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Black.copy(alpha = 0.05f),
                    blur = 5.dp,
                    offsetY = 2.dp,
                    offsetX = 0.dp,
                    spread = 0.dp
                ).clip(RoundedCornerShape(16.dp))
                .background(color = White)
                .clickable { isExpanded = !isExpanded }
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 17.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = standard,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Box(
                        modifier = Modifier
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_under_direct_foreground),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                val formatter = DateTimeFormatter.ofPattern("d", Locale.KOREA) // 일(day)만
                val allItems = (0..6).map { offset ->
                    val date = startOfWeek.plusDays(offset.toLong())
                    val dayLabel = date.format(formatter) // "10", "11", ...
                    listOf("월", "화", "수", "목", "금", "토", "일")[offset] to dayLabel
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    allItems.forEach { (today, day) ->
                        SubWeekCheck(
                            modifier = Modifier.weight(1f), // 각 아이템이 동일한 너비 차지
                            today = today,
                            day = day
                        )
                    }
                }
            }
        }
    }
}
