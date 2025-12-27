package com.kuit.afternote.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.ui.theme.AfternoteTheme

/**
 * 하단 네비게이션 바
 *
 * 피그마 디자인 기반:
 * - 높이: 88dp
 * - 흰색 배경
 * - 4개 아이템: 홈, 기록, 타임레터, 애프터노트
 */
@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    selectedItem: BottomNavItem = BottomNavItem.HOME,
    onItemSelected: (BottomNavItem) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(88.dp)
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 38.dp, top = 19.dp, end = 38.dp, bottom = 45.dp)
        ) {
            BottomNavItem.entries.forEach { item ->
                Box(modifier = Modifier.weight(1f)) {
                    BottomNavigationItem(
                        iconRes = item.iconRes,
                        label = item.label,
                        isSelected = item == selectedItem,
                        onClick = { onItemSelected(item) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomNavigationBarPreview() {
    AfternoteTheme {
        BottomNavigationBar(
            selectedItem = BottomNavItem.HOME,
            onItemSelected = {}
        )
    }
}
