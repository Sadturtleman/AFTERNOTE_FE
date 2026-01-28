package com.kuit.afternote.feature.mainpage.presentation.component.edit.processingmethod

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.AddCircleButton
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.ProcessingMethodItem
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.White

/**
 * 처리 방법 리스트 컴포넌트
 */
@Composable
fun ProcessingMethodList(
    modifier: Modifier = Modifier,
    params: ProcessingMethodListParams,
    state: ProcessingMethodListState = rememberProcessingMethodListState(
        initialShowTextField = params.initialShowTextField,
        initialExpandedItemId = params.initialExpandedItemId
    )
) {
    val items = params.items
    val onItemAdded = params.onItemAdded
    val focusManager = LocalFocusManager.current

    // 초기화: 아이템들의 expanded 상태 설정
    LaunchedEffect(items, params.initialExpandedItemId) {
        state.initializeExpandedStates(items, params.initialExpandedItemId)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = White, shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items.forEach { item ->
            ProcessingMethodCheckbox(
                item = item,
                expanded = state.expandedStates[item.id] ?: false,
                onClick = {
                    focusManager.clearFocus()
                },
                onMoreClick = {
                    focusManager.clearFocus()
                    state.toggleItemExpanded(item.id)
                },
                onDismissDropdown = {
                    state.expandedStates[item.id] = false
                },
                onEditClick = { params.onItemEditClick(item.id) },
                onDeleteClick = { params.onItemDeleteClick(item.id) }
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        // 텍스트 필드 (버튼 클릭 시 표시)
        AddItemTextField(
            visible = state.showTextField,
            onItemAdded = onItemAdded,
            onVisibilityChanged = { isVisible ->
                state.setTextFieldVisible(isVisible)
                params.onTextFieldVisibilityChanged(isVisible)
            },
            previousFocusedState = state.previousFocusedState,
            onPreviousFocusedStateChange = state::updatePreviousFocusedState
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 추가 버튼
        AddCircleButton(
            contentDescription = "추가",
            onClick = {
                state.toggleTextField()
                params.onAddClick()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProcessingMethodListPreview() {
    AfternoteTheme {
        ProcessingMethodList(
            params = ProcessingMethodListParams(
                items = listOf(
                    ProcessingMethodItem("1", "게시물 내리기"),
                    ProcessingMethodItem("2", "댓글 비활성화")
                ),
                onAddClick = {},
                onItemMoreClick = {},
                onItemEditClick = {},
                onItemDeleteClick = {},
                onItemAdded = {},
                onTextFieldVisibilityChanged = {},
                initialShowTextField = true
            )
        )
    }
}

@Preview(showBackground = true, name = "드롭다운 펼쳐진 상태")
@Composable
private fun ProcessingMethodListWithDropdownPreview() {
    AfternoteTheme {
        ProcessingMethodList(
            params = ProcessingMethodListParams(
                items = listOf(
                    ProcessingMethodItem("1", "게시물 내리기"),
                    ProcessingMethodItem("2", "댓글 비활성화"),
                    ProcessingMethodItem("3", "추모 계정으로 전환하기")
                ),
                onAddClick = {},
                onItemMoreClick = {},
                onItemEditClick = {},
                onItemDeleteClick = {},
                onItemAdded = {},
                onTextFieldVisibilityChanged = {},
                initialShowTextField = false,
                initialExpandedItemId = "1"
            )
        )
    }
}
