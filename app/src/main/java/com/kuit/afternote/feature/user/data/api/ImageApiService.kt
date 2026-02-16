package com.kuit.afternote.feature.user.data.api

import com.kuit.afternote.data.remote.ApiResponse
import com.kuit.afternote.feature.user.data.dto.PresignedUrlRequestDto
import com.kuit.afternote.feature.user.data.dto.PresignedUrlResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Image API (presigned URL for S3 upload).
 * POST /images/presigned-url — S3 이미지 업로드를 위한 Presigned URL을 생성합니다.
 */
fun interface ImageApiService {
    @POST("images/presigned-url")
    suspend fun getPresignedUrl(
        @Body body: PresignedUrlRequestDto
    ): ApiResponse<PresignedUrlResponseDto?>
}
