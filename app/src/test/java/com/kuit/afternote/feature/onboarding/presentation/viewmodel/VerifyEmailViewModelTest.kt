package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import android.util.Log
import com.kuit.afternote.feature.auth.domain.model.EmailVerifyResult
import com.kuit.afternote.feature.auth.domain.usecase.VerifyEmailUseCase
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
 * [VerifyEmailViewModel] 단위 테스트.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class VerifyEmailViewModelTest {
    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var verifyEmailUseCase: VerifyEmailUseCase
    private lateinit var viewModel: VerifyEmailViewModel

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.w(any(), any<String>()) } returns 0

        verifyEmailUseCase = mockk()
        viewModel = VerifyEmailViewModel(verifyEmailUseCase)
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun verifyEmail_whenBlankEmail_setsErrorMessage() {
        viewModel.verifyEmail("", "123456")

        assertEquals("이메일과 인증번호를 입력하세요.", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.verifySuccess)
    }

    @Test
    fun verifyEmail_whenBlankCode_setsErrorMessage() {
        viewModel.verifyEmail("a@b.com", "")

        assertEquals("이메일과 인증번호를 입력하세요.", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun verifyEmail_whenSuccess_setsVerifySuccess() =
        runTest {
            coEvery { verifyEmailUseCase(any(), any()) } returns Result.success(EmailVerifyResult(isVerified = true))

            viewModel.verifyEmail("a@b.com", "123456")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.verifySuccess)
            assertNull(viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun verifyEmail_whenFailure_setsErrorMessage() =
        runTest {
            coEvery { verifyEmailUseCase(any(), any()) } returns Result.failure(RuntimeException("invalid code"))

            viewModel.verifyEmail("a@b.com", "000000")
            advanceUntilIdle()

            assertEquals("invalid code", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.verifySuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun clearVerifySuccess_resetsVerifySuccess() =
        runTest {
            coEvery { verifyEmailUseCase(any(), any()) } returns Result.success(EmailVerifyResult(isVerified = true))
            viewModel.verifyEmail("a@b.com", "123456")
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.verifySuccess)

            viewModel.clearVerifySuccess()

            assertFalse(viewModel.uiState.value.verifySuccess)
        }

    @Test
    fun clearError_clearsErrorMessage() {
        viewModel.verifyEmail("", "")
        assertEquals("이메일과 인증번호를 입력하세요.", viewModel.uiState.value.errorMessage)

        viewModel.clearError()

        assertNull(viewModel.uiState.value.errorMessage)
    }

    // ========== HTTP Error Cases ==========

    @Test
    fun verifyEmail_when400BadRequest_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Invalid code format"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<EmailVerifyResult>(400, errorBody))
            coEvery { verifyEmailUseCase(any(), any()) } returns Result.failure(httpException)

            viewModel.verifyEmail("a@b.com", "invalid")
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.errorMessage
                    ?.contains("400") == true
            )
            assertFalse(viewModel.uiState.value.verifySuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun verifyEmail_when401Unauthorized_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Code expired or invalid"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<EmailVerifyResult>(401, errorBody))
            coEvery { verifyEmailUseCase(any(), any()) } returns Result.failure(httpException)

            viewModel.verifyEmail("a@b.com", "000000")
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.errorMessage
                    ?.contains("401") == true
            )
            assertFalse(viewModel.uiState.value.verifySuccess)
        }

    @Test
    fun verifyEmail_when404NotFound_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":404,"code":404,"message":"Email not found"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<EmailVerifyResult>(404, errorBody))
            coEvery { verifyEmailUseCase(any(), any()) } returns Result.failure(httpException)

            viewModel.verifyEmail("unknown@b.com", "123456")
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.errorMessage
                    ?.contains("404") == true
            )
            assertFalse(viewModel.uiState.value.verifySuccess)
        }

    @Test
    fun verifyEmail_when500ServerError_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Internal server error"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<EmailVerifyResult>(500, errorBody))
            coEvery { verifyEmailUseCase(any(), any()) } returns Result.failure(httpException)

            viewModel.verifyEmail("a@b.com", "123456")
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.errorMessage
                    ?.contains("500") == true
            )
            assertFalse(viewModel.uiState.value.verifySuccess)
        }

    // ========== Network Error Cases ==========

    @Test
    fun verifyEmail_whenNetworkError_setsErrorMessage() =
        runTest {
            coEvery { verifyEmailUseCase(any(), any()) } returns Result.failure(
                java.io.IOException("Network unavailable")
            )

            viewModel.verifyEmail("a@b.com", "123456")
            advanceUntilIdle()

            assertEquals("Network unavailable", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.verifySuccess)
        }
}
