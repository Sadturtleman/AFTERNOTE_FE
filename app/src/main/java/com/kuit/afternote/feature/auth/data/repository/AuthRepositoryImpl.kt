package com.kuit.afternote.feature.auth.data.repository

import android.util.Log
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
        Log.d(TAG, "sendEmailCode: email=$email")
        api.sendEmailCode(SendEmailCodeRequest(email))
        Log.d(TAG, "sendEmailCode: SUCCESS")
        Unit
    }

    override suspend fun verifyEmail(email: String, certificateCode: String): Result<EmailVerifyResult> =
        runCatching {
            Log.d(TAG, "verifyEmail: email=$email, code=$certificateCode")
            val response = api.verifyEmail(VerifyEmailRequest(email, certificateCode))
            Log.d(TAG, "verifyEmail: response=$response")
            val data = response.data ?: throw IllegalStateException("data is null")
            AuthMapper.toEmailVerifyResult(data)
        }

    override suspend fun signUp(
        email: String,
        password: String,
        name: String,
        profileUrl: String?
    ): Result<SignUpResult> = runCatching {
        Log.d(TAG, "signUp: email=$email, name=$name")
        val response = api.signUp(SignUpRequest(email, password, name, profileUrl))
        Log.d(TAG, "signUp: response=$response")
        val data = response.data ?: throw IllegalStateException("data is null")
        AuthMapper.toSignUpResult(data)
    }

    override suspend fun login(email: String, password: String): Result<LoginResult> = runCatching {
        Log.d(TAG, "login: email=$email")
        val response = api.login(LoginRequest(email, password))
        Log.d(TAG, "login: response status=${response.status}, message=${response.message}")
        val data = response.data ?: throw IllegalStateException("data is null")
        AuthMapper.toLoginResult(data)
    }

    override suspend fun reissue(refreshToken: String): Result<ReissueResult> = runCatching {
        Log.d(TAG, "reissue: refreshToken=${refreshToken.take(n = 20)}...")
        val response = api.reissue(ReissueRequest(refreshToken))
        Log.d(TAG, "reissue: response=$response")
        val data = response.data ?: throw IllegalStateException("data is null")
        AuthMapper.toReissueResult(data)
    }

    override suspend fun logout(refreshToken: String): Result<Unit> = runCatching {
        Log.d(TAG, "logout: refreshToken=${refreshToken.take(n = 20)}...")
        api.logout(LogoutRequest(refreshToken))
        Log.d(TAG, "logout: SUCCESS")
        Unit
    }

    override suspend fun passwordChange(
        currentPassword: String,
        newPassword: String
    ): Result<Unit> = runCatching {
        Log.d(TAG, "passwordChange: calling API")
        Log.d(TAG, "passwordChange: currentPassword length=${currentPassword.length}")
        Log.d(TAG, "passwordChange: newPassword length=${newPassword.length}")
        val response = api.passwordChange(PasswordChangeRequest(currentPassword, newPassword))
        Log.d(TAG, "passwordChange: response status=${response.status}, message=${response.message}")
        Unit
    }

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }
}
