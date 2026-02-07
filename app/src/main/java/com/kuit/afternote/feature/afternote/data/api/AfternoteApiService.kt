package com.kuit.afternote.feature.afternote.data.api

import com.kuit.afternote.data.remote.ApiResponse
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreateGalleryRequestDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreateSocialRequestDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteIdResponseDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteListResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Afternote API. Uses server request/response form directly (no extra transformation).
 * - GET /afternotes (list)
 * - POST /afternotes (create)
 * - PATCH /afternotes/{id} (update)
 */
interface AfternoteApiService {
    @GET("afternotes")
    suspend fun getAfternotes(
        @Query("category") category: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): ApiResponse<AfternoteListResponseDto?>

    @POST("afternotes")
    suspend fun createAfternoteSocial(
        @Body body: AfternoteCreateSocialRequestDto
    ): ApiResponse<AfternoteIdResponseDto?>

    @POST("afternotes")
    suspend fun createAfternoteGallery(
        @Body body: AfternoteCreateGalleryRequestDto
    ): ApiResponse<AfternoteIdResponseDto?>

}
