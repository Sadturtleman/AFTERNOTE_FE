package com.kuit.afternote.feature.mainpage.presentation.util

import com.kuit.afternote.R

/**
 * 서비스명에 따라 적절한 아이콘 리소스를 반환하는 유틸리티 함수
 */
fun getIconResForServiceName(serviceName: String): Int =
    when (serviceName) {
        "인스타그램" -> R.drawable.img_insta_pattern
        "갤러리" -> R.drawable.ic_gallery
        "추모 가이드라인" -> R.drawable.ic_memorial_guideline
        "네이버 메일" -> R.drawable.img_naver_mail

        else -> R.drawable.img_logo
    }
