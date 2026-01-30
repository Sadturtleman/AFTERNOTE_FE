package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * Style configuration for Label component.
 *
 * @param fontSize Font size of the label text (default: 16.sp)
 * @param lineHeight Line height of the label text (default: 22.sp)
 * @param fontWeight Font weight of the label text (default: Medium)
 * @param color Text color (default: Gray9)
 * @param requiredDotOffsetY Vertical offset of the required dot from the top (default: 4.dp)
 */
data class LabelStyle(
    val fontSize: TextUnit = 16.sp,
    val lineHeight: TextUnit = 22.sp,
    val fontWeight: FontWeight = FontWeight.Medium,
    val color: Color = Gray9,
    val requiredDotOffsetY: Dp = 4.dp
)

/**
 * 라벨 컴포넌트 (필수 표시 옵션 포함)
 *
 * 피그마 디자인 기반:
 * - 텍스트: 기본값 16sp, Medium, Gray9 (LabelStyle로 커스터마이징 가능)
 * - 필수 표시 (isRequired=true): 파란 점 4dp, 텍스트 오른쪽 위 꼭짓점으로부터 오른쪽 8.dp
 *
 * @param modifier Modifier for the component
 * @param text Label text
 * @param isRequired Whether to show the required indicator (blue dot)
 * @param style Style configuration for the label
 */
@Composable
fun Label(
    modifier: Modifier = Modifier,
    text: String,
    isRequired: Boolean = false,
    style: LabelStyle = LabelStyle()
) {
    Box(modifier = modifier) {
        var textWidth by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        Text(
            text = text,
            style = TextStyle(
                fontSize = style.fontSize,
                lineHeight = style.lineHeight,
                fontFamily = Sansneo,
                fontWeight = style.fontWeight,
                color = style.color
            ),
            modifier = Modifier.onGloballyPositioned { coordinates ->
                textWidth = with(density) { coordinates.size.width.toDp() }
            }
        )

        if (isRequired) {
            Box(
                modifier = Modifier
                    .offset(x = textWidth + 8.dp, y = style.requiredDotOffsetY)
                    .size(4.dp)
                    .background(color = B2, shape = CircleShape)
            )
        }
    }
}

@Preview(showBackground = true, name = "기본 라벨")
@Composable
private fun LabelPreview() {
    AfternoteTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Label(text = "이름")
        }
    }
}

@Preview(showBackground = true, name = "필수 라벨")
@Composable
private fun LabelRequiredPreview() {
    AfternoteTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Label(
                text = "정보 처리 방법",
                isRequired = true
            )
        }
    }
}

@Preview(showBackground = true, name = "작은 라벨 (12sp)")
@Composable
private fun LabelSmallPreview() {
    AfternoteTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Label(
                text = "종류",
                style = LabelStyle(
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

@Preview(showBackground = true, name = "다양한 라벨 스타일")
@Composable
private fun LabelVariantsPreview() {
    AfternoteTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Label(text = "일반 라벨")
            Label(text = "필수 라벨", isRequired = true)
            Label(
                text = "작은 필수 라벨",
                isRequired = true,
                style = LabelStyle(
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Normal,
                    requiredDotOffsetY = 2.dp
                )
            )
        }
    }
}
