package com.kuit.afternote.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(88.dp)
            .background(Color.White)
            .padding(start = 32.dp, top = 19.dp, end = 32.dp, bottom = 23.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 홈
        BottomNavigationItem(
            iconRes = BottomNavItem.HOME.iconRes,
            label = BottomNavItem.HOME.label,
            isSelected = BottomNavItem.HOME == selectedItem,
            onClick = { onItemSelected(BottomNavItem.HOME) },
            iconTextSpacing = BottomNavItem.HOME.iconTextSpacing
        )

        Spacer(modifier = Modifier.weight(62f))

        // 기록
        BottomNavigationItem(
            iconRes = BottomNavItem.RECORD.iconRes,
            label = BottomNavItem.RECORD.label,
            isSelected = BottomNavItem.RECORD == selectedItem,
            onClick = { onItemSelected(BottomNavItem.RECORD) },
            iconTextSpacing = BottomNavItem.RECORD.iconTextSpacing
        )

        Spacer(modifier = Modifier.weight(57f))

        // 타임레터
        BottomNavigationItem(
            iconRes = BottomNavItem.TIME_LETTER.iconRes,
            label = BottomNavItem.TIME_LETTER.label,
            isSelected = BottomNavItem.TIME_LETTER == selectedItem,
            onClick = { onItemSelected(BottomNavItem.TIME_LETTER) },
            iconTextSpacing = BottomNavItem.TIME_LETTER.iconTextSpacing
        )

        Spacer(modifier = Modifier.weight(46f))

        // 애프터노트
        BottomNavigationItem(
            iconRes = BottomNavItem.AFTERNOTE.iconRes,
            label = BottomNavItem.AFTERNOTE.label,
            isSelected = BottomNavItem.AFTERNOTE == selectedItem,
            onClick = { onItemSelected(BottomNavItem.AFTERNOTE) },
            iconTextSpacing = BottomNavItem.AFTERNOTE.iconTextSpacing
        )
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
