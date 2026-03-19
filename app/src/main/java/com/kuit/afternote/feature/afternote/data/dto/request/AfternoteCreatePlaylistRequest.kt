package com.kuit.afternote.feature.afternote.data.dto.request

import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylist
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRef
import kotlinx.serialization.Serializable

/**
 * Server request form for POST /afternotes (PLAYLIST category).
 * receivers: 수신자 목록 (모든 카테고리에서 가능).
 */
@Serializable
data class AfternoteCreatePlaylistRequest(
    val category: String = "PLAYLIST",
    val title: String,
    val playlist: AfternotePlaylist,
    val receivers: List<AfternoteReceiverRef> = emptyList(),
)
