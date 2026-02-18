package com.kuit.afternote.feature.dailyrecord.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.feature.dailyrecord.data.dto.Emotion
import com.kuit.afternote.feature.dailyrecord.data.dto.EmotionResponse
import com.kuit.afternote.ui.theme.Sansneo

private data class BubblePosition(val xRate: Float, val yRate: Float)

private val BubblePresets = listOf(
    BubblePosition(0.25f, 0.35f), // 메인 (가장 큰 버블)
    BubblePosition(0.55f, 0.20f), // 우상단
    BubblePosition(0.80f, 0.50f), // 우측 중앙
    BubblePosition(0.60f, 0.75f)  // 하단 중앙
)

private val BubbleGradients = listOf(
    listOf(Color(0xFFD1EFFF), Color(0xFFFFE2D1)),
    listOf(Color(0xFFE3F2FD), Color(0xFFFCE4EC)),
    listOf(Color(0xFFFFE0B2), Color(0xFFFFF9C4)),
    listOf(Color(0xFFBBDEFB), Color(0xFFE3F2FD))
)

private val PlaceholderEmotions = listOf(
    Emotion(keyword = "일상", percentage = 50.0),
    Emotion(keyword = "기록", percentage = 30.0),
    Emotion(keyword = "마음", percentage = 20.0),
    Emotion(keyword = "추억", percentage = 10.0)
)

// 데이터가 없을 때 사용할 차분한 회색 톤의 그라데이션
private val PlaceholderGradients = listOf(
    listOf(Color(0xFFF2F2F2), Color(0xFFE0E0E0)),
    listOf(Color(0xFFF8F8F8), Color(0xFFECECEC)),
    listOf(Color(0xFFF2F2F2), Color(0xFFF8F8F8)),
    listOf(Color(0xFFE0E0E0), Color(0xFFF2F2F2))
)

@Composable
fun EmotionBubbleReport(
    modifier: Modifier = Modifier,
    userName: String = "박서연",
    emotionResponse: EmotionResponse
) {
    // 데이터 유무 확인
    val isEmpty = emotionResponse.emotions.isEmpty()
    val emotions = if (isEmpty) PlaceholderEmotions else emotionResponse.emotions.take(4)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            contentAlignment = Alignment.Center
        ) {
            val maxWidth = constraints.maxWidth.toFloat()
            val maxHeight = constraints.maxHeight.toFloat()

            emotions.forEachIndexed { index, emotion ->
                val preset = BubblePresets.getOrElse(index) { BubblePosition(0.5f, 0.5f) }

                // 데이터가 없을 때는 Placeholder용 그라데이션 사용
                val colors = if (isEmpty) {
                    PlaceholderGradients.getOrElse(index) { PlaceholderGradients[0] }
                } else {
                    BubbleGradients.getOrElse(index) { BubbleGradients[0] }
                }

                val bubbleSize = (80 + (emotion.percentage * 0.6)).coerceAtMost(140.0).dp

                EmotionBubble(
                    keyword = emotion.keyword,
                    size = bubbleSize,
                    gradient = Brush.linearGradient(colors),
                    isPlaceholder = isEmpty, // 상태 전달
                    modifier = Modifier.offset(
                        x = (preset.xRate * maxWidth / 3).dp - (bubbleSize / 2),
                        y = (preset.yRate * maxHeight / 3).dp - (bubbleSize / 2)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 요약 텍스트는 그대로 유지하여 기록을 유도
        EmotionSummaryText(emotionResponse.summary)
    }
}

@Composable
private fun EmotionBubble(
    keyword: String,
    size: Dp,
    gradient: Brush,
    isPlaceholder: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(gradient)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = keyword,
            fontSize = (size.value / 6).sp,
            fontWeight = FontWeight.Bold,
            // 플레이스홀더일 때는 글자 색상을 연하게 조절
            color = if (isPlaceholder) Color(0xFFAAAAAA) else Color(0xFF444444),
            textAlign = TextAlign.Center,
            fontFamily = Sansneo
        )
    }
}
@Composable
private fun EmotionSummaryText(summary: String) {

    Text(
        text = summary,
        fontSize = 18.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = Sansneo,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 40.dp),
        color = Color(0xFF222222)
    )
}
