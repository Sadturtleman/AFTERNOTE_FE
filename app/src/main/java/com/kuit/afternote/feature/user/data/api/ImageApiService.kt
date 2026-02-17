package com.kuit.afternote.feature.user.data.api

import com.kuit.afternote.data.remote.ApiResponse
import com.kuit.afternote.feature.user.data.dto.PresignedUrlRequestDto
import com.kuit.afternote.feature.user.data.dto.PresignedUrlResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * File API (presigned URL for S3 upload).
 * POST /files/presigned-url — S3 파일 업로드를 위한 Presigned URL을 생성합니다.
 * 지원 형식: 이미지(jpg, jpeg, png, gif, webp, heic), 영상(mp4, mov), 음성(mp3, m4a, wav), 문서(pdf).
 */
fun interface ImageApiService {
    @POST("files/presigned-url")
    suspend fun getPresignedUrl(
        @Body body: PresignedUrlRequestDto
    ): ApiResponse<PresignedUrlResponseDto?>
}
