package com.kuit.afternote.feature.dailyrecord.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.Gray3
import com.kuit.afternote.ui.theme.Gray5

/**
 * 재사용 하고 싶어서
 * 활동 한 눈에 보기 때도 사용하려고 만듦
 */
@Composable
fun SubWeekCheck(
    modifier: Modifier = Modifier,
    today: String,
    day: String,
    ) {
    Column(
        modifier = modifier, // 전달받은 modifier 적용
        horizontalAlignment = Alignment.CenterHorizontally // 가운데 정렬

    ) {
        Text(
            text = today,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1, // 텍스트가 최대 한 줄로 표시
            overflow = TextOverflow.Ellipsis //한 줄 안에 못 들어가면, 잘린 부분을 ..으로 표시
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = day,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = Gray5
        )
    }
}
