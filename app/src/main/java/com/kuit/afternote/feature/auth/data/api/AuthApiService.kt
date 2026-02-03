package com.kuit.afternote.feature.auth.data.api

import com.kuit.afternote.data.remote.ApiResponse
import com.kuit.afternote.feature.auth.data.dto.KakaoLoginRequest
import com.kuit.afternote.feature.auth.data.dto.LoginData
import com.kuit.afternote.feature.auth.data.dto.LoginRequest
import com.kuit.afternote.feature.auth.data.dto.LogoutRequest
import com.kuit.afternote.feature.auth.data.dto.PasswordChangeRequest
import com.kuit.afternote.feature.auth.data.dto.ReissueData
import com.kuit.afternote.feature.auth.data.dto.ReissueRequest
import com.kuit.afternote.feature.auth.data.dto.SendEmailCodeRequest
import com.kuit.afternote.feature.auth.data.dto.SignUpData
import com.kuit.afternote.feature.auth.data.dto.SignUpRequest
import com.kuit.afternote.feature.auth.data.dto.VerifyEmailRequest
import kotlinx.serialization.json.JsonObject
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Auth API 서비스. (스웨거 기준)
 *
 * - POST /auth/email/send, /auth/email/verify, /auth/sign-up, /auth/login
 * - POST /auth/reissue, /auth/logout, /auth/password/change
 */
interface AuthApiService {
    @POST("auth/email/send")
    suspend fun sendEmailCode(
        @Body body: SendEmailCodeRequest
    ): ApiResponse<Unit?>

    @POST("auth/email/verify")
    suspend fun verifyEmail(
        @Body body: VerifyEmailRequest
    ): ApiResponse<JsonObject?>

    @POST("auth/sign-up")
    suspend fun signUp(
        @Body body: SignUpRequest
    ): ApiResponse<SignUpData?>

    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequest
    ): ApiResponse<LoginData?>

    @POST("auth/kakao")
    suspend fun kakaoLogin(
        @Body body: KakaoLoginRequest
    ): ApiResponse<LoginData?>

    @POST("auth/reissue")
    suspend fun reissue(
        @Body body: ReissueRequest
    ): ApiResponse<ReissueData?>

    @POST("auth/logout")
    suspend fun logout(
        @Body body: LogoutRequest
    ): ApiResponse<Unit?>

    @POST("auth/password/change")
    suspend fun passwordChange(
        @Body body: PasswordChangeRequest
    ): ApiResponse<Unit?>
}
