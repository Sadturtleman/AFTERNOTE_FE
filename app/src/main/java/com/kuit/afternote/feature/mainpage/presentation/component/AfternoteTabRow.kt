package com.kuit.afternote.feature.mainpage.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Black9
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Gray5

/**
 * 애프터노트 탭 카테고리
 */
enum class AfternoteTab(val label: String) {
    ALL("전체"),
    SOCIAL_NETWORK("소셜네트워크"),
    BUSINESS("비즈니스"),
    GALLERY_AND_FILES("갤러리 및 파일"),
    ASSET_MANAGEMENT("재산 처리"),
    MEMORIAL("추모 가이드라인")
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
            .height(32.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) B1 else Gray2)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tab.label,
            color = if (isSelected) androidx.compose.ui.graphics.Color.White else Black9,
            fontSize = 14.sp
        )
    }
}

/**
 * 애프터노트 상단 탭 로우 컴포넌트
 * 
 * 피그마 디자인 기반:
 * - 가로 스크롤 가능한 탭 바
 * - 선택된 탭은 파란색 배경, 흰색 텍스트
 * - 선택되지 않은 탭은 회색 배경, 검은색 텍스트
 */
@Composable
fun AfternoteTabRow(
    selectedTab: AfternoteTab = AfternoteTab.ALL,
    onTabSelected: (AfternoteTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AfternoteTab.values().forEach { tab ->
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
        var selectedTab by remember { mutableStateOf(AfternoteTab.ALL) }
        
        AfternoteTabRow(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )
    }
}


