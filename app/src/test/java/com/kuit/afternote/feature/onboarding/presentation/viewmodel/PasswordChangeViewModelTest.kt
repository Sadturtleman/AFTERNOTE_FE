package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import android.util.Log
import com.kuit.afternote.feature.auth.domain.usecase.PasswordChangeUseCase
import com.kuit.afternote.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
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
 * [PasswordChangeViewModel] 단위 테스트.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PasswordChangeViewModelTest {
    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var passwordChangeUseCase: PasswordChangeUseCase
    private lateinit var viewModel: PasswordChangeViewModel

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.w(any(), any<String>()) } returns 0

        passwordChangeUseCase = mockk()
        viewModel = PasswordChangeViewModel(passwordChangeUseCase)
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun changePassword_whenBlankCurrentPassword_setsErrorMessage() {
        viewModel.changePassword("", "newPwd123!")

        assertEquals("현재 비밀번호를 입력하세요.", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.passwordChangeSuccess)
    }

    @Test
    fun changePassword_whenBlankNewPassword_setsErrorMessage() {
        viewModel.changePassword("currentPwd123!", "")

        assertEquals("새 비밀번호를 입력하세요.", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.passwordChangeSuccess)
    }

    @Test
    fun changePassword_whenSuccess_setsPasswordChangeSuccess() =
        runTest {
            coEvery { passwordChangeUseCase(any(), any()) } returns Result.success(Unit)

            viewModel.changePassword("currentPwd123!", "newPwd123!")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.passwordChangeSuccess)
            assertNull(viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun changePassword_whenFailure_setsErrorMessage() =
        runTest {
            coEvery { passwordChangeUseCase(any(), any()) } returns Result.failure(
                RuntimeException("현재 비밀번호가 일치하지 않습니다.")
            )

            viewModel.changePassword("wrongPwd", "newPwd123!")
            advanceUntilIdle()

            assertEquals("현재 비밀번호가 일치하지 않습니다.", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.passwordChangeSuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun clearPasswordChangeSuccess_resetsPasswordChangeSuccess() =
        runTest {
            coEvery { passwordChangeUseCase(any(), any()) } returns Result.success(Unit)
            viewModel.changePassword("currentPwd123!", "newPwd123!")
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.passwordChangeSuccess)

            viewModel.clearPasswordChangeSuccess()

            assertFalse(viewModel.uiState.value.passwordChangeSuccess)
        }

    @Test
    fun clearError_clearsErrorMessage() {
        viewModel.changePassword("", "newPwd123!")
        assertEquals("현재 비밀번호를 입력하세요.", viewModel.uiState.value.errorMessage)

        viewModel.clearError()

        assertNull(viewModel.uiState.value.errorMessage)
    }

    // ========== HTTP Error Cases ==========

    @Test
    fun changePassword_when400BadRequest_setsUserFriendlyErrorMessage() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Invalid password format"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Unit>(400, errorBody))
            coEvery { passwordChangeUseCase(any(), any()) } returns Result.failure(httpException)

            viewModel.changePassword("currentPwd!", "weak")
            advanceUntilIdle()

            // Server message contains "format" -> maps to password format requirement message
            assertEquals(
                "비밀번호는 영문, 숫자, 특수문자를 포함한 8~20자여야 합니다.",
                viewModel.uiState.value.errorMessage
            )
            assertFalse(viewModel.uiState.value.passwordChangeSuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun changePassword_when400WrongPassword_setsUserFriendlyErrorMessage() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Current password is wrong"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Unit>(400, errorBody))
            coEvery { passwordChangeUseCase(any(), any()) } returns Result.failure(httpException)

            viewModel.changePassword("wrongPwd!", "newPwd123!")
            advanceUntilIdle()

            // Server message contains "wrong" -> maps to wrong password message
            assertEquals("현재 비밀번호가 일치하지 않습니다.", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.passwordChangeSuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun changePassword_when401Unauthorized_setsUserFriendlyErrorMessage() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Current password is incorrect"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Unit>(401, errorBody))
            coEvery { passwordChangeUseCase(any(), any()) } returns Result.failure(httpException)

            viewModel.changePassword("wrongPwd!", "newPwd123!")
            advanceUntilIdle()

            assertEquals("현재 비밀번호가 일치하지 않습니다.", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.passwordChangeSuccess)
        }

    @Test
    fun changePassword_when500ServerError_setsUserFriendlyErrorMessage() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Internal server error"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Unit>(500, errorBody))
            coEvery { passwordChangeUseCase(any(), any()) } returns Result.failure(httpException)

            viewModel.changePassword("currentPwd!", "newPwd123!")
            advanceUntilIdle()

            assertEquals("서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.passwordChangeSuccess)
        }

    // ========== Network Error Cases ==========

    @Test
    fun changePassword_whenNetworkError_setsUserFriendlyErrorMessage() =
        runTest {
            coEvery { passwordChangeUseCase(any(), any()) } returns Result.failure(
                java.io.IOException("Network unavailable")
            )

            viewModel.changePassword("currentPwd!", "newPwd123!")
            advanceUntilIdle()

            assertEquals("네트워크 연결을 확인해주세요.", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.passwordChangeSuccess)
        }
}
