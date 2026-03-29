package com.kuit.afternote.feature.afternote.data.repository

import com.kuit.afternote.core.data.model.requireData
import com.kuit.afternote.feature.afternote.data.api.AfternotePresignedUrlApi
import com.kuit.afternote.feature.afternote.data.dto.AfternotePresignedUrlRequestDto
import com.kuit.afternote.feature.afternote.domain.repository.MemorialThumbnailUploadRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Named

private const val DIRECTORY_AFTERNOTES = "afternotes"
private const val EXTENSION_JPG = "jpg"
private const val CONTENT_TYPE_JPEG = "image/jpeg"

/**
 * Uploads memorial thumbnail via POST /files/presigned-url (directory "afternotes") then S3 PUT.
 */
class MemorialThumbnailUploadRepositoryImpl
    @Inject
    constructor(
        private val presignedUrlApi: AfternotePresignedUrlApi,
        @Named("S3Upload") private val okHttpClient: OkHttpClient,
        @Named("IoDispatcher") private val ioDispatcher: CoroutineDispatcher,
    ) : MemorialThumbnailUploadRepository {
        override suspend fun uploadThumbnail(jpegBytes: ByteArray): Result<String> =
            runCatching {
                val presigned =
                    presignedUrlApi
                        .getPresignedUrl(
                            AfternotePresignedUrlRequestDto(
                                directory = DIRECTORY_AFTERNOTES,
                                extension = EXTENSION_JPG,
                            ),
                        ).requireData()
                        ?: throw IllegalStateException("Presigned URL response data is null")

                val contentType = presigned.contentType.ifBlank { CONTENT_TYPE_JPEG }
                val requestBody = jpegBytes.toRequestBody(contentType.toMediaType())
                val putRequest =
                    Request
                        .Builder()
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
    }
