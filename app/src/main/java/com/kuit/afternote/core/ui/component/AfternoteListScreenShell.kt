package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                    start = 20.dp,
                    end = 20.dp
                )
        ) {
            content(
                Modifier.padding(top = paddingValues.calculateTopPadding())
            )
            if (showFab) {
                AddFloatingActionButton(onClick = onFabClick)
            }
        }
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
