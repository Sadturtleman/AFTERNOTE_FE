package com.kuit.afternote.feature.auth.data.repository

import com.kuit.afternote.feature.auth.data.api.AuthApiService
import com.kuit.afternote.feature.auth.data.dto.LoginRequest
import com.kuit.afternote.feature.auth.data.dto.LogoutRequest
import com.kuit.afternote.feature.auth.data.dto.PasswordChangeRequest
import com.kuit.afternote.feature.auth.data.dto.ReissueRequest
import com.kuit.afternote.feature.auth.data.dto.SendEmailCodeRequest
import com.kuit.afternote.feature.auth.data.dto.SignUpRequest
import com.kuit.afternote.feature.auth.data.dto.VerifyEmailRequest
import com.kuit.afternote.feature.auth.data.mapper.AuthMapper
import com.kuit.afternote.feature.auth.domain.model.EmailVerifyResult
import com.kuit.afternote.feature.auth.domain.model.LoginResult
import com.kuit.afternote.feature.auth.domain.model.ReissueResult
import com.kuit.afternote.feature.auth.domain.model.SignUpResult
import com.kuit.afternote.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * AuthRepository 구현체. (스웨거 기준)
 */
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService
) : AuthRepository {

    override suspend fun sendEmailCode(email: String): Result<Unit> = runCatching {
        api.sendEmailCode(SendEmailCodeRequest(email))
        Unit
    }

    override suspend fun verifyEmail(email: String, certificateCode: String): Result<EmailVerifyResult> =
        runCatching {
            val response = api.verifyEmail(VerifyEmailRequest(email, certificateCode))
            android.util.Log.d("AuthRepositoryImpl", "verifyEmail 응답: status=${response.status}, data=${response.data}")
            
            // 실제 서버가 data: null을 반환하는 경우 처리
            // 스웨거 명세에 따르면 data 구조가 미정의이므로, null인 경우도 성공으로 간주
            val data = response.data
            if (data == null) {
                android.util.Log.w("AuthRepositoryImpl", "verifyEmail 응답의 data가 null입니다. 성공으로 간주합니다.")
                // data가 null이어도 HTTP 200이면 성공으로 간주하고 기본값 사용
                EmailVerifyResult(isVerified = true)
            } else {
                AuthMapper.toEmailVerifyResult(data)
            }
        }

    override suspend fun signUp(
        email: String,
        password: String,
        name: String,
        profileUrl: String?
    ): Result<SignUpResult> = runCatching {
        val response = api.signUp(SignUpRequest(email, password, name, profileUrl))
        val data = response.data ?: throw IllegalStateException("data is null")
        AuthMapper.toSignUpResult(data)
    }

    override suspend fun login(email: String, password: String): Result<LoginResult> = runCatching {
        val response = api.login(LoginRequest(email, password))
        val data = response.data ?: throw IllegalStateException("data is null")
        AuthMapper.toLoginResult(data)
    }

    override suspend fun reissue(refreshToken: String): Result<ReissueResult> = runCatching {
        val response = api.reissue(ReissueRequest(refreshToken))
        val data = response.data ?: throw IllegalStateException("data is null")
        AuthMapper.toReissueResult(data)
    }

    override suspend fun logout(refreshToken: String): Result<Unit> = runCatching {
        api.logout(LogoutRequest(refreshToken))
        Unit
    }

    override suspend fun passwordChange(
        currentPassword: String,
        newPassword: String
    ): Result<Unit> = runCatching {
        api.passwordChange(PasswordChangeRequest(currentPassword, newPassword))
        Unit
    }
}
