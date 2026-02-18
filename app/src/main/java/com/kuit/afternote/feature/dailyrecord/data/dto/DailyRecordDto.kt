package com.kuit.afternote.feature.dailyrecord.data.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

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

@Serializable
data class EmotionResponse(
    val emotions: List<Emotion>,
    val summary: String = ""
)

@Serializable
data class Emotion(
    val keyword: String,
    val percentage: Double
)

/**
 * 기록 목록 데이터
 */
@Serializable
data class MindRecordListData(
    /** 기록 요약 리스트 (각 항목에 content 포함) */
    val records: List<MindRecordSummary>,
    /** 표시된 날짜들 (달력 하이라이트용) */
    val markedDates: List<String>? = emptyList()
)

/**
 * 기록 요약 정보
 * DAILY_QUESTION 타입은 API에서 question 필드로 질문 내용을 반환할 수 있음.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class MindRecordSummary(
    /** 기록 고유 ID */
    val recordId: Long,
    /** 기록 타입 (예: DIARY, LETTER 등) */
    val type: String,
    /** 기록 제목 (API: title 또는 question) */
    @JsonNames("question", "title") val title: String? = null,
    /** 기록 날짜 (yyyy-MM-dd) */
    val date: String,
    /** 임시 저장 여부 */
    val isDraft: Boolean,
    /** 기록 내용 (API: content 또는 answer). DAILY_QUESTION은 answer로 반환될 수 있음. */
    @JsonNames("answer", "content") val content: String? = null
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
 * 데일리 질문 조회 응답 (GET /daily-question)
 * 오늘 날짜 기준으로 서버가 1개의 질문을 반환합니다.
 * API 스펙: { questionId, content }
 */
@Serializable
data class DailyQuestionResponse(
    val questionId: Long,
    val content: String
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
    val title: String?,
    val content: String?,
    val date: String?,
    val isDraft: Boolean,
    val questionId: Long? = null,
    val category: String? = null,
    val imageList: List<MindRecordImageItem>? = emptyList()  // 추가
)

@Serializable
data class MindRecordImageItem(
    val mediaType: String,
    val imageUrl: String
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

@Serializable
data class ReceiverEnabledRequest(
    val enabled: Boolean
)

