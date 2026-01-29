package com.kuit.afternote.core.ui.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.R
import com.kuit.afternote.core.uimodel.AfternoteListDisplayItem
import com.kuit.afternote.ui.theme.AfternoteTheme

/**
 * Shared list content for 애프터노트 list screens (writer main and receiver list).
 * Same look: tab row, then empty state or list of items. Only FAB differs at shell level.
 */
@Composable
fun AfternoteListContent(
    modifier: Modifier = Modifier,
    items: List<AfternoteListDisplayItem>,
    selectedTab: AfternoteTab = AfternoteTab.ALL,
    onTabSelected: (AfternoteTab) -> Unit,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        AfternoteTabRow(
            selectedTab = selectedTab,
            onTabSelected = onTabSelected
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (items.isEmpty() && selectedTab == AfternoteTab.ALL) {
            EmptyAfternoteContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
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
    }
}

@Preview(showBackground = true)
@Composable
private fun AfternoteListContentEmptyPreview() {
    AfternoteTheme {
        AfternoteListContent(
            items = emptyList(),
            selectedTab = AfternoteTab.ALL,
            onTabSelected = {},
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AfternoteListContentWithItemsPreview() {
    AfternoteTheme {
        AfternoteListContent(
            items = listOf(
                AfternoteListDisplayItem(
                    id = "1",
                    serviceName = "추모 가이드라인",
                    date = "2025.12.01",
                    iconResId = R.drawable.img_logo
                ),
                AfternoteListDisplayItem(
                    id = "2",
                    serviceName = "인스타그램",
                    date = "2025.11.26",
                    iconResId = R.drawable.img_logo
                )
            ),
            selectedTab = AfternoteTab.ALL,
            onTabSelected = {},
            onItemClick = {}
        )
    }
}
