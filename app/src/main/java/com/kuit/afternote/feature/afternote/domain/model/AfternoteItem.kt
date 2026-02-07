package com.kuit.afternote.feature.afternote.domain.model

/**
 * 애프터노트 아이템 도메인 모델
 *
 * @param id 고유 식별자
 * @param serviceName 서비스명
 * @param date 날짜 (yyyy.MM.dd 형식)
 * @param type 서비스 타입 (필터링용)
 * @param processingMethods 소셜네트워크/비즈니스 등 처리 방법 목록
 * @param galleryProcessingMethods 갤러리/파일 처리 방법 목록
 */
data class AfternoteItem(
    val id: String,
    val serviceName: String,
    val date: String,
    val type: ServiceType,
    val processingMethods: List<AfternoteProcessingMethod> = emptyList(),
    val galleryProcessingMethods: List<AfternoteProcessingMethod> = emptyList()
)
