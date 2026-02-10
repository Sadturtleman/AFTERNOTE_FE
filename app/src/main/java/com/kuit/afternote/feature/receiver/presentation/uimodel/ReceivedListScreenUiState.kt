package com.kuit.afternote.feature.receiver.presentation.uimodel

/**
 * 수신 타임레터 목록 화면(설정 플로우)용 리스트 아이템 UI 모델.
 */
data class ReceivedTimeLetterListItemUi(
    val timeLetterId: Long,
    val receiverName: String,
    val sendAt: String,
    val title: String,
    val content: String
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
 */
data class ReceivedAfternoteListItemUi(
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
