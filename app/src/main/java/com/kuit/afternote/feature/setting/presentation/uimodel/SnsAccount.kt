package com.kuit.afternote.feature.setting.presentation.uimodel

import androidx.compose.ui.graphics.Color

data class SnsAccount(
    val name: String,
    val iconBackground: Color,
    val initialValue: Boolean,
    val iconRes: Int? = null // 실제 프로젝트에서는 로고 이미지 리소스 ID 사용
)

