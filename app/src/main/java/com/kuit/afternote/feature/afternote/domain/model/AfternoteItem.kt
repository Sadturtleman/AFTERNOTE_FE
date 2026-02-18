package com.kuit.afternote.feature.afternote.domain.model

import com.kuit.afternote.core.domain.model.AfternoteServiceType
import com.kuit.afternote.feature.dailyrecord.presentation.uimodel.MindRecordUiModel

/**
 * 애프터노트 아이템 도메인 모델
 *
 * @param id 고유 식별자
 * @param serviceName 서비스명
 * @param date 날짜 (yyyy.MM.dd 형식)
 * @param type 서비스 타입 (필터링용)
 * @param accountId 계정 아이디
 * @param password 계정 비밀번호
 * @param message 남기실 말씀
 * @param accountProcessingMethod 계정 처리 방법 (추모 계정 전환 / 영구 삭제)
 * @param informationProcessingMethod 정보 처리 방법 (갤러리/파일: 수신자 전달 / 추가 수신자 전달)
 * @param processingMethods 소셜네트워크/비즈니스 등 처리 방법 목록
 * @param galleryProcessingMethods 갤러리/파일 처리 방법 목록
 */
data class AfternoteItem(
    val id: String,
    val serviceName: String,
    val date: String,
    val type: AfternoteServiceType,
    val accountId: String = "",
    val password: String = "",
    val message: String = "",
    val accountProcessingMethod: String = "",
    val informationProcessingMethod: String = "",
    val processingMethods: List<AfternoteProcessingMethod> = emptyList(),
    val galleryProcessingMethods: List<AfternoteProcessingMethod> = emptyList()
)

fun AfternoteItem.toMindRecordUiModel(): MindRecordUiModel {
    // 1. 날짜 포맷팅 로직 (안정성을 위해 java.time 사용)
    val inputFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd")
    val isoFormatter = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE // yyyy-MM-dd

    val parsedDate = runCatching {
        java.time.LocalDate.parse(this.date, inputFormatter)
    }.getOrElse {
        java.time.LocalDate.now() // 파싱 실패 시 오늘 날짜로 폴백 (시스템 안정성)
    }

    val originalDateStr = parsedDate.format(isoFormatter) // "2026-02-06"
    val formattedDateStr = "${parsedDate.monthValue}월 ${parsedDate.dayOfMonth}일"

    return MindRecordUiModel(
        // String ID를 Long으로 변환 (UI용 unique key 확보)
        id = this.id.hashCode().toLong(),
        title = this.serviceName,
        formattedDate = formattedDateStr,
        draftLabel = "완료", // 애프터노트는 도메인상 완료된 데이터로 간주
        content = this.message,
        type = this.type.name,
        category = null, // 필요 시 mapping 로직 추가
        originalDate = originalDateStr
    )
}
