package com.kuit.afternote.feature.afternote.data.mapper

import com.kuit.afternote.domain.model.AfternoteServiceType
import com.kuit.afternote.feature.afternote.data.dto.AfternoteListItem
import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylist
import com.kuit.afternote.feature.afternote.data.dto.AfternoteSong
import com.kuit.afternote.feature.afternote.data.dto.response.AfternoteDetailResponse
import com.kuit.afternote.feature.afternote.data.dto.response.AfternoteListResponse
import com.kuit.afternote.feature.afternote.domain.model.Detail
import com.kuit.afternote.feature.afternote.domain.model.DetailCredentials
import com.kuit.afternote.feature.afternote.domain.model.DetailProcessing
import com.kuit.afternote.feature.afternote.domain.model.DetailReceiver
import com.kuit.afternote.feature.afternote.domain.model.DetailTimestamps
import com.kuit.afternote.feature.afternote.domain.model.Item
import com.kuit.afternote.feature.afternote.domain.model.PagedAfternotes
import com.kuit.afternote.feature.afternote.domain.model.playlist.DetailSong
import com.kuit.afternote.feature.afternote.domain.model.playlist.PlaylistDetail
import com.kuit.afternote.feature.afternote.domain.model.playlist.PlaylistDetailMemorialMedia

/**
 * Maps server DTOs to domain models at the boundary only.
 */
// -- List mapping --

fun AfternoteListItem.toDomain() =
    Item(
        id = afternoteId.toString(),
        serviceName = title,
        date = formatDateFromServer(createdAt),
        type = categoryToServiceType(category),
    )

fun List<AfternoteListItem>.toDomainList() = map { it.toDomain() }

// -- Detail mapping --

fun AfternoteDetailResponse.toDetailDomain(): Detail {
    val timestamps =
        DetailTimestamps(
            createdAt = formatDateFromServer(createdAt),
            updatedAt = formatDateFromServer(createdAt),
        )
    val detailCredentials =
        DetailCredentials(
            id = credentials?.id,
            password = credentials?.password,
        )
    val receivers =
        receivers?.map {
            DetailReceiver(
                receiverId = it.receiverId,
                name = it.name ?: "",
                relation = it.relation ?: "",
                phone = it.phone ?: "",
            )
        } ?: emptyList()
    val detailProcessing =
        DetailProcessing(
            method = processMethod,
            actions = actions ?: emptyList(),
            leaveMessage = leaveMessage,
        )
    val detailSong: (AfternoteSong) -> DetailSong = { s ->
        DetailSong(
            id = s.id,
            title = s.title,
            artist = s.artist,
            coverUrl = s.coverUrl,
        )
    }

    val songs: (AfternotePlaylist) -> List<DetailSong> = {
        val afternoteSongList = it.songs
        afternoteSongList.map { afternoteSong ->
            detailSong(afternoteSong)
        }
    }

    val playlistDetailMemorialMedia: (AfternotePlaylist) -> PlaylistDetailMemorialMedia = {
        PlaylistDetailMemorialMedia(
            photoUrl = it.memorialPhotoUrl ?: it.profilePhoto,
            videoUrl = it.memorialVideo?.videoUrl,
            thumbnailUrl = it.memorialVideo?.thumbnailUrl,
        )
    }
    val playlist =
        playlist?.let {
            PlaylistDetail(
                profilePhoto = it.profilePhoto,
                atmosphere = it.atmosphere,
                songs = songs(it),
                playlistDetailMemorialMedia = playlistDetailMemorialMedia(it),
            )
        }

    return Detail(
        id = afternoteId,
        category = category,
        title = title,
        timestamps = timestamps,
        type = categoryToServiceType(category),
        credentials = detailCredentials,
        receivers = receivers,
        processing = detailProcessing,
        playlist = playlist,
    )
}

fun AfternoteListResponse.toPagedNotes() =
    PagedAfternotes(
        items = content.toDomainList(),
        hasNext = hasNext,
    )

// -- Internal helpers --

private fun formatDateFromServer(createdAt: String): String =
    try {
        // Server returns "2025-11-26T14:30:00" -> display "2025.11.26"
        val datePart = createdAt.substringBefore('T')
        datePart.replace('-', '.')
    } catch (_: Exception) {
        createdAt
    }

internal fun categoryToServiceType(category: String): AfternoteServiceType =
    when (category.uppercase()) {
        "SOCIAL" -> AfternoteServiceType.SOCIAL_NETWORK
        "GALLERY" -> AfternoteServiceType.GALLERY_AND_FILES
        "MUSIC", "PLAYLIST" -> AfternoteServiceType.MEMORIAL
        else -> AfternoteServiceType.SOCIAL_NETWORK
    }
