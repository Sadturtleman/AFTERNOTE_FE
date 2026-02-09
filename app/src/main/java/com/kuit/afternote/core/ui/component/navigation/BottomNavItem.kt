package com.kuit.afternote.core.ui.component.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.unit.dp
import com.kuit.afternote.R

/**
 * 하단 네비게이션 바 아이템 enum
 */
enum class BottomNavItem(
    @StringRes val labelResId: Int,
    val iconRes: Int,
    val iconTextSpacing: androidx.compose.ui.unit.Dp
) {
    HOME(R.string.nav_home, R.drawable.ic_home, 4.dp),
    RECORD(R.string.nav_record, R.drawable.ic_book_open, 2.dp),
    TIME_LETTER(R.string.nav_timeletter, R.drawable.ic_mail, 2.dp),
    AFTERNOTE(R.string.nav_afternote, R.drawable.ic_edit, 3.dp)
}
