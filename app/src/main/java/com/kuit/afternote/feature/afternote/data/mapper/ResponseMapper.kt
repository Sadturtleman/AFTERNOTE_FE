package com.kuit.afternote.feature.afternote.data.mapper

import com.kuit.afternote.feature.afternote.data.dto.AfternoteDetailReceiver
import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylist
import com.kuit.afternote.feature.afternote.data.dto.AfternoteSong
import com.kuit.afternote.feature.afternote.data.dto.response.AfternoteDetailResponse
import com.kuit.afternote.feature.afternote.data.dto.response.AfternoteListResponse
import com.kuit.afternote.feature.afternote.domain.model.Detail
import com.kuit.afternote.feature.afternote.domain.model.DetailCredentials
import com.kuit.afternote.feature.afternote.domain.model.DetailProcessing
import com.kuit.afternote.feature.afternote.domain.model.DetailReceiver
import com.kuit.afternote.feature.afternote.domain.model.DetailTimestamps
import com.kuit.afternote.feature.afternote.domain.model.PagedAfternotes
import com.kuit.afternote.feature.afternote.domain.model.playlist.DetailSong
import com.kuit.afternote.feature.afternote.domain.model.playlist.PlaylistDetail
import com.kuit.afternote.feature.afternote.domain.model.playlist.PlaylistDetailMemorialMedia

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
    val toDetailReceiver: (AfternoteDetailReceiver) -> DetailReceiver = {
        DetailReceiver(
            receiverId = it.receiverId,
            name = it.name ?: "",
            relation = it.relation ?: "",
            phone = it.phone ?: "",
        )
    }
    val receivers =
        receivers?.map {
            toDetailReceiver(it)
        } ?: emptyList()
    val detailProcessing =
        DetailProcessing(
            method = processMethod,
            actions = actions ?: emptyList(),
            leaveMessage = leaveMessage,
        )
    val toDetailSong: (AfternoteSong) -> DetailSong = { s ->
        DetailSong(
            id = s.id,
            title = s.title,
            artist = s.artist,
            coverUrl = s.coverUrl,
        )
    }

    val toDetailSongList: (AfternotePlaylist) -> List<DetailSong> = {
        val afternoteSongList = it.songs
        afternoteSongList.map { afternoteSong ->
            toDetailSong(afternoteSong)
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
                songs = toDetailSongList(it),
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
