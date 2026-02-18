package com.kuit.afternote.feature.dailyrecord.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
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
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 일기,깊은생각 top 바
 * 우측 상단: 화살표
 * 중간 상단: 제목
 * 좌측 상단: "등록" 버튼
 */

@Composable
fun RecordSubTopbar(
    modifier: Modifier = Modifier,
    text: String,
    onLeftClock: () -> Unit,
    onRightClick: () -> Unit,
    isRegisterEnabled: Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(bottom = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Sansneo
        )

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
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable(enabled = isRegisterEnabled) { onRightClick() }
                .padding(end = 13.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "등록",
                color = if (isRegisterEnabled) Gray5 else Gray5.copy(alpha = 0.5f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Sansneo
            )
        }
    }

    HorizontalDivider(color = Color.LightGray, thickness = 0.8.dp)
}

@Preview(showBackground = true)
@Composable
private fun RecordSubTopbarPrev() {
//    RecordSubTopbar(
//        text = "일기 작성하기"
//    )
}
