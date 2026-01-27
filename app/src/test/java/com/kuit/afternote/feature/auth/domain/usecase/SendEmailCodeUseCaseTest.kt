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
 * [SendEmailCodeUseCase] 단위 테스트.
 */
class SendEmailCodeUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var sendEmailCodeUseCase: SendEmailCodeUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        sendEmailCodeUseCase = SendEmailCodeUseCase(authRepository)
    }

    @Test
    fun invoke_whenSuccess_returnsUnit() = runTest {
        coEvery { authRepository.sendEmailCode(any()) } returns Result.success(Unit)

        val result = sendEmailCodeUseCase("a@b.com")

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { authRepository.sendEmailCode("a@b.com") }
    }

    @Test
    fun invoke_whenFailure_returnsFailure() = runTest {
        coEvery { authRepository.sendEmailCode(any()) } returns Result.failure(RuntimeException("rate limit"))

        val result = sendEmailCodeUseCase("a@b.com")

        assertTrue(result.isFailure)
        assertEquals("rate limit", result.exceptionOrNull()?.message)
    }
}
