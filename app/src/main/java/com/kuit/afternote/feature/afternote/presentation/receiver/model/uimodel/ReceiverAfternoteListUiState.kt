package com.kuit.afternote.feature.afternote.presentation.receiver.model.uimodel
import com.kuit.afternote.feature.afternote.presentation.shared.ui.component.list.AfternoteTab
import com.kuit.afternote.core.component.navigation.BottomNavItem
import com.kuit.afternote.feature.afternote.presentation.shared.model.uimodel.AfternoteListDisplayItem

/**
 * Receiver list screen UI state. Same shape as writer main (items, selectedTab, selectedBottomNavItem).
 */
data class ReceiverAfternoteListUiState(
    val selectedTab: AfternoteTab = AfternoteTab.ALL,
    val selectedBottomNavItem: BottomNavItem = BottomNavItem.AFTERNOTE,
    val items: List<AfternoteListDisplayItem> = emptyList()
)
