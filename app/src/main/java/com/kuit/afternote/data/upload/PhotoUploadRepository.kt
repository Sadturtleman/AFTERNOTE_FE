package com.kuit.afternote.data.upload

/**
 * Core photo upload: content URI → presigned URL → S3 PUT → file URL.
 * Reusable for profile image, memorial photo, and any other image upload that uses
 * POST /files/presigned-url and the same S3 flow.
 */
fun interface PhotoUploadRepository {
    /**
     * @param uriString content URI of the selected image (e.g. from gallery).
     * @param directory target directory for the file (e.g. "profiles", "afternotes").
     * @return Success with image URL (https) or failure.
     */
    suspend fun upload(uriString: String, directory: String): Result<String>
}
