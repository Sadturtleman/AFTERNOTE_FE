package com.kuit.afternote.feature.afternote.data.service

import com.kuit.afternote.data.BaseResponse
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteCreateGalleryRequest
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteCreatePlaylistRequest
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteCreateSocialRequest
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteUpdateRequest
import com.kuit.afternote.feature.afternote.data.dto.response.AfternoteDetailResponse
import com.kuit.afternote.feature.afternote.data.dto.response.AfternoteIdResponse
import com.kuit.afternote.feature.afternote.data.dto.response.AfternoteListResponse
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
    @GET("api/afternotes")
    suspend fun getAfternotes(
        @Query("category") category: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
    ): BaseResponse<AfternoteListResponse?>

    @GET("api/afternotes/{afternoteId}")
    suspend fun getAfternoteDetail(
        @Path("afternoteId") afternoteId: Long,
    ): BaseResponse<AfternoteDetailResponse?>

    @POST("api/afternotes")
    suspend fun createAfternoteSocial(
        @Body body: AfternoteCreateSocialRequest,
    ): BaseResponse<AfternoteIdResponse?>

    @POST("api/afternotes")
    suspend fun createAfternoteGallery(
        @Body body: AfternoteCreateGalleryRequest,
    ): BaseResponse<AfternoteIdResponse?>

    @POST("api/afternotes")
    suspend fun createAfternotePlaylist(
        @Body body: AfternoteCreatePlaylistRequest,
    ): BaseResponse<AfternoteIdResponse?>

    @PATCH("api/afternotes/{afternoteId}")
    suspend fun updateAfternote(
        @Path("afternoteId") afternoteId: Long,
        @Body body: AfternoteUpdateRequest,
    ): BaseResponse<AfternoteIdResponse?>

    @DELETE("api/afternotes/{afternoteId}")
    suspend fun deleteAfternote(
        @Path("afternoteId") afternoteId: Long,
    ): BaseResponse<AfternoteIdResponse?>
}
