package com.kuit.afternote.feature.user.presentation.viewmodel

import com.kuit.afternote.feature.user.domain.usecase.RegisterReceiverUseCase
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
 * [RegisterReceiverViewModel] 단위 테스트.
 * POST /users/receivers
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RegisterReceiverViewModelTest {
    @get:Rule
    val mainRule = MainCoroutineRule()

    private lateinit var registerReceiverUseCase: RegisterReceiverUseCase
    private lateinit var viewModel: RegisterReceiverViewModel

    @Before
    fun setUp() {
        registerReceiverUseCase = mockk()
        viewModel = RegisterReceiverViewModel(registerReceiverUseCase)
    }

    @Test
    fun registerReceiver_whenSuccess_setsRegisteredReceiverId() =
        runTest {
            coEvery {
                registerReceiverUseCase(
                    name = any(),
                    relation = any(),
                    phone = any(),
                    email = any()
                )
            } returns Result.success(1L)

            viewModel.registerReceiver(name = "김지은", relation = "딸")
            advanceUntilIdle()

            assertEquals(1L, viewModel.uiState.value.registeredReceiverId)
            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(null, viewModel.uiState.value.errorMessage)
        }

    @Test
    fun registerReceiver_when400BadRequest_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":400,"code":400,"message":"Validation failed"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(400, errorBody))
            coEvery {
                registerReceiverUseCase(name = any(), relation = any(), phone = any(), email = any())
            } returns Result.failure(httpException)

            viewModel.registerReceiver(name = "김지은", relation = "DAUGHTER")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("400") == true)
            assertEquals(null, viewModel.uiState.value.registeredReceiverId)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun registerReceiver_when401Unauthorized_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":401,"code":401,"message":"Unauthorized"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(401, errorBody))
            coEvery {
                registerReceiverUseCase(name = any(), relation = any(), phone = any(), email = any())
            } returns Result.failure(httpException)

            viewModel.registerReceiver(name = "김지은", relation = "딸")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("401") == true)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun registerReceiver_when500ServerError_setsErrorMessage() =
        runTest {
            val errorBody = """{"status":500,"code":500,"message":"Server error"}"""
                .toResponseBody("application/json".toMediaType())
            val httpException = HttpException(Response.error<Any>(500, errorBody))
            coEvery {
                registerReceiverUseCase(name = any(), relation = any(), phone = any(), email = any())
            } returns Result.failure(httpException)

            viewModel.registerReceiver(name = "김지은", relation = "딸")
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.errorMessage?.contains("500") == true)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun registerReceiver_whenNetworkError_setsErrorMessage() =
        runTest {
            coEvery {
                registerReceiverUseCase(name = any(), relation = any(), phone = any(), email = any())
            } returns Result.failure(java.io.IOException("Network unavailable"))

            viewModel.registerReceiver(name = "김지은", relation = "딸")
            advanceUntilIdle()

            assertEquals("Network unavailable", viewModel.uiState.value.errorMessage)
            assertFalse(viewModel.uiState.value.isLoading)
        }
}
