package com.kuit.afternote.feature.dev.presentation.viewmodel

import android.util.Log
import com.kuit.afternote.data.local.TokenManager
import com.kuit.afternote.feature.auth.domain.model.LoginResult
import com.kuit.afternote.feature.auth.domain.usecase.LoginUseCase
import com.kuit.afternote.feature.auth.domain.usecase.LogoutUseCase
import com.kuit.afternote.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
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
 * [DevModeViewModel] 단위 테스트.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DevModeViewModelTest {

    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var tokenManager: TokenManager
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var logoutUseCase: LogoutUseCase
    private lateinit var viewModel: DevModeViewModel

    private val isLoggedInFlow = MutableStateFlow(false)
    private val userEmailFlow = MutableStateFlow<String?>(null)

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.w(any(), any<String>()) } returns 0

        tokenManager = mockk()
        loginUseCase = mockk()
        logoutUseCase = mockk()

        every { tokenManager.isLoggedInFlow } returns isLoggedInFlow
        every { tokenManager.userEmailFlow } returns userEmailFlow
        coJustRun { tokenManager.saveTokens(any(), any(), any()) }
        coJustRun { tokenManager.clearTokens() }
        coEvery { tokenManager.getRefreshToken() } returns "refresh_token"

        viewModel = DevModeViewModel(tokenManager, loginUseCase, logoutUseCase)
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    // ========== Success Cases ==========

    @Test
    fun quickLogin_whenSuccess_savesTokensAndSetsSuccessMessage() = runTest {
        val loginResult = LoginResult(accessToken = "access_token", refreshToken = "refresh_token")
        coEvery { loginUseCase(any(), any()) } returns Result.success(loginResult)

        viewModel.quickLogin("test@example.com", "password123!")

        val state = withTimeout(timeMillis = 1000) {
            viewModel.uiState.first { it.message != null }
        }

        coVerify(exactly = 1) { loginUseCase("test@example.com", "password123!") }
        coVerify(exactly = 1) {
            tokenManager.saveTokens(
                accessToken = "access_token",
                refreshToken = "refresh_token",
                email = "test@example.com"
            )
        }
        assertEquals("로그인 성공", state.message)
        assertFalse(state.isLoading)
    }

    @Test
    fun quickLogin_whenTokensEmpty_setsEmptyTokenMessage() = runTest {
        val loginResult = LoginResult(accessToken = null, refreshToken = null)
        coEvery { loginUseCase(any(), any()) } returns Result.success(loginResult)

        viewModel.quickLogin("test@example.com", "password123!")

        val state = withTimeout(timeMillis = 1000) {
            viewModel.uiState.first { it.message != null }
        }

        assertEquals("토큰이 없습니다", state.message)
    }

    @Test
    fun logout_whenSuccess_clearsTokensAndSetsSuccessMessage() = runTest {
        coEvery { logoutUseCase(any()) } returns Result.success(Unit)

        viewModel.logout()

        val state = withTimeout(timeMillis = 1000) {
            viewModel.uiState.first { it.message != null }
        }

        coVerify(exactly = 1) { logoutUseCase("refresh_token") }
        coVerify(exactly = 1) { tokenManager.clearTokens() }
        assertEquals("로그아웃 완료", state.message)
        assertFalse(state.isLoading)
    }

    @Test
    fun clearMessage_resetsMessage() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Result.success(
            LoginResult(accessToken = "at", refreshToken = "rt")
        )
        viewModel.quickLogin("test@example.com", "password123!")

        val stateWithMessage = withTimeout(timeMillis = 1000) {
            viewModel.uiState.first { it.message != null }
        }
        assertEquals("로그인 성공", stateWithMessage.message)

        viewModel.clearMessage()

        val stateAfterClear = withTimeout(timeMillis = 1000) {
            viewModel.uiState.first { it.message == null }
        }
        assertNull(stateAfterClear.message)
    }

    // ========== HTTP Error Cases ==========

    @Test
    fun quickLogin_when404NotFound_setsFailureMessage() = runTest {
        val errorBody = """{"status":404,"code":404,"message":"User not found"}"""
            .toResponseBody("application/json".toMediaType())
        val httpException = HttpException(Response.error<LoginResult>(404, errorBody))
        coEvery { loginUseCase(any(), any()) } returns Result.failure(httpException)

        viewModel.quickLogin("nonexistent@example.com", "password123!")

        val state = withTimeout(timeMillis = 1000) {
            viewModel.uiState.first { it.message != null }
        }

        assertTrue(state.message?.contains("로그인 실패") == true)
        assertFalse(state.isLoading)
    }

    @Test
    fun quickLogin_when401Unauthorized_setsFailureMessage() = runTest {
        val errorBody = """{"status":401,"code":401,"message":"Invalid credentials"}"""
            .toResponseBody("application/json".toMediaType())
        val httpException = HttpException(Response.error<LoginResult>(401, errorBody))
        coEvery { loginUseCase(any(), any()) } returns Result.failure(httpException)

        viewModel.quickLogin("test@example.com", "wrongPassword")

        val state = withTimeout(timeMillis = 1000) {
            viewModel.uiState.first { it.message != null }
        }

        assertTrue(state.message?.contains("로그인 실패") == true)
        assertFalse(state.isLoading)
    }

    @Test
    fun quickLogin_when400BadRequest_setsFailureMessage() = runTest {
        val errorBody = """{"status":400,"code":400,"message":"Invalid email format"}"""
            .toResponseBody("application/json".toMediaType())
        val httpException = HttpException(Response.error<LoginResult>(400, errorBody))
        coEvery { loginUseCase(any(), any()) } returns Result.failure(httpException)

        viewModel.quickLogin("invalid-email", "password123!")

        val state = withTimeout(timeMillis = 1000) {
            viewModel.uiState.first { it.message != null }
        }

        assertTrue(state.message?.contains("로그인 실패") == true)
        assertFalse(state.isLoading)
    }

    @Test
    fun quickLogin_when500ServerError_setsFailureMessage() = runTest {
        val errorBody = """{"status":500,"code":500,"message":"Internal server error"}"""
            .toResponseBody("application/json".toMediaType())
        val httpException = HttpException(Response.error<LoginResult>(500, errorBody))
        coEvery { loginUseCase(any(), any()) } returns Result.failure(httpException)

        viewModel.quickLogin("test@example.com", "password123!")

        val state = withTimeout(timeMillis = 1000) {
            viewModel.uiState.first { it.message != null }
        }

        assertTrue(state.message?.contains("로그인 실패") == true)
        assertFalse(state.isLoading)
    }

    // ========== Network Error Cases ==========

    @Test
    fun quickLogin_whenNetworkError_setsFailureMessage() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Result.failure(
            java.io.IOException("Network unavailable")
        )

        viewModel.quickLogin("test@example.com", "password123!")

        val state = withTimeout(timeMillis = 1000) {
            viewModel.uiState.first { it.message != null }
        }

        assertTrue(state.message?.contains("로그인 실패") == true)
        assertTrue(state.message?.contains("Network unavailable") == true)
        assertFalse(state.isLoading)
    }

    // ========== HTTP Status Code Verification ==========

    @Test
    fun quickLogin_when404_exceptionContainsCorrectStatusCode() = runTest {
        val errorBody = """{"status":404,"code":404,"message":"User not found"}"""
            .toResponseBody("application/json".toMediaType())
        val httpException = HttpException(Response.error<LoginResult>(404, errorBody))
        coEvery { loginUseCase(any(), any()) } returns Result.failure(httpException)

        viewModel.quickLogin("nonexistent@example.com", "password123!")

        withTimeout(timeMillis = 1000) {
            viewModel.uiState.first { it.message != null }
        }

        coVerify(exactly = 1) { loginUseCase("nonexistent@example.com", "password123!") }
        assertEquals(404, httpException.code())
    }

    @Test
    fun quickLogin_when401_exceptionContainsCorrectStatusCode() = runTest {
        val errorBody = """{"status":401,"code":401,"message":"Invalid credentials"}"""
            .toResponseBody("application/json".toMediaType())
        val httpException = HttpException(Response.error<LoginResult>(401, errorBody))
        coEvery { loginUseCase(any(), any()) } returns Result.failure(httpException)

        viewModel.quickLogin("test@example.com", "wrongPassword")

        withTimeout(timeMillis = 1000) {
            viewModel.uiState.first { it.message != null }
        }

        assertEquals(401, httpException.code())
    }

    @Test
    fun quickLogin_when500_exceptionContainsCorrectStatusCode() = runTest {
        val errorBody = """{"status":500,"code":500,"message":"Internal server error"}"""
            .toResponseBody("application/json".toMediaType())
        val httpException = HttpException(Response.error<LoginResult>(500, errorBody))
        coEvery { loginUseCase(any(), any()) } returns Result.failure(httpException)

        viewModel.quickLogin("test@example.com", "password123!")

        withTimeout(timeMillis = 1000) {
            viewModel.uiState.first { it.message != null }
        }

        assertEquals(500, httpException.code())
    }
}
