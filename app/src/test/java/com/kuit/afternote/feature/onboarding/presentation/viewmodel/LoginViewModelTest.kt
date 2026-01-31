package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import com.kuit.afternote.data.local.TokenManager
import com.kuit.afternote.feature.auth.domain.model.LoginResult
import com.kuit.afternote.feature.auth.domain.usecase.KakaoLoginUseCase
import com.kuit.afternote.feature.auth.domain.usecase.LoginUseCase
import com.kuit.afternote.feature.dev.domain.TestAccountManager
import com.kuit.afternote.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

/**
 * [LoginViewModel] 단위 테스트.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var loginUseCase: LoginUseCase
    private lateinit var kakaoLoginUseCase: KakaoLoginUseCase
    private lateinit var tokenManager: TokenManager
    private lateinit var testAccountManager: TestAccountManager
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        loginUseCase = mockk()
        kakaoLoginUseCase = mockk()
        tokenManager = mockk()
        testAccountManager = mockk()
        coJustRun { tokenManager.saveTokens(any(), any(), any()) }
        coJustRun { testAccountManager.updateStoredPassword(any()) }
        viewModel = LoginViewModel(loginUseCase, kakaoLoginUseCase, tokenManager, testAccountManager)
    }

    @Test
    fun login_whenBlankEmail_setsErrorMessage() {
        viewModel.login("", "pwd")

        assertEquals("이메일과 비밀번호를 입력하세요.", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.loginSuccess)
    }

    @Test
    fun login_whenBlankPassword_setsErrorMessage() {
        viewModel.login("a@b.com", "")

        assertEquals("이메일과 비밀번호를 입력하세요.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun login_whenSuccess_setsLoginSuccess() =
        runTest {
            coEvery { loginUseCase(any(), any()) } returns Result.success(LoginResult(accessToken = "at", refreshToken = "rt"))

            viewModel.login("a@b.com", "pwd")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.loginSuccess)
            assertNull(viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun login_whenFailure_setsErrorMessage() =
        runTest {
            coEvery { loginUseCase(any(), any()) } returns Result.failure(RuntimeException("invalid credentials"))

            viewModel.login("a@b.com", "wrong")
            advanceUntilIdle()

            assertEquals("invalid credentials", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.loginSuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun clearLoginSuccess_resetsLoginSuccess() =
        runTest {
            coEvery { loginUseCase(any(), any()) } returns Result.success(LoginResult())
            viewModel.login("a@b.com", "pwd")
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.loginSuccess)

            viewModel.clearLoginSuccess()

            assertFalse(viewModel.uiState.value.loginSuccess)
        }

    @Test
    fun clearError_clearsErrorMessage() {
        viewModel.login("", "pwd")
        assertEquals("이메일과 비밀번호를 입력하세요.", viewModel.uiState.value.errorMessage)

        viewModel.clearError()

        assertNull(viewModel.uiState.value.errorMessage)
    }

    // ========== HTTP Error Cases ==========

    @Test
    fun login_when404NotFound_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":404,"code":404,"message":"User not found"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<LoginResult>(404, errorBody))
            coEvery { loginUseCase(any(), any()) } returns Result.failure(httpException)

            viewModel.login("nonexistent@example.com", "password123!")
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.errorMessage
                    ?.contains("404") == true
            )
            assertFalse(viewModel.uiState.value.loginSuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun login_when401Unauthorized_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Invalid credentials"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<LoginResult>(401, errorBody))
            coEvery { loginUseCase(any(), any()) } returns Result.failure(httpException)

            viewModel.login("test@example.com", "wrongPassword")
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.errorMessage
                    ?.contains("401") == true
            )
            assertFalse(viewModel.uiState.value.loginSuccess)
        }

    @Test
    fun login_when400BadRequest_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Invalid email format"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<LoginResult>(400, errorBody))
            coEvery { loginUseCase(any(), any()) } returns Result.failure(httpException)

            viewModel.login("invalid-email", "password123!")
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.errorMessage
                    ?.contains("400") == true
            )
            assertFalse(viewModel.uiState.value.loginSuccess)
        }

    @Test
    fun login_when500ServerError_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Internal server error"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<LoginResult>(500, errorBody))
            coEvery { loginUseCase(any(), any()) } returns Result.failure(httpException)

            viewModel.login("test@example.com", "password123!")
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.errorMessage
                    ?.contains("500") == true
            )
            assertFalse(viewModel.uiState.value.loginSuccess)
        }

    // ========== Network Error Cases ==========

    @Test
    fun login_whenNetworkError_setsErrorMessage() =
        runTest {
            coEvery { loginUseCase(any(), any()) } returns Result.failure(
                java.io.IOException("Network unavailable")
            )

            viewModel.login("test@example.com", "password123!")
            advanceUntilIdle()

            assertEquals("Network unavailable", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.loginSuccess)
        }

    // ========== Kakao Login ==========

    @Test
    fun kakaoLogin_whenBlankToken_setsErrorMessage() {
        viewModel.kakaoLogin("")

        assertEquals("카카오 로그인에 실패했습니다.", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.loginSuccess)
    }

    @Test
    fun kakaoLogin_whenSuccess_setsLoginSuccess() =
        runTest {
            coEvery { kakaoLoginUseCase(any()) } returns Result.success(
                LoginResult(accessToken = "at", refreshToken = "rt")
            )

            viewModel.kakaoLogin("kakaoAccessToken")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.loginSuccess)
            assertNull(viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun kakaoLogin_when400BadRequest_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Invalid access token"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<LoginResult>(400, errorBody))
            coEvery { kakaoLoginUseCase(any()) } returns Result.failure(httpException)

            viewModel.kakaoLogin("badToken")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("400") == true)
            assertFalse(viewModel.uiState.value.loginSuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun kakaoLogin_when401Unauthorized_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Unauthorized"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<LoginResult>(401, errorBody))
            coEvery { kakaoLoginUseCase(any()) } returns Result.failure(httpException)

            viewModel.kakaoLogin("expiredToken")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("401") == true)
            assertFalse(viewModel.uiState.value.loginSuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun kakaoLogin_when404NotFound_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":404,"code":404,"message":"Not found"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<LoginResult>(404, errorBody))
            coEvery { kakaoLoginUseCase(any()) } returns Result.failure(httpException)

            viewModel.kakaoLogin("token")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("404") == true)
            assertFalse(viewModel.uiState.value.loginSuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun kakaoLogin_when500ServerError_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Internal server error"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<LoginResult>(500, errorBody))
            coEvery { kakaoLoginUseCase(any()) } returns Result.failure(httpException)

            viewModel.kakaoLogin("token")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("500") == true)
            assertFalse(viewModel.uiState.value.loginSuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun kakaoLogin_whenNetworkError_setsErrorMessage() =
        runTest {
            coEvery { kakaoLoginUseCase(any()) } returns Result.failure(
                java.io.IOException("Network unavailable")
            )

            viewModel.kakaoLogin("token")
            advanceUntilIdle()

            assertEquals("Network unavailable", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.loginSuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }
}
