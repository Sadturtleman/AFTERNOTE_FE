package com.kuit.afternote.feature.afternote.data.repository

import com.kuit.afternote.core.uimodel.PlaylistSongDisplay
import com.kuit.afternote.feature.afternote.data.api.MusicApiService
import com.kuit.afternote.feature.afternote.data.dto.MusicTrackDto
import com.kuit.afternote.feature.afternote.domain.repository.iface.MusicSearchRepository
import javax.inject.Inject

/**
 * Data layer: calls music search API and maps DTO to [PlaylistSongDisplay].
 * API returns raw { "tracks": [...] } (no ApiResponse wrapper).
 */
class MusicSearchRepositoryImpl
    @Inject
    constructor(
        private val api: MusicApiService
    ) : MusicSearchRepository {

    override suspend fun search(keyword: String): Result<List<PlaylistSongDisplay>> = runCatching {
        val trimmed = keyword.trim()
        if (trimmed.isEmpty()) return@runCatching emptyList<PlaylistSongDisplay>()
        val response = api.search(keyword = trimmed)
        response.tracks.mapIndexed { index, dto -> dto.toPlaylistSongDisplay(index) }
    }

    private fun MusicTrackDto.toPlaylistSongDisplay(index: Int): PlaylistSongDisplay {
        val id = "${artist}|$title|$index"
        return PlaylistSongDisplay(
            id = id,
            title = title,
            artist = artist,
            albumImageUrl = albumImageUrl
        )
    }
}
