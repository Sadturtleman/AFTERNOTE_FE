package com.kuit.afternote.feature.afternote.domain.repository

/**
 * Memorial (playlist) thumbnail upload via POST /images/presigned-url and S3 PUT.
 * Used for "장례식에 남길 영상" thumbnail so the server receives an image URL, not inline data.
 */
fun interface MemorialThumbnailUploadRepository {
    /**
     * Uploads JPEG thumbnail bytes to directory "afternotes" and returns the image URL.
     *
     * @param jpegBytes JPEG-encoded thumbnail (e.g. first frame of video).
     * @return Success with imageUrl (https) or failure.
     */
    suspend fun uploadThumbnail(jpegBytes: ByteArray): Result<String>
}
