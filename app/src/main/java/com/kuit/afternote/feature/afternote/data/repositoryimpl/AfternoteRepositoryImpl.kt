package com.kuit.afternote.feature.afternote.data.repositoryimpl

import android.util.Log
import com.kuit.afternote.data.requireData
import com.kuit.afternote.data.requireStatus
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentials
import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylist
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRef
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteCreateGalleryRequest
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteCreatePlaylistRequest
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteCreateSocialRequest
import com.kuit.afternote.feature.afternote.data.dto.request.AfternoteUpdateRequest
import com.kuit.afternote.feature.afternote.data.mapper.AfternoteMapper
import com.kuit.afternote.feature.afternote.data.service.AfternoteApiService
import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetail
import com.kuit.afternote.feature.afternote.domain.model.PagedAfternotes
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
        private fun <T> Result<T>.logFailure() =
            onFailure { error ->
                Log.e("AfternoteRepository", error.message.toString())
            }

        override suspend fun getAfternotes(
            category: String?,
            page: Int,
            size: Int,
        ): Result<PagedAfternotes> =
            runCatching {
                val response = api.getAfternotes(category = category, page = page, size = size)
                val data = response.requireData()
                val content = data.content
                val hasNext = data.hasNext
                PagedAfternotes(items = AfternoteMapper.toDomainList(content), hasNext = hasNext)
            }.logFailure()

        override suspend fun createSocial(
            title: String,
            processMethod: String,
            actions: List<String>,
            leaveMessage: String?,
            credentialsId: String?,
            credentialsPassword: String?,
            receiverIds: List<Long>,
        ): Result<Long> =
            runCatching {
                val request =
                    AfternoteCreateSocialRequest(
                        category = "SOCIAL",
                        title = title,
                        processMethod = processMethod,
                        actions = actions,
                        leaveMessage = leaveMessage,
                        credentials =
                            when {
                                credentialsId != null || credentialsPassword != null -> {
                                    AfternoteCredentials(
                                        id = credentialsId,
                                        password = credentialsPassword,
                                    )
                                }

                                else -> {
                                    null
                                }
                            },
                        receivers = receiverIds.map { AfternoteReceiverRef(receiverId = it) },
                    )
                val response = api.createAfternoteSocial(request)
                val data = response.requireData()
                data.afternoteId
            }.logFailure()

        override suspend fun createGallery(
            title: String,
            processMethod: String,
            actions: List<String>,
            leaveMessage: String?,
            receiverIds: List<Long>,
        ): Result<Long> =
            runCatching {
                val request =
                    AfternoteCreateGalleryRequest(
                        category = "GALLERY",
                        title = title,
                        processMethod = processMethod,
                        actions = actions,
                        leaveMessage = leaveMessage,
                        receivers = receiverIds.map { AfternoteReceiverRef(receiverId = it) },
                    )
                val response = api.createAfternoteGallery(request)
                val data = response.requireData()
                data.afternoteId
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
            playlist: AfternotePlaylist,
            receiverIds: List<Long>,
        ): Result<Long> =
            runCatching {
                val request =
                    AfternoteCreatePlaylistRequest(
                        category = "PLAYLIST",
                        title = title,
                        playlist = playlist,
                        receivers = receiverIds.map { AfternoteReceiverRef(receiverId = it) },
                    )
                val response = api.createAfternotePlaylist(request)
                val data = response.requireData()
                data.afternoteId
            }.logFailure()

        /**
         * PATCH /afternotes/{afternoteId} — 부분 수정 (수정할 필드만 전송).
         */
        override suspend fun updateAfternote(
            afternoteId: Long,
            request: AfternoteUpdateRequest,
        ): Result<Long> =
            runCatching {
                val response = api.updateAfternote(afternoteId = afternoteId, request = request)
                val data = response.requireData()
                data.afternoteId
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
