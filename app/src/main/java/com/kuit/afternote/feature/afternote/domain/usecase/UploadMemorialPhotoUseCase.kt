package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.data.upload.PhotoUploadRepository
import javax.inject.Inject

private const val DIRECTORY_AFTERNOTES = "afternotes"

/**
 * Uploads memorial (playlist) photo from content URI via presigned URL.
 * Returns the image URL to send as playlist.memorialPhotoUrl.
 */
class UploadMemorialPhotoUseCase
    @Inject
    constructor(
        private val photoUploadRepository: PhotoUploadRepository
    ) {
    suspend operator fun invoke(uriString: String): Result<String> =
        photoUploadRepository.upload(uriString, DIRECTORY_AFTERNOTES)
}
