package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.domain.repository.MemorialThumbnailUploadRepository
import javax.inject.Inject

/**
 * Uploads memorial thumbnail (JPEG bytes) via POST /images/presigned-url and S3.
 * Returns the image URL to use as memorialVideo.thumbnailUrl.
 */
class UploadMemorialThumbnailUseCase
    @Inject
    constructor(
        private val repository: MemorialThumbnailUploadRepository
    ) {
        suspend operator fun invoke(jpegBytes: ByteArray): Result<String> =
            repository.uploadThumbnail(jpegBytes)
    }
