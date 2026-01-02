package com.kuit.afternote.feature.mainpage.presentation.component.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.feature.mainpage.presentation.model.AfternoteTab
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Gray9

/**
 * 개별 탭 아이템 컴포넌트
 */
@Composable
internal fun TabItem(
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
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TabItemPreview() {
    AfternoteTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TabItem(
                tab = AfternoteTab.ALL,
                isSelected = true,
                onClick = { }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TabItem(
                tab = AfternoteTab.SOCIAL_NETWORK,
                isSelected = false,
                onClick = { }
            )
        }
    }
}
