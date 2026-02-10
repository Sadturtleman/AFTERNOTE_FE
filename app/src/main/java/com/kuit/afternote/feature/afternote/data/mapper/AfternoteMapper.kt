package com.kuit.afternote.feature.afternote.data.mapper

import android.util.Log
import com.kuit.afternote.feature.afternote.data.dto.AfternoteListItemDto
import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem
import com.kuit.afternote.feature.afternote.domain.model.ServiceType

/**
 * Maps server list form (DTO) to domain at the boundary only.
 * Server: afternoteId, title, category, createdAt.
 */
object AfternoteMapper {
    private const val TAG = "AfternoteMapper"
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

    private fun formatDateFromServer(createdAt: String): String {
        return try {
            // Server returns "2025-11-26T14:30:00" -> display "2025.11.26"
            val datePart = createdAt.substringBefore('T')
            datePart.replace('-', '.')
        } catch (_: Exception) {
            createdAt
        }
    }

    private fun categoryToServiceType(category: String): ServiceType {
        val type =
            when (category.uppercase()) {
                "SOCIAL" -> ServiceType.SOCIAL_NETWORK
                "GALLERY" -> ServiceType.GALLERY_AND_FILES
                "MUSIC", "PLAYLIST" -> ServiceType.MEMORIAL
                "BUSINESS" -> ServiceType.BUSINESS
                else -> ServiceType.OTHER
            }
        Log.d(TAG, "categoryToServiceType: category=$category -> type=$type")
        return type
    }
}
