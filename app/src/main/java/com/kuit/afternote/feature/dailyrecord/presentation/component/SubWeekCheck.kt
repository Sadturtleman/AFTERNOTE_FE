package com.kuit.afternote.feature.dailyrecord.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
        modifier = Modifier
            .padding(start = 15.dp)
    ) {
        Text(
            text = today,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = day,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = Gray5
        )
    }
}
