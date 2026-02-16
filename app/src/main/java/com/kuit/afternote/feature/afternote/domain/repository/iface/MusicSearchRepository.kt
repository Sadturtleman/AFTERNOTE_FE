package com.kuit.afternote.feature.afternote.domain.repository.iface

import com.kuit.afternote.core.uimodel.PlaylistSongDisplay

/**
 * Music search by keyword (artist or title).
 * API: GET /api/music/search?keyword= — 200 success, 400 when keyword is missing.
 */
fun interface MusicSearchRepository {

    /**
     * Searches for tracks by keyword. Call only when [keyword] is non-blank (API returns 400 otherwise).
     *
     * @param keyword 검색할 가수명 또는 노래 제목
     * @return Result with list of [PlaylistSongDisplay], or failure on API error
     */
    suspend fun search(keyword: String): Result<List<PlaylistSongDisplay>>
}
