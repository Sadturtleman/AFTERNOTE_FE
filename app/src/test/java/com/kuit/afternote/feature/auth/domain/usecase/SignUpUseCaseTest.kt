package com.kuit.afternote.feature.auth.domain.usecase

import com.kuit.afternote.feature.auth.domain.model.SignUpResult
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
 * [SignUpUseCase] 단위 테스트.
 */
class SignUpUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var signUpUseCase: SignUpUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        signUpUseCase = SignUpUseCase(authRepository)
    }

    @Test
    fun invoke_whenSuccess_returnsSignUpResult() =
        runTest {
            val expected = SignUpResult(userId = 1L, email = "a@b.com")
            coEvery { authRepository.signUp(any(), any(), any(), any()) } returns Result.success(expected)

            val result = signUpUseCase("a@b.com", "pwd1!", "name", null)

            assertTrue(result.isSuccess)
            assertEquals(expected, result.getOrNull())
            coVerify(exactly = 1) { authRepository.signUp("a@b.com", "pwd1!", "name", null) }
        }

    @Test
    fun invoke_withProfileUrl_callsRepositoryWithProfileUrl() =
        runTest {
            coEvery { authRepository.signUp(any(), any(), any(), any()) } returns Result.success(SignUpResult(2L, "b@c.com"))

            signUpUseCase("b@c.com", "pwd2!", "nick", "https://img.url/p.jpg")

            coVerify(exactly = 1) { authRepository.signUp("b@c.com", "pwd2!", "nick", "https://img.url/p.jpg") }
        }

    @Test
    fun invoke_whenFailure_returnsFailure() =
        runTest {
            coEvery { authRepository.signUp(any(), any(), any(), any()) } returns Result.failure(RuntimeException("email exists"))

            val result = signUpUseCase("a@b.com", "pwd", "n", null)

            assertTrue(result.isFailure)
            assertEquals("email exists", result.exceptionOrNull()?.message)
        }
}
