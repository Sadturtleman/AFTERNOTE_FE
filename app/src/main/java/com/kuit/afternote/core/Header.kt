package com.kuit.afternote.core

import android.R.attr.text
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
    Row(
        modifier = modifier
            .fillMaxWidth()
//            .height(40.dp)
            .padding(
                top = 4.dp,
                bottom = 10.dp
            ),
        horizontalArrangement = Arrangement.Center
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
            )
        )
    }
}

/**
 * 뒤로가기 + 타이틀 + 액션 버튼 헤더
 * Box + Alignment 방식으로 타이틀을 정확히 화면 중앙에 고정
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
            .padding(horizontal = 20.dp)
    ) {
        // 뒤로가기 버튼 (왼쪽)
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(
                    top = 11.dp,
                    bottom = 17.dp
                )
                .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "뒤로가기",
                modifier = Modifier.size(
                    width = 6.dp,
                    height = 12.dp
                )
            )
        }

        // 타이틀 (정확히 중앙)
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
                .padding(
                    top = 4.dp,
                    bottom = 10.dp
                )
        )

        // 액션 버튼 (오른쪽)
        Text(
            text = actionText,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Normal,
                color = Gray5,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(
                    top = 7.dp,
                    bottom = 13.dp
                )
                .clickable(onClick = onActionClick)
        )
    }
}

/**
 * 뒤로가기 + 타이틀 헤더 (액션 버튼 없음)
 * Box + Alignment 방식으로 타이틀을 정확히 화면 중앙에 고정
 */
@Composable
fun Header(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        // 뒤로가기 버튼 (왼쪽)
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(
                    top = 11.dp,
                    bottom = 17.dp
                )
                .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "뒤로가기",
                modifier = Modifier.size(
                    width = 6.dp,
                    height = 12.dp
                )
            )
        }

        // 타이틀 (정확히 중앙)
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
                .padding(
                    top = 4.dp,
                    bottom = 10.dp
                )
        )
    }
}

/**
 * 뒤로가기 + 편집 아이콘 헤더
 */
@Composable
fun Header(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Gray1)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        // 뒤로가기 버튼
        Box(
            modifier = Modifier
                .padding(
                    top = 11.dp,
                    bottom = 17.dp
                )
        ) {
            Image(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(
                        width = 6.dp,
                        height = 12.dp
                    )
                    .clickable(onClick = onBackClick)
            )
        }

        // 편집 버튼
        Box(
            modifier = Modifier
                .padding(
                    top = 4.dp,
                    bottom = 12.dp
                )
        ) {
            Image(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = "편집",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onEditClick)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HeaderPreview() {
    Column {
        Header(title = "애프터노트")
        Header(
            title = "애프터노트 작성하기",
            onBackClick = {},
            onActionClick = {}
        )
        Header(
            title = "추모 플레이리스트",
            onBackClick = {}
        )
        Header(
            onBackClick = {},
            onEditClick = {}
        )
    }
}
