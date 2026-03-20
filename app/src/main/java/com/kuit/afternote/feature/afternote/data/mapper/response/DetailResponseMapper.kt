package com.kuit.afternote.feature.afternote.data.mapper.response

import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentials
import com.kuit.afternote.feature.afternote.data.dto.AfternoteDetailReceiver
import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylist
import com.kuit.afternote.feature.afternote.data.dto.AfternoteSong
import com.kuit.afternote.feature.afternote.data.dto.response.AfternoteDetailResponse
import com.kuit.afternote.feature.afternote.data.mapper.categoryToServiceType
import com.kuit.afternote.feature.afternote.data.mapper.formatDateFromServer
import com.kuit.afternote.feature.afternote.domain.model.Detail
import com.kuit.afternote.feature.afternote.domain.model.DetailCredentials
import com.kuit.afternote.feature.afternote.domain.model.DetailProcessing
import com.kuit.afternote.feature.afternote.domain.model.DetailReceiver
import com.kuit.afternote.feature.afternote.domain.model.DetailTimestamps
import com.kuit.afternote.feature.afternote.domain.model.playlist.DetailSong
import com.kuit.afternote.feature.afternote.domain.model.playlist.PlaylistDetail
import com.kuit.afternote.feature.afternote.domain.model.playlist.PlaylistDetailMemorialMedia

fun AfternoteDetailResponse.toDetailDomain(): Detail =
    Detail(
        id = afternoteId,
        category = category,
        title = title,
        timestamps = toTimestamps(),
        type = categoryToServiceType(category),
        credentials = credentials?.toDomain(),
        receivers = receivers.toDomain(),
        processing = toProcessing(),
        playlist = playlist?.toDomain(),
    )

private fun List<AfternoteDetailReceiver>?.toDomain() =
    this?.map { a ->
        a.toDomain()
    } ?: emptyList()

private fun AfternoteDetailResponse.toTimestamps(): DetailTimestamps =
    DetailTimestamps(
        createdAt = formatDateFromServer(createdAt),
        updatedAt = formatDateFromServer(updatedAt),
    )

private fun AfternoteDetailResponse.toProcessing() =
    DetailProcessing(
        method = processMethod,
        actions = actions ?: emptyList(),
        leaveMessage = leaveMessage,
    )

private fun AfternoteCredentials.toDomain() =
    DetailCredentials(
        id = id,
        password = password,
    )

private fun AfternotePlaylist.toDomain(): PlaylistDetail {
    val playlistDetailMemorialMedia =
        PlaylistDetailMemorialMedia(
            photoUrl = memorialPhotoUrl ?: profilePhoto,
            videoUrl = memorialVideo?.videoUrl,
            thumbnailUrl = memorialVideo?.thumbnailUrl,
        )
    return PlaylistDetail(
        profilePhoto = profilePhoto,
        atmosphere = atmosphere,
        songs = songs.map { it.toDomain() },
        playlistDetailMemorialMedia = playlistDetailMemorialMedia,
    )
}

private fun AfternoteDetailReceiver.toDomain() =
    DetailReceiver(
        receiverId = receiverId,
        name = name ?: "",
        relation = relation ?: "",
        phone = phone ?: "",
    )

private fun AfternoteSong.toDomain() =
    DetailSong(
        id = id,
        title = title,
        artist = artist,
        coverUrl = coverUrl,
    )
