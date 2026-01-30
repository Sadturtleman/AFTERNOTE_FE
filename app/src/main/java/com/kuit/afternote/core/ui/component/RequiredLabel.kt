package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.ui.theme.AfternoteTheme

/**
 * 필수 표시가 있는 라벨 컴포넌트
 *
 * **Note**: This is a convenience wrapper around [Label] with `isRequired = true`.
 * For more customization options, use [Label] directly with [LabelStyle].
 *
 * 피그마 디자인 기반:
 * - 텍스트: 16sp, Medium, Gray9
 * - 파란 점: 4dp 크기, 텍스트 오른쪽 위 꼭짓점으로부터 오른쪽 8.dp, 아래 offsetY.dp
 *
 * @param modifier Modifier for the component
 * @param text Label text
 * @param offsetY Vertical offset of the required dot from the top (default: 4f)
 */
@Composable
fun RequiredLabel(
    modifier: Modifier = Modifier,
    text: String,
    offsetY: Float = 4f
) {
    Label(
        modifier = modifier,
        text = text,
        isRequired = true,
        style = LabelStyle(requiredDotOffsetY = offsetY.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun RequiredLabelPreview() {
    AfternoteTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            RequiredLabel(text = "정보 처리 방법")
        }
    }
}
