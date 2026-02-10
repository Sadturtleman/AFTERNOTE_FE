package com.kuit.afternote.feature.afternote.presentation.screen

import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetail
import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetailReceiver
import com.kuit.afternote.feature.afternote.domain.model.ServiceType
import com.kuit.afternote.feature.afternote.domain.usecase.DeleteAfternoteUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.GetAfternoteDetailUseCase
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

@OptIn(ExperimentalCoroutinesApi::class)
class AfternoteDetailViewModelTest {

    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var getDetailUseCase: GetAfternoteDetailUseCase
    private lateinit var deleteUseCase: DeleteAfternoteUseCase
    private lateinit var viewModel: AfternoteDetailViewModel

    private val sampleDetail =
        AfternoteDetail(
            id = 10L,
            category = "SOCIAL",
            title = "인스타그램",
            createdAt = "2025.11.26",
            updatedAt = "2025.11.26",
            type = ServiceType.SOCIAL_NETWORK,
            credentialsId = "id",
            credentialsPassword = "pw",
            receivers = listOf(AfternoteDetailReceiver("수신인", "친구", "010-0000-0000")),
            processMethod = "MEMORIAL",
            actions = listOf("게시물 내리기"),
            leaveMessage = "감사했습니다",
            playlist = null
        )

    @Before
    fun setUp() {
        getDetailUseCase = mockk()
        deleteUseCase = mockk()
        viewModel = AfternoteDetailViewModel(getDetailUseCase, deleteUseCase)
    }

    @Test
    fun loadDetail_whenSuccess_setsDetail() =
        runTest {
            coEvery { getDetailUseCase(afternoteId = 10L) } returns Result.success(sampleDetail)

            viewModel.loadDetail(10L)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(sampleDetail, viewModel.uiState.value.detail)
            assertNull(viewModel.uiState.value.error)
        }

    @Test
    fun loadDetail_whenFailure_setsError() =
        runTest {
            coEvery { getDetailUseCase(afternoteId = 10L) } returns
                Result.failure(RuntimeException("Not found"))

            viewModel.loadDetail(10L)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertNull(viewModel.uiState.value.detail)
            assertTrue(viewModel.uiState.value.error?.contains("Not found") == true)
        }

    @Test
    fun loadDetail_when400BadRequest_setsError() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Bad request"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<AfternoteDetail>(400, errorBody))
            coEvery { getDetailUseCase(afternoteId = 10L) } returns Result.failure(httpException)

            viewModel.loadDetail(10L)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertTrue(viewModel.uiState.value.error?.contains("400") == true)
        }

    @Test
    fun loadDetail_when401Unauthorized_setsError() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Unauthorized"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<AfternoteDetail>(401, errorBody))
            coEvery { getDetailUseCase(afternoteId = 10L) } returns Result.failure(httpException)

            viewModel.loadDetail(10L)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertTrue(viewModel.uiState.value.error?.contains("401") == true)
        }

    @Test
    fun loadDetail_when404NotFound_setsError() =
        runTest {
            val errorBody = """{"status":404,"code":404,"message":"Not found"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<AfternoteDetail>(404, errorBody))
            coEvery { getDetailUseCase(afternoteId = 10L) } returns Result.failure(httpException)

            viewModel.loadDetail(10L)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertTrue(viewModel.uiState.value.error?.contains("404") == true)
        }

    @Test
    fun loadDetail_when500ServerError_setsError() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Server error"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<AfternoteDetail>(500, errorBody))
            coEvery { getDetailUseCase(afternoteId = 10L) } returns Result.failure(httpException)

            viewModel.loadDetail(10L)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertTrue(viewModel.uiState.value.error?.contains("500") == true)
        }

    @Test
    fun loadDetail_whenNetworkError_setsError() =
        runTest {
            coEvery { getDetailUseCase(afternoteId = 10L) } returns
                Result.failure(java.io.IOException("Network unavailable"))

            viewModel.loadDetail(10L)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals("Network unavailable", viewModel.uiState.value.error)
        }

    @Test
    fun deleteAfternote_whenSuccess_setsDeleteSuccess() =
        runTest {
            coEvery { deleteUseCase(afternoteId = 10L) } returns Result.success(Unit)

            viewModel.deleteAfternote(10L)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isDeleting)
            assertTrue(viewModel.uiState.value.deleteSuccess)
            assertNull(viewModel.uiState.value.deleteError)
        }

    @Test
    fun deleteAfternote_whenFailure_setsDeleteError() =
        runTest {
            coEvery { deleteUseCase(afternoteId = 10L) } returns
                Result.failure(RuntimeException("Forbidden"))

            viewModel.deleteAfternote(10L)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isDeleting)
            assertFalse(viewModel.uiState.value.deleteSuccess)
            assertTrue(viewModel.uiState.value.deleteError?.contains("Forbidden") == true)
        }

    @Test
    fun deleteAfternote_when404NotFound_setsDeleteError() =
        runTest {
            val errorBody = """{"status":404,"code":404,"message":"Not found"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Unit>(404, errorBody))
            coEvery { deleteUseCase(afternoteId = 10L) } returns Result.failure(httpException)

            viewModel.deleteAfternote(10L)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isDeleting)
            assertFalse(viewModel.uiState.value.deleteSuccess)
            assertTrue(viewModel.uiState.value.deleteError?.contains("404") == true)
        }

    @Test
    fun deleteAfternote_when500ServerError_setsDeleteError() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Server error"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Unit>(500, errorBody))
            coEvery { deleteUseCase(afternoteId = 10L) } returns Result.failure(httpException)

            viewModel.deleteAfternote(10L)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isDeleting)
            assertFalse(viewModel.uiState.value.deleteSuccess)
            assertTrue(viewModel.uiState.value.deleteError?.contains("500") == true)
        }

    @Test
    fun deleteAfternote_whenNetworkError_setsDeleteError() =
        runTest {
            coEvery { deleteUseCase(afternoteId = 10L) } returns
                Result.failure(java.io.IOException("Network unavailable"))

            viewModel.deleteAfternote(10L)
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isDeleting)
            assertEquals("Network unavailable", viewModel.uiState.value.deleteError)
        }

    @Test
    fun clearDeleteResult_resetsDeleteFlags() =
        runTest {
            coEvery { deleteUseCase(afternoteId = 10L) } returns Result.success(Unit)
            viewModel.deleteAfternote(10L)
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.deleteSuccess)

            viewModel.clearDeleteResult()

            assertFalse(viewModel.uiState.value.deleteSuccess)
            assertNull(viewModel.uiState.value.deleteError)
        }
}
