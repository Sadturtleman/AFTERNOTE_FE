package com.kuit.afternote.feature.afternote.presentation.receiver.uimodel
/**
 * 수신 애프터노트 목록 화면(설정 플로우)용 리스트 아이템 UI 모델.
 *
 * @param id 애프터노트 ID (상세·플레이리스트 라우트 이동 시 사용)
 * @param title 목록 표시용 제목 (API title)
 */
data class ReceivedAfternoteListItemUi(
    val id: Long,
    val title: String,
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
