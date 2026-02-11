package com.kuit.afternote.feature.auth.data.repository

import android.util.Log
import com.kuit.afternote.data.remote.requireData
import com.kuit.afternote.data.remote.requireSuccess
import com.kuit.afternote.feature.auth.data.api.AuthApiService
import com.kuit.afternote.feature.auth.data.dto.KakaoLoginRequest
import com.kuit.afternote.feature.auth.data.dto.LoginRequest
import com.kuit.afternote.feature.auth.data.dto.LogoutRequest
import com.kuit.afternote.feature.auth.data.dto.PasswordChangeRequest
import com.kuit.afternote.feature.auth.data.dto.ReissueRequest
import com.kuit.afternote.feature.auth.data.dto.SendEmailCodeRequest
import com.kuit.afternote.feature.auth.data.dto.SignUpRequest
import com.kuit.afternote.feature.auth.data.dto.VerifyEmailData
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
class AuthRepositoryImpl
    @Inject
    constructor(
        private val api: AuthApiService
    ) : AuthRepository {
        override suspend fun sendEmailCode(email: String): Result<Unit> =
            runCatching {
                Log.d(TAG, "sendEmailCode: email=$email")
                api.sendEmailCode(SendEmailCodeRequest(email))
                Log.d(TAG, "sendEmailCode: SUCCESS")
            }

        override suspend fun verifyEmail(
            email: String,
            certificateCode: String
        ): Result<EmailVerifyResult> =
            runCatching {
                Log.d(TAG, "verifyEmail: email=$email, code=$certificateCode")
                val response = api.verifyEmail(VerifyEmailRequest(email, certificateCode))
                Log.d(TAG, "verifyEmail: response=$response")
                response.requireSuccess()

                AuthMapper.toEmailVerifyResult(response.data ?: VerifyEmailData(isVerified = null))
            }

        override suspend fun signUp(
            email: String,
            password: String,
            name: String,
            profileUrl: String?
        ): Result<SignUpResult> =
            runCatching {
                Log.d(TAG, "signUp: email=$email, name=$name")
                val response = api.signUp(SignUpRequest(email, password, name, profileUrl))
                Log.d(TAG, "signUp: response=$response")
                AuthMapper.toSignUpResult(response.requireData())
            }

        override suspend fun login(
            email: String,
            password: String
        ): Result<LoginResult> =
            runCatching {
                Log.d(TAG, "login: email=$email")
                val response = api.login(LoginRequest(email, password))
                Log.d(TAG, "login: response status=${response.status}, message=${response.message}")
                AuthMapper.toLoginResult(response.requireData())
            }

        override suspend fun kakaoLogin(accessToken: String): Result<LoginResult> =
            runCatching {
                val response = api.kakaoLogin(KakaoLoginRequest(accessToken))
                Log.d(TAG, "kakaoLogin: response status=${response.status}, message=${response.message}")
                AuthMapper.toLoginResult(response.requireData())
            }

        override suspend fun reissue(refreshToken: String): Result<ReissueResult> =
            runCatching {
                Log.d(TAG, "reissue: refreshToken=${refreshToken.take(n = 20)}...")
                val response = api.reissue(ReissueRequest(refreshToken))
                Log.d(TAG, "reissue: response=$response")
                AuthMapper.toReissueResult(response.requireData())
            }

        override suspend fun logout(refreshToken: String): Result<Unit> =
            runCatching {
                Log.d(TAG, "logout: refreshToken=${refreshToken.take(n = 20)}...")
                api.logout(LogoutRequest(refreshToken))
                Log.d(TAG, "logout: SUCCESS")
            }

        override suspend fun passwordChange(
            currentPassword: String,
            newPassword: String
        ): Result<Unit> =
            runCatching {
                Log.d(TAG, "passwordChange: calling API")
                Log.d(TAG, "passwordChange: currentPassword length=${currentPassword.length}")
                Log.d(TAG, "passwordChange: newPassword length=${newPassword.length}")
                val response = api.passwordChange(PasswordChangeRequest(currentPassword, newPassword))
                Log.d(TAG, "passwordChange: response status=${response.status}, message=${response.message}")
            }

        companion object {
            private const val TAG = "AuthRepositoryImpl"
        }
    }
