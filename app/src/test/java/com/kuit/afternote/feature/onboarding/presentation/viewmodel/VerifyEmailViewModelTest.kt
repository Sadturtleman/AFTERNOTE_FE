package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import com.kuit.afternote.feature.auth.domain.model.EmailVerifyResult
import com.kuit.afternote.feature.auth.domain.usecase.VerifyEmailUseCase
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
        verifyEmailUseCase = mockk()
        viewModel = VerifyEmailViewModel(verifyEmailUseCase)
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
    fun verifyEmail_whenSuccess_setsVerifySuccess() = runTest {
        coEvery { verifyEmailUseCase(any(), any()) } returns Result.success(EmailVerifyResult(isVerified = true))

        viewModel.verifyEmail("a@b.com", "123456")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.verifySuccess)
        assertNull(viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun verifyEmail_whenFailure_setsErrorMessage() = runTest {
        coEvery { verifyEmailUseCase(any(), any()) } returns Result.failure(RuntimeException("invalid code"))

        viewModel.verifyEmail("a@b.com", "000000")
        advanceUntilIdle()

        assertEquals("invalid code", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.verifySuccess)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun clearVerifySuccess_resetsVerifySuccess() = runTest {
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
}
