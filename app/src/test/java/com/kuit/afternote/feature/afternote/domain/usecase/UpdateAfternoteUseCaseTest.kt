package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.data.dto.AfternoteUpdateRequestDto
import com.kuit.afternote.feature.afternote.domain.repository.iface.AfternoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpdateAfternoteUseCaseTest {

    private lateinit var repository: AfternoteRepository
    private lateinit var useCase: UpdateAfternoteUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = UpdateAfternoteUseCase(repository)
    }

    @Test
    fun invoke_whenSuccess_returnsId() =
        runTest {
            val body =
                AfternoteUpdateRequestDto(
                    title = "수정된 제목",
                    processMethod = "MEMORIAL",
                    actions = listOf("게시물 유지"),
                    leaveMessage = "수정된 메시지",
                    credentials = null,
                    receivers = null,
                    playlist = null
                )
            coEvery {
                repository.updateAfternote(
                    afternoteId = 10L,
                    body = any()
                )
            } returns Result.success(10L)

            val result = useCase(afternoteId = 10L, body = body)

            assertTrue(result.isSuccess)
            assertEquals(10L, result.getOrNull())
            coVerify(exactly = 1) {
                repository.updateAfternote(
                    afternoteId = 10L,
                    body = body
                )
            }
        }

    @Test
    fun invoke_whenFailure_returnsFailure() =
        runTest {
            val body =
                AfternoteUpdateRequestDto(
                    title = "제목",
                    processMethod = null,
                    actions = null,
                    leaveMessage = null,
                    credentials = null,
                    receivers = null,
                    playlist = null
                )
            coEvery {
                repository.updateAfternote(
                    afternoteId = 10L,
                    body = any()
                )
            } returns Result.failure(RuntimeException("404 Not found"))

            val result = useCase(afternoteId = 10L, body = body)

            assertTrue(result.isFailure)
            assertEquals("404 Not found", result.exceptionOrNull()?.message)
        }
}
