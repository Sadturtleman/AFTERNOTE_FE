package com.kuit.afternote.feature.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendEmailCodeRequest(
    val email: String
)

@Serializable
data class VerifyEmailRequest(
    val email: String,
    @SerialName("certificateCode") val certificateCode: String
)

@Serializable
data class VerifyEmailData(
    @SerialName("isVerified") val isVerified: Boolean? = null
)

@Serializable
data class SignUpRequest(
    val email: String,
    val password: String,
    val name: String,
    @SerialName("profileUrl") val profileUrl: String? = null
)

@Serializable
data class SignUpData(
    @SerialName("userId") val userId: Long,
    val email: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class KakaoLoginRequest(
    @SerialName("accessToken") val accessToken: String
)

@Serializable
data class LoginData(
    @SerialName("accessToken") val accessToken: String? = null,
    @SerialName("refreshToken") val refreshToken: String? = null
)

@Serializable
data class ReissueRequest(
    @SerialName("refreshToken") val refreshToken: String
)

@Serializable
data class ReissueData(
    @SerialName("accessToken") val accessToken: String? = null,
    @SerialName("refreshToken") val refreshToken: String? = null
)

@Serializable
data class LogoutRequest(
    @SerialName("refreshToken") val refreshToken: String
)

@Serializable
data class PasswordChangeRequest(
    @SerialName("currentPassword") val currentPassword: String,
    @SerialName("newPassword") val newPassword: String
)
