package com.kuit.afternote.feature.afternote.data.mapper

import com.kuit.afternote.feature.afternote.data.dto.AfternoteDetailResponseDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteListItemDto
import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetail
import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetailReceiver
import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetailSong
import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem
import com.kuit.afternote.feature.afternote.domain.model.AfternotePlaylistDetail
import com.kuit.afternote.feature.afternote.domain.model.ServiceType

/**
 * Maps server DTOs to domain models at the boundary only.
 */
object AfternoteMapper {

    // -- List mapping --

    fun toDomain(dto: AfternoteListItemDto): AfternoteItem =
        AfternoteItem(
            id = dto.afternoteId.toString(),
            serviceName = dto.title,
            date = formatDateFromServer(dto.createdAt),
            type = categoryToServiceType(dto.category),
            processingMethods = emptyList(),
            galleryProcessingMethods = emptyList()
        )

    fun toDomainList(dtos: List<AfternoteListItemDto>): List<AfternoteItem> =
        dtos.map { toDomain(it) }

    // -- Detail mapping --

    fun toDetailDomain(dto: AfternoteDetailResponseDto): AfternoteDetail =
        AfternoteDetail(
            id = dto.afternoteId,
            category = dto.category,
            title = dto.title,
            createdAt = formatDateFromServer(dto.createdAt),
            updatedAt = formatDateFromServer(dto.updatedAt),
            type = categoryToServiceType(dto.category),
            credentialsId = dto.credentials?.id,
            credentialsPassword = dto.credentials?.password,
            receivers = dto.receivers?.map { r ->
                AfternoteDetailReceiver(
                    name = r.name ?: "",
                    relation = r.relation ?: "",
                    phone = r.phone ?: ""
                )
            } ?: emptyList(),
            processMethod = dto.processMethod,
            actions = dto.actions ?: emptyList(),
            leaveMessage = dto.leaveMessage,
            playlist = dto.playlist?.let { p ->
                AfternotePlaylistDetail(
                    profilePhoto = p.profilePhoto,
                    atmosphere = p.atmosphere,
                    songs = p.songs.map { s ->
                        AfternoteDetailSong(
                            id = s.id,
                            title = s.title,
                            artist = s.artist,
                            coverUrl = s.coverUrl
                        )
                    },
                    memorialVideoUrl = p.memorialVideo?.videoUrl,
                    memorialThumbnailUrl = p.memorialVideo?.thumbnailUrl
                )
            }
        )

    // -- Internal helpers --

    private fun formatDateFromServer(createdAt: String): String {
        return try {
            // Server returns "2025-11-26T14:30:00" -> display "2025.11.26"
            val datePart = createdAt.substringBefore('T')
            datePart.replace('-', '.')
        } catch (_: Exception) {
            createdAt
        }
    }

    internal fun categoryToServiceType(category: String): ServiceType =
        when (category.uppercase()) {
            "SOCIAL" -> ServiceType.SOCIAL_NETWORK
            "GALLERY" -> ServiceType.GALLERY_AND_FILES
            "MUSIC", "PLAYLIST" -> ServiceType.MEMORIAL
            "BUSINESS" -> ServiceType.BUSINESS
            else -> ServiceType.OTHER
        }
}
