package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AfternoteCredentials(
    @SerialName("id") val id: String? = null,
    @SerialName("password") val password: String? = null,
)
