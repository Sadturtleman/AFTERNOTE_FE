package com.kuit.afternote.feature.setting.presentation.viewmodel

import com.kuit.afternote.data.local.TokenManager
import com.kuit.afternote.feature.user.domain.usecase.WithdrawAccountUseCase
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
 * [WithdrawalPasswordViewModel] 단위 테스트.
 *
 * 확인 문장("탈퇴하겠습니다.") 검증 및 DELETE /users/me (회원 탈퇴) 호출,
 * HTTP 에러/네트워크 에러 처리 검증.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class WithdrawalPasswordViewModelTest {
    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var withdrawAccountUseCase: WithdrawAccountUseCase
    private lateinit var tokenManager: TokenManager
    private lateinit var viewModel: WithdrawalPasswordViewModel

    @Before
    fun setUp() {
        withdrawAccountUseCase = mockk()
        tokenManager = mockk()
        coJustRun { tokenManager.clearTokens() }
        viewModel = WithdrawalPasswordViewModel(withdrawAccountUseCase, tokenManager)
    }

    @Test
    fun submitWithdrawal_whenWrongSentence_setsShowSentenceError() {
        viewModel.submitWithdrawal("잘못된 문장")

        assertTrue(viewModel.uiState.value.showSentenceError)
        assertFalse(viewModel.uiState.value.withdrawalComplete)
    }

    @Test
    fun submitWithdrawal_whenEmptyInput_setsShowSentenceError() {
        viewModel.submitWithdrawal("")

        assertTrue(viewModel.uiState.value.showSentenceError)
        assertFalse(viewModel.uiState.value.withdrawalComplete)
    }

    @Test
    fun submitWithdrawal_whenBlankInput_setsShowSentenceError() {
        viewModel.submitWithdrawal("   ")

        assertTrue(viewModel.uiState.value.showSentenceError)
        assertFalse(viewModel.uiState.value.withdrawalComplete)
    }

    @Test
    fun submitWithdrawal_whenCorrectSentence_clearsTokensAndSetsWithdrawalComplete() =
        runTest {
            coEvery { withdrawAccountUseCase() } returns Result.success(Unit)

            viewModel.submitWithdrawal("탈퇴하겠습니다.")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.withdrawalComplete)
            assertNull(viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.showSentenceError)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun submitWithdrawal_whenCorrectSentenceWithWhitespace_clearsTokensAndSetsWithdrawalComplete() =
        runTest {
            coEvery { withdrawAccountUseCase() } returns Result.success(Unit)

            viewModel.submitWithdrawal("  탈퇴하겠습니다.  ")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.withdrawalComplete)
        }

    @Test
    fun submitWithdrawal_when400BadRequest_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Bad request"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Unit>(400, errorBody))
            coEvery { withdrawAccountUseCase() } returns Result.failure(httpException)

            viewModel.submitWithdrawal("탈퇴하겠습니다.")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage!!.contains("400"))
            assertFalse(viewModel.uiState.value.withdrawalComplete)
            assertFalse(viewModel.uiState.value.showSentenceError)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun submitWithdrawal_when401Unauthorized_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Unauthorized"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Unit>(401, errorBody))
            coEvery { withdrawAccountUseCase() } returns Result.failure(httpException)

            viewModel.submitWithdrawal("탈퇴하겠습니다.")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage!!.contains("401"))
            assertFalse(viewModel.uiState.value.withdrawalComplete)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun submitWithdrawal_when404NotFound_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":404,"code":404,"message":"Not found"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Unit>(404, errorBody))
            coEvery { withdrawAccountUseCase() } returns Result.failure(httpException)

            viewModel.submitWithdrawal("탈퇴하겠습니다.")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage!!.contains("404"))
            assertFalse(viewModel.uiState.value.withdrawalComplete)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun submitWithdrawal_when500ServerError_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Server error"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Unit>(500, errorBody))
            coEvery { withdrawAccountUseCase() } returns Result.failure(httpException)

            viewModel.submitWithdrawal("탈퇴하겠습니다.")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage!!.contains("500"))
            assertFalse(viewModel.uiState.value.withdrawalComplete)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun submitWithdrawal_whenNetworkError_setsErrorMessage() =
        runTest {
            coEvery { withdrawAccountUseCase() } returns Result.failure(
                java.io.IOException("Network unavailable")
            )

            viewModel.submitWithdrawal("탈퇴하겠습니다.")
            advanceUntilIdle()

            assertTrue(
                viewModel.uiState.value.errorMessage!!.contains("Network") ||
                    viewModel.uiState.value.errorMessage == "회원 탈퇴에 실패했습니다."
            )
            assertFalse(viewModel.uiState.value.withdrawalComplete)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun clearWithdrawalComplete_resetsWithdrawalComplete() =
        runTest {
            coEvery { withdrawAccountUseCase() } returns Result.success(Unit)
            viewModel.submitWithdrawal("탈퇴하겠습니다.")
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.withdrawalComplete)

            viewModel.clearWithdrawalComplete()

            assertFalse(viewModel.uiState.value.withdrawalComplete)
        }

    @Test
    fun clearSentenceError_clearsShowSentenceError() {
        viewModel.submitWithdrawal("잘못된 문장")
        assertTrue(viewModel.uiState.value.showSentenceError)

        viewModel.clearSentenceError()

        assertFalse(viewModel.uiState.value.showSentenceError)
    }
}
