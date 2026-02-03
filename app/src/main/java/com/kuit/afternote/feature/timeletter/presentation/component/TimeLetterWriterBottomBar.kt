package com.kuit.afternote.feature.timeletter.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R

/**
 * 타임레터 작성 화면 하단 바
 *
 * @param draftCount 임시저장된 레터 개수
 * @param onLinkClick 링크 클릭 콜백
 * @param onAddClick 추가 클릭 콜백
 * @param onSaveDraftClick 임시저장 클릭 콜백
 * @param onDraftCountClick 임시저장 개수 클릭 콜백 (DraftLetterScreen으로 이동)
 * @param onMoreClick 더보기(추가 메뉴) 클릭 콜백
 * @param modifier Modifier
 */
@Composable
fun TimeLetterWriterBottomBar(
    draftCount: Int,
    onLinkClick: () -> Unit,
    onAddClick: () -> Unit,
    onSaveDraftClick: () -> Unit,
    onDraftCountClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(88.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(R.drawable.ic_link),
            contentDescription = "링크",
            modifier = Modifier
                .padding(start = 24.dp)
                .size(24.dp)
                .clickable { onLinkClick() }
        )
        Image(
            painterResource(R.drawable.ic_additional),
            contentDescription = "더보기",
            modifier = Modifier
                .padding(start = 16.33.dp)
                .size(24.dp)
                .clickable { onMoreClick() }
        )
        Text(
            text = "임시저장",
            modifier = Modifier
                .padding(start = 176.97.dp)
                .clickable { onSaveDraftClick() },
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            fontFamily = FontFamily(Font(R.font.sansneoregular)),
            lineHeight = 22.sp,
            color = Color(0xFF9E9E9E)
        )
        Image(
            painterResource(R.drawable.ic_radio_bar),
            contentDescription = "라디오 바",
            modifier = Modifier
                .padding(start = 16.dp)
        )
        Text(
            text = draftCount.toString(),
            modifier = Modifier
                .padding(start = 16.dp)
                .clickable { onDraftCountClick() },
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            fontFamily = FontFamily(Font(R.font.sansneoregular)),
            lineHeight = 22.sp,
            color = Color(0xFF9E9E9E)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeLetterWriterBottomBarPreview() {
    TimeLetterWriterBottomBar(
        draftCount = 3,
        onAddClick = {},
        onLinkClick = {},
        onSaveDraftClick = {},
        onDraftCountClick = {},
        onMoreClick = {}
    )
}
