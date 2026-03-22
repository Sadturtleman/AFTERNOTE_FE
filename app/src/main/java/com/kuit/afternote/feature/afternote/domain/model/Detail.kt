package com.kuit.afternote.feature.afternote.domain.model

import com.kuit.afternote.domain.model.AfternoteServiceType
import com.kuit.afternote.feature.afternote.domain.model.playlist.PlaylistDetail

/**
 * 애프터노트 상세 도메인 모델.
 *
 * 서버 GET /api/afternotes/{id} 응답을 도메인 계층으로 매핑한 형태.
 * 카테고리별로 관련 필드만 non-null:
 * - SOCIAL: credentials, processMethod, actions
 * - GALLERY: receivers, processMethod, actions
 * - PLAYLIST: playlist
 */
data class Detail(
    val id: Long,
    val category: String,
    val title: String,
    val timestamps: DetailTimestamps,
    val type: AfternoteServiceType,
    val credentials: DetailCredentials?,
    val receivers: List<DetailReceiver>,
    val processing: DetailProcessing?,
    val playlist: PlaylistDetail?,
)

data class DetailTimestamps(
    val createdAt: String,
    val updatedAt: String,
)

data class DetailCredentials(
    val id: String?,
    val password: String?,
)

data class DetailProcessing(
    val method: String?,
    val actions: List<String>,
    val leaveMessage: String?,
)
