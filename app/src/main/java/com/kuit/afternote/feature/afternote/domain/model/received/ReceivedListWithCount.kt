package com.kuit.afternote.feature.afternote.domain.model.received

/**
 * 수신·인증번호 기반 목록 API의 items + totalCount.
 *
 * Afternote feature는 다른 feature의 동일 형태 타입에 의존하지 않기 위해 로컬 정의를 둡니다.
 */
data class ReceivedListWithCount<T>(
    val items: List<T>,
    val totalCount: Int,
)
