package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import com.kuit.afternote.data.local.TokenManager
import com.kuit.afternote.feature.auth.domain.model.LoginResult
import com.kuit.afternote.feature.auth.domain.model.SignUpResult
import com.kuit.afternote.feature.auth.domain.usecase.LoginUseCase
import com.kuit.afternote.feature.auth.domain.usecase.SignUpUseCase
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
 * [SignUpViewModel] 단위 테스트.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SignUpViewModelTest {
    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var signUpUseCase: SignUpUseCase
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var tokenManager: TokenManager
    private lateinit var viewModel: SignUpViewModel

    @Before
    fun setUp() {
        signUpUseCase = mockk()
        loginUseCase = mockk()
        tokenManager = mockk(relaxed = true)
        viewModel = SignUpViewModel(signUpUseCase, loginUseCase, tokenManager)
    }

    @Test
    fun signUp_whenBlankEmail_setsErrorMessage() {
        viewModel.signUp("", "pwd1!", "name", null)

        assertEquals("이메일을 입력하세요.", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.signUpSuccess)
    }

    @Test
    fun signUp_whenBlankPassword_setsErrorMessage() {
        viewModel.signUp("a@b.com", "", "name", null)

        assertEquals("비밀번호를 입력하세요.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun signUp_whenBlankName_setsErrorMessage() {
        viewModel.signUp("a@b.com", "pwd1!", "", null)

        assertEquals("이름을 입력하세요.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun signUp_whenSuccess_thenLoginSuccess_setsSignUpSuccess() =
        runTest {
            coEvery { signUpUseCase(any(), any(), any(), any()) } returns Result.success(SignUpResult(1L, "a@b.com"))
            coEvery { loginUseCase(any(), any()) } returns Result.success(
                LoginResult(accessToken = "access", refreshToken = "refresh")
            )
            coJustRun { tokenManager.saveTokens(any(), any(), any()) }

            viewModel.signUp("a@b.com", "pwd1!", "name", null)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.signUpSuccess)
            assertNull(viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun signUp_withProfileUrl_callsUseCaseWithProfileUrl() =
        runTest {
            coEvery { signUpUseCase(any(), any(), any(), any()) } returns Result.success(SignUpResult(2L, "b@c.com"))
            coEvery { loginUseCase(any(), any()) } returns Result.success(
                LoginResult(accessToken = "a", refreshToken = "r")
            )
            coJustRun { tokenManager.saveTokens(any(), any(), any()) }

            viewModel.signUp("b@c.com", "pwd2!", "nick", "https://img/p.jpg")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.signUpSuccess)
        }

    @Test
    fun signUp_whenFailure_setsErrorMessage() =
        runTest {
            coEvery { signUpUseCase(any(), any(), any(), any()) } returns Result.failure(RuntimeException("email exists"))

            viewModel.signUp("a@b.com", "pwd", "n", null)
            advanceUntilIdle()

            assertEquals("email exists", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.signUpSuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun clearSignUpSuccess_resetsSignUpSuccess() =
        runTest {
            coEvery { signUpUseCase(any(), any(), any(), any()) } returns Result.success(SignUpResult(1L, "a@b.com"))
            coEvery { loginUseCase(any(), any()) } returns Result.success(
                LoginResult(accessToken = "a", refreshToken = "r")
            )
            coJustRun { tokenManager.saveTokens(any(), any(), any()) }
            viewModel.signUp("a@b.com", "pwd", "n", null)
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.signUpSuccess)

            viewModel.clearSignUpSuccess()

            assertFalse(viewModel.uiState.value.signUpSuccess)
        }

    @Test
    fun clearError_clearsErrorMessage() {
        viewModel.signUp("", "pwd", "n", null)
        assertEquals("이메일을 입력하세요.", viewModel.uiState.value.errorMessage)

        viewModel.clearError()

        assertNull(viewModel.uiState.value.errorMessage)
    }

    // ========== HTTP Error Cases ==========

    @Test
    fun signUp_when400BadRequest_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Invalid email format"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<SignUpResult>(400, errorBody))
            coEvery { signUpUseCase(any(), any(), any(), any()) } returns Result.failure(httpException)

            viewModel.signUp("invalid-email", "pwd1!", "name", null)
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.errorMessage
                    ?.contains("400") == true
            )
            assertFalse(viewModel.uiState.value.signUpSuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun signUp_when409Conflict_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":409,"code":409,"message":"Email already exists"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<SignUpResult>(409, errorBody))
            coEvery { signUpUseCase(any(), any(), any(), any()) } returns Result.failure(httpException)

            viewModel.signUp("existing@example.com", "pwd1!", "name", null)
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.errorMessage
                    ?.contains("409") == true
            )
            assertFalse(viewModel.uiState.value.signUpSuccess)
        }

    @Test
    fun signUp_when500ServerError_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Internal server error"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<SignUpResult>(500, errorBody))
            coEvery { signUpUseCase(any(), any(), any(), any()) } returns Result.failure(httpException)

            viewModel.signUp("a@b.com", "pwd1!", "name", null)
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.errorMessage
                    ?.contains("500") == true
            )
            assertFalse(viewModel.uiState.value.signUpSuccess)
        }

    // ========== Network Error Cases ==========

    @Test
    fun signUp_whenNetworkError_setsErrorMessage() =
        runTest {
            coEvery { signUpUseCase(any(), any(), any(), any()) } returns Result.failure(
                java.io.IOException("Network unavailable")
            )

            viewModel.signUp("a@b.com", "pwd1!", "name", null)
            advanceUntilIdle()

            assertEquals("Network unavailable", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.signUpSuccess)
        }
}
