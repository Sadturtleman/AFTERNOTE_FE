package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuit.afternote.core.AddFloatingActionButton
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.core.Header
import com.kuit.afternote.feature.mainpage.domain.model.AfternoteItem
import com.kuit.afternote.feature.mainpage.presentation.component.main.AfternoteListItem
import com.kuit.afternote.feature.mainpage.presentation.component.main.AfternoteTab
import com.kuit.afternote.feature.mainpage.presentation.component.main.AfternoteTabRow
import com.kuit.afternote.feature.mainpage.presentation.component.main.EmptyAfternoteContent
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Spacing

/**
 * 애프터노트 메인 화면 Route
 * ViewModel 주입 및 네비게이션 처리 담당
 */
@Composable
fun AfternoteMainRoute(
    viewModel: AfternoteMainViewModel = viewModel(),
    onNavigateToDetail: () -> Unit = {},
    onNavigateToAdd: () -> Unit = {},
    initialItems: List<AfternoteItem> = emptyList()
) {
    // 초기 데이터 설정 (한 번만 실행)
    LaunchedEffect(initialItems) {
        if (initialItems.isNotEmpty()) {
            viewModel.setItems(initialItems)
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AfternoteMainScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                is AfternoteMainEvent.ClickItem -> onNavigateToDetail()
                is AfternoteMainEvent.ClickAdd -> onNavigateToAdd()
                else -> viewModel.onEvent(event)
            }
        }
    )
}

/**
 * 애프터노트 메인 화면 (순수 UI 컴포저블)
 * Stateless Composable로 상태를 파라미터로 받음
 */
@Composable
fun AfternoteMainScreen(
    modifier: Modifier = Modifier,
    uiState: AfternoteMainUiState,
    onEvent: (AfternoteMainEvent) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                selectedItem = uiState.selectedBottomNavItem,
                onItemSelected = { navItem -> onEvent(AfternoteMainEvent.SelectBottomNav(navItem)) }
            )
        }
    ) { innerPadding ->
        // 패턴 A: Scaffold 기본 동작 활용 + 수동 계산
        // innerPadding에는 시스템 인셋(상태바)과 bottomBar 높이가 자동으로 포함됨
        // 피그마 디자인 요구값을 innerPadding에 더하여 정확한 패딩 적용
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    // 상단: 시스템 상태바 높이 + 디자인 요구값 (필요시 추가)
                    top = innerPadding.calculateTopPadding() + 20.dp,
                    // 하단: BottomBar 높이 (자동 계산됨)
                    bottom = innerPadding.calculateBottomPadding() + 16.dp,
                    // 좌우: 디자인 요구값 직접 적용
                    start = 20.dp,
                    end = 20.dp
                )
                // 중요: 시스템 인셋을 소비했다고 명시하여 자식 컴포넌트에서 중첩 적용 방지
                .consumeWindowInsets(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Header(title = "애프터노트")

                Spacer(modifier = Modifier.height(height = Spacing.m))

                if (uiState.items.isEmpty() && uiState.selectedTab == AfternoteTab.ALL) {
                    EmptyAfternoteContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                } else {
                    AfternoteContent(
                        selectedTab = uiState.selectedTab,
                        items = uiState.items,
                        onTabSelected = { onEvent(AfternoteMainEvent.SelectTab(it)) },
                        onItemClick = { onEvent(AfternoteMainEvent.ClickItem(it)) }
                    )
                }
            }

            // FAB 버튼
            AddFloatingActionButton(
                onClick = { onEvent(AfternoteMainEvent.ClickAdd) }
            )
        }
    }
}

/**
 * 애프터노트 콘텐츠 컴포넌트
 * 하위 컴포넌트 분리로 인지 복잡도 감소
 */
@Composable
private fun AfternoteContent(
    selectedTab: AfternoteTab,
    items: List<AfternoteItem>,
    onTabSelected: (AfternoteTab) -> Unit,
    onItemClick: (String) -> Unit
) {
    AfternoteTabRow(
        selectedTab = selectedTab,
        onTabSelected = onTabSelected
    )

    Spacer(modifier = Modifier.height(height = 20.dp))

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
//        contentPadding = PaddingValues(
//            start = 20.dp,
//            top = 20.dp,
//            end = 20.dp,
//            bottom = 20.dp
//        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            AfternoteListItem(
                item = item,
                onClick = { onItemClick(item.id) }
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
        AfternoteMainScreen(
            uiState = AfternoteMainUiState(
                selectedTab = AfternoteTab.ALL,
                items = listOf(
                    AfternoteItem(
                        id = "1",
                        serviceName = "인스타그램",
                        date = "2023.11.24",
                        type = com.kuit.afternote.feature.mainpage.domain.model.ServiceType.SOCIAL_NETWORK
                    ),
                    AfternoteItem(
                        id = "2",
                        serviceName = "갤러리",
                        date = "2023.11.25",
                        type = com.kuit.afternote.feature.mainpage.domain.model.ServiceType.GALLERY_AND_FILES
                    )
                )
            ),
            onEvent = {}
        )
    }
}

@Preview(
    showBackground = true,
    name = "Empty State",
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun AfternoteMainScreenEmptyPreview() {
    AfternoteTheme {
        AfternoteMainScreen(
            uiState = AfternoteMainUiState(),
            onEvent = {}
        )
    }
}
