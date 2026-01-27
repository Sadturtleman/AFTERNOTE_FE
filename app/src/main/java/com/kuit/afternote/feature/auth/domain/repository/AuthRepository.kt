package com.kuit.afternote.feature.auth.domain.repository

import com.kuit.afternote.feature.auth.domain.model.EmailVerifyResult
import com.kuit.afternote.feature.auth.domain.model.LoginResult
import com.kuit.afternote.feature.auth.domain.model.ReissueResult
import com.kuit.afternote.feature.auth.domain.model.SignUpResult

/**
 * Auth 도메인 Repository 인터페이스. (스웨거 기준)
 */
interface AuthRepository {

    suspend fun sendEmailCode(email: String): Result<Unit>

    suspend fun verifyEmail(email: String, certificateCode: String): Result<EmailVerifyResult>

    suspend fun signUp(
        email: String,
        password: String,
        name: String,
        profileUrl: String?
    ): Result<SignUpResult>

    suspend fun login(email: String, password: String): Result<LoginResult>

    suspend fun reissue(refreshToken: String): Result<ReissueResult>

    suspend fun logout(refreshToken: String): Result<Unit>

    suspend fun passwordChange(currentPassword: String, newPassword: String): Result<Unit>
}
