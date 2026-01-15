package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.kuit.afternote.feature.mainpage.presentation.model.ProcessingMethodItem

/**
 * ProcessingMethodList의 상태를 관리하는 State Holder
 */
@Stable
class ProcessingMethodListState(
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
     * 초기화: 아이템들의 expanded 상태 설정
     */
    fun initializeExpandedStates(
        items: List<ProcessingMethodItem>,
        initialExpandedItemId: String?
    ) {
        items.forEach { item ->
            if (!expandedStates.containsKey(item.id)) {
                expandedStates[item.id] = initialExpandedItemId == item.id
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
 * ProcessingMethodListState를 생성하는 Composable 함수
 */
@Composable
fun rememberProcessingMethodListState(
    initialShowTextField: Boolean = false,
    initialExpandedItemId: String? = null
): ProcessingMethodListState =
    remember {
        ProcessingMethodListState(
            initialShowTextField = initialShowTextField,
            initialExpandedItemId = initialExpandedItemId
        )
    }
