package com.kuit.afternote.feature.auth.domain.usecase

import com.kuit.afternote.feature.auth.domain.model.EmailVerifyResult
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
 * [VerifyEmailUseCase] 단위 테스트.
 */
class VerifyEmailUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var verifyEmailUseCase: VerifyEmailUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        verifyEmailUseCase = VerifyEmailUseCase(authRepository)
    }

    @Test
    fun invoke_whenSuccess_returnsEmailVerifyResult() =
        runTest {
            val expected = EmailVerifyResult(isVerified = true)
            coEvery { authRepository.verifyEmail(any(), any()) } returns Result.success(expected)

            val result = verifyEmailUseCase("a@b.com", "123456")

            assertTrue(result.isSuccess)
            assertEquals(expected, result.getOrNull())
            coVerify(exactly = 1) { authRepository.verifyEmail("a@b.com", "123456") }
        }

    @Test
    fun invoke_whenFailure_returnsFailure() =
        runTest {
            coEvery { authRepository.verifyEmail(any(), any()) } returns Result.failure(RuntimeException("invalid code"))

            val result = verifyEmailUseCase("a@b.com", "000000")

            assertTrue(result.isFailure)
            assertEquals("invalid code", result.exceptionOrNull()?.message)
        }
}
