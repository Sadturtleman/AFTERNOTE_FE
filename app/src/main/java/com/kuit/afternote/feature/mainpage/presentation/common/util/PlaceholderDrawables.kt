package com.kuit.afternote.feature.mainpage.presentation.common.util

import com.kuit.afternote.R

/**
 * 더미/개발용 플레이리스트 앨범 커버 placeholder 이미지 (img_placeholder_1 ~ 12) 순환 반환.
 * 실제 앨범 아트 연동 전까지 사용.
 */
object PlaceholderDrawables {
    private val LIST = listOf(
        R.drawable.img_placeholder_1,
        R.drawable.img_placeholder_2,
        R.drawable.img_placeholder_3,
        R.drawable.img_placeholder_4,
        R.drawable.img_placeholder_5,
        R.drawable.img_placeholder_6,
        R.drawable.img_placeholder_7,
        R.drawable.img_placeholder_8,
        R.drawable.img_placeholder_9,
        R.drawable.img_placeholder_10,
        R.drawable.img_placeholder_11,
        R.drawable.img_placeholder_12
    )

    /**
     * 1-based 목록 순번(displayIndex)에 맞는 placeholder drawable id.
     */
    fun forDisplayIndex(displayIndex: Int): Int = LIST[(displayIndex - 1) % LIST.size]

    /**
     * 0-based index에 맞는 placeholder drawable id.
     */
    fun forZeroBasedIndex(index: Int): Int = LIST[index % LIST.size]
}
