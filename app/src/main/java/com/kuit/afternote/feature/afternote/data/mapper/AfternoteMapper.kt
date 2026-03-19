package com.kuit.afternote.feature.afternote.data.mapper

import com.kuit.afternote.core.domain.model.AfternoteServiceType
import com.kuit.afternote.feature.afternote.data.dto.AfternoteDetailResponse
import com.kuit.afternote.feature.afternote.data.dto.AfternoteListItem
import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetail
import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetailReceiver
import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetailSong
import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem
import com.kuit.afternote.feature.afternote.domain.model.AfternotePlaylistDetail

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
            processingMethods = emptyList(),
            galleryProcessingMethods = emptyList(),
        )

    fun toDomainList(dtos: List<AfternoteListItem>): List<AfternoteItem> = dtos.map { toDomain(it) }

    // -- Detail mapping --

    fun toDetailDomain(dto: AfternoteDetailResponse): AfternoteDetail =
        AfternoteDetail(
            id = dto.afternoteId,
            category = dto.category,
            title = dto.title,
            createdAt = formatDateFromServer(dto.createdAt),
            updatedAt = formatDateFromServer(dto.updatedAt),
            type = categoryToServiceType(dto.category),
            credentialsId = dto.credentials?.id,
            credentialsPassword = dto.credentials?.password,
            receivers =
                dto.receivers?.map { r ->
                    AfternoteDetailReceiver(
                        receiverId = r.receiverId,
                        name = r.name ?: "",
                        relation = r.relation ?: "",
                        phone = r.phone ?: "",
                    )
                } ?: emptyList(),
            processMethod = dto.processMethod,
            actions = dto.actions ?: emptyList(),
            leaveMessage = dto.leaveMessage,
            playlist =
                dto.playlist?.let { p ->
                    AfternotePlaylistDetail(
                        profilePhoto = p.profilePhoto,
                        atmosphere = p.atmosphere,
                        memorialPhotoUrl = p.memorialPhotoUrl ?: p.profilePhoto,
                        songs =
                            p.songs.map { s ->
                                AfternoteDetailSong(
                                    id = s.id,
                                    title = s.title,
                                    artist = s.artist,
                                    coverUrl = s.coverUrl,
                                )
                            },
                        memorialVideoUrl = p.memorialVideo?.videoUrl,
                        memorialThumbnailUrl = p.memorialVideo?.thumbnailUrl,
                    )
                },
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
