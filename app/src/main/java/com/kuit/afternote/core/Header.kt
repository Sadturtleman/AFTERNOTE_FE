package com.kuit.afternote.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
 * 공통 헤더 컴포넌트 (오버로딩)
 *
 * rem 기반 반응형 디자인:
 * - 컴포넌트 크기: 고정 dp 값 사용 (height: 40dp)
 * - Padding: 화면 가장자리는 고정값 (horizontal: 20dp)
 */
@Composable
fun Header(
    modifier: Modifier = Modifier,
    title: String
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
        )
    }
}

/**
 * 뒤로가기 + 타이틀 + 액션 버튼 헤더
 */
@Composable
fun Header(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit,
    onActionClick: () -> Unit,
    actionText: String = "등록"
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Gray1)
            .padding(horizontal = 20.dp)
    ) {
        // 뒤로가기 버튼
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .clickable(onClick = onBackClick)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "뒤로가기",
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // 타이틀
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

        // 액션 버튼
        Text(
            text = actionText,
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
                .clickable(onClick = onActionClick)
        )
    }
}

/**
 * 뒤로가기 + 타이틀 + 편집 아이콘 헤더
 */
@Composable
fun Header(
    modifier: Modifier = Modifier,
    title: String,
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
                painter = painterResource(R.drawable.ic_arrow_back),
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
                fontFamily = Sansneo,
                fontWeight = FontWeight.Bold,
                color = Gray9,
                textAlign = TextAlign.Center,
                letterSpacing = (-0.05).sp
            ),
            modifier = Modifier.align(Alignment.Center)
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
private fun HeaderPreview() {
    AfternoteTheme {
        Column {
            Header(title = "애프터노트")
            Header(
                title = "애프터노트 작성하기",
                onBackClick = {},
                onActionClick = {}
            )
            Header(
                title = "애프터노트 상세",
                onBackClick = {},
                onEditClick = {}
            )
        }
    }
}
