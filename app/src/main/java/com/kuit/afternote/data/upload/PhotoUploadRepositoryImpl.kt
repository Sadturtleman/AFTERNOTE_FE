package com.kuit.afternote.data.upload

import android.content.Context
import android.net.Uri
import com.kuit.afternote.data.remote.requireData
import com.kuit.afternote.feature.user.data.api.ImageApiService
import com.kuit.afternote.feature.user.data.dto.PresignedUrlRequestDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Named

private const val DEFAULT_EXTENSION = "jpg"

/**
 * Shared implementation: uploads image from content URI to the given directory
 * via POST /files/presigned-url and S3 PUT. Used for profile photo, memorial photo, etc.
 */
class PhotoUploadRepositoryImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val imageApi: ImageApiService,
        @Named("S3Upload") private val okHttpClient: OkHttpClient,
        @Named("IoDispatcher") private val ioDispatcher: CoroutineDispatcher
    ) : PhotoUploadRepository {

    override suspend fun upload(uriString: String, directory: String): Result<String> =
        runCatching {
            val uri = Uri.parse(uriString)
            val extension = extensionFromUri(uri)
            val presigned = imageApi.getPresignedUrl(
                PresignedUrlRequestDto(
                    directory = directory,
                    extension = extension
                )
            ).requireData()
                ?: throw IllegalStateException("Presigned URL response data is null")

            val bytes = withContext(ioDispatcher) {
                context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    ?: throw IllegalStateException("Could not read image from URI")
            }

            val contentType = presigned.contentType.ifBlank { "image/jpeg" }
            val requestBody = bytes.toRequestBody(contentType.toMediaType())
            val putRequest =
                Request.Builder()
                    .url(presigned.presignedUrl)
                    .put(requestBody)
                    .header("Content-Type", contentType)
                    .build()

            withContext(ioDispatcher) {
                okHttpClient.newCall(putRequest).execute().use { response ->
                    check(response.isSuccessful) {
                        "S3 upload failed: ${response.code} ${response.message}"
                    }
                }
            }
            presigned.fileUrl
        }

    private fun extensionFromUri(uri: Uri): String {
        val mime = context.contentResolver.getType(uri) ?: return DEFAULT_EXTENSION
        return when {
            mime == "image/jpeg" || mime == "image/jpg" -> "jpg"
            mime == "image/png" -> "png"
            mime == "image/webp" -> "webp"
            mime == "image/gif" -> "gif"
            else -> DEFAULT_EXTENSION
        }
    }
}
