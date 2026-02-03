package com.kuit.afternote.feature.user.presentation.viewmodel

import com.kuit.afternote.feature.user.domain.model.ReceiverTimeLetterItem
import com.kuit.afternote.feature.user.domain.usecase.GetReceiverTimeLettersUseCase
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
 * [ReceiverTimeLettersViewModel] 단위 테스트.
 * GET /users/receivers/{receiverId}/time-letters
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ReceiverTimeLettersViewModelTest {
    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var getReceiverTimeLettersUseCase: GetReceiverTimeLettersUseCase
    private lateinit var viewModel: ReceiverTimeLettersViewModel

    @Before
    fun setUp() {
        getReceiverTimeLettersUseCase = mockk()
        viewModel = ReceiverTimeLettersViewModel(getReceiverTimeLettersUseCase)
    }

    @Test
    fun loadTimeLetters_whenSuccess_setsItems() =
        runTest {
            val list = listOf(
                ReceiverTimeLetterItem(
                    timeLetterId = 12L,
                    receiverName = "박채연",
                    sendAt = "2027-11-24",
                    title = "채연아 20번째 생일을 축하해",
                    content = "네가 태어난 게 엊그제 같은데..."
                )
            )
            coEvery { getReceiverTimeLettersUseCase(receiverId = 1L) } returns Result.success(list)

            viewModel.loadTimeLetters(receiverId = 1L)
            advanceUntilIdle()

            assertEquals(1, viewModel.uiState.value.items.size)
            assertEquals(12L, viewModel.uiState.value.items[0].timeLetterId)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadTimeLetters_when401Unauthorized_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Unauthorized"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(401, errorBody))
            coEvery { getReceiverTimeLettersUseCase(receiverId = any()) } returns Result.failure(httpException)

            viewModel.loadTimeLetters(receiverId = 1L)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("401") == true)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadTimeLetters_when404NotFound_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":404,"code":404,"message":"Not found"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(404, errorBody))
            coEvery { getReceiverTimeLettersUseCase(receiverId = any()) } returns Result.failure(httpException)

            viewModel.loadTimeLetters(receiverId = 1L)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("404") == true)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadTimeLetters_when500ServerError_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Server error"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(500, errorBody))
            coEvery { getReceiverTimeLettersUseCase(receiverId = any()) } returns Result.failure(httpException)

            viewModel.loadTimeLetters(receiverId = 1L)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("500") == true)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadTimeLetters_whenNetworkError_setsErrorMessage() =
        runTest {
            coEvery { getReceiverTimeLettersUseCase(receiverId = any()) } returns Result.failure(
                java.io.IOException("Network unavailable")
            )

            viewModel.loadTimeLetters(receiverId = 1L)
            advanceUntilIdle()

            assertEquals("Network unavailable", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.isLoading)
        }
}
