package com.kuit.afternote.feature.auth.domain.usecase

import com.kuit.afternote.feature.auth.domain.model.LoginResult
import com.kuit.afternote.feature.auth.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

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
}
