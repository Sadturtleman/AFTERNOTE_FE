package com.kuit.afternote.feature.user.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.kuit.afternote.feature.user.domain.model.ReceiverAfterNoteSourceItem
import com.kuit.afternote.feature.user.domain.usecase.GetReceiverAfterNotesUseCase
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
 * [ReceiverAfterNotesViewModel] 단위 테스트.
 * GET /users/receivers/{receiverId}/after-notes
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ReceiverAfterNotesViewModelTest {
    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var getReceiverAfterNotesUseCase: GetReceiverAfterNotesUseCase

    @Before
    fun setUp() {
        getReceiverAfterNotesUseCase = mockk()
    }

    private fun savedStateHandleWithReceiverId(receiverId: String): SavedStateHandle =
        SavedStateHandle(mapOf("receiverId" to receiverId, "receiverName" to ""))

    @Test
    fun loadAfterNotes_whenSuccess_setsItems() =
        runTest {
            val list = listOf(
                ReceiverAfterNoteSourceItem(sourceType = "INSTAGRAM", lastUpdatedAt = "2025-11-26"),
                ReceiverAfterNoteSourceItem(sourceType = "GALLERY", lastUpdatedAt = "2025-11-26")
            )
            coEvery { getReceiverAfterNotesUseCase(receiverId = 1L) } returns Result.success(list)
            val savedStateHandle = savedStateHandleWithReceiverId("")
            val viewModel = ReceiverAfterNotesViewModel(
                savedStateHandle,
                getReceiverAfterNotesUseCase
            )
            viewModel.loadAfterNotes(receiverId = 1L)
            advanceUntilIdle()

            assertEquals(2, viewModel.uiState.value.items.size)
            assertEquals("INSTAGRAM", viewModel.uiState.value.items[0].sourceType)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadAfterNotes_when401Unauthorized_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Unauthorized"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(401, errorBody))
            coEvery { getReceiverAfterNotesUseCase(receiverId = any()) } returns Result.failure(httpException)
            val savedStateHandle = savedStateHandleWithReceiverId("")
            val viewModel = ReceiverAfterNotesViewModel(
                savedStateHandle,
                getReceiverAfterNotesUseCase
            )
            viewModel.loadAfterNotes(receiverId = 1L)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("401") == true)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadAfterNotes_when404NotFound_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":404,"code":404,"message":"Not found"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(404, errorBody))
            coEvery { getReceiverAfterNotesUseCase(receiverId = any()) } returns Result.failure(httpException)
            val savedStateHandle = savedStateHandleWithReceiverId("")
            val viewModel = ReceiverAfterNotesViewModel(
                savedStateHandle,
                getReceiverAfterNotesUseCase
            )
            viewModel.loadAfterNotes(receiverId = 1L)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("404") == true)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadAfterNotes_when500ServerError_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Server error"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(500, errorBody))
            coEvery { getReceiverAfterNotesUseCase(receiverId = any()) } returns Result.failure(httpException)
            val savedStateHandle = savedStateHandleWithReceiverId("")
            val viewModel = ReceiverAfterNotesViewModel(
                savedStateHandle,
                getReceiverAfterNotesUseCase
            )
            viewModel.loadAfterNotes(receiverId = 1L)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("500") == true)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun loadAfterNotes_whenNetworkError_setsErrorMessage() =
        runTest {
            coEvery { getReceiverAfterNotesUseCase(receiverId = any()) } returns Result.failure(
                java.io.IOException("Network unavailable")
            )
            val savedStateHandle = savedStateHandleWithReceiverId("")
            val viewModel = ReceiverAfterNotesViewModel(
                savedStateHandle,
                getReceiverAfterNotesUseCase
            )
            viewModel.loadAfterNotes(receiverId = 1L)
            advanceUntilIdle()

            assertEquals("Network unavailable", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.isLoading)
        }
}
