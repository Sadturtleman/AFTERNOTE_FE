package com.kuit.afternote.feature.afternote.presentation.screen

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.core.ui.screen.AfternoteListScreen
import com.kuit.afternote.core.ui.screen.AfternoteListScreenListParams
import com.kuit.afternote.core.ui.screen.AfternoteListScreenShellParams
import com.kuit.afternote.core.uimodel.AfternoteListDisplayItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem
import com.kuit.afternote.feature.afternote.domain.model.ServiceType
import com.kuit.afternote.feature.afternote.presentation.common.util.IconResourceMapper

data class AfternoteListRouteCallbacks(
    val onNavigateToDetail: (String) -> Unit = {},
    val onNavigateToGalleryDetail: (String) -> Unit = {},
    val onNavigateToMemorialGuidelineDetail: (String) -> Unit = {},
    val onNavigateToAdd: () -> Unit = {},
    val onBottomNavTabSelected: (BottomNavItem) -> Unit = {}
)

/**
 * 애프터노트 목록 Route.
 *
 * 실제 동작에서는 항상 서버에서 목록을 우선 로드합니다.
 * [initialItems]는 Preview나 명시적인 더미 모드에서만 사용해야 하며,
 * 프로덕션 경로에서는 빈 리스트로 전달됩니다.
 */
@Composable
fun AfternoteListRoute(
    viewModel: AfternoteListViewModel = hiltViewModel(),
    callbacks: AfternoteListRouteCallbacks = AfternoteListRouteCallbacks(),
    initialItems: List<AfternoteItem> = emptyList(),
    onItemsChanged: (List<AfternoteItem>) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        // 항상 서버 데이터를 우선 로드
        viewModel.loadAfternotes()
        // Preview·더미 모드에서 initialItems를 명시적으로 넘긴 경우에만 사용
        if (initialItems.isNotEmpty()) {
            viewModel.setItems(initialItems)
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Propagate items to parent only when the list actually changes (by ID set).
    // Avoids expensive NavHost recomposition on every return from sub-routes
    // when the API returns the same data.
    val itemIds = remember(uiState.items) { uiState.items.map { it.id }.toSet() }
    LaunchedEffect(itemIds) {
        if (uiState.items.isNotEmpty()) {
            Log.d("AfternoteListRoute", "items changed: size=${uiState.items.size}")
            onItemsChanged(uiState.items)
        }
    }

    val displayItems = uiState.items.map { item ->
        AfternoteListDisplayItem(
            id = item.id,
            serviceName = item.serviceName,
            date = item.date,
            iconResId = IconResourceMapper.getIconResForServiceType(item.type)
        )
    }

    AfternoteListScreen(
        shell = AfternoteListScreenShellParams(
            title = "애프터노트",
            bottomBarSelectedItem = uiState.selectedBottomNavItem,
            onBottomBarItemSelected = {
                viewModel.onEvent(AfternoteListEvent.SelectBottomNav(it))
                callbacks.onBottomNavTabSelected(it)
            },
            showFab = true,
            onFabClick = callbacks.onNavigateToAdd
        ),
        list = AfternoteListScreenListParams(
            items = displayItems,
            selectedTab = uiState.selectedTab,
            onTabSelected = { viewModel.onEvent(AfternoteListEvent.SelectTab(it)) },
            onItemClick = { itemId ->
                val item = uiState.items.find { it.id == itemId } ?: return@AfternoteListScreenListParams
                // Design has two detail screens only: Social Network, Gallery and Files. Others use Social-style detail.
                when (item.type) {
                    ServiceType.GALLERY_AND_FILES -> callbacks.onNavigateToGalleryDetail(itemId)
                    ServiceType.MEMORIAL -> callbacks.onNavigateToMemorialGuidelineDetail(itemId)
                    else -> callbacks.onNavigateToDetail(itemId)
                }
            }
        )
    )
}
