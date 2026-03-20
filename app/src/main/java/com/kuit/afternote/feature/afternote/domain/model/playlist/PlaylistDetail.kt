package com.kuit.afternote.feature.afternote.domain.model.playlist

/**
 * 추모 가이드라인 카테고리의 플레이리스트 상세.
 */
data class PlaylistDetail(
    val profilePhoto: String?,
    val atmosphere: String?,
    val songs: List<DetailSong>,
    val playlistDetailMemorialMedia: PlaylistDetailMemorialMedia,
)

data class PlaylistDetailMemorialMedia(
    val photoUrl: String?,
    val videoUrl: String?,
    val thumbnailUrl: String?,
)

/**
 * 플레이리스트 내 개별 곡 정보.
 */
data class DetailSong(
    val id: Long?,
    val title: String,
    val artist: String,
    val coverUrl: String?,
)
