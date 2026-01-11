package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.kuit.afternote.R
import com.kuit.afternote.feature.mainpage.presentation.model.DropdownMenuOverlayParams
import com.kuit.afternote.feature.mainpage.presentation.model.ProcessingMethodItem
import com.kuit.afternote.feature.mainpage.presentation.model.ProcessingMethodListParams
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.White

/**
 * 처리 방법 리스트 컴포넌트
 */
@Composable
fun ProcessingMethodList(
    modifier: Modifier = Modifier,
    params: ProcessingMethodListParams
) {
    val items = params.items
    val onAddClick = params.onAddClick
    val onItemMoreClick = params.onItemMoreClick
    val onItemEditClick = params.onItemEditClick
    val onItemDeleteClick = params.onItemDeleteClick
    val onItemAdded = params.onItemAdded
    val onTextFieldVisibilityChanged = params.onTextFieldVisibilityChanged
    val initialShowTextField = params.initialShowTextField
    val initialExpandedItemId = params.initialExpandedItemId
    var showTextField by remember { mutableStateOf(initialShowTextField) }
    val focusManager = LocalFocusManager.current

    // 각 아이템의 expanded 상태를 추적하기 위한 맵
    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }
    // 각 아이템의 위치를 추적하기 위한 맵
    val itemPositions = remember { mutableStateMapOf<String, Offset>() }
    // 각 아이템의 크기를 추적하기 위한 맵
    val itemSizes = remember { mutableStateMapOf<String, IntSize>() }
    // 부모 Box의 루트 위치
    var boxPositionInRoot by remember { mutableStateOf(Offset.Zero) }

    items.forEach { item ->
        if (!expandedStates.containsKey(item.id)) {
            expandedStates[item.id] = initialExpandedItemId == item.id
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                boxPositionInRoot = coordinates.positionInRoot()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items.forEachIndexed { index, item ->
                val expanded = expandedStates[item.id] ?: false

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            // 각 아이템의 위치와 크기 저장
                            itemPositions[item.id] = coordinates.positionInRoot()
                            itemSizes[item.id] = coordinates.size
                        }
                ) {
                    ProcessingMethodCheckbox(
                        item = item,
                        onClick = {
                            focusManager.clearFocus()
                            if (showTextField) {
                                // 텍스트 필드가 열려있으면 포커스를 해제하여 아이템 추가
                                focusManager.clearFocus()
                            }
                        },
                        onMoreClick = {
                            focusManager.clearFocus()
                            expandedStates[item.id] = !expanded
                        }
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            // 텍스트 필드 (버튼 클릭 시 표시)
            AddItemTextField(
                visible = showTextField,
                onItemAdded = onItemAdded,
                onVisibilityChanged = { isVisible ->
                    showTextField = isVisible
                    onTextFieldVisibilityChanged(isVisible)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 추가 버튼
            Image(
                painter = painterResource(R.drawable.ic_add_circle),
                contentDescription = "추가",
                modifier = Modifier
                    .clickable(onClick = {
                        showTextField = !showTextField
                        onAddClick()
                    })
            )
        }

        // 드롭다운 메뉴 오버레이
        DropdownMenuOverlay(
            params = DropdownMenuOverlayParams(
                itemIds = items.map { it.id },
                expandedStates = expandedStates,
                itemPositions = itemPositions,
                itemSizes = itemSizes,
                boxPositionInRoot = boxPositionInRoot,
                onItemEditClick = onItemEditClick,
                onItemDeleteClick = onItemDeleteClick,
                onExpandedStateChanged = { id, isExpanded ->
                    expandedStates[id] = isExpanded
                }
            )
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
