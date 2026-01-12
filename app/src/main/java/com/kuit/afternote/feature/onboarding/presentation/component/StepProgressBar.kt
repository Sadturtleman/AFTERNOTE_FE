package com.kuit.afternote.feature.onboarding.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray3

@Composable
fun StepProgressBar(
    step: Int,
    totalStep: Int,
    progressColor: Color = B3,
    trackColor: Color = Gray3
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

@Preview(showBackground = true)
@Composable
private fun StepProgressBarPreview() {
    AfternoteTheme {
        Column {
            StepProgressBar(step = 1, totalStep = 5)
            StepProgressBar(step = 3, totalStep = 5)
            StepProgressBar(step = 5, totalStep = 5)
        }
    }
}
