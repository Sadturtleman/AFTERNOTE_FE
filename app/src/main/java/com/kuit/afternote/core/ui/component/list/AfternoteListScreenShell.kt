package com.kuit.afternote.core.ui.component.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kuit.afternote.core.ui.component.ScaffoldContentWithOptionalFab
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.ui.theme.AfternoteTheme

/**
 * Shared shell for 애프터노트 list screens (writer main and receiver list).
 * Provides Scaffold with TopBar, BottomNavigationBar, optional FAB, and content slot.
 */
@Composable
fun AfternoteListScreenShell(
    modifier: Modifier = Modifier,
    title: String = "애프터노트",
    bottomBarSelectedItem: BottomNavItem = BottomNavItem.AFTERNOTE,
    onBottomBarItemSelected: (BottomNavItem) -> Unit,
    showFab: Boolean = false,
    onFabClick: () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(title = title)
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = bottomBarSelectedItem,
                onItemSelected = onBottomBarItemSelected
            )
        }
    ) { paddingValues ->
        ScaffoldContentWithOptionalFab(
            paddingValues = paddingValues,
            showFab = showFab,
            onFabClick = onFabClick,
            content = content
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AfternoteListScreenShellPreview() {
    AfternoteTheme {
        AfternoteListScreenShell(
            title = "애프터노트",
            bottomBarSelectedItem = BottomNavItem.AFTERNOTE,
            onBottomBarItemSelected = {},
            showFab = false,
            content = { modifier ->
                Box(modifier = modifier.fillMaxSize()) {
                    Text("Content")
                }
            }
        )
    }
}
