
package com.kuit.afternote.feature.afternote.presentation.component.edit.processingmethod

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.ProcessingMethodItem

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

    var editingItemId by mutableStateOf<String?>(null)
        private set

    val expandedStates = mutableStateMapOf<String, Boolean>()

    /** Stored from constructor for use when [initializeExpandedStates] is called with null. */
    private val defaultExpandedItemId: String? = initialExpandedItemId

    /**
     * 초기화: 아이템들의 expanded 상태 설정
     * @param initialExpandedItemId 아이템 id; null이면 생성자에서 받은 [defaultExpandedItemId] 사용
     */
    fun initializeExpandedStates(
        items: List<ProcessingMethodItem>,
        initialExpandedItemId: String?
    ) {
        val expandedId = initialExpandedItemId ?: defaultExpandedItemId
        items.forEach { item ->
            if (!expandedStates.containsKey(item.id)) {
                expandedStates[item.id] = expandedId == item.id
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

    /**
     * 아이템 인라인 편집 모드 시작
     */
    fun startEditing(itemId: String) {
        editingItemId = itemId
    }

    /**
     * 아이템 인라인 편집 모드 종료
     */
    fun stopEditing() {
        editingItemId = null
    }
}

/**
 * MainPageEditReceiverListState를 생성하는 Composable 함수
 *
 * ProcessingMethodListState를 생성하는 Composable 함수
 */
@Composable
fun rememberProcessingMethodListState(
    initialShowTextField: Boolean = false
): ProcessingMethodListState =
    remember {
        ProcessingMethodListState(
            initialShowTextField = initialShowTextField,
        )
    }
