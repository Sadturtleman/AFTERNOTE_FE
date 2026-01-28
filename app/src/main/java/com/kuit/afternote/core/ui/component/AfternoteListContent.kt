package com.kuit.afternote.core.ui.component

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
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.uimodel.AfternoteListDisplayItem

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
