package com.kuit.afternote.feature.auth.data.repository

import com.kuit.afternote.data.remote.ApiException
import com.kuit.afternote.data.remote.ApiResponse
import com.kuit.afternote.feature.auth.data.api.AuthApiService
import com.kuit.afternote.feature.auth.data.dto.LoginData
import com.kuit.afternote.feature.auth.data.dto.LoginRequest
import com.kuit.afternote.feature.auth.data.dto.LogoutRequest
import com.kuit.afternote.feature.auth.data.dto.PasswordChangeRequest
import com.kuit.afternote.feature.auth.data.dto.ReissueData
import com.kuit.afternote.feature.auth.data.dto.ReissueRequest
import com.kuit.afternote.feature.auth.data.dto.SendEmailCodeRequest
import com.kuit.afternote.feature.auth.data.dto.SignUpData
import com.kuit.afternote.feature.auth.data.dto.SignUpRequest
import com.kuit.afternote.feature.auth.data.dto.VerifyEmailData
import com.kuit.afternote.feature.auth.data.dto.VerifyEmailRequest
import com.kuit.afternote.feature.auth.domain.model.EmailVerifyResult
import com.kuit.afternote.feature.auth.domain.model.LoginResult
import com.kuit.afternote.feature.auth.domain.model.ReissueResult
import com.kuit.afternote.feature.auth.domain.model.SignUpResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

/**
 * [AuthRepositoryImpl] 단위 테스트.
 */
class AuthRepositoryImplTest {
    private lateinit var api: AuthApiService
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        api = mockk()
        repository = AuthRepositoryImpl(api)
    }

    @Test
    fun sendEmailCode_whenSuccess_returnsUnit() =
        runTest {
            coEvery { api.sendEmailCode(any()) } returns ApiResponse(200, 200, "Success", null)

            val result = repository.sendEmailCode("test@example.com")

            assertTrue(result.isSuccess)
            coVerify(exactly = 1) { api.sendEmailCode(SendEmailCodeRequest("test@example.com")) }
        }

    @Test
    fun verifyEmail_whenSuccess_returnsEmailVerifyResult() =
        runTest {
            val response = ApiResponse<VerifyEmailData?>(
                status = 200,
                code = 200,
                message = "Success",
                data = VerifyEmailData(isVerified = true)
            )
            coEvery { api.verifyEmail(any()) } returns response

            val result = repository.verifyEmail("test@example.com", "123456")

            assertTrue(result.isSuccess)
            assertEquals(EmailVerifyResult(isVerified = true), result.getOrNull())
            coVerify(exactly = 1) { api.verifyEmail(VerifyEmailRequest("test@example.com", "123456")) }
        }

    @Test
    fun verifyEmail_whenDataIsNull_returnsFailure() =
        runTest {
            val response = ApiResponse<VerifyEmailData?>(200, 200, "Success", null)
            coEvery { api.verifyEmail(any()) } returns response

            val result = repository.verifyEmail("test@example.com", "123456")

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is ApiException)
        }

    @Test
    fun signUp_whenSuccess_returnsSignUpResult() =
        runTest {
            val response = ApiResponse<SignUpData?>(
                status = 200,
                code = 200,
                message = "Success",
                data = SignUpData(userId = 1L, email = "test@example.com")
            )
            coEvery { api.signUp(any()) } returns response

            val result = repository.signUp("test@example.com", "password123!", "홍길동", null)

            assertTrue(result.isSuccess)
            assertEquals(SignUpResult(userId = 1L, email = "test@example.com"), result.getOrNull())
            coVerify(exactly = 1) {
                api.signUp(SignUpRequest("test@example.com", "password123!", "홍길동", null))
            }
        }

    @Test
    fun login_whenSuccess_returnsLoginResult() =
        runTest {
            val response = ApiResponse<LoginData?>(
                status = 200,
                code = 200,
                message = "Success",
                data = LoginData(accessToken = "at", refreshToken = "rt")
            )
            coEvery { api.login(any()) } returns response

            val result = repository.login("test@example.com", "password123!")

            assertTrue(result.isSuccess)
            assertEquals(LoginResult(accessToken = "at", refreshToken = "rt"), result.getOrNull())
            coVerify(exactly = 1) { api.login(LoginRequest("test@example.com", "password123!")) }
        }

    @Test
    fun reissue_whenSuccess_returnsReissueResult() =
        runTest {
            val response = ApiResponse<ReissueData?>(
                status = 200,
                code = 200,
                message = "Success",
                data = ReissueData(accessToken = "new_at", refreshToken = "new_rt")
            )
            coEvery { api.reissue(any()) } returns response

            val result = repository.reissue("old_refresh_token")

            assertTrue(result.isSuccess)
            assertEquals(ReissueResult(accessToken = "new_at", refreshToken = "new_rt"), result.getOrNull())
            coVerify(exactly = 1) { api.reissue(ReissueRequest("old_refresh_token")) }
        }

    @Test
    fun logout_whenSuccess_returnsUnit() =
        runTest {
            coEvery { api.logout(any()) } returns ApiResponse(200, 200, "Success", null)

            val result = repository.logout("refresh_token")

            assertTrue(result.isSuccess)
            coVerify(exactly = 1) { api.logout(LogoutRequest("refresh_token")) }
        }

    @Test
    fun passwordChange_whenSuccess_returnsUnit() =
        runTest {
            coEvery { api.passwordChange(any()) } returns ApiResponse(200, 200, "Success", null)

            val result = repository.passwordChange("oldPwd", "newPwd")

            assertTrue(result.isSuccess)
            coVerify(exactly = 1) { api.passwordChange(PasswordChangeRequest("oldPwd", "newPwd")) }
        }

    // ========== HTTP Error Cases ==========

    @Test
    fun login_when404NotFound_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":404,"code":404,"message":"User not found"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.login(any()) } throws HttpException(
                Response.error<LoginData>(404, errorBody)
            )

            val result = repository.login("nonexistent@example.com", "password123!")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(404, (exception as HttpException).code())
        }

    @Test
    fun login_when401Unauthorized_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Invalid credentials"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.login(any()) } throws HttpException(
                Response.error<LoginData>(401, errorBody)
            )

            val result = repository.login("test@example.com", "wrongPassword")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(401, (exception as HttpException).code())
        }

    @Test
    fun login_when400BadRequest_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Invalid email format"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.login(any()) } throws HttpException(
                Response.error<LoginData>(400, errorBody)
            )

            val result = repository.login("invalid-email", "password123!")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(400, (exception as HttpException).code())
        }

    @Test
    fun login_when500ServerError_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Internal server error"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.login(any()) } throws HttpException(
                Response.error<LoginData>(500, errorBody)
            )

            val result = repository.login("test@example.com", "password123!")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(500, (exception as HttpException).code())
        }

    @Test
    fun signUp_when409Conflict_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":409,"code":409,"message":"Email already exists"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.signUp(any()) } throws HttpException(
                Response.error<SignUpData>(409, errorBody)
            )

            val result = repository.signUp("existing@example.com", "password123!", "홍길동", null)

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(409, (exception as HttpException).code())
        }

    @Test
    fun sendEmailCode_when429TooManyRequests_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":429,"code":429,"message":"Too many requests"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.sendEmailCode(any()) } throws HttpException(
                Response.error<Unit>(429, errorBody)
            )

            val result = repository.sendEmailCode("test@example.com")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(429, (exception as HttpException).code())
        }

    @Test
    fun verifyEmail_when400InvalidCode_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Invalid verification code"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.verifyEmail(any()) } throws HttpException(
                Response.error<VerifyEmailData>(400, errorBody)
            )

            val result = repository.verifyEmail("test@example.com", "000000")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(400, (exception as HttpException).code())
        }

    @Test
    fun reissue_when401ExpiredToken_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Refresh token expired"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.reissue(any()) } throws HttpException(
                Response.error<ReissueData>(401, errorBody)
            )

            val result = repository.reissue("expired_refresh_token")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(401, (exception as HttpException).code())
        }

    @Test
    fun passwordChange_when401Unauthorized_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Current password is incorrect"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.passwordChange(any()) } throws HttpException(
                Response.error<Unit>(401, errorBody)
            )

            val result = repository.passwordChange("wrongOldPwd", "newPwd")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(401, (exception as HttpException).code())
        }

    @Test
    fun logout_when401InvalidToken_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Invalid refresh token"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.logout(any()) } throws HttpException(
                Response.error<Unit>(401, errorBody)
            )

            val result = repository.logout("invalid_refresh_token")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(401, (exception as HttpException).code())
        }

    // ========== Additional signUp Error Cases ==========

    @Test
    fun signUp_when400BadRequest_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Invalid password format"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.signUp(any()) } throws HttpException(
                Response.error<SignUpData>(400, errorBody)
            )

            val result = repository.signUp("test@example.com", "weak", "홍길동", null)

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(400, (exception as HttpException).code())
        }

    @Test
    fun signUp_when500ServerError_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Internal server error"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.signUp(any()) } throws HttpException(
                Response.error<SignUpData>(500, errorBody)
            )

            val result = repository.signUp("test@example.com", "password123!", "홍길동", null)

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(500, (exception as HttpException).code())
        }

    // ========== Additional sendEmailCode Error Cases ==========

    @Test
    fun sendEmailCode_when400BadRequest_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Invalid email format"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.sendEmailCode(any()) } throws HttpException(
                Response.error<Unit>(400, errorBody)
            )

            val result = repository.sendEmailCode("invalid-email")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(400, (exception as HttpException).code())
        }

    @Test
    fun sendEmailCode_when500ServerError_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Failed to send email"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.sendEmailCode(any()) } throws HttpException(
                Response.error<Unit>(500, errorBody)
            )

            val result = repository.sendEmailCode("test@example.com")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(500, (exception as HttpException).code())
        }

    // ========== Additional verifyEmail Error Cases ==========

    @Test
    fun verifyEmail_when401CodeExpired_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Verification code expired"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.verifyEmail(any()) } throws HttpException(
                Response.error<VerifyEmailData>(401, errorBody)
            )

            val result = repository.verifyEmail("test@example.com", "123456")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(401, (exception as HttpException).code())
        }

    @Test
    fun verifyEmail_when404EmailNotFound_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":404,"code":404,"message":"Email not found"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.verifyEmail(any()) } throws HttpException(
                Response.error<VerifyEmailData>(404, errorBody)
            )

            val result = repository.verifyEmail("nonexistent@example.com", "123456")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(404, (exception as HttpException).code())
        }

    @Test
    fun verifyEmail_when500ServerError_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Internal server error"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.verifyEmail(any()) } throws HttpException(
                Response.error<VerifyEmailData>(500, errorBody)
            )

            val result = repository.verifyEmail("test@example.com", "123456")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(500, (exception as HttpException).code())
        }

    // ========== Additional reissue Error Cases ==========

    @Test
    fun reissue_when400InvalidToken_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Invalid token format"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.reissue(any()) } throws HttpException(
                Response.error<ReissueData>(400, errorBody)
            )

            val result = repository.reissue("invalid_format_token")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(400, (exception as HttpException).code())
        }

    @Test
    fun reissue_when500ServerError_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Internal server error"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.reissue(any()) } throws HttpException(
                Response.error<ReissueData>(500, errorBody)
            )

            val result = repository.reissue("valid_refresh_token")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(500, (exception as HttpException).code())
        }

    // ========== Additional logout Error Cases ==========

    @Test
    fun logout_when400InvalidToken_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Invalid token format"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.logout(any()) } throws HttpException(
                Response.error<Unit>(400, errorBody)
            )

            val result = repository.logout("invalid_format_token")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(400, (exception as HttpException).code())
        }

    @Test
    fun logout_when500ServerError_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Internal server error"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.logout(any()) } throws HttpException(
                Response.error<Unit>(500, errorBody)
            )

            val result = repository.logout("refresh_token")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(500, (exception as HttpException).code())
        }

    // ========== Additional passwordChange Error Cases ==========

    @Test
    fun passwordChange_when400BadRequest_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Invalid password format"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.passwordChange(any()) } throws HttpException(
                Response.error<Unit>(400, errorBody)
            )

            val result = repository.passwordChange("oldPwd", "weak")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(400, (exception as HttpException).code())
        }

    @Test
    fun passwordChange_when404UserNotFound_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":404,"code":404,"message":"User not found"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.passwordChange(any()) } throws HttpException(
                Response.error<Unit>(404, errorBody)
            )

            val result = repository.passwordChange("oldPwd", "newPwd123!")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(404, (exception as HttpException).code())
        }

    @Test
    fun passwordChange_when500ServerError_returnsFailureWithHttpException() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Internal server error"}"""
                .toResponseBody("application/json".toMediaType())
            coEvery { api.passwordChange(any()) } throws HttpException(
                Response.error<Unit>(500, errorBody)
            )

            val result = repository.passwordChange("oldPwd", "newPwd123!")

            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is HttpException)
            assertEquals(500, (exception as HttpException).code())
        }

    // ========== Network Error Cases ==========

    @Test
    fun login_whenNetworkError_returnsFailure() =
        runTest {
            coEvery { api.login(any()) } throws java.io.IOException("Network unavailable")

            val result = repository.login("test@example.com", "password123!")

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is java.io.IOException)
        }

    @Test
    fun signUp_whenNetworkError_returnsFailure() =
        runTest {
            coEvery { api.signUp(any()) } throws java.io.IOException("Network unavailable")

            val result = repository.signUp("test@example.com", "password123!", "홍길동", null)

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is java.io.IOException)
        }

    @Test
    fun sendEmailCode_whenNetworkError_returnsFailure() =
        runTest {
            coEvery { api.sendEmailCode(any()) } throws java.io.IOException("Network unavailable")

            val result = repository.sendEmailCode("test@example.com")

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is java.io.IOException)
        }

    @Test
    fun verifyEmail_whenNetworkError_returnsFailure() =
        runTest {
            coEvery { api.verifyEmail(any()) } throws java.io.IOException("Network unavailable")

            val result = repository.verifyEmail("test@example.com", "123456")

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is java.io.IOException)
        }

    @Test
    fun reissue_whenNetworkError_returnsFailure() =
        runTest {
            coEvery { api.reissue(any()) } throws java.io.IOException("Network unavailable")

            val result = repository.reissue("refresh_token")

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is java.io.IOException)
        }

    @Test
    fun logout_whenNetworkError_returnsFailure() =
        runTest {
            coEvery { api.logout(any()) } throws java.io.IOException("Network unavailable")

            val result = repository.logout("refresh_token")

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is java.io.IOException)
        }

    @Test
    fun passwordChange_whenNetworkError_returnsFailure() =
        runTest {
            coEvery { api.passwordChange(any()) } throws java.io.IOException("Network unavailable")

            val result = repository.passwordChange("oldPwd", "newPwd")

            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is java.io.IOException)
        }
}
