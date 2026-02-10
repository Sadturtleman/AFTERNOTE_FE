package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetail
import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetailReceiver
import com.kuit.afternote.feature.afternote.domain.model.ServiceType
import com.kuit.afternote.feature.afternote.domain.repository.iface.AfternoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetAfternoteDetailUseCaseTest {

    private lateinit var repository: AfternoteRepository
    private lateinit var useCase: GetAfternoteDetailUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetAfternoteDetailUseCase(repository)
    }

    @Test
    fun invoke_whenSuccess_returnsDetail() =
        runTest {
            val detail =
                AfternoteDetail(
                    id = 10L,
                    category = "SOCIAL",
                    title = "인스타그램",
                    createdAt = "2025.11.26",
                    updatedAt = "2025.11.26",
                    type = ServiceType.SOCIAL_NETWORK,
                    credentialsId = "id",
                    credentialsPassword = "pw",
                    receivers = listOf(AfternoteDetailReceiver("수신인", "친구", "010-0000-0000")),
                    processMethod = "MEMORIAL",
                    actions = listOf("게시물 내리기"),
                    leaveMessage = "감사했습니다",
                    playlist = null
                )
            coEvery { repository.getAfternoteDetail(afternoteId = 10L) } returns Result.success(detail)

            val result = useCase(afternoteId = 10L)

            assertTrue(result.isSuccess)
            assertEquals(10L, result.getOrNull()?.id)
            coVerify(exactly = 1) { repository.getAfternoteDetail(afternoteId = 10L) }
        }

    @Test
    fun invoke_whenFailure_returnsFailure() =
        runTest {
            coEvery { repository.getAfternoteDetail(afternoteId = 10L) } returns
                Result.failure(RuntimeException("Not found"))

            val result = useCase(afternoteId = 10L)

            assertTrue(result.isFailure)
            assertEquals("Not found", result.exceptionOrNull()?.message)
        }
}
