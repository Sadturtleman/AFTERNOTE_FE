package com.kuit.afternote.feature.dailyrecord.data.dto

import kotlinx.serialization.Serializable


/**
 * 마음의 기록 API DTOs. (스웨거 기준)
 */
/**
 * 전체 마음 기록 목록 조회 응답 DTO (GET /mind-records)
 * Swagger 응답 구조를 기반으로 함
 */
@Serializable
data class MindRecordListResponse(
    /** 응답 상태 코드 (예: 200 성공, 400 오류) */
    val status: Int,
    /** 내부 비즈니스 코드 */
    val code: Int,
    /** 응답 메시지 */
    val message: String,
    /** 실제 데이터 (기록 목록과 표시된 날짜들) */
    val data: MindRecordListData?
)

/**
 * 기록 목록 데이터
 */
@Serializable
data class MindRecordListData(
    /** 기록 요약 리스트 */
    val records: List<MindRecordSummary>,
    /** 표시된 날짜들 (달력 하이라이트용) */
    val markedDates: List<String>? = emptyList()
)

/**
 * 기록 요약 정보
 */
@Serializable
data class MindRecordSummary(
    /** 기록 고유 ID */
    val recordId: Long,
    /** 기록 타입 (예: DIARY, LETTER 등) */
    val type: String,
    /** 기록 제목 */
    val title: String,
    /** 기록 날짜 (yyyy-MM-dd) */
    val date: String,
    /** 임시 저장 여부 */
    val isDraft: Boolean
)

/**
 * 단건 기록 조회 응답 DTO (GET /mind-records/{recordId})
 */
@Serializable
data class MindRecordDetailResponse(
    val status: Int,
    val code: Int,
    val message: String,
    val data: MindRecordDetail?
)

/**
 * 단건 기록 상세 정보
 */
@Serializable
data class MindRecordDetail(
    val recordId: Long,
    val type: String,
    val title: String,
    val date: String,
    val isDraft: Boolean,
    /** 기록 본문 내용 */
    val content: String,
    /** 질문 ID (있을 경우) */
    val questionId: Long?,
    /** 카테고리 (예: 자아성찰, 가치관 등) */
    val category: String?
)

/**
 * 기록 작성 요청 DTO (POST /mind-records)
 */
@Serializable
data class PostMindRecordRequest(
    val type: String,
    val title: String,
    val content: String,
    val date: String,
    val isDraft: Boolean,
    val questionId: Long?,
    val category: String?
)

/**
 * 기록 작성 응답 DTO (POST /mind-records)
 */
@Serializable
data class PostMindRecordResponse(
    val status: Int,
    val code: Int,
    val message: String,
    val data: PostMindRecordData?
)

/**
 * 작성된 기록의 ID 정보
 */
@Serializable
data class PostMindRecordData(
    val recordId: Long
)

/**
 * 기록 작성
 */
@Serializable
data class CreateMindRecordRequest(
    val type: String,
    val title: String,
    val content: String,
    val date: String,
    val isDraft: Boolean,
    val questionId: Long? = null,
    val category: String? = null
)

@Serializable
data class UpdateMindRecordRequest(
    val title: String,
    val content: String,
    val date: String,
    val type: String,
    val category: String?,
    val isDraft: Boolean
)

@Serializable
data class UpdateMindRecordResponse(
    val status: Int,
    val code: Int,
    val message: String,
    val data: MindRecordDetail? // 수정된 기록 반환
)

