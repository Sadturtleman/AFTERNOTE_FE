package com.kuit.afternote.feature.mainpage.presentation.component.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray9

/**
 * 애프터노트 상세 화면 헤더 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 높이: 40dp
 * - 배경색: Gray1 (#FAFAFA)
 * - 뒤로가기 버튼, 중앙 타이틀, 편집 버튼
 * - 상태바는 Scaffold의 paddingValues가 자동으로 처리
 */
@Composable
fun DetailHeader(
    modifier: Modifier = Modifier,
    title: String = "애프터노트 상세",
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Gray1)
    ) {
        // 뒤로가기 버튼
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp)
                .size(24.dp)
                .clickable(onClick = onBackClick)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_chevron_left),
                contentDescription = "뒤로가기",
                modifier = Modifier.size(24.dp)
            )
        }

        // 타이틀
        Text(
            text = title,
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 26.sp,
                fontFamily = FontFamily(Font(R.font.sansneobold)),
                fontWeight = FontWeight(700),
                color = Gray9,
                textAlign = TextAlign.Center,
                letterSpacing = (-0.05).sp
            ),
            modifier = Modifier
                .align(Alignment.Center)
        )

        // 편집 버튼
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 20.dp)
                .size(24.dp)
                .clickable(onClick = onEditClick)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = "편집",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailHeaderPreview() {
    AfternoteTheme {
        DetailHeader(
            onBackClick = {},
            onEditClick = {}
        )
    }
}
