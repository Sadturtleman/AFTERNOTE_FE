package com.kuit.afternote.feature.user.data.repository

import android.content.Context
import android.net.Uri
import com.kuit.afternote.data.remote.requireData
import com.kuit.afternote.feature.user.data.api.ImageApiService
import com.kuit.afternote.feature.user.data.dto.PresignedUrlRequestDto
import com.kuit.afternote.feature.user.domain.repository.ProfileImageUploadRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Named

private const val DIRECTORY_PROFILES = "profiles"
private const val DEFAULT_EXTENSION = "jpg"

/**
 * Presigned URL을 받아 프로필 이미지를 S3에 업로드하고 imageUrl을 반환합니다.
 */
class ProfileImageUploadRepositoryImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val imageApi: ImageApiService,
        @Named("S3Upload") private val okHttpClient: OkHttpClient,
        @Named("IoDispatcher") private val ioDispatcher: CoroutineDispatcher
    ) : ProfileImageUploadRepository {

    override suspend fun uploadProfileImage(uriString: String): Result<String> =
        runCatching {
            val uri = Uri.parse(uriString)
            val extension = extensionFromUri(uri)
            val presigned = imageApi.getPresignedUrl(
                PresignedUrlRequestDto(
                    directory = DIRECTORY_PROFILES,
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
            presigned.imageUrl
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
