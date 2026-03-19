package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AfternoteCredentials(
    val id: String? = null,
    val password: String? = null,
)
