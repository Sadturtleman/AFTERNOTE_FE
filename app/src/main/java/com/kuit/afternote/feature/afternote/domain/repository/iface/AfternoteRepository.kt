package com.kuit.afternote.feature.afternote.domain.repository.iface

import com.kuit.afternote.feature.afternote.data.dto.AfternoteDetailResponseDto
import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylistDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteUpdateRequestDto
import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem

/**
 * Afternote 도메인 Repository 인터페이스.
 *
 * API spec 기준:
 * - GET /afternotes (목록)
 * - GET /afternotes/{afternoteId} (상세)
 * - POST /afternotes (SOCIAL / GALLERY / PLAYLIST 생성)
 * - PATCH /afternotes/{afternoteId} (수정)
 * - DELETE /afternotes/{afternoteId} (삭제)
 */
interface AfternoteRepository {

    suspend fun getAfternotes(
        category: String?,
        page: Int,
        size: Int
    ): Result<List<AfternoteItem>>

    suspend fun createSocial(
        title: String,
        processMethod: String,
        actions: List<String>,
        leaveMessage: String?,
        credentialsId: String?,
        credentialsPassword: String?
    ): Result<Long>

    suspend fun createGallery(
        title: String,
        processMethod: String,
        actions: List<String>,
        leaveMessage: String?,
        receiverIds: List<Long>
    ): Result<Long>

    suspend fun getAfternoteDetail(afternoteId: Long): Result<AfternoteDetailResponseDto>

    suspend fun createPlaylist(
        title: String,
        playlist: AfternotePlaylistDto
    ): Result<Long>

    suspend fun updateAfternote(
        afternoteId: Long,
        body: AfternoteUpdateRequestDto
    ): Result<Long>

    suspend fun deleteAfternote(afternoteId: Long): Result<Unit>
}

