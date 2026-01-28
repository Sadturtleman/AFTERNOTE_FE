package com.kuit.afternote.feature.receiver.presentation.screen

import com.kuit.afternote.core.ui.component.AfternoteTab
import com.kuit.afternote.core.ui.component.BottomNavItem
import com.kuit.afternote.core.uimodel.AfternoteListDisplayItem

/**
 * Receiver list screen UI state. Same shape as writer main (items, selectedTab, selectedBottomNavItem).
 */
data class ReceiverAfternoteListUiState(
    val selectedTab: AfternoteTab = AfternoteTab.ALL,
    val selectedBottomNavItem: BottomNavItem = BottomNavItem.AFTERNOTE,
    val items: List<AfternoteListDisplayItem> = emptyList()
)
