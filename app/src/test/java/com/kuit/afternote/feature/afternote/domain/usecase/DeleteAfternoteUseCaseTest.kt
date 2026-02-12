package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.domain.repository.iface.AfternoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteAfternoteUseCaseTest {

    private lateinit var repository: AfternoteRepository
    private lateinit var useCase: DeleteAfternoteUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = DeleteAfternoteUseCase(repository)
    }

    @Test
    fun invoke_whenSuccess_returnsUnit() =
        runTest {
            coEvery { repository.deleteAfternote(afternoteId = 10L) } returns Result.success(Unit)

            val result = useCase(afternoteId = 10L)

            assertTrue(result.isSuccess)
            coVerify(exactly = 1) { repository.deleteAfternote(afternoteId = 10L) }
        }

    @Test
    fun invoke_whenFailure_returnsFailure() =
        runTest {
            coEvery { repository.deleteAfternote(afternoteId = 10L) } returns
                Result.failure(RuntimeException("Forbidden"))

            val result = useCase(afternoteId = 10L)

            assertTrue(result.isFailure)
            assertEquals("Forbidden", result.exceptionOrNull()?.message)
        }
}
