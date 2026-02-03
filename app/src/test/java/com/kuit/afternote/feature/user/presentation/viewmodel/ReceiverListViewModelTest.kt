package com.kuit.afternote.feature.user.presentation.viewmodel

import com.kuit.afternote.feature.user.domain.model.ReceiverListItem
import com.kuit.afternote.feature.user.domain.usecase.GetReceiversUseCase
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
 * [ReceiverListViewModel] 단위 테스트.
 * GET /users/receivers
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ReceiverListViewModelTest {
    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var getReceiversUseCase: GetReceiversUseCase
    private lateinit var viewModel: ReceiverListViewModel

    @Before
    fun setUp() {
        getReceiversUseCase = mockk()
        viewModel = ReceiverListViewModel(getReceiversUseCase)
    }

    @Test
    fun loadReceivers_whenSuccess_setsReceivers() =
        runTest {
            val list = listOf(
                ReceiverListItem(receiverId = 1L, name = "김지은", relation = "딸"),
                ReceiverListItem(receiverId = 2L, name = "김혜성", relation = "아들")
            )
            coEvery { getReceiversUseCase() } returns Result.success(list)

            viewModel.loadReceivers()
            advanceUntilIdle()

            assertEquals(2, viewModel.uiState.value.receivers.size)
            assertEquals(1L, viewModel.uiState.value.receivers[0].receiverId)
            assertEquals("김지은", viewModel.uiState.value.receivers[0].name)
            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(null, viewModel.uiState.value.errorMessage)
        }

    @Test
    fun loadReceivers_when400BadRequest_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Bad request"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(400, errorBody))
            coEvery { getReceiversUseCase() } returns Result.failure(httpException)

            viewModel.loadReceivers()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("400") == true)
            assertTrue(viewModel.uiState.value.receivers.isEmpty())
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadReceivers_when401Unauthorized_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Unauthorized"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(401, errorBody))
            coEvery { getReceiversUseCase() } returns Result.failure(httpException)

            viewModel.loadReceivers()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("401") == true)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadReceivers_when404NotFound_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":404,"code":404,"message":"Not found"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(404, errorBody))
            coEvery { getReceiversUseCase() } returns Result.failure(httpException)

            viewModel.loadReceivers()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("404") == true)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadReceivers_when500ServerError_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Server error"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(500, errorBody))
            coEvery { getReceiversUseCase() } returns Result.failure(httpException)

            viewModel.loadReceivers()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("500") == true)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadReceivers_whenNetworkError_setsErrorMessage() =
        runTest {
            coEvery { getReceiversUseCase() } returns Result.failure(
                java.io.IOException("Network unavailable")
            )

            viewModel.loadReceivers()
            advanceUntilIdle()

            assertEquals("Network unavailable", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun clearError_clearsErrorMessage() =
        runTest {
            coEvery { getReceiversUseCase() } returns Result.failure(
                java.io.IOException("Network error")
            )
            viewModel.loadReceivers()
            advanceUntilIdle()
            assertEquals("Network error", viewModel.uiState.value.errorMessage)

            viewModel.clearError()

            assertEquals(null, viewModel.uiState.value.errorMessage)
        }
}
