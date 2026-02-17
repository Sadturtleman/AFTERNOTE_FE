package com.kuit.afternote.feature.afternote.data.repository

import android.util.Log
import com.kuit.afternote.data.remote.requireData
import com.kuit.afternote.data.remote.requireSuccess
import com.kuit.afternote.feature.afternote.data.api.AfternoteApiService
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreateGalleryRequestDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreatePlaylistRequestDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreateSocialRequestDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentialsDto
import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylistDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRefDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteUpdateRequestDto
import com.kuit.afternote.feature.afternote.data.mapper.AfternoteMapper
import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetail
import com.kuit.afternote.feature.afternote.domain.model.PagedAfternotes
import com.kuit.afternote.feature.afternote.domain.repository.iface.AfternoteRepository
import javax.inject.Inject

private const val TAG = "AfternoteRepo"

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
    ): Result<PagedAfternotes> = runCatching {
        val response = api.getAfternotes(category = category, page = page, size = size)
        val data = response.requireData()
        val content = data?.content.orEmpty()
        val hasNext = data?.hasNext ?: false
        PagedAfternotes(items = AfternoteMapper.toDomainList(content), hasNext = hasNext)
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
        Log.d(
            TAG,
            "createSocial: title=${body.title}, processMethod=${body.processMethod}, " +
                "actions=${body.actions}, hasCredentials=${body.credentials != null}"
        )
        val response = api.createAfternoteSocial(body)
        Log.d(
            TAG,
            "createSocial response: status=${response.status}, code=${response.code}, " +
                "message=${response.message}, data=${response.data}"
        )
        response.requireData().afternoteId
    }.also { result ->
        result.onFailure { e ->
            if (e is retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e(TAG, "createSocial: FAILED ${e.code()} body=$errorBody", e)
            } else {
                Log.e(TAG, "createSocial: FAILED", e)
            }
        }
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
        Log.d(
            TAG,
            "createGallery: title=${body.title}, processMethod=${body.processMethod}, " +
                "actions=${body.actions}, receiverIds=$receiverIds"
        )
        val response = api.createAfternoteGallery(body)
        Log.d(
            TAG,
            "createGallery response: status=${response.status}, code=${response.code}, " +
                "message=${response.message}, data=${response.data}"
        )
        response.requireData().afternoteId
    }.also { result ->
        result.onFailure { e ->
            if (e is retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e(TAG, "createGallery: FAILED ${e.code()} body=$errorBody", e)
            } else {
                Log.e(TAG, "createGallery: FAILED", e)
            }
        }
    }

    /**
     * GET /afternotes/{afternoteId} — 상세 조회. DTO → domain 매핑 포함.
     */
    override suspend fun getAfternoteDetail(afternoteId: Long): Result<AfternoteDetail> =
        runCatching {
            val response = api.getAfternoteDetail(afternoteId = afternoteId)
            AfternoteMapper.toDetailDomain(response.requireData())
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
        Log.d(
            TAG,
            "createPlaylist: title=${body.title}, songsCount=${playlist.songs.size}"
        )
        val response = api.createAfternotePlaylist(body)
        Log.d(
            TAG,
            "createPlaylist response: status=${response.status}, code=${response.code}, " +
                "message=${response.message}, data=${response.data}"
        )
        response.requireData().afternoteId
    }.also { result ->
        result.onFailure { e ->
            Log.e(TAG, "createPlaylist: FAILED", e)
        }
    }

    /**
     * PATCH /afternotes/{afternoteId} — 부분 수정 (수정할 필드만 전송).
     */
    override suspend fun updateAfternote(
        afternoteId: Long,
        body: AfternoteUpdateRequestDto
    ): Result<Long> = runCatching {
        Log.d(
            TAG,
            "updateAfternote: id=$afternoteId, title=${body.title}, " +
                "processMethod=${body.processMethod}, actions=${body.actions}"
        )
        val response = api.updateAfternote(afternoteId = afternoteId, body = body)
        Log.d(
            TAG,
            "updateAfternote response: status=${response.status}, code=${response.code}, " +
                "message=${response.message}, data=${response.data}"
        )
        response.requireData().afternoteId
    }.also { result ->
        result.onFailure { e ->
            Log.e(TAG, "updateAfternote: FAILED, id=$afternoteId", e)
        }
    }

    /**
     * DELETE /afternotes/{afternoteId}.
     */
    override suspend fun deleteAfternote(afternoteId: Long): Result<Unit> = runCatching {
        api.deleteAfternote(afternoteId = afternoteId).requireSuccess()
    }
}
