package com.kuit.afternote.feature.afternote.domain.repository

/**
 * Memorial (playlist) video upload via POST /files/presigned-url and S3 PUT.
 * Used so the server receives an HTTPS video URL, not a local content:// URI.
 */
fun interface MemorialVideoUploadRepository {
    /**
     * Uploads the video at the given content URI to S3 and returns the file URL.
     *
     * @param contentUriString Local content URI (e.g. from Photo Picker).
     * @return Success with video fileUrl (https) or failure.
     */
    suspend fun uploadVideo(contentUriString: String): Result<String>
}
