package com.kuit.afternote.feature.afternote.presentation.component.edit.afternoteeditreceiver

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiver

/**
 * AfternoteEditReceiverList의 상태를 관리하는 State Holder
 */
@Stable
class AfternoteEditReceiverListState(
    initialShowTextField: Boolean = false,
) {
    var showTextField by mutableStateOf(initialShowTextField)
        private set

    val expandedStates = mutableStateMapOf<String, Boolean>()

    /**
     * 초기화: 수신자들의 expanded 상태 설정
     */
    fun initializeExpandedStates(
        afternoteEditReceivers: List<AfternoteEditReceiver>,
        initialExpandedItemId: String?
    ) {
        afternoteEditReceivers.forEach { receiver ->
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
 * AfternoteEditReceiverListState를 생성하는 Composable 함수
 */
@Composable
fun rememberAfternoteEditReceiverListState(initialShowTextField: Boolean = false): AfternoteEditReceiverListState =
    remember {
        AfternoteEditReceiverListState(
            initialShowTextField = initialShowTextField,
        )
    }
