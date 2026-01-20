package com.kuit.afternote.feature.mainpage.presentation.component.edit.recipient

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
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
    val itemPositions = mutableStateMapOf<String, Offset>()
    val itemSizes = mutableStateMapOf<String, IntSize>()
    var boxPositionInRoot by mutableStateOf(Offset.Zero)
        private set

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
     * 박스 위치 업데이트
     */
    fun updateBoxPosition(offset: Offset) {
        boxPositionInRoot = offset
    }

    /**
     * 아이템 expanded 상태 토글
     */
    fun toggleItemExpanded(itemId: String) {
        expandedStates[itemId] = !(expandedStates[itemId] ?: false)
    }

    /**
     * 아이템 위치 업데이트
     */
    fun updateItemPosition(
        itemId: String,
        offset: Offset
    ) {
        itemPositions[itemId] = offset
    }

    /**
     * 아이템 크기 업데이트
     */
    fun updateItemSize(
        itemId: String,
        size: IntSize
    ) {
        itemSizes[itemId] = size
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
