package com.kuit.afternote.core.ui.component.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.AfternoteTheme

/**
 * 체크 원 아이콘 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 체크 아이콘: 파란색 원 안에 체크마크, 16dp
 * - 처리 방법 리스트에서 장식용으로 사용
 */
@Composable
fun CheckCircleIcon(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_check_circle),
        contentDescription = null,
        modifier = modifier.size(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun CheckCircleIconPreview() {
    AfternoteTheme {
        CheckCircleIcon()
    }
}
