package com.kuit.afternote.feature.auth.domain.model

/**
 * 이메일 인증번호 확인 성공 결과.
 */
data class EmailVerifyResult(
    val isVerified: Boolean
)

/**
 * 회원가입 성공 결과. (스웨거: userId)
 */
data class SignUpResult(
    val userId: Long,
    val email: String
)

/**
 * 로그인 성공 결과.
 */
data class LoginResult(
    val accessToken: String? = null,
    val refreshToken: String? = null
)

/**
 * 토큰 재발급 성공 결과.
 */
data class ReissueResult(
    val accessToken: String? = null,
    val refreshToken: String? = null
)
