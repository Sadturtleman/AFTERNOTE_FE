package com.kuit.afternote.feature.mainpage.presentation.component.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray2

/**
 * 정보 카드 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 배경색: Gray2 (#EEEEEE)
 * - 모서리: 16dp
 * - 패딩: 16dp
 */
@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Gray2
    ) {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoCardPreview() {
    AfternoteTheme {
        InfoCard {
            Column {
                Text("제목")
                Text("내용")
            }
        }
    }
}
