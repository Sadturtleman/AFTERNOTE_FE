package com.kuit.afternote.feature.mainpage.presentation.component.edit.model

import androidx.compose.runtime.Immutable

/**
 * 노래 데이터 모델
 */
@Immutable
data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val albumCoverUrl: String? = null
)
