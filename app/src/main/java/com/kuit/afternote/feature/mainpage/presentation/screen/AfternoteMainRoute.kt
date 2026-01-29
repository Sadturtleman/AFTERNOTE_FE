package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.core.ui.screen.AfternoteListScreen
import com.kuit.afternote.core.ui.screen.AfternoteListScreenListParams
import com.kuit.afternote.core.ui.screen.AfternoteListScreenShellParams
import com.kuit.afternote.core.uimodel.AfternoteListDisplayItem
import com.kuit.afternote.feature.mainpage.domain.model.AfternoteItem
import com.kuit.afternote.feature.mainpage.presentation.common.util.IconResourceMapper

/**
 * 애프터노트 메인 Route. Maps domain items to display items and calls shared AfternoteListScreen.
 */
@Composable
fun AfternoteMainRoute(
    viewModel: AfternoteMainViewModel = hiltViewModel(),
    onNavigateToDetail: () -> Unit = {},
    onNavigateToGalleryDetail: () -> Unit = {},
    onNavigateToAdd: () -> Unit = {},
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
            onBottomBarItemSelected = { viewModel.onEvent(AfternoteMainEvent.SelectBottomNav(it)) },
            showFab = true,
            onFabClick = onNavigateToAdd
        ),
        list = AfternoteListScreenListParams(
            items = displayItems,
            selectedTab = uiState.selectedTab,
            onTabSelected = { viewModel.onEvent(AfternoteMainEvent.SelectTab(it)) },
            onItemClick = { itemId ->
                val item = uiState.items.find { it.id == itemId }
                if (item?.serviceName == "갤러리") {
                    onNavigateToGalleryDetail()
                } else {
                    onNavigateToDetail()
                }
            }
        )
    )
}
