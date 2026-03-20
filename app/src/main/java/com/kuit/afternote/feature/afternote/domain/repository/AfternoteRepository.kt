package com.kuit.afternote.feature.afternote.domain.repository

import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetail
import com.kuit.afternote.feature.afternote.domain.model.AfternoteUpdateRequestInput
import com.kuit.afternote.feature.afternote.domain.model.PagedAfternotes
import com.kuit.afternote.feature.afternote.domain.model.playlist.AfternotePlaylistInput

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
        size: Int,
    ): Result<PagedAfternotes>

    suspend fun createSocial(
        title: String,
        processMethod: String,
        actions: List<String>,
        leaveMessage: String?,
        credentialsId: String?,
        credentialsPassword: String?,
        receiverIds: List<Long> = emptyList(),
    ): Result<Long>

    suspend fun createGallery(
        title: String,
        processMethod: String,
        actions: List<String>,
        leaveMessage: String?,
        receiverIds: List<Long>,
    ): Result<Long>

    suspend fun getAfternoteDetail(afternoteId: Long): Result<AfternoteDetail>

    suspend fun createPlaylist(
        title: String,
        playlist: AfternotePlaylistInput,
        receiverIds: List<Long> = emptyList(),
    ): Result<Long>

    suspend fun updateAfternote(
        afternoteId: Long,
        input: AfternoteUpdateRequestInput,
    ): Result<Long>

    suspend fun deleteAfternote(afternoteId: Long): Result<Unit>
}
