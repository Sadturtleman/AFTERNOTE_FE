package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.feature.mainpage.presentation.component.main.AfternoteListItem
import com.kuit.afternote.feature.mainpage.presentation.component.main.AfternoteTab
import com.kuit.afternote.feature.mainpage.presentation.component.main.AfternoteTabRow
import com.kuit.afternote.feature.mainpage.presentation.component.main.MainHeader
import com.kuit.afternote.feature.mainpage.presentation.component.main.getIconResForTitle
import com.kuit.afternote.ui.expand.dropShadow
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray1

/**
 * 애프터노트 메인 화면
 *
 * - 상단 탭
 * - 애프터노트 리스트
 * - 하단 FAB 버튼
 */
@Composable
fun AfternoteMainScreen(
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(AfternoteTab.ALL) }
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.HOME) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Gray1,
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { selectedBottomNavItem = it }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: 새 애프터노트 생성 */ },
                modifier = Modifier
                    .size(56.dp)
                    .padding(end = 4.dp) // 기본 16dp에서 20dp로 조정 (4dp 추가)
                    .dropShadow(
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.15f), // 피그마: #000000 15%
                        blur = 10.dp, // 피그마: Blur 10
                        offsetY = 2.dp, // 피그마: Y 2
                        offsetX = 0.dp, // 피그마: X 0
                        spread = 0.dp // 피그마: Spread 0
                    ),
                containerColor = Color.Transparent,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFBDE0FF), // B3
                                    Color(0xFFFFE1CC) // 주황색 계열
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "새 애프터노트 추가",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            // 헤더
            MainHeader()
            // 상단 탭
            AfternoteTabRow(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            // 임시 데이터로 리스트 표시
            val allItems = listOf(
                "인스타그램" to "2023.11.24",
                "갤러리" to "2023.11.25",
                "갤러리" to "2023.11.26"
            )

            // 탭에 따라 필터링된 아이템
            val filteredItems = when (selectedTab) {
                AfternoteTab.ALL -> allItems
                AfternoteTab.SOCIAL_NETWORK -> allItems.filter { (title, _) ->
                    title.contains("인스타그램")
                }
                AfternoteTab.GALLERY_AND_FILES -> allItems.filter { (title, _) ->
                    title.contains("갤러리")
                }
                else -> emptyList() // 다른 탭들은 아직 데이터가 없음
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 20.dp,
                    end = 20.dp,
                    bottom = 104.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredItems) { (title, date) ->
                    AfternoteListItem(
                        title = title,
                        date = date,
                        imageRes = getIconResForTitle(title),
                        onClick = onItemClick
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
