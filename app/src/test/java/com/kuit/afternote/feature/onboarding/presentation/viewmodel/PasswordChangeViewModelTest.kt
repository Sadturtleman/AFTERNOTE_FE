package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import com.kuit.afternote.feature.auth.domain.usecase.PasswordChangeUseCase
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
        passwordChangeUseCase = mockk()
        viewModel = PasswordChangeViewModel(passwordChangeUseCase)
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
    fun changePassword_whenSuccess_setsPasswordChangeSuccess() = runTest {
        coEvery { passwordChangeUseCase(any(), any()) } returns Result.success(Unit)

        viewModel.changePassword("currentPwd123!", "newPwd123!")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.passwordChangeSuccess)
        assertNull(viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun changePassword_whenFailure_setsErrorMessage() = runTest {
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
    fun clearPasswordChangeSuccess_resetsPasswordChangeSuccess() = runTest {
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
}
