package com.kuit.afternote.feature.dailyrecord.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.ui.expand.dropShadow
@Composable
fun RecordweekendMindKeyword(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .padding(top = 30.dp)
            .padding(horizontal = 20.dp)
    ) {
        RecordTextComponent(title = "나의 감정 키워드")
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
                )
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            Column {
                EmotionBubbleGroup()
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "이번 주 박서연 님의 기록에서는\n‘가족’을 위한 ‘감사’의 마음이 엿보입니다."
                )
            }
        }
    }
}

@Composable
fun EmotionBubble(emotion: EmotionKeyword) {
    Box(
        modifier = Modifier
            .size(emotion.weight.dp)
            .clip(RoundedCornerShape(50))
            .background(emotion.color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emotion.keyword,
            color = Color.White
        )
    }
}

@Composable
fun EmotionBubbleGroup() {
    val emotions = listOf(
        EmotionKeyword("가족", 100, Color(0xFFFFD700)),
        EmotionKeyword("감사", 80, Color(0xFFFF8C00)),
        EmotionKeyword("사랑", 70, Color(0xFFFF69B4)),
        EmotionKeyword("그리움", 60, Color(0xFF87CEEB))
    )

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        emotions.forEach { emotion ->
            EmotionBubble(emotion)
        }
    }

}

