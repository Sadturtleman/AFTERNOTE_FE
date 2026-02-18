package com.kuit.afternote.feature.receiverauth.domain.usecase

import com.kuit.afternote.feature.receiverauth.domain.entity.DeliveryVerificationStatus
import com.kuit.afternote.feature.receiverauth.domain.repository.iface.GetDeliveryVerificationStatusRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * [GetDeliveryVerificationStatusUseCase] 단위 테스트.
 */
class GetDeliveryVerificationStatusUseCaseTest {

    private lateinit var getDeliveryVerificationStatusRepository: GetDeliveryVerificationStatusRepository
    private lateinit var useCase: GetDeliveryVerificationStatusUseCase

    @Before
    fun setUp() {
        getDeliveryVerificationStatusRepository = mockk()
        useCase = GetDeliveryVerificationStatusUseCase(getDeliveryVerificationStatusRepository)
    }

    @Test
    fun invoke_whenSuccess_returnsDeliveryVerificationStatus() = runTest {
        val expected = DeliveryVerificationStatus(
            id = 1L,
            status = "PENDING",
            adminNote = null,
            createdAt = "2025-01-01T00:00:00"
        )
        coEvery { getDeliveryVerificationStatusRepository.getStatus(any()) } returns Result.success(expected)

        val result = useCase("auth-code")

        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrNull())
        coVerify(exactly = 1) { getDeliveryVerificationStatusRepository.getStatus("auth-code") }
    }

    @Test
    fun invoke_whenFailure_returnsFailure() = runTest {
        coEvery { getDeliveryVerificationStatusRepository.getStatus(any()) } returns
            Result.failure(RuntimeException("Network error"))

        val result = useCase("auth-code")

        assertTrue(result.isFailure)
    }
}
