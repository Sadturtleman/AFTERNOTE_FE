package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import com.kuit.afternote.feature.auth.domain.model.LoginResult
import com.kuit.afternote.feature.auth.domain.usecase.LoginUseCase
import com.kuit.afternote.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * [LoginViewModel] 단위 테스트.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var loginUseCase: LoginUseCase
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        loginUseCase = mockk()
        viewModel = LoginViewModel(loginUseCase)
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
    fun login_whenSuccess_setsLoginSuccess() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Result.success(LoginResult(accessToken = "at", refreshToken = "rt"))

        viewModel.login("a@b.com", "pwd")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.loginSuccess)
        assertNull(viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun login_whenFailure_setsErrorMessage() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Result.failure(RuntimeException("invalid credentials"))

        viewModel.login("a@b.com", "wrong")
        advanceUntilIdle()

        assertEquals("invalid credentials", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.loginSuccess)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun clearLoginSuccess_resetsLoginSuccess() = runTest {
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
}
