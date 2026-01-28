package com.kuit.afternote.feature.setting.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kuit.afternote.ui.theme.B2

@Composable
fun PasscodeIndicator(
    totalDots: Int = 4,
    // 입력된 개수
    filledDots: Int = 0
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            val isFilled = index < filledDots
            Box(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .size(15.dp)
                    .border(
                        width = 2.dp,
                        // B2 컬러
                        color = B2,
                        shape = CircleShape
                    ).background(
                        color = if (isFilled) Color(0xFF91C1FF) else Color.Transparent,
                        shape = CircleShape
                    )
            )
        }
    }
}
