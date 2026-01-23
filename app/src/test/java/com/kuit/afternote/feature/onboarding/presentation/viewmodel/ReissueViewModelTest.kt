package com.kuit.afternote.feature.onboarding.presentation.viewmodel

import com.kuit.afternote.feature.auth.domain.model.ReissueResult
import com.kuit.afternote.feature.auth.domain.usecase.ReissueUseCase
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
 * [ReissueViewModel] 단위 테스트.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ReissueViewModelTest {

    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var reissueUseCase: ReissueUseCase
    private lateinit var viewModel: ReissueViewModel

    @Before
    fun setUp() {
        reissueUseCase = mockk()
        viewModel = ReissueViewModel(reissueUseCase)
    }

    @Test
    fun reissue_whenBlankRefreshToken_setsErrorMessage() {
        viewModel.reissue("")

        assertEquals("리프레시 토큰을 입력하세요.", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.reissueSuccess)
    }

    @Test
    fun reissue_whenSuccess_setsReissueSuccess() = runTest {
        coEvery { reissueUseCase(any()) } returns Result.success(
            ReissueResult(accessToken = "at", refreshToken = "rt")
        )

        viewModel.reissue("refreshToken")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.reissueSuccess)
        assertNull(viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun reissue_whenFailure_setsErrorMessage() = runTest {
        coEvery { reissueUseCase(any()) } returns Result.failure(RuntimeException("invalid token"))

        viewModel.reissue("invalidToken")
        advanceUntilIdle()

        assertEquals("invalid token", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.reissueSuccess)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun clearReissueSuccess_resetsReissueSuccess() = runTest {
        coEvery { reissueUseCase(any()) } returns Result.success(ReissueResult())
        viewModel.reissue("refreshToken")
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.reissueSuccess)

        viewModel.clearReissueSuccess()

        assertFalse(viewModel.uiState.value.reissueSuccess)
    }

    @Test
    fun clearError_clearsErrorMessage() {
        viewModel.reissue("")
        assertEquals("리프레시 토큰을 입력하세요.", viewModel.uiState.value.errorMessage)

        viewModel.clearError()

        assertNull(viewModel.uiState.value.errorMessage)
    }
}
