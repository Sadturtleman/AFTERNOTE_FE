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

// 1. 화면 비율(0.0 ~ 1.0)에 따른 위치 정의 (화면 중앙 기준이 아니라 전체 영역에서의 상대 좌표)
private data class BubblePosition(val xRate: Float, val yRate: Float)

// 4방향으로 고르게 분산되도록 좌표 수정 (좌상, 우상, 좌하, 우하)
private val BubblePresets = listOf(
    BubblePosition(0.20f, 0.25f), // 좌측 상단 (메인 느낌)
    BubblePosition(0.75f, 0.20f), // 우측 상단
    BubblePosition(0.25f, 0.75f), // 좌측 하단
    BubblePosition(0.80f, 0.70f)  // 우측 하단
)

private val BubbleGradients = listOf(
    listOf(Color(0xFFD1EFFF), Color(0xFFFFE2D1)),
    listOf(Color(0xFFE3F2FD), Color(0xFFFCE4EC)),
    listOf(Color(0xFFFFE0B2), Color(0xFFFFF9C4)),
    listOf(Color(0xFFBBDEFB), Color(0xFFE3F2FD))
)

// [Visual Update] 더미 데이터 색상을 "탁한 회색"에서 "밝고 깨끗한 회색/화이트 톤"으로 변경
private val PlaceholderGradients = listOf(
    listOf(Color(0xFFFFFFFF), Color(0xFFF5F5F5)), // 거의 흰색에 가까운 밝은 톤
    listOf(Color(0xFFFAFAFA), Color(0xFFEEEEEE)), // 아주 연한 회색
    listOf(Color(0xFFF0F0F0), Color(0xFFFFFFFF)),
    listOf(Color(0xFFFFFFFF), Color(0xFFF0F4F8))  // 아주 미세한 쿨톤 섞임
)

private val PlaceholderEmotions = listOf(
    Emotion(keyword = "일상", percentage = 50.0),
    Emotion(keyword = "기록", percentage = 30.0),
    Emotion(keyword = "마음", percentage = 20.0),
    Emotion(keyword = "추억", percentage = 10.0)
)

@Composable
fun EmotionBubbleReport(
    modifier: Modifier = Modifier,
    userName: String = "박서연",
    emotionResponse: EmotionResponse
) {
    val isEmpty = emotionResponse.emotions.isNullOrEmpty()
    // null-safe 처리 추가
    val emotions = if (isEmpty) PlaceholderEmotions else (emotionResponse.emotions ?: emptyList()).take(4)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // BoxWithConstraints를 사용하여 부모의 크기를 측정
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            // 내부 요소들이 겹칠 수 있도록 TopStart 기준 정렬
            contentAlignment = Alignment.TopStart
        ) {
            val containerWidth = maxWidth
            val containerHeight = maxHeight

            emotions.forEachIndexed { index, emotion ->
                val preset = BubblePresets.getOrElse(index) { BubblePosition(0.5f, 0.5f) }

                val colors = if (isEmpty) {
                    PlaceholderGradients.getOrElse(index) { PlaceholderGradients[0] }
                } else {
                    BubbleGradients.getOrElse(index) { BubbleGradients[0] }
                }

                val bubbleSize = (80 + (emotion.percentage ?: 0.0) * 0.6).coerceAtMost(140.0).dp

                // [Logic Update] 4방향 분산 로직 (화면 크기 * 비율 - 버블 반경)
                // 버블의 중심점이 해당 비율 좌표에 오도록 계산
                val offsetX = (containerWidth * preset.xRate) - (bubbleSize / 2)
                val offsetY = (containerHeight * preset.yRate) - (bubbleSize / 2)

                EmotionBubble(
                    keyword = emotion.keyword ?: "",
                    size = bubbleSize,
                    gradient = Brush.linearGradient(colors),
                    isPlaceholder = isEmpty,
                    modifier = Modifier.offset(x = offsetX, y = offsetY)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // summary가 null일 경우 대비
        EmotionSummaryText(if (isEmpty) "기록을 남기면 마음의 지도를 그려드릴게요." else (emotionResponse.summary ?: ""))
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
            // 더미 데이터일 때 텍스트 색상을 더 연하게 하여 부드러운 느낌 강조
            color = if (isPlaceholder) Color(0xFFC0C0C0) else Color(0xFF444444),
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
