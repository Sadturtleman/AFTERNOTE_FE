package com.kuit.afternote.feature.afternote.domain.model

data class SearchedSong(
    val id: String,
    val title: String,
    val artist: String,
    val albumImageUrl: String? = null,
)
