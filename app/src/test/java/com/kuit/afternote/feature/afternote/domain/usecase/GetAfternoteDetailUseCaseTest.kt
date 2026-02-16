package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetail
import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetailReceiver
import com.kuit.afternote.core.domain.model.AfternoteServiceType
import com.kuit.afternote.feature.afternote.domain.repository.iface.AfternoteRepository
import com.kuit.afternote.feature.user.domain.model.ReceiverListItem
import com.kuit.afternote.feature.user.domain.usecase.GetReceiversUseCase
import com.kuit.afternote.feature.user.domain.usecase.GetUserIdUseCase
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
    private lateinit var getReceiversUseCase: GetReceiversUseCase
    private lateinit var getUserIdUseCase: GetUserIdUseCase
    private lateinit var useCase: GetAfternoteDetailUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getReceiversUseCase = mockk()
        getUserIdUseCase = mockk()
        useCase = GetAfternoteDetailUseCase(
            repository = repository,
            getReceiversUseCase = getReceiversUseCase,
            getUserIdUseCase = getUserIdUseCase
        )
    }

    @Test
    fun invoke_whenSuccess_resolvesReceiverNamesAndReturnsDetail() =
        runTest {
            val detail =
                AfternoteDetail(
                    id = 10L,
                    category = "GALLERY",
                    title = "갤러리",
                    createdAt = "2025.11.26",
                    updatedAt = "2025.11.26",
                    type = AfternoteServiceType.GALLERY_AND_FILES,
                    credentialsId = null,
                    credentialsPassword = null,
                    receivers = listOf(
                        AfternoteDetailReceiver(
                            receiverId = 1L,
                            name = "",
                            relation = "",
                            phone = ""
                        )
                    ),
                    processMethod = "TRANSFER",
                    actions = emptyList(),
                    leaveMessage = null,
                    playlist = null
                )
            val receiversList = listOf(
                ReceiverListItem(receiverId = 1L, name = "김수신", relation = "친구")
            )
            coEvery { repository.getAfternoteDetail(afternoteId = 10L) } returns Result.success(detail)
            coEvery { getUserIdUseCase() } returns 100L
            coEvery { getReceiversUseCase(userId = 100L) } returns Result.success(receiversList)

            val result = useCase(afternoteId = 10L)

            assertTrue(result.isSuccess)
            assertEquals(10L, result.getOrNull()?.id)
            assertEquals("김수신", result.getOrNull()?.receivers?.single()?.name)
            assertEquals("친구", result.getOrNull()?.receivers?.single()?.relation)
            coVerify(exactly = 1) { repository.getAfternoteDetail(afternoteId = 10L) }
            coVerify(exactly = 1) { getUserIdUseCase() }
            coVerify(exactly = 1) { getReceiversUseCase(userId = 100L) }
        }

    @Test
    fun invoke_whenRepositoryFails_returnsFailure() =
        runTest {
            coEvery { repository.getAfternoteDetail(afternoteId = 10L) } returns
                Result.failure(RuntimeException("Not found"))

            val result = useCase(afternoteId = 10L)

            assertTrue(result.isFailure)
            assertEquals("Not found", result.exceptionOrNull()?.message)
            coVerify(exactly = 0) { getUserIdUseCase() }
        }

    @Test
    fun invoke_whenNoUserId_returnsDetailWithoutResolvingReceivers() =
        runTest {
            val detail =
                AfternoteDetail(
                    id = 10L,
                    category = "GALLERY",
                    title = "갤러리",
                    createdAt = "2025.11.26",
                    updatedAt = "2025.11.26",
                    type = AfternoteServiceType.GALLERY_AND_FILES,
                    credentialsId = null,
                    credentialsPassword = null,
                    receivers = listOf(
                        AfternoteDetailReceiver(
                            receiverId = 1L,
                            name = "",
                            relation = "",
                            phone = ""
                        )
                    ),
                    processMethod = "TRANSFER",
                    actions = emptyList(),
                    leaveMessage = null,
                    playlist = null
                )
            coEvery { repository.getAfternoteDetail(afternoteId = 10L) } returns Result.success(detail)
            coEvery { getUserIdUseCase() } returns null

            val result = useCase(afternoteId = 10L)

            assertTrue(result.isSuccess)
            assertEquals(10L, result.getOrNull()?.id)
            assertEquals("", result.getOrNull()?.receivers?.single()?.name)
            coVerify(exactly = 0) { getReceiversUseCase(any()) }
        }
}
