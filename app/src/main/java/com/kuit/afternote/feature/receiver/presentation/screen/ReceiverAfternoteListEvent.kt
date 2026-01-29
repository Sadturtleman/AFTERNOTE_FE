package com.kuit.afternote.feature.receiver.presentation.screen

import com.kuit.afternote.core.ui.component.list.AfternoteTab
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem

/**
 * Receiver list screen events. Same shape as writer main (no ClickAdd).
 */
sealed interface ReceiverAfternoteListEvent {
    data class SelectTab(
        val tab: AfternoteTab
    ) : ReceiverAfternoteListEvent

    data class SelectBottomNav(
        val navItem: BottomNavItem
    ) : ReceiverAfternoteListEvent

    data class ClickItem(
        val itemId: String
    ) : ReceiverAfternoteListEvent
}
