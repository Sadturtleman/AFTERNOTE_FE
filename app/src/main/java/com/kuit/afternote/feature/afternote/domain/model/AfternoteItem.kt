package com.kuit.afternote.feature.afternote.domain.model

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
    val type: ServiceType,
    val accountId: String = "",
    val password: String = "",
    val message: String = "",
    val accountProcessingMethod: String = "",
    val informationProcessingMethod: String = "",
    val processingMethods: List<AfternoteProcessingMethod> = emptyList(),
    val galleryProcessingMethods: List<AfternoteProcessingMethod> = emptyList()
)
