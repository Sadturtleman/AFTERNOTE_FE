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
 * [PasswordChangeUseCase] 단위 테스트.
 */
class PasswordChangeUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var passwordChangeUseCase: PasswordChangeUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        passwordChangeUseCase = PasswordChangeUseCase(authRepository)
    }

    @Test
    fun invoke_whenSuccess_returnsUnit() = runTest {
        coEvery { authRepository.passwordChange(any(), any()) } returns Result.success(Unit)

        val result = passwordChangeUseCase("oldPwd123!", "newPwd456!")

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { authRepository.passwordChange("oldPwd123!", "newPwd456!") }
    }

    @Test
    fun invoke_whenFailure_returnsFailure() = runTest {
        coEvery { authRepository.passwordChange(any(), any()) } returns Result.failure(RuntimeException("wrong password"))

        val result = passwordChangeUseCase("wrongPwd", "newPwd")

        assertTrue(result.isFailure)
        assertEquals("wrong password", result.exceptionOrNull()?.message)
    }
}
