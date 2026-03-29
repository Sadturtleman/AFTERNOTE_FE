package com.kuit.afternote.feature.afternote.data.api

import com.kuit.afternote.core.data.model.BaseResponse
import com.kuit.afternote.feature.afternote.data.dto.AfternotePresignedUrlRequestDto
import com.kuit.afternote.feature.afternote.data.dto.AfternotePresignedUrlResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

/** POST /files/presigned-url — Afternote memorial 업로드 전용 Retrofit 바인딩 (user feature 비의존). */
fun interface AfternotePresignedUrlApi {
    @POST("files/presigned-url")
    suspend fun getPresignedUrl(
        @Body body: AfternotePresignedUrlRequestDto,
    ): BaseResponse<AfternotePresignedUrlResponseDto?>
}
