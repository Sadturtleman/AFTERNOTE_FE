package com.kuit.afternote.feature.dailyrecord.presentation.component

data class EmotionKeyword(
    val keyword: String,
    val weight: Int,   // 크기 (버블 크기)
    val color: androidx.compose.ui.graphics.Color   // 색상
)

