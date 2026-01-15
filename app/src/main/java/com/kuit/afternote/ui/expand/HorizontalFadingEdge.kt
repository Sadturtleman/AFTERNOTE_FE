package com.kuit.afternote.ui.expand

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp

/**
 * LazyRow나 가로 스크롤 가능한 컨테이너의 오른쪽 끝에 페이드 아웃 효과를 추가하는 Modifier
 *
 * Alpha Masking 방식을 사용하여 배경색에 상관없이 콘텐츠 자체를 투명하게 만듭니다.
 * 이 방식은 배경이 이미지나 그라디언트여도 자연스럽게 동작합니다.
 *
 * **Modifier 적용 순서 중요:**
 * - `horizontalFadingEdge` -> `horizontalScroll` -> `padding` 순서로 적용해야 합니다.
 * - 이렇게 해야 페이드 효과가 화면에 고정되고 스크롤되는 콘텐츠를 따라 움직이지 않습니다.
 *
 * @param edgeWidth 페이드 아웃 영역의 너비 (일반적으로 24dp ~ 48dp)
 */
fun Modifier.horizontalFadingEdge(edgeWidth: Dp) =
    this
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent() // 콘텐츠 먼저 그림

            // 오른쪽 끝 페이드 아웃 마스크
            // BlendMode.DstIn: 검은색(불투명) 부분은 콘텐츠를 유지하고, 투명한 부분은 콘텐츠를 지움
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Black, Color.Transparent),
                    startX = size.width - edgeWidth.toPx(),
                    endX = size.width
                ),
                blendMode = BlendMode.DstIn
            )
        }
