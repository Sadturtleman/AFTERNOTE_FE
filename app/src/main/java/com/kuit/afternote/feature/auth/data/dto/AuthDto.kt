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
    val isVerified: Boolean? = null
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

/** Request for unified social login (POST /auth/social/login). */
@Serializable
data class SocialLoginRequest(
    val provider: String,
    @SerialName("accessToken") val accessToken: String
)

@Serializable
data class LoginData(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val newUser: Boolean? = null
)

@Serializable
data class ReissueRequest(
    val refreshToken: String
)

@Serializable
data class ReissueData(
    val accessToken: String? = null,
    val refreshToken: String? = null
)

@Serializable
data class LogoutRequest(
    val refreshToken: String
)

@Serializable
data class PasswordChangeRequest(
    val currentPassword: String,
    val newPassword: String
)
