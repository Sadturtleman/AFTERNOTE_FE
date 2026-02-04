package com.kuit.afternote.feature.user.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.kuit.afternote.feature.user.domain.model.DailyQuestionAnswerItem
import com.kuit.afternote.feature.user.domain.usecase.GetReceiverDailyQuestionsUseCase
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
 * [ReceiverDailyQuestionsViewModel] 단위 테스트.
 * GET /users/receivers/{receiverId}/daily-questions
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ReceiverDailyQuestionsViewModelTest {
    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var getReceiverDailyQuestionsUseCase: GetReceiverDailyQuestionsUseCase

    @Before
    fun setUp() {
        getReceiverDailyQuestionsUseCase = mockk()
    }

    private fun savedStateHandleWithReceiverId(receiverId: String): SavedStateHandle =
        SavedStateHandle(mapOf("receiverId" to receiverId, "receiverName" to ""))

    @Test
    fun loadDailyQuestions_whenSuccess_setsItems() =
        runTest {
            val list = listOf(
                DailyQuestionAnswerItem(
                    dailyQuestionAnswerId = 130L,
                    question = "오늘 하루, 누구에게 가장 고마웠나요?",
                    answer = "아무 말 없이...",
                    createdAt = "2025-10-09"
                )
            )
            coEvery { getReceiverDailyQuestionsUseCase(receiverId = 1L) } returns Result.success(list)
            val savedStateHandle = savedStateHandleWithReceiverId("")
            val viewModel = ReceiverDailyQuestionsViewModel(
                savedStateHandle,
                getReceiverDailyQuestionsUseCase
            )
            viewModel.loadDailyQuestions(receiverId = 1L)
            advanceUntilIdle()

            assertEquals(1, viewModel.uiState.value.items.size)
            assertEquals(130L, viewModel.uiState.value.items[0].dailyQuestionAnswerId)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadDailyQuestions_when401Unauthorized_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Unauthorized"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(401, errorBody))
            coEvery { getReceiverDailyQuestionsUseCase(receiverId = any()) } returns Result.failure(httpException)
            val savedStateHandle = savedStateHandleWithReceiverId("")
            val viewModel = ReceiverDailyQuestionsViewModel(
                savedStateHandle,
                getReceiverDailyQuestionsUseCase
            )
            viewModel.loadDailyQuestions(receiverId = 1L)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("401") == true)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadDailyQuestions_when404NotFound_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":404,"code":404,"message":"Not found"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(404, errorBody))
            coEvery { getReceiverDailyQuestionsUseCase(receiverId = any()) } returns Result.failure(httpException)
            val savedStateHandle = savedStateHandleWithReceiverId("")
            val viewModel = ReceiverDailyQuestionsViewModel(
                savedStateHandle,
                getReceiverDailyQuestionsUseCase
            )
            viewModel.loadDailyQuestions(receiverId = 1L)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("404") == true)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadDailyQuestions_when500ServerError_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Server error"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(500, errorBody))
            coEvery { getReceiverDailyQuestionsUseCase(receiverId = any()) } returns Result.failure(httpException)
            val savedStateHandle = savedStateHandleWithReceiverId("")
            val viewModel = ReceiverDailyQuestionsViewModel(
                savedStateHandle,
                getReceiverDailyQuestionsUseCase
            )
            viewModel.loadDailyQuestions(receiverId = 1L)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("500") == true)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadDailyQuestions_whenNetworkError_setsErrorMessage() =
        runTest {
            coEvery { getReceiverDailyQuestionsUseCase(receiverId = any()) } returns Result.failure(
                java.io.IOException("Network unavailable")
            )
            val savedStateHandle = savedStateHandleWithReceiverId("")
            val viewModel = ReceiverDailyQuestionsViewModel(
                savedStateHandle,
                getReceiverDailyQuestionsUseCase
            )
            viewModel.loadDailyQuestions(receiverId = 1L)
            advanceUntilIdle()

            assertEquals("Network unavailable", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.isLoading)
        }
}
