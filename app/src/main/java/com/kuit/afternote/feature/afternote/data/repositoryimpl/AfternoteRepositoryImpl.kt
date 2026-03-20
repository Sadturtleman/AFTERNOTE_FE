package com.kuit.afternote.feature.afternote.data.repositoryimpl

import android.util.Log
import com.kuit.afternote.data.dto.response.requireData
import com.kuit.afternote.data.dto.response.requireStatus
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRef
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteCreateGalleryRequest
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteCreatePlaylistRequest
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteCreateSocialRequest
import com.kuit.afternote.feature.afternote.data.dto.response.AfternoteIdResponse
import com.kuit.afternote.feature.afternote.data.mapper.AfternoteMapper
import com.kuit.afternote.feature.afternote.data.mapper.toDto
import com.kuit.afternote.feature.afternote.data.service.AfternoteApiService
import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetail
import com.kuit.afternote.feature.afternote.domain.model.CreateGalleryInput
import com.kuit.afternote.feature.afternote.domain.model.CreateSocialInput
import com.kuit.afternote.feature.afternote.domain.model.PagedAfternotes
import com.kuit.afternote.feature.afternote.domain.model.UpdateRequestInput
import com.kuit.afternote.feature.afternote.domain.model.playlist.PlaylistInput
import com.kuit.afternote.feature.afternote.domain.repository.AfternoteRepository
import javax.inject.Inject

/**
 * Data layer: calls Afternote API, maps DTO → domain at boundary.
 *
 * API spec: GET/POST /afternotes, GET/PATCH/DELETE /afternotes/{id}.
 */
class AfternoteRepositoryImpl
    @Inject
    constructor(
        private val api: AfternoteApiService,
    ) : AfternoteRepository {
        override suspend fun getAfternotes(
            category: String?,
            page: Int,
            size: Int,
        ): Result<PagedAfternotes> =
            runCatching {
                val response = api.getAfternotes(category = category, page = page, size = size)
                val data = response.requireData()
                AfternoteMapper.toPagedNotes(data)
            }.logFailure()

        override suspend fun createSocial(input: CreateSocialInput): Result<Long> =
            runCatching {
                val request =
                    AfternoteCreateSocialRequest(
                        category = "SOCIAL",
                        title = input.title,
                        processMethod = input.processMethod,
                        actions = input.actions,
                        leaveMessage = input.leaveMessage,
                        credentials = input.credentials?.toDto(),
                        receivers = input.receiverIds.map { AfternoteReceiverRef(receiverId = it) },
                    )
                val response = api.createAfternoteSocial(request)
                val data = response.requireData()
                getAfternoteId(data)
            }.logFailure()

        override suspend fun createGallery(input: CreateGalleryInput): Result<Long> =
            runCatching {
                val request =
                    AfternoteCreateGalleryRequest(
                        category = "GALLERY",
                        title = input.title,
                        processMethod = input.processMethod,
                        actions = input.actions,
                        leaveMessage = input.leaveMessage,
                        receivers = input.receiverIds.map { AfternoteReceiverRef(receiverId = it) },
                    )
                val response = api.createAfternoteGallery(request)
                val data = response.requireData()
                getAfternoteId(data)
            }.logFailure()

        /**
         * GET /afternotes/{afternoteId} — 상세 조회. DTO → domain 매핑 포함.
         */
        override suspend fun getAfternoteDetail(afternoteId: Long): Result<AfternoteDetail> =
            runCatching {
                val response = api.getAfternoteDetail(afternoteId = afternoteId)
                val data = response.requireData()
                AfternoteMapper.toDetailDomain(data)
            }.logFailure()

        /**
         * POST /afternotes (PLAYLIST category).
         */
        override suspend fun createPlaylist(
            title: String,
            playlist: PlaylistInput,
            receiverIds: List<Long>,
        ): Result<Long> =
            runCatching {
                val request =
                    AfternoteCreatePlaylistRequest(
                        category = "PLAYLIST",
                        title = title,
                        playlist = playlist.toDto(),
                        receivers = receiverIds.map { AfternoteReceiverRef(receiverId = it) },
                    )
                val response = api.createAfternotePlaylist(request)
                val data = response.requireData()
                getAfternoteId(data)
            }.logFailure()

        /**
         * PATCH /afternotes/{afternoteId} — 부분 수정 (수정할 필드만 전송).
         */
        override suspend fun updateAfternote(
            afternoteId: Long,
            input: UpdateRequestInput,
        ): Result<Long> =
            runCatching {
                val response = api.updateAfternote(afternoteId = afternoteId, request = input.toDto())
                val data = response.requireData()
                getAfternoteId(data)
            }.logFailure()

        /**
         * DELETE /afternotes/{afternoteId}.
         */
        override suspend fun deleteAfternote(afternoteId: Long): Result<Unit> =
            runCatching {
                val response = api.deleteAfternote(afternoteId = afternoteId)
                response.requireStatus()
            }.logFailure()
    }

private fun getAfternoteId(data: AfternoteIdResponse) = data.afternoteId

private fun <T> Result<T>.logFailure() =
    onFailure { error ->
        val message = error.message
        val msg = message.toString()
        Log.e("AfternoteRepository", msg)
    }
