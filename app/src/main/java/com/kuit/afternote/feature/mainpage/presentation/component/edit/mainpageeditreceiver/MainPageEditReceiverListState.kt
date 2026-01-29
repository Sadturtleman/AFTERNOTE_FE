package com.kuit.afternote.feature.mainpage.presentation.component.edit.mainpageeditreceiver

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.MainPageEditReceiver

/**
 * MainPageEditReceiverList의 상태를 관리하는 State Holder
 */
@Stable
class MainPageEditReceiverListState(
    initialShowTextField: Boolean = false,
) {
    var showTextField by mutableStateOf(initialShowTextField)
        private set

    val expandedStates = mutableStateMapOf<String, Boolean>()

    /**
     * 초기화: 수신자들의 expanded 상태 설정
     */
    fun initializeExpandedStates(
        mainPageEditReceivers: List<MainPageEditReceiver>,
        initialExpandedItemId: String?
    ) {
        mainPageEditReceivers.forEach { receiver ->
            if (!expandedStates.containsKey(receiver.id)) {
                expandedStates[receiver.id] = initialExpandedItemId == receiver.id
            }
        }
    }

    /**
     * 텍스트 필드 표시/숨김 토글
     */
    fun toggleTextField() {
        showTextField = !showTextField
    }

    /**
     * 아이템 expanded 상태 토글
     */
    fun toggleItemExpanded(itemId: String) {
        expandedStates[itemId] = !(expandedStates[itemId] ?: false)
    }
}

/**
 * MainPageEditReceiverListState를 생성하는 Composable 함수
 */
@Composable
fun rememberMainPageEditReceiverListState(
    initialShowTextField: Boolean = false,
): MainPageEditReceiverListState = remember {
    MainPageEditReceiverListState(
        initialShowTextField = initialShowTextField,
    )
}
