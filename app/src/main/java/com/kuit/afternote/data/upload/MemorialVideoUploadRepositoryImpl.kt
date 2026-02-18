package com.kuit.afternote.data.upload

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.kuit.afternote.data.remote.requireData
import com.kuit.afternote.feature.afternote.domain.repository.MemorialVideoUploadRepository
import com.kuit.afternote.feature.user.data.api.ImageApiService
import com.kuit.afternote.feature.user.data.dto.PresignedUrlRequestDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Named

private const val DIRECTORY_AFTERNOTES = "afternotes"
private const val DEFAULT_VIDEO_EXTENSION = "mp4"

/**
 * Uploads memorial video via POST /files/presigned-url (directory "afternotes") then S3 PUT.
 * Reads from content URI (e.g. Photo Picker) and returns the HTTPS fileUrl so the backend
 * stores a usable URL, not a local content:// URI.
 */
class MemorialVideoUploadRepositoryImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val imageApi: ImageApiService,
        @Named("S3Upload") private val okHttpClient: OkHttpClient,
        @Named("IoDispatcher") private val ioDispatcher: CoroutineDispatcher
    ) : MemorialVideoUploadRepository {

    override suspend fun uploadVideo(contentUriString: String): Result<String> =
        runCatching {
            val uri = contentUriString.toUri()
            val extension = videoExtensionFromUri(uri)
            val presigned = imageApi.getPresignedUrl(
                PresignedUrlRequestDto(
                    directory = DIRECTORY_AFTERNOTES,
                    extension = extension
                )
            ).requireData()

            val tempFile = withContext(ioDispatcher) {
                context.contentResolver.openInputStream(uri)?.use { input ->
                    val file = File.createTempFile("memorial_video_", ".$extension")
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                    file
                } ?: throw IllegalStateException("Could not read video from URI")
            }

            try {
                val contentType = presigned.contentType.ifBlank { "video/$extension" }
                val requestBody = tempFile.asRequestBody(contentType.toMediaType())
                val putRequest =
                    Request.Builder()
                        .url(presigned.presignedUrl)
                        .put(requestBody)
                        .header("Content-Type", contentType)
                        .build()

                withContext(ioDispatcher) {
                    okHttpClient.newCall(putRequest).execute().use { response ->
                        check(response.isSuccessful) {
                            "S3 video upload failed: ${response.code} ${response.message}"
                        }
                    }
                }
                presigned.fileUrl
            } finally {
                if (!tempFile.delete()) {
                    // Temp file cleanup failed; file may remain until system cleanup
                }
            }
        }

    private fun videoExtensionFromUri(uri: Uri): String {
        val mime = context.contentResolver.getType(uri) ?: return DEFAULT_VIDEO_EXTENSION
        return when {
            mime == "video/mp4" -> "mp4"
            mime == "video/quicktime" -> "mov"
            mime.startsWith("video/") -> mime.removePrefix("video/").takeIf { it.isNotBlank() } ?: DEFAULT_VIDEO_EXTENSION
            else -> DEFAULT_VIDEO_EXTENSION
        }
    }
}
