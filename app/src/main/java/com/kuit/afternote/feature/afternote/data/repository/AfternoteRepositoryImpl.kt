package com.kuit.afternote.feature.afternote.data.repository

import com.kuit.afternote.data.remote.requireData
import com.kuit.afternote.data.remote.requireSuccess
import com.kuit.afternote.feature.afternote.data.api.AfternoteApiService
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreateGalleryRequestDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreatePlaylistRequestDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreateSocialRequestDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentialsDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteDetailResponseDto
import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylistDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRefDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteUpdateRequestDto
import com.kuit.afternote.feature.afternote.data.mapper.AfternoteMapper
import com.kuit.afternote.feature.afternote.domain.repository.iface.AfternoteRepository
import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem
import javax.inject.Inject

/**
 * Data layer: calls Afternote API, maps DTO → domain at boundary.
 *
 * API spec: GET/POST /afternotes, GET/PATCH/DELETE /afternotes/{id}.
 */
class AfternoteRepositoryImpl
    @Inject
    constructor(
        private val api: AfternoteApiService
    ) : AfternoteRepository {

    override suspend fun getAfternotes(
        category: String?,
        page: Int,
        size: Int
    ): Result<List<AfternoteItem>> = runCatching {
        val response = api.getAfternotes(category = category, page = page, size = size)
        val data = response.requireData()
        AfternoteMapper.toDomainList(data.content)
    }

    override suspend fun createSocial(
        title: String,
        processMethod: String,
        actions: List<String>,
        leaveMessage: String?,
        credentialsId: String?,
        credentialsPassword: String?
    ): Result<Long> = runCatching {
        val body = AfternoteCreateSocialRequestDto(
            category = "SOCIAL",
            title = title,
            processMethod = processMethod,
            actions = actions,
            leaveMessage = leaveMessage,
            credentials = when {
                credentialsId != null || credentialsPassword != null -> AfternoteCredentialsDto(
                    id = credentialsId,
                    password = credentialsPassword
                )
                else -> null
            }
        )
        val response = api.createAfternoteSocial(body)
        response.requireData().afternoteId
    }

    override suspend fun createGallery(
        title: String,
        processMethod: String,
        actions: List<String>,
        leaveMessage: String?,
        receiverIds: List<Long>
    ): Result<Long> = runCatching {
        val body = AfternoteCreateGalleryRequestDto(
            category = "GALLERY",
            title = title,
            processMethod = processMethod,
            actions = actions,
            leaveMessage = leaveMessage,
            receivers = receiverIds.map { AfternoteReceiverRefDto(receiverId = it) }
        )
        val response = api.createAfternoteGallery(body)
        response.requireData().afternoteId
    }

    /**
     * GET /afternotes/{afternoteId} — 상세 조회.
     */
    override suspend fun getAfternoteDetail(afternoteId: Long): Result<AfternoteDetailResponseDto> =
        runCatching {
            val response = api.getAfternoteDetail(afternoteId = afternoteId)
            response.requireData()
        }

    /**
     * POST /afternotes (PLAYLIST category).
     */
    override suspend fun createPlaylist(
        title: String,
        playlist: AfternotePlaylistDto
    ): Result<Long> = runCatching {
        val body = AfternoteCreatePlaylistRequestDto(
            category = "PLAYLIST",
            title = title,
            playlist = playlist
        )
        val response = api.createAfternotePlaylist(body)
        response.requireData().afternoteId
    }

    /**
     * PATCH /afternotes/{afternoteId} — 부분 수정 (수정할 필드만 전송).
     */
    override suspend fun updateAfternote(
        afternoteId: Long,
        body: AfternoteUpdateRequestDto
    ): Result<Long> = runCatching {
        val response = api.updateAfternote(afternoteId = afternoteId, body = body)
        response.requireData().afternoteId
    }

    /**
     * DELETE /afternotes/{afternoteId}.
     */
    override suspend fun deleteAfternote(afternoteId: Long): Result<Unit> = runCatching {
        api.deleteAfternote(afternoteId = afternoteId).requireSuccess()
    }
}
