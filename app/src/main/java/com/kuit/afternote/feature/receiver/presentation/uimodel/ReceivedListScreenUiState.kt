package com.kuit.afternote.feature.receiver.presentation.uimodel

/**
 * 수신 타임레터 목록 화면(설정 플로우)용 리스트 아이템 UI 모델.
 *
 * API 목록 항목의 표시용 필드 및 읽음 여부(isRead)를 담습니다.
 */
data class ReceivedTimeLetterListItemUi(
    val timeLetterId: Long,
    val timeLetterReceiverId: Long,
    val senderName: String,
    val sendAt: String,
    val title: String,
    val content: String,
    val isRead: Boolean = false
)

/**
 * 수신 타임레터 목록 화면(설정 플로우) UI 상태.
 */
data class ReceiverTimeLettersListUiState(
    val items: List<ReceivedTimeLetterListItemUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * 수신 애프터노트 목록 화면(설정 플로우)용 리스트 아이템 UI 모델.
 *
 * @param id 애프터노트 ID (상세·플레이리스트 라우트 이동 시 사용)
 */
data class ReceivedAfternoteListItemUi(
    val id: Long,
    val sourceType: String,
    val lastUpdatedAt: String
)

/**
 * 수신 애프터노트 목록 화면(설정 플로우) UI 상태.
 */
data class ReceiverAfternotesListUiState(
    val items: List<ReceivedAfternoteListItemUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
