package com.kuit.afternote.feature.mainpage.presentation.component.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.feature.mainpage.presentation.model.AfternoteTab
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Gray9

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

/**
 * 개별 탭 아이템 컴포넌트
 */
@Composable
private fun TabItem(
    tab: AfternoteTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(34.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(if (isSelected) B3 else Gray2)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tab.label,
            color = Gray9,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AfternoteTabRowPreview() {
    AfternoteTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AfternoteTabRow(
                selectedTab = AfternoteTab.ALL,
                onTabSelected = { }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TabItem(
                tab = AfternoteTab.ALL,
                isSelected = true,
                onClick = { }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TabItem(
                tab = AfternoteTab.SOCIAL_NETWORK,
                isSelected = false,
                onClick = { }
            )
        }
    }
}
