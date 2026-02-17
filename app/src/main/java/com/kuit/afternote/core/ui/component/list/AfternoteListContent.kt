package com.kuit.afternote.core.ui.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.R
import com.kuit.afternote.core.uimodel.AfternoteListDisplayItem
import com.kuit.afternote.ui.theme.AfternoteTheme

private const val LOAD_MORE_THRESHOLD = 3

/**
 * List params for [AfternoteListContent] (S107: keep param count ≤7).
 *
 * @param items Items to show
 * @param selectedTab Current tab
 * @param onTabSelected Tab selection callback
 * @param onItemClick Item click callback
 * @param hasNext Whether more pages exist
 * @param isLoadingMore Whether next page is loading
 * @param onLoadMore Callback when user scrolls near end and more can load
 */
data class AfternoteListContentListParams(
    val items: List<AfternoteListDisplayItem>,
    val selectedTab: AfternoteTab = AfternoteTab.ALL,
    val onTabSelected: (AfternoteTab) -> Unit = {},
    val onItemClick: (String) -> Unit = {},
    val hasNext: Boolean = false,
    val isLoadingMore: Boolean = false,
    val onLoadMore: () -> Unit = {}
)

/**
 * Shared list content for 애프터노트 list screens (writer main and receiver list).
 * Same look: tab row, then empty state or list of items. Only FAB differs at shell level.
 * When [list.hasNext] is true and user scrolls near the end, [list.onLoadMore] is called.
 */
@Composable
fun AfternoteListContent(
    modifier: Modifier = Modifier,
    list: AfternoteListContentListParams
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        AfternoteTabRow(
            selectedTab = list.selectedTab,
            onTabSelected = list.onTabSelected
        )
        Spacer(modifier = Modifier.height(20.dp))
        if (list.items.isEmpty() && list.selectedTab == AfternoteTab.ALL) {
            EmptyAfternoteContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        } else {
            AfternoteListContentPagedList(list = list)
        }
    }
}

@Composable
private fun AfternoteListContentPagedList(list: AfternoteListContentListParams) {
    val listState = rememberLazyListState()
    Box(modifier = Modifier.fillMaxWidth()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = list.items, key = { it.id }) { item ->
                AfternoteListItem(
                    item = item,
                    onClick = { list.onItemClick(item.id) }
                )
            }
        }
        if (list.hasNext && list.isLoadingMore) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                contentAlignment = Alignment.Center
            ) {}
        }
    }
    if (list.hasNext && !list.isLoadingMore && list.items.isNotEmpty()) {
        LaunchedEffect(listState, list.items.size) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                .collect { visible ->
                    val lastIndex = visible.lastOrNull()?.index ?: return@collect
                    if (lastIndex >= list.items.size - LOAD_MORE_THRESHOLD) {
                        list.onLoadMore()
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
            list = AfternoteListContentListParams(
                items = emptyList(),
                selectedTab = AfternoteTab.ALL
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AfternoteListContentWithItemsPreview() {
    AfternoteTheme {
        AfternoteListContent(
            list = AfternoteListContentListParams(
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
                selectedTab = AfternoteTab.ALL
            )
        )
    }
}
