package com.kuit.afternote.feature.auth.domain.usecase

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
 * [LogoutUseCase] 단위 테스트.
 */
class LogoutUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var logoutUseCase: LogoutUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        logoutUseCase = LogoutUseCase(authRepository)
    }

    @Test
    fun invoke_whenSuccess_returnsUnit() = runTest {
        coEvery { authRepository.logout(any()) } returns Result.success(Unit)

        val result = logoutUseCase("refresh_token")

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { authRepository.logout("refresh_token") }
    }

    @Test
    fun invoke_whenFailure_returnsFailure() = runTest {
        coEvery { authRepository.logout(any()) } returns Result.failure(RuntimeException("logout failed"))

        val result = logoutUseCase("invalid_token")

        assertTrue(result.isFailure)
        assertEquals("logout failed", result.exceptionOrNull()?.message)
    }
}
