package com.kuit.afternote.feature.receiverauth.domain.usecase

import com.kuit.afternote.feature.receiverauth.domain.repository.iface.UploadReceiverDocumentRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * [UploadReceiverDocumentUseCase] 단위 테스트.
 */
class UploadReceiverDocumentUseCaseTest {

    private lateinit var uploadReceiverDocumentRepository: UploadReceiverDocumentRepository
    private lateinit var useCase: UploadReceiverDocumentUseCase

    @Before
    fun setUp() {
        uploadReceiverDocumentRepository = mockk()
        useCase = UploadReceiverDocumentUseCase(uploadReceiverDocumentRepository)
    }

    @Test
    fun invoke_whenSuccess_returnsFileUrl() = runTest {
        val authCode = "auth-123"
        val uriString = "content://uri"
        val fileUrl = "https://bucket.s3.amazonaws.com/receiver-docs/abc.pdf"
        coEvery {
            uploadReceiverDocumentRepository.uploadDocument(authCode, uriString)
        } returns Result.success(fileUrl)

        val result = useCase(authCode, uriString)

        assertTrue(result.isSuccess)
        assertEquals(fileUrl, result.getOrNull())
        coVerify(exactly = 1) {
            uploadReceiverDocumentRepository.uploadDocument(authCode, uriString)
        }
    }

    @Test
    fun invoke_whenFailure_returnsFailure() = runTest {
        coEvery {
            uploadReceiverDocumentRepository.uploadDocument(any(), any())
        } returns Result.failure(RuntimeException("Upload failed"))

        val result = useCase("code", "uri")

        assertTrue(result.isFailure)
        assertEquals("Upload failed", result.exceptionOrNull()?.message)
    }
}
