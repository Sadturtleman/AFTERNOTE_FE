package com.kuit.afternote.feature.receiver.presentation.screen.afternote

import com.kuit.afternote.core.ui.component.list.AfternoteTab
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem

/**
 * 수신자 애프터노트 목록 화면에서 발생하는 사용자 이벤트.
 */
sealed interface ReceiverAfternoteListEvent {

    data class SelectTab(val tab: AfternoteTab) : ReceiverAfternoteListEvent

    data class SelectBottomNav(val navItem: BottomNavItem) : ReceiverAfternoteListEvent

    data class ClickItem(val itemId: String) : ReceiverAfternoteListEvent
}
