package com.kuit.afternote.feature.afternote.domain.model

import com.kuit.afternote.core.domain.model.AfternoteServiceType

/**
 * 애프터노트 상세 도메인 모델.
 *
 * 서버 GET /api/afternotes/{id} 응답을 도메인 계층으로 매핑한 형태.
 * 카테고리별로 관련 필드만 non-null:
 * - SOCIAL: credentials, processMethod, actions
 * - GALLERY: receivers, processMethod, actions
 * - PLAYLIST: playlist
 */
data class AfternoteDetail(
    val id: Long,
    val category: String,
    val title: String,
    val createdAt: String,
    val updatedAt: String,
    val type: AfternoteServiceType,
    val credentialsId: String?,
    val credentialsPassword: String?,
    val receivers: List<AfternoteDetailReceiver>,
    val processMethod: String?,
    val actions: List<String>,
    val leaveMessage: String?,
    val playlist: AfternotePlaylistDetail?
)

/**
 * 갤러리 카테고리의 수신자 정보.
 * receiverId: from API; name/relation may be resolved from GET /users/receivers when API returns only IDs.
 */
data class AfternoteDetailReceiver(
    val receiverId: Long? = null,
    val name: String,
    val relation: String,
    val phone: String
)

/**
 * 추모 가이드라인 카테고리의 플레이리스트 상세.
 */
data class AfternotePlaylistDetail(
    val profilePhoto: String?,
    val atmosphere: String?,
    val memorialPhotoUrl: String?,
    val songs: List<AfternoteDetailSong>,
    val memorialVideoUrl: String?,
    val memorialThumbnailUrl: String?
)

/**
 * 플레이리스트 내 개별 곡 정보.
 */
data class AfternoteDetailSong(
    val id: Long?,
    val title: String,
    val artist: String,
    val coverUrl: String?
)
