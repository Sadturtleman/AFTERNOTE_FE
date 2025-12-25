package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray1

/**
 * 애프터노트 메인 화면
 * 
 * 피그마 디자인 기반으로 구현
 * - 상단 탭 (전체/소셜/기타)
 * - 애프터노트 리스트
 * - 하단 FAB 버튼
 */
@Composable
fun AfternoteMainScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Gray1)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // TODO: 상단 탭 추가
            // TODO: 검색바 추가
            // TODO: 리스트 추가
            
            // 임시: 화면이 제대로 표시되는지 확인용
            Text(
                text = "애프터노트 메인 화면",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun AfternoteMainScreenPreview() {
    AfternoteTheme {
        AfternoteMainScreen()
    }
}

