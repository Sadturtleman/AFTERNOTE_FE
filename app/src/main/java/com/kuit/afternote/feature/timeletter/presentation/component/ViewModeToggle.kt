package com.kuit.afternote.feature.timeletter.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.R
import com.kuit.afternote.feature.timeletter.presentation.uimodel.ViewMode
import com.kuit.afternote.ui.theme.White

/**
 * 리스트형 / 블록형 토글 버튼
 */
@Composable
fun ViewModeToggle(
    currentMode: ViewMode,
    onModeChange: (ViewMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .background(
                color = Color(0xFFEEEEEE),
                shape = RoundedCornerShape(40.dp)
            )
            .padding(
                vertical = 6.dp,
                horizontal = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 리스트 버튼
        ToggleButton(
            isSelected = currentMode == ViewMode.LIST,
            iconResId = R.drawable.ic_radio_list,  // 리스트 아이콘
            contentDescription = "리스트형",
            onClick = { onModeChange(ViewMode.LIST) }
        )
        Image(
            painterResource(R.drawable.ic_radio_bar),
            contentDescription = "라디오 바"
        )
        // 블록 버튼
        ToggleButton(
            isSelected = currentMode == ViewMode.BLOCK,
            iconResId = R.drawable.ic_radio_block,  // 블록 아이콘
            contentDescription = "블록형",
            onClick = { onModeChange(ViewMode.BLOCK) }
        )
    }
}

@Composable
private fun ToggleButton(
    isSelected: Boolean,
    iconResId: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(
                color = Color(0xFFEEEEEE)
            )
            .size(
                24.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    )  {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(
                if (isSelected) Color(0xFF6B8FF8) else Color(0xFF9E9E9E)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ViewModeToggleListPreview() {
    ViewModeToggle(
        currentMode = ViewMode.LIST,
        onModeChange = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ViewModeToggleBlockPreview() {
    ViewModeToggle(
        currentMode = ViewMode.BLOCK,
        onModeChange = {}
    )
}


