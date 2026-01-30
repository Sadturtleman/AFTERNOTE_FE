package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import com.kuit.afternote.feature.auth.domain.usecase.SendEmailCodeUseCase
import com.kuit.afternote.util.MainCoroutineRule
import io.mockk.coEvery
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
 * [SendEmailCodeViewModel] 단위 테스트.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SendEmailCodeViewModelTest {
    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var sendEmailCodeUseCase: SendEmailCodeUseCase
    private lateinit var viewModel: SendEmailCodeViewModel

    @Before
    fun setUp() {
        sendEmailCodeUseCase = mockk()
        viewModel = SendEmailCodeViewModel(sendEmailCodeUseCase)
    }

    @Test
    fun sendEmailCode_whenBlankEmail_setsErrorMessage() {
        viewModel.sendEmailCode("")

        assertEquals("이메일을 입력하세요.", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.sendSuccess)
    }

    @Test
    fun sendEmailCode_whenSuccess_setsSendSuccess() =
        runTest {
            coEvery { sendEmailCodeUseCase(any()) } returns Result.success(Unit)

            viewModel.sendEmailCode("a@b.com")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.sendSuccess)
            assertNull(viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun sendEmailCode_whenFailure_setsErrorMessage() =
        runTest {
            coEvery { sendEmailCodeUseCase(any()) } returns Result.failure(RuntimeException("rate limit"))

            viewModel.sendEmailCode("a@b.com")
            advanceUntilIdle()

            assertEquals("rate limit", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.sendSuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun clearSendSuccess_resetsSendSuccess() =
        runTest {
            coEvery { sendEmailCodeUseCase(any()) } returns Result.success(Unit)
            viewModel.sendEmailCode("a@b.com")
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.sendSuccess)

            viewModel.clearSendSuccess()

            assertFalse(viewModel.uiState.value.sendSuccess)
        }

    @Test
    fun clearError_clearsErrorMessage() {
        viewModel.sendEmailCode("")
        assertEquals("이메일을 입력하세요.", viewModel.uiState.value.errorMessage)

        viewModel.clearError()

        assertNull(viewModel.uiState.value.errorMessage)
    }

    // ========== HTTP Error Cases ==========

    @Test
    fun sendEmailCode_when400BadRequest_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Invalid email format"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Unit>(400, errorBody))
            coEvery { sendEmailCodeUseCase(any()) } returns Result.failure(httpException)

            viewModel.sendEmailCode("invalid-email")
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.errorMessage
                    ?.contains("400") == true
            )
            assertFalse(viewModel.uiState.value.sendSuccess)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun sendEmailCode_when429TooManyRequests_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":429,"code":429,"message":"Rate limit exceeded"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Unit>(429, errorBody))
            coEvery { sendEmailCodeUseCase(any()) } returns Result.failure(httpException)

            viewModel.sendEmailCode("a@b.com")
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.errorMessage
                    ?.contains("429") == true
            )
            assertFalse(viewModel.uiState.value.sendSuccess)
        }

    @Test
    fun sendEmailCode_when500ServerError_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Internal server error"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Unit>(500, errorBody))
            coEvery { sendEmailCodeUseCase(any()) } returns Result.failure(httpException)

            viewModel.sendEmailCode("a@b.com")
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.errorMessage
                    ?.contains("500") == true
            )
            assertFalse(viewModel.uiState.value.sendSuccess)
        }

    // ========== Network Error Cases ==========

    @Test
    fun sendEmailCode_whenNetworkError_setsErrorMessage() =
        runTest {
            coEvery { sendEmailCodeUseCase(any()) } returns Result.failure(
                java.io.IOException("Network unavailable")
            )

            viewModel.sendEmailCode("a@b.com")
            advanceUntilIdle()

            assertEquals("Network unavailable", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.sendSuccess)
        }
}
