package com.kuit.afternote.feature.afternote.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.core.ui.screen.AfternoteListScreen
import com.kuit.afternote.core.ui.screen.AfternoteListScreenListParams
import com.kuit.afternote.core.ui.screen.AfternoteListScreenShellParams
import com.kuit.afternote.core.uimodel.AfternoteListDisplayItem
import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.feature.afternote.domain.model.ServiceType
import com.kuit.afternote.feature.afternote.presentation.common.util.IconResourceMapper

/**
 * 애프터노트 목록 Route. List is supplied via [initialItems]; detail/edit resolve from nav graph state.
 */
@Composable
fun AfternoteListRoute(
    viewModel: AfternoteListViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit = {},
    onNavigateToGalleryDetail: (String) -> Unit = {},
    onNavigateToAdd: () -> Unit = {},
    onBottomNavTabSelected: (BottomNavItem) -> Unit = {},
    initialItems: List<AfternoteItem> = emptyList()
) {
    LaunchedEffect(initialItems) {
        if (initialItems.isNotEmpty()) {
            viewModel.setItems(initialItems)
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                onBottomNavTabSelected(it)
            },
            showFab = true,
            onFabClick = onNavigateToAdd
        ),
        list = AfternoteListScreenListParams(
            items = displayItems,
            selectedTab = uiState.selectedTab,
            onTabSelected = { viewModel.onEvent(AfternoteListEvent.SelectTab(it)) },
            onItemClick = { itemId ->
                val item = uiState.items.find { it.id == itemId } ?: return@AfternoteListScreenListParams
                // Design has two detail screens only: Social Network, Gallery and Files. Others use Social-style detail.
                if (item.type == ServiceType.GALLERY_AND_FILES) {
                    onNavigateToGalleryDetail(itemId)
                } else {
                    onNavigateToDetail(itemId)
                }
            }
        )
    )
}
