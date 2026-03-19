package com.kuit.afternote.feature.afternote.data.dto.request

import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylist
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRef
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Server request form for POST /afternotes (PLAYLIST category).
 * receivers: 수신자 목록 (모든 카테고리에서 가능).
 */
@Serializable
data class AfternoteCreatePlaylistRequest(
    @SerialName("category") val category: String = "PLAYLIST",
    @SerialName("title") val title: String,
    @SerialName("playlist") val playlist: AfternotePlaylist,
    @SerialName("receivers") val receivers: List<AfternoteReceiverRef> = emptyList(),
)
