package com.kuit.afternote.feature.auth.domain.usecase

import com.kuit.afternote.feature.auth.domain.model.ReissueResult
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
 * [ReissueUseCase] 단위 테스트.
 */
class ReissueUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var reissueUseCase: ReissueUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        reissueUseCase = ReissueUseCase(authRepository)
    }

    @Test
    fun invoke_whenSuccess_returnsReissueResult() =
        runTest {
            val expected = ReissueResult(
                accessToken = "new_access_token",
                refreshToken = "new_refresh_token"
            )
            coEvery { authRepository.reissue(any()) } returns Result.success(expected)

            val result = reissueUseCase("old_refresh_token")

            assertTrue(result.isSuccess)
            assertEquals(expected, result.getOrNull())
            coVerify(exactly = 1) { authRepository.reissue("old_refresh_token") }
        }

    @Test
    fun invoke_whenFailure_returnsFailure() =
        runTest {
            coEvery { authRepository.reissue(any()) } returns Result.failure(RuntimeException("invalid token"))

            val result = reissueUseCase("invalid_token")

            assertTrue(result.isFailure)
            assertEquals("invalid token", result.exceptionOrNull()?.message)
        }
}
