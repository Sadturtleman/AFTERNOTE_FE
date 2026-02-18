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

class CreateSocialAfternoteUseCaseTest {

    private lateinit var repository: AfternoteRepository
    private lateinit var useCase: CreateSocialAfternoteUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = CreateSocialAfternoteUseCase(repository)
    }

    @Test
    fun invoke_whenSuccess_returnsId() =
        runTest {
            coEvery {
                repository.createSocial(
                    title = any(),
                    processMethod = any(),
                    actions = any(),
                    leaveMessage = any(),
                    credentialsId = any(),
                    credentialsPassword = any(),
                    receiverIds = any()
                )
            } returns Result.success(3L)

            val result =
                useCase(
                    title = "인스타그램",
                    processMethod = "MEMORIAL",
                    actions = listOf("게시물 내리기"),
                    leaveMessage = "감사했습니다",
                    credentialsId = "id",
                    credentialsPassword = "pw"
                )

            assertTrue(result.isSuccess)
            assertEquals(3L, result.getOrNull())
            coVerify(exactly = 1) {
                repository.createSocial(
                    title = "인스타그램",
                    processMethod = "MEMORIAL",
                    actions = listOf("게시물 내리기"),
                    leaveMessage = "감사했습니다",
                    credentialsId = "id",
                    credentialsPassword = "pw",
                    receiverIds = emptyList()
                )
            }
        }

    @Test
    fun invoke_whenFailure_returnsFailure() =
        runTest {
            coEvery {
                repository.createSocial(
                    title = any(),
                    processMethod = any(),
                    actions = any(),
                    leaveMessage = any(),
                    credentialsId = any(),
                    credentialsPassword = any(),
                    receiverIds = any()
                )
            } returns Result.failure(RuntimeException("Server error"))

            val result =
                useCase(
                    title = "인스타그램",
                    processMethod = "MEMORIAL",
                    actions = emptyList(),
                    leaveMessage = null,
                    credentialsId = null,
                    credentialsPassword = null
                )

            assertTrue(result.isFailure)
            assertEquals("Server error", result.exceptionOrNull()?.message)
        }
}
