package com.kuit.afternote.feature.afternote.data.repository

import com.kuit.afternote.feature.afternote.data.dto.MusicTrack
import com.kuit.afternote.feature.afternote.data.service.MusicApiService
import com.kuit.afternote.feature.afternote.domain.model.SearchedSong
import com.kuit.afternote.feature.afternote.domain.repository.MusicSearchRepository
import javax.inject.Inject

/**
 * Data layer: calls music search API and maps DTO to [com.kuit.afternote.core.uimodel.PlaylistSongDisplay].
 * API returns raw { "tracks": [...] } (no BaseResponse wrapper).
 */
class MusicSearchRepositoryImpl
    @Inject
    constructor(
        private val api: MusicApiService,
    ) : MusicSearchRepository {
        override suspend fun search(keyword: String): Result<List<SearchedSong>> {
            val trimmed = keyword.trim()
            if (trimmed.isEmpty()) return Result.success(emptyList())
            return runCatching {
                val response = api.search(keyword = trimmed)
                val tracks = response.tracks
                tracks.mapIndexed { index, dto -> dto.toPlaylistSongDisplay(index) }
            }
        }

        private fun MusicTrack.toPlaylistSongDisplay(index: Int): SearchedSong {
            val id = "$artist|$title|$index"
            return SearchedSong(
                id = id,
                title = title,
                artist = artist,
                albumImageUrl = albumImageUrl,
            )
        }
    }
