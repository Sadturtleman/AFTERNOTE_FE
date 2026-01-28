package com.kuit.afternote.feature.setting.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kuit.afternote.ui.expand.dropShadow
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray2

@Composable
fun ThumbSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    // 트랙 (배경)
    Box(
        modifier = Modifier
            .width(64.dp)
            .height(32.dp)
            .background(
                color = if (checked) Color(0xFF91C1FF) else Gray2,
                shape = CircleShape
            ).clickable { onCheckedChange(!checked) }
            .padding(2.dp), // 내부 여백
        contentAlignment = Alignment.CenterStart
    ) {
        // 엄지 (Thumb)
        Box(
            modifier = Modifier
                .size(24.dp) // 여기서 원하는 대로 크기 조절 가능!
                .background(if (!checked) B2 else Color.White, CircleShape)
                .dropShadow(
                    shape = CircleShape,
                    offsetX = 0.dp,
                    offsetY = 2.dp,
                    blur = 10.dp,
                    spread = 0.dp,
                    color = if (!checked) Color.Black.copy(0.15f) else Color.White
                )
        )
    }
}
