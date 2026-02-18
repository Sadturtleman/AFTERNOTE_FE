package com.kuit.afternote.feature.receiverauth.data.mapper

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedAfternote
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedAfternoteDetail
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedAfternotePlaylist
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedMindRecord
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedPlaylistSong
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetterMedia
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedAfternoteAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedAfternoteDetailAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedAfternoteSongInfoDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedMindRecordAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedTimeLetterAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.ReceivedTimeLetterDetailAuthResponseDto
import com.kuit.afternote.feature.receiverauth.data.dto.TimeLetterMediaAuthResponseDto

/**
 * receiver-auth API DTO를 receiver 도메인 엔티티로 매핑합니다.
 */

fun ReceivedTimeLetterAuthResponseDto.toReceivedTimeLetter(): ReceivedTimeLetter =
    ReceivedTimeLetter(
        timeLetterId = id,
        timeLetterReceiverId = timeLetterReceiverId,
        title = title,
        content = content,
        sendAt = sendAt,
        status = status,
        senderName = senderName,
        deliveredAt = deliveredAt,
        createdAt = createdAt,
        mediaList = mediaList.map { it.toReceivedTimeLetterMedia() },
        isRead = isRead
    )

fun TimeLetterMediaAuthResponseDto.toReceivedTimeLetterMedia(): ReceivedTimeLetterMedia =
    ReceivedTimeLetterMedia(
        id = id,
        mediaType = mediaType,
        mediaUrl = mediaUrl
    )

fun ReceivedTimeLetterDetailAuthResponseDto.toReceivedTimeLetter(): ReceivedTimeLetter =
    ReceivedTimeLetter(
        timeLetterId = id,
        timeLetterReceiverId = timeLetterReceiverId,
        title = title,
        content = content,
        sendAt = sendAt,
        status = status,
        senderName = senderName,
        deliveredAt = deliveredAt,
        createdAt = createdAt,
        mediaList = mediaList.map { it.toReceivedTimeLetterMedia() },
        isRead = isRead
    )

fun ReceivedMindRecordAuthResponseDto.toReceivedMindRecord(): ReceivedMindRecord =
    ReceivedMindRecord(
        mindRecordId = id,
        sourceType = type,
        content = title,
        recordDate = recordDate
    )

fun ReceivedAfternoteAuthResponseDto.toReceivedAfternote(): ReceivedAfternote =
    ReceivedAfternote(
        id = id,
        sourceType = category.orEmpty(),
        lastUpdatedAt = createdAt.orEmpty(),
        leaveMessage = leaveMessage
    )

fun ReceivedAfternoteSongInfoDto.toReceivedPlaylistSong(): ReceivedPlaylistSong =
    ReceivedPlaylistSong(
        title = title.orEmpty(),
        artist = artist.orEmpty(),
        coverUrl = coverUrl
    )

fun ReceivedAfternoteDetailAuthResponseDto.toReceivedAfternoteDetail(): ReceivedAfternoteDetail =
    ReceivedAfternoteDetail(
        id = id,
        category = category,
        title = title,
        playlist = playlist?.let { p ->
            ReceivedAfternotePlaylist(
                atmosphere = p.atmosphere,
                songs = p.songs.map { it.toReceivedPlaylistSong() }
            )
        }
    )
