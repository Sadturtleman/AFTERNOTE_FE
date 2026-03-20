package com.kuit.afternote.feature.afternote.domain.model

import com.kuit.afternote.feature.afternote.domain.model.playlist.AfternotePlaylistInput

data class AfternoteUpdateRequestInput(
    val category: String,
    val title: String,
    val processMethod: String? = null,
    val actions: List<String>? = null,
    val leaveMessage: String? = null,
    val credentials: CredentialsInput? = null,
    val receivers: List<ReceiverRefInput>? = null,
    val playlist: AfternotePlaylistInput? = null,
)

data class CredentialsInput(
    val id: String? = null,
    val password: String? = null,
)

data class ReceiverRefInput(
    val receiverId: Long,
)
