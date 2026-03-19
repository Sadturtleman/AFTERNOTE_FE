package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AfternotePlaylist(
    val profilePhoto: String? = null,
    val atmosphere: String? = null,
    val memorialPhotoUrl: String? = null,
    val songs: List<AfternoteSong> = emptyList(),
    val memorialVideo: AfternoteMemorialVideo? = null,
)
