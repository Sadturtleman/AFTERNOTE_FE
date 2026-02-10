package com.kuit.afternote.feature.afternote.data.api

import com.kuit.afternote.data.remote.ApiResponse
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreateGalleryRequestDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreatePlaylistRequestDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreateSocialRequestDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteDetailResponseDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteIdResponseDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteListResponseDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteUpdateRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Afternote API (Swagger / API spec).
 *
 * - GET /afternotes — list (category, page, size)
 * - GET /afternotes/{afternoteId} — detail
 * - POST /afternotes — create (SOCIAL / GALLERY / PLAYLIST)
 * - PATCH /afternotes/{afternoteId} — update
 * - DELETE /afternotes/{afternoteId} — delete
 */
interface AfternoteApiService {

    @GET("afternotes")
    suspend fun getAfternotes(
        @Query("category") category: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): ApiResponse<AfternoteListResponseDto?>

    @GET("afternotes/{afternoteId}")
    suspend fun getAfternoteDetail(
        @Path("afternoteId") afternoteId: Long
    ): ApiResponse<AfternoteDetailResponseDto?>

    @POST("afternotes")
    suspend fun createAfternoteSocial(
        @Body body: AfternoteCreateSocialRequestDto
    ): ApiResponse<AfternoteIdResponseDto?>

    @POST("afternotes")
    suspend fun createAfternoteGallery(
        @Body body: AfternoteCreateGalleryRequestDto
    ): ApiResponse<AfternoteIdResponseDto?>

    @POST("afternotes")
    suspend fun createAfternotePlaylist(
        @Body body: AfternoteCreatePlaylistRequestDto
    ): ApiResponse<AfternoteIdResponseDto?>

    @PATCH("afternotes/{afternoteId}")
    suspend fun updateAfternote(
        @Path("afternoteId") afternoteId: Long,
        @Body body: AfternoteUpdateRequestDto
    ): ApiResponse<AfternoteIdResponseDto?>

    @DELETE("afternotes/{afternoteId}")
    suspend fun deleteAfternote(
        @Path("afternoteId") afternoteId: Long
    ): ApiResponse<AfternoteIdResponseDto?>
}
