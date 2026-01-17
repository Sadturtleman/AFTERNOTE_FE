package com.kuit.afternote.core.ui.component

import androidx.compose.ui.unit.dp
import com.kuit.afternote.R

/**
 * 하단 네비게이션 바 아이템 enum
 */
enum class BottomNavItem(
    val label: String,
    val iconRes: Int,
    val iconTextSpacing: androidx.compose.ui.unit.Dp
) {
    HOME("홈", R.drawable.ic_home, 4.dp),
    RECORD("기록", R.drawable.ic_book_open, 2.dp),
    TIME_LETTER("타임레터", R.drawable.ic_mail, 2.dp),
    AFTERNOTE("애프터노트", R.drawable.ic_edit, 3.dp)
}
