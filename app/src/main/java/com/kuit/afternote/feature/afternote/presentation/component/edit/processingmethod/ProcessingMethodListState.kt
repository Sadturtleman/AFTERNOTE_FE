<<<<<<<< HEAD:app/src/main/java/com/kuit/afternote/feature/mainpage/presentation/component/edit/mainpageeditreceiver/MainPageEditReceiverListState.kt
package com.kuit.afternote.feature.mainpage.presentation.component.edit.mainpageeditreceiver
========
package com.kuit.afternote.feature.afternote.presentation.component.edit.processingmethod
>>>>>>>> 18f1a0c82c9329353ff9254b226743d587b4b33f:app/src/main/java/com/kuit/afternote/feature/afternote/presentation/component/edit/processingmethod/ProcessingMethodListState.kt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
<<<<<<<< HEAD:app/src/main/java/com/kuit/afternote/feature/mainpage/presentation/component/edit/mainpageeditreceiver/MainPageEditReceiverListState.kt
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.MainPageEditReceiver

/**
 * MainPageEditReceiverList의 상태를 관리하는 State Holder
 */
@Stable
class MainPageEditReceiverListState(
========
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.ProcessingMethodItem

/**
 * ProcessingMethodList의 상태를 관리하는 State Holder
 */
@Stable
class ProcessingMethodListState(
>>>>>>>> 18f1a0c82c9329353ff9254b226743d587b4b33f:app/src/main/java/com/kuit/afternote/feature/afternote/presentation/component/edit/processingmethod/ProcessingMethodListState.kt
    initialShowTextField: Boolean = false,
) {
    var showTextField by mutableStateOf(initialShowTextField)
        private set

    val expandedStates = mutableStateMapOf<String, Boolean>()

    /** Stored from constructor for use when [initializeExpandedStates] is called with null. */
    private val defaultExpandedItemId: String? = initialExpandedItemId

    /**
     * 초기화: 아이템들의 expanded 상태 설정
     * @param initialExpandedItemId 아이템 id; null이면 생성자에서 받은 [defaultExpandedItemId] 사용
     */
    fun initializeExpandedStates(
<<<<<<<< HEAD:app/src/main/java/com/kuit/afternote/feature/mainpage/presentation/component/edit/mainpageeditreceiver/MainPageEditReceiverListState.kt
        mainPageEditReceivers: List<MainPageEditReceiver>,
        initialExpandedItemId: String?
    ) {
        mainPageEditReceivers.forEach { receiver ->
            if (!expandedStates.containsKey(receiver.id)) {
                expandedStates[receiver.id] = initialExpandedItemId == receiver.id
========
        items: List<ProcessingMethodItem>,
        initialExpandedItemId: String?
    ) {
        val expandedId = initialExpandedItemId ?: defaultExpandedItemId
        items.forEach { item ->
            if (!expandedStates.containsKey(item.id)) {
                expandedStates[item.id] = expandedId == item.id
>>>>>>>> 18f1a0c82c9329353ff9254b226743d587b4b33f:app/src/main/java/com/kuit/afternote/feature/afternote/presentation/component/edit/processingmethod/ProcessingMethodListState.kt
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
<<<<<<<< HEAD:app/src/main/java/com/kuit/afternote/feature/mainpage/presentation/component/edit/mainpageeditreceiver/MainPageEditReceiverListState.kt
 * MainPageEditReceiverListState를 생성하는 Composable 함수
 */
@Composable
fun rememberMainPageEditReceiverListState(initialShowTextField: Boolean = false,): MainPageEditReceiverListState =
    remember {
        MainPageEditReceiverListState(
========
 * ProcessingMethodListState를 생성하는 Composable 함수
 */
@Composable
fun rememberProcessingMethodListState(
    initialShowTextField: Boolean = false,
    initialExpandedItemId: String? = null
): ProcessingMethodListState =
    remember {
        ProcessingMethodListState(
>>>>>>>> 18f1a0c82c9329353ff9254b226743d587b4b33f:app/src/main/java/com/kuit/afternote/feature/afternote/presentation/component/edit/processingmethod/ProcessingMethodListState.kt
            initialShowTextField = initialShowTextField,
        )
    }
