package com.kuit.afternote.feature.mainpage.presentation.component.common.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 애프터노트 수정 화면 헤더 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 높이: 40dp
 * - 배경색: Gray1 (#FAFAFA)
 * - 뒤로가기 버튼, 중앙 타이틀 "애프터노트 작성하기", 우측 상단 "등록" 버튼
 * - 각 요소 상단 간격: 뒤로가기 4dp, 타이틀 6dp, 등록 버튼 8dp
 * - 상태바는 Scaffold의 paddingValues가 자동으로 처리
 */
@Composable
fun EditHeader(
    modifier: Modifier = Modifier,
    title: String = "애프터노트 작성하기",
    onBackClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Gray1)
            .padding(horizontal = 20.dp)
    ) {
        // 뒤로가기 버튼 - 상단 4dp
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .clickable(onClick = onBackClick)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .padding(top = 4.dp)
            )
        }

        // 타이틀 - 상단 6dp
        Text(
            text = title,
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Bold,
                color = Gray9,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 6.dp)
        )

        // 등록 버튼 - 상단 8dp
        Text(
            text = "등록",
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Normal,
                color = Gray5
            ),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp)
                .clickable(onClick = onRegisterClick)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditHeaderPreview() {
    AfternoteTheme {
        EditHeader(
            onBackClick = {},
            onRegisterClick = {}
        )
    }
}
