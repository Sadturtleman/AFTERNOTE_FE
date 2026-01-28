package com.kuit.afternote.feature.dailyrecord.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kuit.afternote.ui.expand.dropShadow

/**
 * 주간리포트 나의 감정 키워드 컴포넌트
 */
@Composable
fun RecordweekendMindKeyword(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .padding(top = 30.dp)
            .padding(horizontal = 20.dp)
    ) {
        RecordTextComponent(
            title = "나의 감정 키워드"
        )
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(221.dp)
                .dropShadow(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Black.copy(alpha = 0.05f),
                    blur = 5.dp,
                    offsetY = 2.dp,
                    offsetX = 0.dp,
                    spread = 0.dp
                ).clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            Text(
                text = "이번 주 박서연 님의 기록에서는\n" +
                    "‘가족’을 위한 ‘감사’의 마음이 엿보입니다."
            )
        }
    }
}
