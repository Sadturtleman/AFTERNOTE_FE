package com.kuit.afternote.feature.timeletter.domain.usecase

import com.kuit.afternote.feature.timeletter.domain.repository.TimeLetterMediaUploadRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * [UploadTimeLetterImageUseCase] 단위 테스트.
 */
class UploadTimeLetterImageUseCaseTest {

    private lateinit var repository: TimeLetterMediaUploadRepository
    private lateinit var useCase: UploadTimeLetterImageUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = UploadTimeLetterImageUseCase(repository)
    }

    @Test
    fun invoke_whenSuccess_returnsFileUrl() = runTest {
        val uriString = "content://media/image/123"
        val fileUrl = "https://afternote-bucket.s3.ap-northeast-2.amazonaws.com/time-letters/uuid.jpg"
        coEvery { repository.uploadImage(uriString) } returns Result.success(fileUrl)

        val result = useCase(uriString)

        assertTrue(result.isSuccess)
        assertEquals(fileUrl, result.getOrNull())
        coVerify(exactly = 1) { repository.uploadImage(uriString) }
    }

    @Test
    fun invoke_whenFailure_returnsFailure() = runTest {
        coEvery { repository.uploadImage(any()) } returns Result.failure(RuntimeException("S3 upload failed"))

        val result = useCase("content://uri")

        assertTrue(result.isFailure)
        assertEquals("S3 upload failed", result.exceptionOrNull()?.message)
    }
}
