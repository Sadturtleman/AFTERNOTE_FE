package com.kuit.afternote.core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kuit.afternote.R
import com.kuit.afternote.core.uimodel.AfternoteListDisplayItem
import com.kuit.afternote.ui.theme.AfternoteTheme

/** Shell params for AfternoteListScreen (title, bottom bar, FAB). */
data class AfternoteListScreenShellParams(
    val title: String = "애프터노트",
    val bottomBarSelectedItem: BottomNavItem = BottomNavItem.AFTERNOTE,
    val onBottomBarItemSelected: (BottomNavItem) -> Unit,
    val showFab: Boolean = false,
    val onFabClick: () -> Unit = {}
)

/** List params for AfternoteListScreen (items, tab, callbacks). */
data class AfternoteListScreenListParams(
    val items: List<AfternoteListDisplayItem>,
    val selectedTab: AfternoteTab = AfternoteTab.ALL,
    val onTabSelected: (AfternoteTab) -> Unit,
    val onItemClick: (String) -> Unit
)

/**
 * Single shared screen for 애프터노트 list (writer main and receiver list).
 * Writer and receiver both call this with showFab true/false and their own state/callbacks.
 */
@Composable
fun AfternoteListScreen(
    modifier: Modifier = Modifier,
    shell: AfternoteListScreenShellParams,
    list: AfternoteListScreenListParams
) {
    AfternoteListScreenShell(
        modifier = modifier,
        title = shell.title,
        bottomBarSelectedItem = shell.bottomBarSelectedItem,
        onBottomBarItemSelected = shell.onBottomBarItemSelected,
        showFab = shell.showFab,
        onFabClick = shell.onFabClick,
        content = { contentModifier ->
            AfternoteListContent(
                modifier = contentModifier,
                items = list.items,
                selectedTab = list.selectedTab,
                onTabSelected = list.onTabSelected,
                onItemClick = list.onItemClick
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun AfternoteListScreenPreview() {
    AfternoteTheme {
        AfternoteListScreen(
            shell = AfternoteListScreenShellParams(
                bottomBarSelectedItem = BottomNavItem.AFTERNOTE,
                onBottomBarItemSelected = {},
                showFab = false,
                onFabClick = {}
            ),
            list = AfternoteListScreenListParams(
                items = listOf(
                    AfternoteListDisplayItem(
                        id = "1",
                        serviceName = "추모 가이드라인",
                        date = "2025.12.01",
                        iconResId = R.drawable.img_logo
                    )
                ),
                selectedTab = AfternoteTab.ALL,
                onTabSelected = {},
                onItemClick = {}
            )
        )
    }
}
