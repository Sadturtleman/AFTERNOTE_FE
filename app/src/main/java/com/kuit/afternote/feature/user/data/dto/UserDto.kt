package com.kuit.afternote.feature.user.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * User API DTOs. (스웨거 기준)
 */

@Serializable
data class UserResponse(
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("phone") val phone: String? = null,
    @SerialName("profileImageUrl") val profileImageUrl: String? = null
)

@Serializable
data class UserUpdateProfileRequest(
    @SerialName("name") val name: String? = null,
    @SerialName("phone") val phone: String? = null,
    @SerialName("profileImageUrl") val profileImageUrl: String? = null
)

@Serializable
data class UserPushSettingResponse(
    @SerialName("timeLetter") val timeLetter: Boolean,
    @SerialName("mindRecord") val mindRecord: Boolean,
    @SerialName("afterNote") val afterNote: Boolean
)

@Serializable
data class UserUpdatePushSettingRequest(
    @SerialName("timeLetter") val timeLetter: Boolean? = null,
    @SerialName("mindRecord") val mindRecord: Boolean? = null,
    @SerialName("afterNote") val afterNote: Boolean? = null
)
