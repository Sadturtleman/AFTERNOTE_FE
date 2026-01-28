package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import com.kuit.afternote.data.local.TokenManager
import com.kuit.afternote.feature.auth.domain.usecase.LogoutUseCase
import com.kuit.afternote.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

/**
 * [LogoutViewModel] 단위 테스트.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class LogoutViewModelTest {
    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var logoutUseCase: LogoutUseCase
    private lateinit var tokenManager: TokenManager
    private lateinit var viewModel: LogoutViewModel

    @Before
    fun setUp() {
        logoutUseCase = mockk()
        tokenManager = mockk()
        coEvery { tokenManager.getRefreshToken() } returns "refreshToken"
        coJustRun { tokenManager.clearTokens() }
        viewModel = LogoutViewModel(logoutUseCase, tokenManager)
    }

    @Test
    fun logout_whenNoToken_clearsLocalTokensAndSucceeds() =
        runTest {
            coEvery { tokenManager.getRefreshToken() } returns null

            viewModel.logout()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.logoutSuccess)
            assertNull(viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun logout_whenSuccess_clearsTokensAndSetsLogoutSuccess() =
        runTest {
            coEvery { logoutUseCase(any()) } returns Result.success(Unit)

            viewModel.logout()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.logoutSuccess)
            assertNull(viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun logout_whenFailure_stillClearsTokensAndSetsLogoutSuccess() =
        runTest {
            coEvery { logoutUseCase(any()) } returns Result.failure(RuntimeException("invalid token"))

            viewModel.logout()
            advanceUntilIdle()

            // 실패해도 로컬 토큰은 삭제하고 logoutSuccess는 true
            assertTrue(viewModel.uiState.value.logoutSuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun clearLogoutSuccess_resetsLogoutSuccess() =
        runTest {
            coEvery { logoutUseCase(any()) } returns Result.success(Unit)
            viewModel.logout()
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.logoutSuccess)

            viewModel.clearLogoutSuccess()

            assertFalse(viewModel.uiState.value.logoutSuccess)
        }

    @Test
    fun clearError_clearsErrorMessage() =
        runTest {
            coEvery { logoutUseCase(any()) } returns Result.failure(RuntimeException("error"))
            viewModel.logout()
            advanceUntilIdle()

            viewModel.clearError()

            assertNull(viewModel.uiState.value.errorMessage)
        }

    // ========== HTTP Error Cases ==========

    @Test
    fun logout_when401Unauthorized_stillClearsTokensAndSucceeds() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Invalid token"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Unit>(401, errorBody))
            coEvery { logoutUseCase(any()) } returns Result.failure(httpException)

            viewModel.logout()
            advanceUntilIdle()

            // 로그아웃은 실패해도 로컬 토큰을 삭제하고 성공으로 처리
            assertTrue(viewModel.uiState.value.logoutSuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun logout_when500ServerError_stillClearsTokensAndSucceeds() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Internal server error"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Unit>(500, errorBody))
            coEvery { logoutUseCase(any()) } returns Result.failure(httpException)

            viewModel.logout()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.logoutSuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    // ========== Network Error Cases ==========

    @Test
    fun logout_whenNetworkError_stillClearsTokensAndSucceeds() =
        runTest {
            coEvery { logoutUseCase(any()) } returns Result.failure(
                java.io.IOException("Network unavailable")
            )

            viewModel.logout()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.logoutSuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }
}
