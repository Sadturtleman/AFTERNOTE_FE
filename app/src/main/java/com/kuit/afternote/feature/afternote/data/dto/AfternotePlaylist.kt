package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AfternotePlaylist(
    @SerialName("profilePhoto") val profilePhoto: String? = null,
    @SerialName("atmosphere") val atmosphere: String? = null,
    @SerialName("memorialPhotoUrl") val memorialPhotoUrl: String? = null,
    @SerialName("songs") val songs: List<AfternoteSong> = emptyList(),
    @SerialName("memorialVideo") val memorialVideo: AfternoteMemorialVideo? = null,
)
