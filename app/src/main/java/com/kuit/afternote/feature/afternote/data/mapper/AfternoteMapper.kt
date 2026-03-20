package com.kuit.afternote.feature.afternote.data.mapper

import com.kuit.afternote.domain.model.AfternoteServiceType
import com.kuit.afternote.feature.afternote.data.dto.AfternoteListItem
import com.kuit.afternote.feature.afternote.data.dto.response.AfternoteDetailResponse
import com.kuit.afternote.feature.afternote.data.dto.response.AfternoteListResponse
import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetail
import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetailCredentials
import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetailProcessing
import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetailReceiver
import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetailTimestamps
import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem
import com.kuit.afternote.feature.afternote.domain.model.PagedAfternotes
import com.kuit.afternote.feature.afternote.domain.model.playlist.AfternoteDetailSong
import com.kuit.afternote.feature.afternote.domain.model.playlist.AfternotePlaylistDetail
import com.kuit.afternote.feature.afternote.domain.model.playlist.PlaylistDetailMemorialMedia

/**
 * Maps server DTOs to domain models at the boundary only.
 */
object AfternoteMapper {
    // -- List mapping --

    fun toDomain(dto: AfternoteListItem): AfternoteItem =
        AfternoteItem(
            id = dto.afternoteId.toString(),
            serviceName = dto.title,
            date = formatDateFromServer(dto.createdAt),
            type = categoryToServiceType(dto.category),
        )

    fun toDomainList(dtos: List<AfternoteListItem>): List<AfternoteItem> = dtos.map { toDomain(it) }

    // -- Detail mapping --

    fun toDetailDomain(dto: AfternoteDetailResponse): AfternoteDetail =
        AfternoteDetail(
            id = dto.afternoteId,
            category = dto.category,
            title = dto.title,
            timestamps =
                AfternoteDetailTimestamps(
                    createdAt = formatDateFromServer(dto.createdAt),
                    updatedAt = formatDateFromServer(dto.createdAt),
                ),
            type = categoryToServiceType(dto.category),
            credentials =
                AfternoteDetailCredentials(
                    id = dto.credentials?.id,
                    password = dto.credentials?.password,
                ),
            receivers =
                dto.receivers?.map { r ->
                    AfternoteDetailReceiver(
                        receiverId = r.receiverId,
                        name = r.name ?: "",
                        relation = r.relation ?: "",
                        phone = r.phone ?: "",
                    )
                } ?: emptyList(),
            processing =
                AfternoteDetailProcessing(
                    method = dto.processMethod,
                    actions = dto.actions ?: emptyList(),
                    leaveMessage = dto.leaveMessage,
                ),
            playlist =
                dto.playlist?.let { p ->
                    AfternotePlaylistDetail(
                        profilePhoto = p.profilePhoto,
                        atmosphere = p.atmosphere,
                        songs =
                            p.songs.map { s ->
                                AfternoteDetailSong(
                                    id = s.id,
                                    title = s.title,
                                    artist = s.artist,
                                    coverUrl = s.coverUrl,
                                )
                            },
                        playlistDetailMemorialMedia =
                            PlaylistDetailMemorialMedia(
                                photoUrl = p.memorialPhotoUrl ?: p.profilePhoto,
                                videoUrl = p.memorialVideo?.videoUrl,
                                thumbnailUrl = p.memorialVideo?.thumbnailUrl,
                            ),
                    )
                },
        )

    fun toPagedNotes(data: AfternoteListResponse) =
        PagedAfternotes(
            items = toDomainList(data.content),
            hasNext = data.hasNext,
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
}
