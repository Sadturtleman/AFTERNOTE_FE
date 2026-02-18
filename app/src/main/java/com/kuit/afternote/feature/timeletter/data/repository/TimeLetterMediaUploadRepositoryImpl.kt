package com.kuit.afternote.feature.timeletter.data.repository

import android.content.Context
import android.net.Uri
import com.kuit.afternote.data.remote.requireData
import com.kuit.afternote.feature.timeletter.domain.repository.TimeLetterMediaUploadRepository
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

/**
 * Presigned URL을 받아 타임레터 첨부 이미지를 S3에 업로드하고 fileUrl을 반환합니다.
 *
 * directory는 백엔드와 합의한 값 사용. 미지원 시 afternotes 등 기존 디렉터리로 변경 가능.
 */
private const val DIRECTORY_TIME_LETTERS = "timeletters"
private const val DEFAULT_IMAGE_EXTENSION = "jpg"
private const val DEFAULT_AUDIO_EXTENSION = "m4a"
private const val DEFAULT_AUDIO_CONTENT_TYPE = "audio/mp4"

class TimeLetterMediaUploadRepositoryImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val imageApi: ImageApiService,
        @Named("S3Upload") private val okHttpClient: OkHttpClient,
        @Named("IoDispatcher") private val ioDispatcher: CoroutineDispatcher
    ) : TimeLetterMediaUploadRepository {

    override suspend fun uploadImage(uriString: String): Result<String> =
        runCatching {
            val uri = Uri.parse(uriString)
            val extension = extensionFromUri(uri)
            val presigned = imageApi.getPresignedUrl(
                PresignedUrlRequestDto(
                    directory = DIRECTORY_TIME_LETTERS,
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

    override suspend fun uploadAudio(uriString: String): Result<String> =
        runCatching {
            val uri = Uri.parse(uriString)
            val (extension, defaultContentType) = audioExtensionAndContentTypeFromUri(uri)
            val presigned = imageApi.getPresignedUrl(
                PresignedUrlRequestDto(
                    directory = DIRECTORY_TIME_LETTERS,
                    extension = extension
                )
            ).requireData()
                ?: throw IllegalStateException("Presigned URL response data is null")

            val bytes = withContext(ioDispatcher) {
                context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    ?: throw IllegalStateException("Could not read audio from URI")
            }

            val contentType = presigned.contentType.ifBlank { defaultContentType }
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
        val mime = context.contentResolver.getType(uri) ?: return DEFAULT_IMAGE_EXTENSION
        return when {
            mime == "image/jpeg" || mime == "image/jpg" -> "jpg"
            mime == "image/png" -> "png"
            mime == "image/webp" -> "webp"
            mime == "image/gif" -> "gif"
            mime == "image/heic" -> "heic"
            else -> DEFAULT_IMAGE_EXTENSION
        }
    }

    private fun audioExtensionAndContentTypeFromUri(uri: Uri): Pair<String, String> {
        val mime = context.contentResolver.getType(uri)
            ?: return DEFAULT_AUDIO_EXTENSION to DEFAULT_AUDIO_CONTENT_TYPE
        return when {
            mime == "audio/mpeg" || mime == "audio/mp3" -> "mp3" to "audio/mpeg"
            mime == "audio/mp4" || mime == "audio/x-m4a" -> "m4a" to DEFAULT_AUDIO_CONTENT_TYPE
            mime == "audio/ogg" -> "ogg" to "audio/ogg"
            mime == "audio/webm" -> "webm" to "audio/webm"
            mime == "audio/amr" -> "amr" to "audio/amr"
            mime == "audio/3gpp" -> "3gp" to "audio/3gpp"
            else -> DEFAULT_AUDIO_EXTENSION to DEFAULT_AUDIO_CONTENT_TYPE
        }
    }
}
