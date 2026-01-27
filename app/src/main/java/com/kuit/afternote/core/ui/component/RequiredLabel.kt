package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 필수 표시가 있는 라벨 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 텍스트: 16sp, Medium, Gray9
 * - 파란 점: 4dp 크기, 텍스트 오른쪽 위 꼭짓점으로부터 오른쪽 8.dp, 아래 offsetY.dp
 */
@Composable
fun RequiredLabel(
    modifier: Modifier = Modifier,
    text: String,
    offsetY: Float = 4f
) {
    Box(modifier = modifier) {
        var textWidth by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight(500),
                color = Gray9
            ),
            modifier = Modifier.onGloballyPositioned { coordinates ->
                textWidth = with(density) { coordinates.size.width.toDp() }
            }
        )
        // 파란 점: 오른쪽 위 꼭짓점으로부터 오른쪽 8.dp, 아래 offsetY.dp
        Box(
            modifier = Modifier
                .offset(x = textWidth + 8.dp, y = offsetY.dp)
                .size(4.dp)
                .background(color = B2, shape = CircleShape)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RequiredLabelPreview() {
    AfternoteTheme {
        RequiredLabel(text = "정보 처리 방법")
    }
}
