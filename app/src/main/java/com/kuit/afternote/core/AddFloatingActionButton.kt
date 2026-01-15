package com.kuit.afternote.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.R
import com.kuit.afternote.ui.expand.dropShadow

/**
 * 추가 버튼 FAB 컴포넌트
 *
 * 피그마 디자인:
 * - 오른쪽 20dp, 아래쪽 16dp 패딩
 * - 그림자: offset (0, 2), blur 40dp, spread 0, 투명도 15%
 *
 * Box scope 안에서 사용해야 합니다.
 */
@Composable
fun BoxScope.AddFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .align(Alignment.BottomEnd)
            .dropShadow(
                shape = CircleShape,
                color = Color(0x26000000),
                blur = 40.dp,
                offsetX = 0.dp,
                offsetY = 2.dp,
                spread = 0.dp
            ).background(Color.White, CircleShape)
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_add_circle_fab_no_padding),
            contentDescription = "새 애프터노트 추가"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddFloatingActionButtonPreview() {
    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        AddFloatingActionButton(
            onClick = {}
        )
    }
}
