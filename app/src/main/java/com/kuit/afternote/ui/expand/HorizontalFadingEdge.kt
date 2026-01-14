package com.kuit.afternote.ui.expand

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * LazyRow나 가로 스크롤 가능한 컨테이너의 오른쪽 끝에 페이드 아웃 효과를 추가하는 Modifier
 *
 * @param edgeWidth 페이드 아웃 영역의 너비
 * @param edgeColor 페이드 아웃이 끝나는 색상 (배경색)
 */
fun Modifier.horizontalFadingEdge(
    edgeWidth: Dp,
    edgeColor: Color
) = drawWithContent {
    val edgeWidthPx = edgeWidth.toPx()

    drawContent()

    // 오른쪽 끝에 그라디언트 오버레이 그리기
    drawRect(
        brush = Brush.horizontalGradient(
            colors = listOf(Color.Transparent, edgeColor),
            startX = size.width - edgeWidthPx,
            endX = size.width
        ),
        topLeft = Offset(size.width - edgeWidthPx, 0f),
        size = Size(edgeWidthPx, size.height)
    )
}
