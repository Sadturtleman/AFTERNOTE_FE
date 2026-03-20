package com.kuit.afternote.feature.afternote.data.mapper

import com.kuit.afternote.domain.model.AfternoteServiceType
import com.kuit.afternote.feature.afternote.data.dto.AfternoteListItem
import com.kuit.afternote.feature.afternote.domain.model.Item

/**
 * Maps server DTOs to domain models at the boundary only.
 */

fun AfternoteListItem.toDomain() =
    Item(
        id = afternoteId.toString(),
        serviceName = title,
        date = formatDateFromServer(createdAt),
        type = categoryToServiceType(category),
    )

fun List<AfternoteListItem>.toDomainList() = map { it.toDomain() }

// -- Internal helpers --

internal fun formatDateFromServer(serverDateTime: String): String =
    try {
        // Server returns "2025-11-26T14:30:00" -> display "2025.11.26"
        val datePart = serverDateTime.substringBefore('T')
        datePart.replace('-', '.')
    } catch (_: Exception) {
        serverDateTime
    }

internal fun categoryToServiceType(category: String): AfternoteServiceType =
    when (category.uppercase()) {
        "SOCIAL" -> AfternoteServiceType.SOCIAL_NETWORK
        "GALLERY" -> AfternoteServiceType.GALLERY_AND_FILES
        "MUSIC", "PLAYLIST" -> AfternoteServiceType.MEMORIAL
        else -> AfternoteServiceType.SOCIAL_NETWORK
    }
