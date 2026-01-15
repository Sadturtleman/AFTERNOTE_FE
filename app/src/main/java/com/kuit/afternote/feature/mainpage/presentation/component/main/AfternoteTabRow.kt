package com.kuit.afternote.feature.mainpage.presentation.component.main

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ArrowIconSpec
import com.kuit.afternote.core.CircleArrowIcon
import com.kuit.afternote.feature.mainpage.presentation.model.AfternoteTab
import com.kuit.afternote.ui.expand.horizontalFadingEdge
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

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
    canScrollRight: Boolean = false,
    onTabSelected: (AfternoteTab) -> Unit,
    onScrollStateChanged: (Boolean) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(scrollState.value) {
        val canScroll = scrollState.canScrollForward
        if (canScroll != canScrollRight) {
            onScrollStateChanged(canScroll)
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalFadingEdge(edgeWidth = 45.dp) // 페이드 효과를 스크롤보다 먼저 적용하여 화면에 고정
                .horizontalScroll(scrollState),
//                .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 0.dp),
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

        // 오른쪽 끝에 화살표 아이콘 (스크롤 가능할 때만 표시)
        if (canScrollRight) {
            CircleArrowIcon(
                iconSpec = ArrowIconSpec(
                    iconRes = R.drawable.ic_arrow_right_tab,
                    contentDescription = "더 보기"
                ),
                backgroundColor = B1,
                size = 12.dp,
                modifier = Modifier.align(Alignment.CenterEnd)
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
            fontFamily = Sansneo,
            fontWeight = FontWeight.Medium,
            lineHeight = 18.sp,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AfternoteTabRowPreview() {
    AfternoteTheme {
        var selectedTab by remember { mutableStateOf(AfternoteTab.ALL) }
        AfternoteTabRow(
            selectedTab = selectedTab,
            canScrollRight = false,
            onTabSelected = { selectedTab = it }
        )
    }
}
