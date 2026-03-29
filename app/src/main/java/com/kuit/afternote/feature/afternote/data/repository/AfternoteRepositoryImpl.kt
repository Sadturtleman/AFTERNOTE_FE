package com.kuit.afternote.feature.afternote.data.repository

import android.util.Log
import com.kuit.afternote.core.data.dto.response.requireData
import com.kuit.afternote.core.data.dto.response.requireStatus
import com.kuit.afternote.feature.afternote.data.dto.response.AfternoteIdResponse
import com.kuit.afternote.feature.afternote.data.mapper.response.toDetailDomain
import com.kuit.afternote.feature.afternote.data.mapper.response.toPagedNotes
import com.kuit.afternote.feature.afternote.data.mapper.toRequest
import com.kuit.afternote.feature.afternote.data.service.AfternoteApiService
import com.kuit.afternote.feature.afternote.domain.model.CreateGalleryInput
import com.kuit.afternote.feature.afternote.domain.model.CreateSocialInput
import com.kuit.afternote.feature.afternote.domain.model.Detail
import com.kuit.afternote.feature.afternote.domain.model.GetAfternotesInput
import com.kuit.afternote.feature.afternote.domain.model.PagedAfternotes
import com.kuit.afternote.feature.afternote.domain.model.UpdateRequestInput
import com.kuit.afternote.feature.afternote.domain.model.playlist.CreatePlaylistInput
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
        override suspend fun getAfternotes(input: GetAfternotesInput): Result<PagedAfternotes> =
            runCatching {
                val response =
                    api.getAfternotes(
                        category = input.category,
                        page = input.page,
                        size = input.size,
                    )
                val data = response.requireData()
                data.toPagedNotes()
            }.logFailure()

        override suspend fun createSocial(input: CreateSocialInput): Result<Long> =
            runCatching {
                val request = input.toRequest()
                val response = api.createAfternoteSocial(request)
                val data = response.requireData()
                getAfternoteId(data)
            }.logFailure()

        override suspend fun createGallery(input: CreateGalleryInput): Result<Long> =
            runCatching {
                val request = input.toRequest()
                val response = api.createAfternoteGallery(request)
                val data = response.requireData()
                getAfternoteId(data)
            }.logFailure()

        /**
         * GET /afternotes/{afternoteId} — 상세 조회. DTO → domain 매핑 포함.
         */
        override suspend fun getAfternoteDetail(afternoteId: Long): Result<Detail> =
            runCatching {
                val response = api.getAfternoteDetail(afternoteId = afternoteId)
                val data = response.requireData()
                data.toDetailDomain()
            }.logFailure()

        /**
         * POST /afternotes (PLAYLIST category).
         */
        override suspend fun createPlaylist(input: CreatePlaylistInput): Result<Long> =
            runCatching {
                val request = input.toRequest()
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
        ): Result<Long> {
            val request = input.toRequest()
            return runCatching {
                val response = api.updateAfternote(afternoteId = afternoteId, request = request)
                val data = response.requireData()
                getAfternoteId(data)
            }.logFailure()
        }

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
