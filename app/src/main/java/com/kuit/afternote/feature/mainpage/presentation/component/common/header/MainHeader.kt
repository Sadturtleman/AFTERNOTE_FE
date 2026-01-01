package com.kuit.afternote.feature.mainpage.presentation.component.common.header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 애프터노트 메인 화면 헤더 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 높이: 40dp
 * - 배경색: Gray1 (#FAFAFA)
 * - 타이틀: "애프터노트", 중앙 정렬, 20sp, Bold
 * - 상태바는 Scaffold의 paddingValues가 자동으로 처리
 */
@Composable
fun MainHeader(
    modifier: Modifier = Modifier,
    title: String = "애프터노트"
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Gray1)
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 26.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Bold,
                color = Gray9,
                textAlign = TextAlign.Center,
                letterSpacing = (-0.05).sp
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .width(92.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainHeaderPreview() {
    AfternoteTheme {
        MainHeader()
    }
}
