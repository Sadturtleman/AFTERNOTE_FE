package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * SelectionDropdown의 UI 상태를 관리하는 State Holder
 * expanded와 boxWidth를 캡슐화하여 파라미터 수를 줄임
 */
@Stable
class SelectionDropdownState(
    initialExpanded: Boolean = false,
    initialBoxWidth: Dp = 0.dp
) {
    var expanded by mutableStateOf(initialExpanded)
        private set

    var boxWidth by mutableStateOf(initialBoxWidth)
        private set

    /**
     * 드롭다운 열림/닫힘 상태 변경
     */
    fun setExpanded(expanded: Boolean) {
        this.expanded = expanded
    }

    /**
     * 박스 너비 업데이트
     */
    fun setBoxWidth(width: Dp) {
        this.boxWidth = width
    }
}

/**
 * SelectionDropdownState를 생성하는 Composable 함수
 */
@Composable
fun rememberSelectionDropdownState(
    initialExpanded: Boolean = false,
    initialBoxWidth: Dp = 0.dp
): SelectionDropdownState =
    remember {
        SelectionDropdownState(
            initialExpanded = initialExpanded,
            initialBoxWidth = initialBoxWidth
        )
    }
