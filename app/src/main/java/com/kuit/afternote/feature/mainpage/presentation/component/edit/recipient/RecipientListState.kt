package com.kuit.afternote.feature.mainpage.presentation.component.edit.recipient

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Recipient

/**
 * RecipientList의 상태를 관리하는 State Holder
 */
@Stable
class RecipientListState(
    initialShowTextField: Boolean = false,
    initialExpandedItemId: String? = null
) {
    var showTextField by mutableStateOf(initialShowTextField)
        private set

    var previousFocusedState by mutableStateOf(false)
        private set

    val expandedStates = mutableStateMapOf<String, Boolean>()

    /**
     * 초기화: 수신자들의 expanded 상태 설정
     */
    fun initializeExpandedStates(
        recipients: List<Recipient>,
        initialExpandedItemId: String?
    ) {
        recipients.forEach { recipient ->
            if (!expandedStates.containsKey(recipient.id)) {
                expandedStates[recipient.id] = initialExpandedItemId == recipient.id
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
     * 텍스트 필드 표시 상태 변경
     */
    fun setTextFieldVisible(visible: Boolean) {
        showTextField = visible
    }

    /**
     * 아이템 expanded 상태 토글
     */
    fun toggleItemExpanded(itemId: String) {
        expandedStates[itemId] = !(expandedStates[itemId] ?: false)
    }

    /**
     * 이전 포커스 상태 업데이트
     */
    fun updatePreviousFocusedState(isFocused: Boolean) {
        previousFocusedState = isFocused
    }
}

/**
 * RecipientListState를 생성하는 Composable 함수
 */
@Composable
fun rememberRecipientListState(
    initialShowTextField: Boolean = false,
    initialExpandedItemId: String? = null
): RecipientListState =
    remember {
        RecipientListState(
            initialShowTextField = initialShowTextField,
            initialExpandedItemId = initialExpandedItemId
        )
    }
