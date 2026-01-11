package com.kuit.afternote.feature.mainpage.presentation.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize

/**
 * DropdownMenuOverlay 함수의 매개변수를 묶는 data class
 */
data class DropdownMenuOverlayParams(
    val itemIds: List<String>,
    val expandedStates: Map<String, Boolean>,
    val itemPositions: Map<String, Offset>,
    val itemSizes: Map<String, IntSize>,
    val boxPositionInRoot: Offset,
    val onItemEditClick: (String) -> Unit,
    val onItemDeleteClick: (String) -> Unit,
    val onExpandedStateChanged: (String, Boolean) -> Unit,
    val menuOffsetX: Float = -91f,
    val menuOffsetY: Float = -2f
)
