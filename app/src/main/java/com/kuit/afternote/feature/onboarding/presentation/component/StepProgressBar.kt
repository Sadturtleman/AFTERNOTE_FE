package com.kuit.afternote.feature.onboarding.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StepProgressBar(
    step: Int,
    totalStep: Int,
    modifier: Modifier = Modifier,
    height: Dp = 3.dp,
    progressColor: Color,
    trackColor: Color
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
    ) {
        val totalWidth = size.width
        val stepWidth = totalWidth / totalStep

        val startX = (step - 1) * stepWidth
        val endX = step * stepWidth

        drawRect(
            color = trackColor,
            size = Size(totalWidth, size.height)
        )

        drawRect(
            color = progressColor,
            topLeft = Offset(startX, 0f),
            size = Size(endX - startX, size.height)
        )
    }
}
