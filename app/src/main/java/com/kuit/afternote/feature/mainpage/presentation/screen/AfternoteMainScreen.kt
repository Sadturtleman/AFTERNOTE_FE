package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.AddFloatingActionButton
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.feature.mainpage.presentation.component.main.AfternoteListItem
import com.kuit.afternote.feature.mainpage.presentation.component.main.AfternoteTab
import com.kuit.afternote.feature.mainpage.presentation.component.main.AfternoteTabRow
import com.kuit.afternote.feature.mainpage.presentation.component.main.EmptyAfternoteContent
import com.kuit.afternote.feature.mainpage.presentation.component.main.MainHeader
import com.kuit.afternote.feature.mainpage.presentation.component.main.getIconResForTitle
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray1

/**
 * 애프터노트 메인 화면
 *
 * - 상단 탭
 * - 애프터노트 리스트 (빈 상태일 경우 EmptyAfternoteContent 표시)
 * - 하단 FAB 버튼
 */
@Composable
fun AfternoteMainScreen(
    modifier: Modifier = Modifier,
    afternoteItems: List<Pair<String, String>> = emptyList(),
    onItemClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(AfternoteTab.ALL) }
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.HOME) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Gray1,
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { selectedBottomNavItem = it }
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    end = 10.dp,
                    bottom = 94.dp
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                MainHeader()

                if (afternoteItems.isEmpty()) {
                    EmptyAfternoteContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                } else {
                    AfternoteTabRow(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )

                    val filteredItems = when (selectedTab) {
                        AfternoteTab.ALL -> afternoteItems
                        AfternoteTab.SOCIAL_NETWORK -> afternoteItems.filter { (title, _) ->
                            title.contains("인스타그램")
                        }

                        AfternoteTab.GALLERY_AND_FILES -> afternoteItems.filter { (title, _) ->
                            title.contains("갤러리")
                        }

                        else -> emptyList()
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            top = 20.dp,
                            end = 20.dp,
                            bottom = 104.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredItems) { (title, date) ->
                            AfternoteListItem(
                                title = title,
                                date = date,
                                imageRes = getIconResForTitle(title),
                                onClick = onItemClick
                            )
                        }
                    }
                }
            }

            // FAB 버튼
            AddFloatingActionButton(
                onClick = onAddClick
            )
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun AfternoteMainScreenPreview() {
    AfternoteTheme {
        AfternoteMainScreen(
            afternoteItems = listOf(
                "인스타그램" to "2023.11.24",
                "갤러리" to "2023.11.25",
                "갤러리" to "2023.11.26"
            )
        )
    }
}

@Preview(
    showBackground = true,
    name = "Empty State",
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun AfternoteMainScreenEmptyPreview() {
    AfternoteTheme {
        AfternoteMainScreen(
            afternoteItems = emptyList()
        )
    }
}
