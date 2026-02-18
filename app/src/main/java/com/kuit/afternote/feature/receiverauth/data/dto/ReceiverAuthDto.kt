package com.kuit.afternote.feature.receiverauth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 수신자 인증번호 기반 콘텐츠 조회 API DTO. (스웨거 기준)
 *
 * - POST /api/receiver-auth/verify (인증번호 검증)
 * - GET /api/receiver-auth/time-letters, mind-records, after-notes (목록 조회)
 */

// --- POST /api/receiver-auth/verify (인증번호 검증) ---

@Serializable
data class ReceiverAuthVerifyRequestDto(
    @SerialName("authCode") val authCode: String
)

@Serializable
data class ReceiverAuthVerifyResponseDto(
    @SerialName("receiverId") val receiverId: Long,
    @SerialName("receiverName") val receiverName: String? = null,
    @SerialName("senderName") val senderName: String? = null,
    @SerialName("relation") val relation: String? = null
)

// --- GET /api/receiver-auth/message (발신자 메시지 조회) ---

/**
 * 수신자 메시지 조회 응답.
 *
 * 인증번호를 통해 발신자가 남긴 메시지를 조회합니다.
 */
@Serializable
data class ReceiverMessageResponseDto(
    val senderName: String,
    val message: String? = null
)

// --- GET /api/receiver-auth/time-letters (타임레터 목록) ---

@Serializable
data class ReceivedTimeLetterAuthResponseDto(
    @SerialName("id") val id: Long,
    @SerialName("timeLetterReceiverId") val timeLetterReceiverId: Long,
    @SerialName("title") val title: String? = null,
    @SerialName("content") val content: String? = null,
    @SerialName("sendAt") val sendAt: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("senderName") val senderName: String? = null,
    @SerialName("deliveredAt") val deliveredAt: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    val mediaList: List<TimeLetterMediaAuthResponseDto> = emptyList(),
    val isRead: Boolean = false
)

@Serializable
data class ReceivedTimeLetterListAuthResponseDto(
    @SerialName("timeLetters") val timeLetters: List<ReceivedTimeLetterAuthResponseDto> = emptyList(),
    @SerialName("totalCount") val totalCount: Int = 0
)

// --- GET /api/receiver-auth/time-letters/{timeLetterReceiverId} (타임레터 상세 조회) ---

/** 타임레터 미디어 응답. */
@Serializable
data class TimeLetterMediaAuthResponseDto(
    val id: Long,
    val mediaType: String? = null,
    val mediaUrl: String
)

/**
 * 수신한 타임레터 상세 응답.
 *
 * 인증번호로 특정 타임레터 상세 조회 시 반환. 읽음 처리도 함께 수행됩니다.
 *
 * @param status 상태 (DRAFT, SCHEDULED, SENT)
 */
@Serializable
data class ReceivedTimeLetterDetailAuthResponseDto(
    val id: Long,
    val timeLetterReceiverId: Long,
    val title: String? = null,
    val content: String? = null,
    val sendAt: String? = null,
    val status: String? = null,
    val senderName: String? = null,
    val deliveredAt: String? = null,
    val createdAt: String? = null,
    val mediaList: List<TimeLetterMediaAuthResponseDto> = emptyList(),
    val isRead: Boolean = false
)

// --- GET /api/receiver-auth/mind-records (마인드레코드 목록) ---

@Serializable
data class ReceivedMindRecordAuthResponseDto(
    @SerialName("id") val id: Long,
    @SerialName("type") val type: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("recordDate") val recordDate: String? = null,
    @SerialName("isDraft") val isDraft: Boolean = false,
    @SerialName("senderName") val senderName: String? = null,
    @SerialName("createdAt") val createdAt: String? = null
)

@Serializable
data class ReceivedMindRecordListAuthResponseDto(
    @SerialName("mindRecords") val mindRecords: List<ReceivedMindRecordAuthResponseDto> = emptyList(),
    @SerialName("totalCount") val totalCount: Int = 0
)

// --- GET /api/receiver-auth/mind-records/{mindRecordId} (마인드레코드 상세 조회) ---

/** 마인드레코드 이미지 응답. */
@Serializable
data class MindRecordImageAuthResponseDto(
    val id: Long,
    val mediaType: String? = null,
    val imageUrl: String
)

/**
 * 수신한 마인드레코드 상세 응답.
 *
 * 인증번호로 특정 마인드레코드 상세 조회 시 반환.
 *
 * @param type 마인드레코드 타입 (예: DIARY)
 */
@Serializable
data class ReceivedMindRecordDetailAuthResponseDto(
    val id: Long,
    val type: String? = null,
    val title: String? = null,
    val recordDate: String? = null,
    val content: String? = null,
    val questionId: Long? = null,
    val questionContent: String? = null,
    val category: String? = null,
    val senderName: String? = null,
    val createdAt: String? = null,
    val imageList: List<MindRecordImageAuthResponseDto> = emptyList()
)

// --- GET /api/receiver-auth/after-notes (애프터노트 목록) ---

@Serializable
data class ReceivedAfternoteAuthResponseDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String? = null,
    @SerialName("category") val category: String? = null,
    @SerialName("leaveMessage") val leaveMessage: String? = null,
    @SerialName("senderId") val senderId: Long? = null,
    @SerialName("senderName") val senderName: String? = null,
    @SerialName("createdAt") val createdAt: String? = null
)

@Serializable
data class ReceivedAfternoteListAuthResponseDto(
    @SerialName("afternotes") val afternotes: List<ReceivedAfternoteAuthResponseDto> = emptyList(),
    @SerialName("totalCount") val totalCount: Int = 0
)

// --- GET /api/receiver-auth/after-notes/{afternoteId} (애프터노트 상세 조회) ---

/** 노래 정보 (PLAYLIST 전용). */
@Serializable
data class ReceivedAfternoteSongInfoDto(
    val title: String? = null,
    val artist: String? = null,
    val coverUrl: String? = null
)

/** 플레이리스트 정보 (PLAYLIST 전용). */
@Serializable
data class ReceivedAfternotePlaylistInfoDto(
    val atmosphere: String? = null,
    val songs: List<ReceivedAfternoteSongInfoDto> = emptyList(),
    val memorialVideo: ReceivedAfternoteMemorialVideoInfoDto? = null
)

/** 추모 영상. */
@Serializable
data class ReceivedAfternoteMemorialVideoInfoDto(
    val videoUrl: String? = null,
    val thumbnailUrl: String? = null
)

/**
 * 수신한 애프터노트 상세 응답.
 *
 * 인증번호로 특정 애프터노트 상세 조회 시 반환.
 *
 * @param category 카테고리 (SOCIAL, GALLERY, PLAYLIST)
 * @param processMethod 처리 방식 (MEMORIAL, DELETE, TRANSFER, ADDITIONAL - SOCIAL/GALLERY 전용)
 * @param actions 체크리스트 (SOCIAL/GALLERY 전용)
 * @param playlist 플레이리스트 정보 (PLAYLIST 전용)
 */
@Serializable
data class ReceivedAfternoteDetailAuthResponseDto(
    val id: Long,
    val category: String? = null,
    val title: String? = null,
    val processMethod: String? = null,
    val actions: List<String> = emptyList(),
    val leaveMessage: String? = null,
    val senderName: String? = null,
    val createdAt: String? = null,
    val playlist: ReceivedAfternotePlaylistInfoDto? = null
)

// --- POST /api/receiver-auth/presigned-url (수신자 파일 업로드용 Presigned URL 생성) ---

/**
 * 수신자 파일 업로드용 Presigned URL 요청.
 *
 * 수신자가 사망확인 서류(PDF, 이미지)를 S3에 업로드하기 위한 Presigned URL을 생성합니다.
 *
 * @param extension 파일 확장자 (예: pdf, jpg, png)
 */
@Serializable
data class ReceiverAuthPresignedUrlRequestDto(
    val extension: String
)

/**
 * 수신자 파일 업로드용 Presigned URL 응답.
 *
 * @param presignedUrl S3 Presigned PUT URL (파일 업로드용)
 * @param fileUrl 업로드 완료 후 사용할 파일 URL
 * @param contentType PUT 요청 시 사용할 Content-Type 헤더 값
 */
@Serializable
data class ReceiverAuthPresignedUrlResponseDto(
    val presignedUrl: String,
    val fileUrl: String,
    val contentType: String
)

// --- POST /api/receiver-auth/delivery-verification (사망확인 서류 제출) ---

/**
 * 사망확인 서류 제출 요청.
 *
 * 전달 조건이 DEATH_CERTIFICATE인 경우 수신자가 인증 서류를 제출합니다.
 */
@Serializable
data class DeliveryVerificationRequestDto(
    val deathCertificateUrl: String,
    val familyRelationCertificateUrl: String
)

/**
 * 사망확인 서류 제출 응답.
 *
 * @param status 사망확인 서류 인증 상태 (PENDING, APPROVED, REJECTED)
 * @param adminNote 관리자 메모 (nullable)
 */
@Serializable
data class DeliveryVerificationResponseDto(
    val id: Long,
    val status: String,
    val deathCertificateUrl: String,
    val familyRelationCertificateUrl: String,
    val adminNote: String? = null,
    val createdAt: String
)
