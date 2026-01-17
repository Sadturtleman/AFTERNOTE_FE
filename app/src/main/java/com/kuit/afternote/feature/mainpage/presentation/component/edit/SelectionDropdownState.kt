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

    var boxWidth by mutableStateOf(initialBoxWidth)
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
