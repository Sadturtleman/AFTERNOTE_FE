package com.kuit.afternote.feature.mainpage.presentation.component

import com.kuit.afternote.R

/**
 * 제목에 따라 적절한 아이콘 리소스를 반환하는 유틸리티 함수
 */
fun getIconResForTitle(title: String): Int =
    when {
        title.contains("인스타그램") || title.contains("Instagram") -> R.drawable.insta_pattern
        title.contains("갤러리") || title.contains("Gallery") -> R.drawable.gallery
        else -> R.drawable.logo
    }
