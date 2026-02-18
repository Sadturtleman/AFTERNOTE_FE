package com.kuit.afternote.feature.receiverauth.data.repository.impl

import android.content.Context
import android.net.Uri
import com.kuit.afternote.feature.receiverauth.data.repository.iface.ReceiverAuthRepository
import com.kuit.afternote.feature.receiverauth.domain.repository.iface.UploadReceiverDocumentRepository
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
 * 수신자 증빙 서류 S3 업로드 Data Repository 구현체.
 *
 * receiver-auth Presigned URL 발급 후 S3 PUT으로 업로드하고 fileUrl을 반환합니다.
 * 확장자는 API 허용 값(jpg, jpeg, png, gif, webp, heic, pdf)으로 Uri/ContentType에서 추론합니다.
 */
class ReceiverDocumentUploadRepositoryImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val receiverAuthRepository: ReceiverAuthRepository,
        @Named("S3Upload") private val okHttpClient: OkHttpClient,
        @Named("IoDispatcher") private val ioDispatcher: CoroutineDispatcher
    ) : UploadReceiverDocumentRepository {

    override suspend fun uploadDocument(authCode: String, uriString: String): Result<String> =
        runCatching {
            val uri = Uri.parse(uriString)
            val extension = extensionFromUri(uri)
            val presignedResult = receiverAuthRepository.getPresignedUrl(authCode, extension)
            val presigned = presignedResult.getOrThrow()

            val bytes = withContext(ioDispatcher) {
                context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    ?: throw IllegalStateException("Could not read file from URI")
            }

            val contentType = presigned.contentType.ifBlank { defaultContentType(extension) }
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

    /**
     * Uri/ContentType에서 API 허용 확장자(jpg, jpeg, png, gif, webp, heic, pdf)로 매핑. 점 없음.
     */
    private fun extensionFromUri(uri: Uri): String {
        val mime = context.contentResolver.getType(uri) ?: return DEFAULT_EXTENSION
        return when {
            mime == "image/jpeg" || mime == "image/jpg" -> "jpg"
            mime == "image/png" -> "png"
            mime == "image/gif" -> "gif"
            mime == "image/webp" -> "webp"
            mime == "image/heic" -> "heic"
            mime == "application/pdf" -> "pdf"
            else -> DEFAULT_EXTENSION
        }
    }

    private fun defaultContentType(extension: String): String =
        when (extension) {
            "pdf" -> "application/pdf"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "webp" -> "image/webp"
            "heic" -> "image/heic"
            else -> "image/jpeg"
        }

    companion object {
        private const val DEFAULT_EXTENSION = "jpg"
    }
}
