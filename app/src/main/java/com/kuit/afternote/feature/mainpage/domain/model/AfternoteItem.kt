package com.kuit.afternote.feature.mainpage.domain.model

/**
 * 애프터노트 아이템 도메인 모델
 *
 * @param id 고유 식별자
 * @param serviceName 서비스명
 * @param date 날짜 (yyyy.MM.dd 형식)
 * @param type 서비스 타입 (필터링용)
 */
data class AfternoteItem(
    val id: String,
    val serviceName: String,
    val date: String,
    val type: ServiceType
)
