package com.kuit.afternote.feature.afternote.domain.repository

import com.kuit.afternote.feature.afternote.domain.model.CreateGalleryInput
import com.kuit.afternote.feature.afternote.domain.model.CreateSocialInput
import com.kuit.afternote.feature.afternote.domain.model.Detail
import com.kuit.afternote.feature.afternote.domain.model.PagedAfternotes
import com.kuit.afternote.feature.afternote.domain.model.UpdateRequestInput
import com.kuit.afternote.feature.afternote.domain.model.playlist.PlaylistInput

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

    suspend fun createSocial(input: CreateSocialInput): Result<Long>

    suspend fun createGallery(input: CreateGalleryInput): Result<Long>

    suspend fun getAfternoteDetail(afternoteId: Long): Result<Detail>

    suspend fun createPlaylist(
        title: String,
        playlist: PlaylistInput,
        receiverIds: List<Long> = emptyList(),
    ): Result<Long>

    suspend fun updateAfternote(
        afternoteId: Long,
        input: UpdateRequestInput,
    ): Result<Long>

    suspend fun deleteAfternote(afternoteId: Long): Result<Unit>
}
