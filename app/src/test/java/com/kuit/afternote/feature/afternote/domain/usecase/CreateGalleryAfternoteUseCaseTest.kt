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

class CreateGalleryAfternoteUseCaseTest {

    private lateinit var repository: AfternoteRepository
    private lateinit var useCase: CreateGalleryAfternoteUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = CreateGalleryAfternoteUseCase(repository)
    }

    @Test
    fun invoke_whenSuccess_returnsId() =
        runTest {
            coEvery {
                repository.createGallery(
                    title = any(),
                    processMethod = any(),
                    actions = any(),
                    leaveMessage = any(),
                    receiverIds = any()
                )
            } returns Result.success(5L)

            val result =
                useCase(
                    title = "가족 사진",
                    processMethod = "DELETE",
                    actions = listOf("사진 백업"),
                    leaveMessage = "소중한 추억들",
                    receiverIds = listOf(1L, 2L)
                )

            assertTrue(result.isSuccess)
            assertEquals(5L, result.getOrNull())
            coVerify(exactly = 1) {
                repository.createGallery(
                    title = "가족 사진",
                    processMethod = "DELETE",
                    actions = listOf("사진 백업"),
                    leaveMessage = "소중한 추억들",
                    receiverIds = listOf(1L, 2L)
                )
            }
        }

    @Test
    fun invoke_whenFailure_returnsFailure() =
        runTest {
            coEvery {
                repository.createGallery(
                    title = any(),
                    processMethod = any(),
                    actions = any(),
                    leaveMessage = any(),
                    receiverIds = any()
                )
            } returns Result.failure(RuntimeException("Bad request"))

            val result =
                useCase(
                    title = "가족 사진",
                    processMethod = "DELETE",
                    actions = emptyList(),
                    leaveMessage = null,
                    receiverIds = emptyList()
                )

            assertTrue(result.isFailure)
            assertEquals("Bad request", result.exceptionOrNull()?.message)
        }
}
