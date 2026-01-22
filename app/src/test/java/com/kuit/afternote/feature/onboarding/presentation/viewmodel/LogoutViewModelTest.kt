package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import com.kuit.afternote.feature.auth.domain.usecase.LogoutUseCase
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
 * [LogoutViewModel] 단위 테스트.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class LogoutViewModelTest {

    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var logoutUseCase: LogoutUseCase
    private lateinit var viewModel: LogoutViewModel

    @Before
    fun setUp() {
        logoutUseCase = mockk()
        viewModel = LogoutViewModel(logoutUseCase)
    }

    @Test
    fun logout_whenBlankRefreshToken_setsErrorMessage() {
        viewModel.logout("")

        assertEquals("리프레시 토큰을 입력하세요.", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.logoutSuccess)
    }

    @Test
    fun logout_whenSuccess_setsLogoutSuccess() = runTest {
        coEvery { logoutUseCase(any()) } returns Result.success(Unit)

        viewModel.logout("refreshToken")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.logoutSuccess)
        assertNull(viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun logout_whenFailure_setsErrorMessage() = runTest {
        coEvery { logoutUseCase(any()) } returns Result.failure(RuntimeException("invalid token"))

        viewModel.logout("invalidToken")
        advanceUntilIdle()

        assertEquals("invalid token", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.logoutSuccess)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun clearLogoutSuccess_resetsLogoutSuccess() = runTest {
        coEvery { logoutUseCase(any()) } returns Result.success(Unit)
        viewModel.logout("refreshToken")
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.logoutSuccess)

        viewModel.clearLogoutSuccess()

        assertFalse(viewModel.uiState.value.logoutSuccess)
    }

    @Test
    fun clearError_clearsErrorMessage() {
        viewModel.logout("")
        assertEquals("리프레시 토큰을 입력하세요.", viewModel.uiState.value.errorMessage)

        viewModel.clearError()

        assertNull(viewModel.uiState.value.errorMessage)
    }
}
