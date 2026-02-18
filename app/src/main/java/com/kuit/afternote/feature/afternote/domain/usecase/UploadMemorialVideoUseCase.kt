package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.domain.repository.MemorialVideoUploadRepository
import javax.inject.Inject

/**
 * Uploads memorial video from a local content URI to S3 via presigned URL.
 * Returns the HTTPS fileUrl so the backend can store a usable video URL.
 */
class UploadMemorialVideoUseCase
    @Inject
    constructor(
        private val repository: MemorialVideoUploadRepository
    ) {
    suspend operator fun invoke(contentUriString: String): Result<String> =
        repository.uploadVideo(contentUriString)
    }
