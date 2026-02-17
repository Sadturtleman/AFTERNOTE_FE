package com.kuit.afternote.feature.afternote.data.api

import com.kuit.afternote.feature.afternote.data.dto.MusicSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Music search API.
 * GET /music/search?keyword= — 검색할 가수명 또는 노래 제목.
 * Server returns 200 with body { "tracks": [...] } (no status/code/message/data wrapper).
 * 400 when keyword is missing.
 */
fun interface MusicApiService {

    @GET("music/search")
    suspend fun search(
        @Query("keyword") keyword: String
    ): MusicSearchResponseDto
}
