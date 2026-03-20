package com.kuit.afternote.feature.afternote.data.mapper

import com.kuit.afternote.domain.model.AfternoteServiceType
import com.kuit.afternote.feature.afternote.data.dto.AfternoteListItem
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
object AfternoteMapper {
    // -- List mapping --

    fun toDomain(dto: AfternoteListItem): Item =
        Item(
            id = dto.afternoteId.toString(),
            serviceName = dto.title,
            date = formatDateFromServer(dto.createdAt),
            type = categoryToServiceType(dto.category),
        )

    fun toDomainList(dtos: List<AfternoteListItem>): List<Item> = dtos.map { toDomain(it) }

    // -- Detail mapping --

    fun toDetailDomain(dto: AfternoteDetailResponse): Detail =
        Detail(
            id = dto.afternoteId,
            category = dto.category,
            title = dto.title,
            timestamps =
                DetailTimestamps(
                    createdAt = formatDateFromServer(dto.createdAt),
                    updatedAt = formatDateFromServer(dto.createdAt),
                ),
            type = categoryToServiceType(dto.category),
            credentials =
                DetailCredentials(
                    id = dto.credentials?.id,
                    password = dto.credentials?.password,
                ),
            receivers =
                dto.receivers?.map { r ->
                    DetailReceiver(
                        receiverId = r.receiverId,
                        name = r.name ?: "",
                        relation = r.relation ?: "",
                        phone = r.phone ?: "",
                    )
                } ?: emptyList(),
            processing =
                DetailProcessing(
                    method = dto.processMethod,
                    actions = dto.actions ?: emptyList(),
                    leaveMessage = dto.leaveMessage,
                ),
            playlist =
                dto.playlist?.let { p ->
                    PlaylistDetail(
                        profilePhoto = p.profilePhoto,
                        atmosphere = p.atmosphere,
                        songs =
                            p.songs.map { s ->
                                DetailSong(
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
