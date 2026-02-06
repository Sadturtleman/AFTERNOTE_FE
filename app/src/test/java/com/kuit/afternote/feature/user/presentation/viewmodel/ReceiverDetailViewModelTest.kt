package com.kuit.afternote.feature.user.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.kuit.afternote.feature.user.domain.model.ReceiverDetail
import com.kuit.afternote.feature.user.domain.usecase.GetReceiverDetailUseCase
import com.kuit.afternote.feature.user.domain.usecase.GetUserIdUseCase
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

/**
 * [ReceiverDetailViewModel] 단위 테스트.
 * GET /users/receivers/{receiverId}
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ReceiverDetailViewModelTest {
    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var getReceiverDetailUseCase: GetReceiverDetailUseCase
    private lateinit var getUserIdUseCase: GetUserIdUseCase

    @Before
    fun setUp() {
        getReceiverDetailUseCase = mockk()
        getUserIdUseCase = mockk()
        coEvery { getUserIdUseCase() } returns 1L
    }

    private fun savedStateHandle(receiverId: String = ""): SavedStateHandle =
        SavedStateHandle(mapOf("receiverId" to receiverId))

    @Test
    fun init_whenReceiverIdNull_setsPlaceholderName() =
        runTest {
            val savedStateHandle = savedStateHandle(receiverId = "")
            val viewModel = ReceiverDetailViewModel(
                savedStateHandle,
                getReceiverDetailUseCase,
                getUserIdUseCase
            )
            advanceUntilIdle()

            assertEquals("수신인", viewModel.uiState.value.name)
            assertEquals("", viewModel.uiState.value.relation)
        }

    @Test
    fun loadReceiverDetail_whenSuccess_setsDetail() =
        runTest {
            val detail = ReceiverDetail(
                receiverId = 1L,
                name = "김지은",
                relation = "딸",
                phone = "010-1234-1234",
                email = "jieun@naver.com",
                dailyQuestionCount = 8,
                timeLetterCount = 12,
                afterNoteCount = 4
            )
            coEvery { getReceiverDetailUseCase(userId = 1L, receiverId = 1L) } returns Result.success(detail)
            val savedStateHandle = savedStateHandle(receiverId = "1")
            val viewModel = ReceiverDetailViewModel(
                savedStateHandle,
                getReceiverDetailUseCase,
                getUserIdUseCase
            )
            viewModel.loadReceiverDetail(receiverId = 1L)
            advanceUntilIdle()

            assertEquals(1L, viewModel.uiState.value.receiverId)
            assertEquals("김지은", viewModel.uiState.value.name)
            assertEquals("딸", viewModel.uiState.value.relation)
            assertEquals(8, viewModel.uiState.value.dailyQuestionCount)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadReceiverDetail_when401Unauthorized_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Unauthorized"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(401, errorBody))
            coEvery { getReceiverDetailUseCase(userId = any(), receiverId = any()) } returns Result.failure(httpException)
            val savedStateHandle = savedStateHandle(receiverId = "1")
            val viewModel = ReceiverDetailViewModel(
                savedStateHandle,
                getReceiverDetailUseCase,
                getUserIdUseCase
            )
            viewModel.loadReceiverDetail(receiverId = 1L)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("401") == true)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadReceiverDetail_when404NotFound_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":404,"code":404,"message":"Receiver not found"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(404, errorBody))
            coEvery { getReceiverDetailUseCase(userId = any(), receiverId = any()) } returns Result.failure(httpException)
            val savedStateHandle = savedStateHandle(receiverId = "1")
            val viewModel = ReceiverDetailViewModel(
                savedStateHandle,
                getReceiverDetailUseCase,
                getUserIdUseCase
            )
            viewModel.loadReceiverDetail(receiverId = 999L)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("404") == true)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadReceiverDetail_when500ServerError_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Server error"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(500, errorBody))
            coEvery { getReceiverDetailUseCase(userId = any(), receiverId = any()) } returns Result.failure(httpException)
            val savedStateHandle = savedStateHandle(receiverId = "1")
            val viewModel = ReceiverDetailViewModel(
                savedStateHandle,
                getReceiverDetailUseCase,
                getUserIdUseCase
            )
            viewModel.loadReceiverDetail(receiverId = 1L)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("500") == true)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadReceiverDetail_whenNetworkError_setsErrorMessage() =
        runTest {
            coEvery { getReceiverDetailUseCase(userId = any(), receiverId = any()) } returns Result.failure(
                java.io.IOException("Network unavailable")
            )
            val savedStateHandle = savedStateHandle(receiverId = "1")
            val viewModel = ReceiverDetailViewModel(
                savedStateHandle,
                getReceiverDetailUseCase,
                getUserIdUseCase
            )
            viewModel.loadReceiverDetail(receiverId = 1L)
            advanceUntilIdle()

            assertEquals("Network unavailable", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.isLoading)
        }
}
