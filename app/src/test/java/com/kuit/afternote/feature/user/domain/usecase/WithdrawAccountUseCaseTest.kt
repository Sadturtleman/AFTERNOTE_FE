package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.feature.user.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * [WithdrawAccountUseCase] 단위 테스트.
 *
 * DELETE /users/me (회원 탈퇴) API 호출 검증.
 */
class WithdrawAccountUseCaseTest {
    private lateinit var userRepository: UserRepository
    private lateinit var withdrawAccountUseCase: WithdrawAccountUseCase

    @Before
    fun setUp() {
        userRepository = mockk()
        withdrawAccountUseCase = WithdrawAccountUseCase(userRepository)
    }

    @Test
    fun invoke_whenSuccess_returnsUnit() =
        runTest {
            coEvery { userRepository.withdrawAccount() } returns Result.success(Unit)

            val result = withdrawAccountUseCase()

            assertTrue(result.isSuccess)
            coVerify(exactly = 1) { userRepository.withdrawAccount() }
        }

    @Test
    fun invoke_whenFailure_returnsFailure() =
        runTest {
            coEvery { userRepository.withdrawAccount() } returns Result.failure(
                RuntimeException("withdraw failed")
            )

            val result = withdrawAccountUseCase()

            assertFalse(result.isSuccess)
            assertTrue(result.exceptionOrNull()?.message == "withdraw failed")
        }
}
