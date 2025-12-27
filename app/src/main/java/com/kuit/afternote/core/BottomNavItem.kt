package com.kuit.afternote.core

import com.kuit.afternote.R

/**
 * 하단 네비게이션 바 아이템 enum
 */
enum class BottomNavItem(
    val label: String,
    val iconRes: Int
) {
    HOME("홈", R.drawable.ic_home),
    RECORD("기록", R.drawable.ic_book_open),
    TIME_LETTER("타임레터", R.drawable.ic_mail),
    AFTERNOTE("애프터노트", R.drawable.ic_edit)
}
