package com.kuit.afternote.feature.receiver.domain.entity

/**
 * 수신 목록 API 응답의 목록과 totalCount를 담는 도메인 래퍼.
 *
 * GET /api/received/{receiverId}/time-letters, after-notes 등 응답의 items + totalCount에 대응합니다.
 *
 * @param items API가 반환한 목록 (페이지 등으로 일부만 올 수 있음)
 * @param totalCount API가 반환한 전체 개수
 */
data class ReceivedListWithCount<T>(
    val items: List<T>,
    val totalCount: Int
)
