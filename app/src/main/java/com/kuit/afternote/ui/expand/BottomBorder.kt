package com.kuit.afternote.ui.expand

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kuit.afternote.ui.theme.Gray2

/**
 * 하단 보더를 그리는 Modifier 확장 함수
 *
 * @param color 보더 색상 (기본값: Gray2)
 * @param width 보더 두께 (기본값: 1.dp)
 */
fun Modifier.bottomBorder(
    color: Color = Gray2,
    width: Dp = 1.dp
) = drawBehind {
    val borderWidth = width.toPx()
    val y = size.height - borderWidth / 2
    drawLine(
        color = color,
        start = Offset(0f, y),
        end = Offset(size.width, y),
        strokeWidth = borderWidth
    )
}
