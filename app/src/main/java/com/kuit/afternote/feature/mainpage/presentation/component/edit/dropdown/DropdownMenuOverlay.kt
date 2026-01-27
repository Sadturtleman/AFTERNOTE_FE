package com.kuit.afternote.feature.mainpage.presentation.component.edit.dropdown

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.kuit.afternote.feature.mainpage.presentation.component.detail.EditDropdownMenu
import com.kuit.afternote.ui.theme.AfternoteTheme

/**
 * 드롭다운 메뉴 오버레이 컴포넌트
 *
 * 아이템의 위치를 추적하여 드롭다운 메뉴를 표시하고, 외부 클릭 시 닫습니다.
 */
@Composable
fun DropdownMenuOverlay(
    modifier: Modifier = Modifier,
    params: DropdownMenuOverlayParams
) {
    val itemIds = params.itemIds
    val expandedStates = params.expandedStates
    val itemPositions = params.itemPositions
    val itemSizes = params.itemSizes
    val boxPositionInRoot = params.boxPositionInRoot
    val menuOffsetX = params.menuOffsetX
    val menuOffsetY = params.menuOffsetY
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { }
    ) {
        // 드롭다운 메뉴를 최상위 Box에 배치 (레이아웃 영향 없음)
        itemIds.forEach { itemId ->
            val expanded = expandedStates[itemId] ?: false
            val position = itemPositions[itemId]
            val size = itemSizes[itemId]

            if (expanded && position != null && size != null) {
                Box(
                    modifier = Modifier
                        .offset(
                            // 아이템 아랫변 기준: 위로 menuOffsetY.dp, 오른쪽으로 menuOffsetX.dp
                            x = with(density) {
                                (position.x + size.width - boxPositionInRoot.x).toDp() + menuOffsetX.dp + 2.dp
                            },
                            y = with(density) {
                                (position.y + size.height - boxPositionInRoot.y).toDp() + menuOffsetY.dp
                            }
                        )
                ) {
                    EditDropdownMenu(
                        onEditClick = {
                            params.onExpandedStateChanged(itemId, false)
                            params.onItemEditClick(itemId)
                        },
                        onDeleteClick = {
                            params.onExpandedStateChanged(itemId, false)
                            params.onItemDeleteClick(itemId)
                        }
                    )
                }
            }
        }

        // 드롭다운 메뉴 외부 클릭 시 닫기
        val hasExpandedDropdown = expandedStates.values.any { it }
        if (hasExpandedDropdown) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        // 모든 드롭다운 닫기
                        expandedStates.keys.forEach { key ->
                            params.onExpandedStateChanged(key, false)
                        }
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DropdownMenuOverlayPreview() {
    AfternoteTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            DropdownMenuOverlay(
                params = DropdownMenuOverlayParams(
                    itemIds = listOf("1", "2"),
                    expandedStates = mapOf("1" to true),
                    itemPositions = mapOf("1" to Offset(100f, 200f)),
                    itemSizes = mapOf("1" to IntSize(200, 50)),
                    boxPositionInRoot = Offset(0f, 0f),
                    onItemEditClick = {},
                    onItemDeleteClick = {},
                    onExpandedStateChanged = { _, _ -> }
                )
            )
        }
    }
}
