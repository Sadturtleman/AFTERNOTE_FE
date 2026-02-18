package com.kuit.afternote.feature.receiverauth.domain.usecase

import com.kuit.afternote.feature.receiverauth.domain.repository.iface.SubmitDeliveryVerificationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * [SubmitDeliveryVerificationUseCase] 단위 테스트.
 */
class SubmitDeliveryVerificationUseCaseTest {

    private lateinit var submitDeliveryVerificationRepository: SubmitDeliveryVerificationRepository
    private lateinit var useCase: SubmitDeliveryVerificationUseCase

    @Before
    fun setUp() {
        submitDeliveryVerificationRepository = mockk()
        useCase = SubmitDeliveryVerificationUseCase(submitDeliveryVerificationRepository)
    }

    @Test
    fun invoke_whenSuccess_returnsSuccess() = runTest {
        coEvery {
            submitDeliveryVerificationRepository.submit(any(), any(), any())
        } returns Result.success(Unit)

        val result = useCase("auth-code", "https://death.pdf", "https://family.pdf")

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) {
            submitDeliveryVerificationRepository.submit(
                "auth-code",
                "https://death.pdf",
                "https://family.pdf"
            )
        }
    }

    @Test
    fun invoke_whenFailure_returnsFailure() = runTest {
        coEvery {
            submitDeliveryVerificationRepository.submit(any(), any(), any())
        } returns Result.failure(RuntimeException("Server error"))

        val result = useCase("auth-code", "url1", "url2")

        assertTrue(result.isFailure)
    }
}
