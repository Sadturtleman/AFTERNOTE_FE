package com.kuit.afternote.feature.dailyrecord.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.Black
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 나의 모든 기록, 주간 리포트 표시해주는 top 바
 * 조건에 따라 옆에 화살표 아이템이 추가되고 사라지도록
 * 구현
 */
@Composable
fun RecordMainTopbar(
    modifier: Modifier = Modifier
        .background(Gray1),
    text: String,
    // 왼쪽 화살표 버튼
    showLeftArrow: Boolean = false,
    onLeftClock: () -> Unit,
    // 스타일
    contentColor: Color = Black,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 10.dp)
            .height(40.dp),
        contentAlignment = Alignment.Center
    ) {
        // 텍스트는 항상 중앙
        Text(
            text = text,
            color = Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Sansneo
        )

        // 왼쪽 화살표 (조건부)
        if (showLeftArrow) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart) // 왼쪽 정렬
                    .size(30.dp)
                    .clickable(onClick = onLeftClock),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_sub_left_foreground),
                    contentDescription = "화살표",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecordMainTopbarPrev() {
    RecordMainTopbar(
        text = "나의 모든 기록",
        showLeftArrow = true,
        onLeftClock = {}
    )
}
