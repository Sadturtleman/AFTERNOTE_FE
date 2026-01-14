package com.kuit.afternote.core

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray4

/**
 * 커스텀 라디오 버튼 컴포넌트
 *
 * 체크 표시(인디케이터)와 윤곽선 간 간격이 전체 크기의 12분의 1이 되도록 자동 계산됩니다.
 *
 * @param selected 선택 여부
 * @param onClick 클릭 이벤트 (null인 경우 클릭 불가, 부모 컴포넌트에서 클릭 처리)
 * @param buttonSize 전체 버튼 크기 (기본값: 24.dp)
 * @param selectedColor 선택된 색상 (기본값: B2)
 * @param unselectedColor 선택 안 된 색상 (기본값: Gray4)
 */
@Composable
fun CustomRadioButton(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: (() -> Unit)? = null,
    buttonSize: Dp = 24.dp,
    selectedColor: Color = B2,
    unselectedColor: Color = Gray4
) {
    // 보더 두께
    val borderWidth = 1.dp

    // 체크 표시와 윤곽선 간 간격 = 전체 크기의 1/12
    val spacing = buttonSize / 12f

    // 체크 표시 크기 = 전체 크기 - (보더 * 2) - (간격 * 2)
    val indicatorSize = buttonSize - (borderWidth * 2) - (spacing * 2)

    Box(
        modifier = modifier
            .size(buttonSize)
            .clip(CircleShape)
            .border(
                width = borderWidth,
                color = if (selected) selectedColor else unselectedColor,
                shape = CircleShape
            ).then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(indicatorSize)
                    .background(
                        color = selectedColor,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomRadioButtonPreview() {
    AfternoteTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomRadioButton(
                selected = true,
                onClick = {}
            )
            CustomRadioButton(
                selected = false,
                onClick = {}
            )
        }
    }
}
