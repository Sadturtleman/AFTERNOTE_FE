package com.kuit.afternote.feature.receiver.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kuit.afternote.core.ui.screen.AfternoteListScreen
import com.kuit.afternote.core.ui.screen.AfternoteListScreenListParams
import com.kuit.afternote.core.ui.screen.AfternoteListScreenShellParams
import com.kuit.afternote.feature.receiver.presentation.screen.afternote.ReceiverAfternoteListEvent
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverAfternoteListUiState

/**
 * Receiver list Route. Calls shared AfternoteListScreen with showFab = false.
 */
@Composable
fun ReceiverAfternoteListRoute(
    modifier: Modifier = Modifier,
    uiState: ReceiverAfternoteListUiState,
    onEvent: (ReceiverAfternoteListEvent) -> Unit
) {
    AfternoteListScreen(
        modifier = modifier,
        shell = AfternoteListScreenShellParams(
            title = "애프터노트",
            bottomBarSelectedItem = uiState.selectedBottomNavItem,
            onBottomBarItemSelected = { onEvent(ReceiverAfternoteListEvent.SelectBottomNav(it)) },
            showFab = false,
            onFabClick = {}
        ),
        list = AfternoteListScreenListParams(
            items = uiState.items,
            selectedTab = uiState.selectedTab,
            onTabSelected = { onEvent(ReceiverAfternoteListEvent.SelectTab(it)) },
            onItemClick = { onEvent(ReceiverAfternoteListEvent.ClickItem(it)) }
        )
    )
}
