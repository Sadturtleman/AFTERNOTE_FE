package com.kuit.afternote.feature.auth.domain.usecase

import com.kuit.afternote.feature.auth.domain.model.LoginResult
import com.kuit.afternote.feature.auth.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

/**
 * [LoginUseCase] 단위 테스트.
 */
class LoginUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        loginUseCase = LoginUseCase(authRepository)
    }

    @Test
    fun invoke_whenSuccess_returnsLoginResult() = runTest {
        val expected = LoginResult(accessToken = "at", refreshToken = "rt")
        coEvery { authRepository.login(any(), any()) } returns Result.success(expected)

        val result = loginUseCase("a@b.com", "pwd123")

        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrNull())
        coVerify(exactly = 1) { authRepository.login("a@b.com", "pwd123") }
    }

    @Test
    fun invoke_whenFailure_returnsFailure() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.failure(RuntimeException("invalid"))

        val result = loginUseCase("a@b.com", "wrong")

        assertTrue(result.isFailure)
        assertEquals("invalid", result.exceptionOrNull()?.message)
    }

    // ========== HTTP Error Cases ==========

    @Test
    fun invoke_when404NotFound_returnsFailureWithHttpException() = runTest {
        val errorBody = """{"status":404,"code":404,"message":"User not found"}"""
            .toResponseBody("application/json".toMediaType())
        val httpException = HttpException(Response.error<LoginResult>(404, errorBody))
        coEvery { authRepository.login(any(), any()) } returns Result.failure(httpException)

        val result = loginUseCase("nonexistent@example.com", "password123!")

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is HttpException)
        assertEquals(404, (exception as HttpException).code())
        coVerify(exactly = 1) { authRepository.login("nonexistent@example.com", "password123!") }
    }

    @Test
    fun invoke_when401Unauthorized_returnsFailureWithHttpException() = runTest {
        val errorBody = """{"status":401,"code":401,"message":"Invalid credentials"}"""
            .toResponseBody("application/json".toMediaType())
        val httpException = HttpException(Response.error<LoginResult>(401, errorBody))
        coEvery { authRepository.login(any(), any()) } returns Result.failure(httpException)

        val result = loginUseCase("test@example.com", "wrongPassword")

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is HttpException)
        assertEquals(401, (exception as HttpException).code())
    }

    @Test
    fun invoke_when400BadRequest_returnsFailureWithHttpException() = runTest {
        val errorBody = """{"status":400,"code":400,"message":"Invalid email format"}"""
            .toResponseBody("application/json".toMediaType())
        val httpException = HttpException(Response.error<LoginResult>(400, errorBody))
        coEvery { authRepository.login(any(), any()) } returns Result.failure(httpException)

        val result = loginUseCase("invalid-email", "password123!")

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is HttpException)
        assertEquals(400, (exception as HttpException).code())
    }

    @Test
    fun invoke_when500ServerError_returnsFailureWithHttpException() = runTest {
        val errorBody = """{"status":500,"code":500,"message":"Internal server error"}"""
            .toResponseBody("application/json".toMediaType())
        val httpException = HttpException(Response.error<LoginResult>(500, errorBody))
        coEvery { authRepository.login(any(), any()) } returns Result.failure(httpException)

        val result = loginUseCase("test@example.com", "password123!")

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is HttpException)
        assertEquals(500, (exception as HttpException).code())
    }

    // ========== Network Error Cases ==========

    @Test
    fun invoke_whenNetworkError_returnsFailure() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.failure(
            java.io.IOException("Network unavailable")
        )

        val result = loginUseCase("test@example.com", "password123!")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is java.io.IOException)
    }
}
