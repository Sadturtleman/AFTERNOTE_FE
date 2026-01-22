package com.kuit.afternote.feature.auth.data.repository

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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

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
    fun sendEmailCode_whenSuccess_returnsUnit() = runTest {
        coEvery { api.sendEmailCode(any()) } returns ApiResponse(200, 200, "Success", null)

        val result = repository.sendEmailCode("test@example.com")

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { api.sendEmailCode(SendEmailCodeRequest("test@example.com")) }
    }

    @Test
    fun verifyEmail_whenSuccess_returnsEmailVerifyResult() = runTest {
        val response = ApiResponse(
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
    fun verifyEmail_whenDataIsNull_returnsFailure() = runTest {
        val response = ApiResponse<VerifyEmailData?>(200, 200, "Success", null)
        coEvery { api.verifyEmail(any()) } returns response

        val result = repository.verifyEmail("test@example.com", "123456")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalStateException)
    }

    @Test
    fun signUp_whenSuccess_returnsSignUpResult() = runTest {
        val response = ApiResponse(
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
    fun login_whenSuccess_returnsLoginResult() = runTest {
        val response = ApiResponse(
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
    fun reissue_whenSuccess_returnsReissueResult() = runTest {
        val response = ApiResponse(
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
    fun logout_whenSuccess_returnsUnit() = runTest {
        coEvery { api.logout(any()) } returns ApiResponse(200, 200, "Success", null)

        val result = repository.logout("refresh_token")

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { api.logout(LogoutRequest("refresh_token")) }
    }

    @Test
    fun passwordChange_whenSuccess_returnsUnit() = runTest {
        coEvery { api.passwordChange(any()) } returns ApiResponse(200, 200, "Success", null)

        val result = repository.passwordChange("oldPwd", "newPwd")

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { api.passwordChange(PasswordChangeRequest("oldPwd", "newPwd")) }
    }
}
