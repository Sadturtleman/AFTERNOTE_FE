package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.feature.mainpage.presentation.component.AfternoteListItem
import com.kuit.afternote.feature.mainpage.presentation.component.AfternoteTab
import com.kuit.afternote.feature.mainpage.presentation.component.AfternoteTabRow
import com.kuit.afternote.feature.mainpage.presentation.component.getIconResForTitle
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
fun AfternoteMainScreen(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(AfternoteTab.ALL) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Gray1)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 상단 탭
            AfternoteTabRow(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            // TODO: 검색바 추가

            // 임시 데이터로 리스트 표시
            val sampleItems = listOf(
                "인스타그램" to "2023.11.24",
                "갤러리" to "2023.11.25",
                "갤러리" to "2023.11.26"
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                    vertical = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sampleItems) { (title, date) ->
                    AfternoteListItem(
                        title = title,
                        date = date,
                        imageRes = getIconResForTitle(title),
                        onClick = { /* TODO: 상세 화면으로 이동 */ }
                    )
                }
            }
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
