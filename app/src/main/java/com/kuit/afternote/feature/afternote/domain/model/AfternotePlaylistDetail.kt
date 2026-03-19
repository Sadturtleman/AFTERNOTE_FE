package com.kuit.afternote.feature.afternote.domain.model

/**
 * 추모 가이드라인 카테고리의 플레이리스트 상세.
 */
data class AfternotePlaylistDetail(
    val profilePhoto: String?,
    val atmosphere: String?,
    val songs: List<AfternoteDetailSong>,
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
data class AfternoteDetailSong(
    val id: Long?,
    val title: String,
    val artist: String,
    val coverUrl: String?,
)
