package com.kuit.afternote.feature.mainpage.presentation.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.ui.theme.AfternoteTheme

/**
 * 애프터노트 상단 탭 로우 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 가로 스크롤 가능한 탭 바
 * - 선택된 탭은 연한 파란색(B3) 배경, 검은색 텍스트
 * - 선택되지 않은 탭은 회색(Gray2) 배경, 검은색 텍스트
 * - 탭 높이: 34dp, 모서리: 17dp, 폰트: 12sp
 */
@Composable
fun AfternoteTabRow(
    modifier: Modifier = Modifier,
    selectedTab: AfternoteTab = AfternoteTab.ALL,
    onTabSelected: (AfternoteTab) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AfternoteTab.entries.forEach { tab ->
            TabItem(
                tab = tab,
                isSelected = tab == selectedTab,
                onClick = { onTabSelected(tab) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AfternoteTabRowPreview() {
    AfternoteTheme {
        AfternoteTabRow(
            selectedTab = AfternoteTab.ALL,
            onTabSelected = { }
        )
    }
}
