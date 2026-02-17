package com.kuit.afternote.feature.afternote.data.api

import com.kuit.afternote.data.remote.ApiResponse
import com.kuit.afternote.feature.afternote.data.dto.MusicSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Music search API.
 * GET /music/search?keyword= — 검색할 가수명 또는 노래 제목.
 * 200: 검색 성공. 400: keyword가 없는 경우.
 */
fun interface MusicApiService {

    @GET("music/search")
    suspend fun search(
        @Query("keyword") keyword: String
    ): ApiResponse<MusicSearchResponseDto?>
}
