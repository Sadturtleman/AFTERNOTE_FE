package com.kuit.afternote.feature.afternote.data.repository

import com.kuit.afternote.data.remote.requireData
import com.kuit.afternote.feature.afternote.data.api.AfternoteApiService
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreateGalleryRequestDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCreateSocialRequestDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentialsDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRefDto
import com.kuit.afternote.feature.afternote.data.mapper.AfternoteMapper
import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem
import javax.inject.Inject

/**
 * Data layer: calls API, maps DTO → domain at boundary.
 * Repository interface is defined by feature owner (Domain); this impl is for use once wired.
 */
class AfternoteRepositoryImpl
    @Inject
    constructor(
        private val api: AfternoteApiService
    ) {

    suspend fun getAfternotes(
        category: String?,
        page: Int,
        size: Int
    ): Result<List<AfternoteItem>> = runCatching {
        val response = api.getAfternotes(category = category, page = page, size = size)
        val data = response.requireData()
        AfternoteMapper.toDomainList(data.content)
    }

    suspend fun createSocial(
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
        response.requireData()?.afternoteId ?: 0L
    }

    suspend fun createGallery(
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
        response.requireData()?.afternoteId ?: 0L
    }
}
