package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.expand.horizontalFadingEdge
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * Shared 애프터노트 tab row (writer main and receiver list).
 * Horizontal scrollable tabs; selected tab B3, unselected Gray2.
 */
@Composable
fun AfternoteTabRow(
    modifier: Modifier = Modifier,
    selectedTab: AfternoteTab = AfternoteTab.ALL,
    onTabSelected: (AfternoteTab) -> Unit
) {
    val scrollState = rememberScrollState()
    val canScrollRight by remember {
        derivedStateOf { scrollState.canScrollForward }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalFadingEdge(edgeWidth = 45.dp)
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AfternoteTab.entries.forEach { tab ->
                TabItem(
                    tab = tab,
                    isSelected = tab == selectedTab,
                    onClick = { onTabSelected(tab) }
                )
            }
        }

        if (canScrollRight) {
            RightArrowIcon(
                iconSpec = ArrowIconSpec(
                    iconRes = R.drawable.ic_arrow_right_tab,
                    contentDescription = "더 보기"
                ),
                backgroundColor = B1,
                size = 16.dp,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
private fun TabItem(
    tab: AfternoteTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(34.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(if (isSelected) B3 else Gray2)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tab.label,
            color = Gray9,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Medium,
            lineHeight = 18.sp,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
@Suppress("AssignedValueIsNeverRead")
private fun AfternoteTabRowPreview() {
    AfternoteTheme {
        var selectedTab by remember { mutableStateOf(AfternoteTab.ALL) }
        AfternoteTabRow(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )
    }
}
