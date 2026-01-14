package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 마지막 순간 질문 텍스트 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 텍스트: 16sp, Regular, Gray9
 * - 라인 높이: 22sp
 */
@Composable
fun LastMomentQuestion(
    modifier: Modifier = Modifier,
    text: String = "마지막 순간,\n당신을 어떤 모습으로 기억해 주길 바라나요?"
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray9
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LastMomentQuestionPreview() {
    AfternoteTheme {
        LastMomentQuestion(
            modifier = Modifier.padding(20.dp)
        )
    }
}
