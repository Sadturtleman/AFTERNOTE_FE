package com.kuit.afternote.feature.timeletter.presentation.uimodel

import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterReceiver

/**
 * 타임레터 작성 화면의 UI 상태
 *
 * @property receiverIds 선택된 수신자 ID 목록 (TimeLetterCreateRequest.receiverIds와 동일)
 * @property title 편지 제목
 * @property content 편지 내용
 * @property sendDate 발송 날짜 (yyyy. MM. dd 형식)
 * @property sendTime 발송 시간 (HH:mm 형식)
 * @property selectedImageUriStrings 첨부할 이미지 URI 문자열 목록 (업로드 대기)
 * @property existingMediaUrls draft 로드 시 기존 미디어 URL 목록 (IMAGE 타입만)
 * @property isLoading 로딩 상태
 * @property isSaveEnabled 저장 버튼 활성화 여부
 * @property showDatePicker DatePicker 표시 여부
 * @property showTimePicker TimePicker 표시 여부
 * @property draftCount 임시저장된 레터 개수
 * @property showWritingPlusMenu 더보기(작성 플러스) 메뉴 표시 여부
 * @property showRecipientDropdown 수신자 선택 드롭다운 표시 여부
 * @property receivers 수신자 목록 (GET /users/receivers와 동일 소스)
 * @property showRegisteredPopUp 타임레터 등록 완료 팝업 표시 여부
 * @property showDraftSavePopUp 임시저장 완료 팝업 표시 여부
 * @property showWaitingAgainPopUp 입력값 재확인 안내 팝업 표시 여부
 * @property draftId 수정 중인 임시저장 레터 ID (null이면 신규 작성)
 */
data class TimeLetterWriterUiState(
    val receiverIds: List<Long> = emptyList(),
    val title: String = "",
    val content: String = "",
    val sendDate: String = "",
    val sendTime: String = "",
    val selectedImageUriStrings: List<String> = emptyList(),
    val existingMediaUrls: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isSaveEnabled: Boolean = false,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val draftCount: Int = 0,
    val showWritingPlusMenu: Boolean = false,
    val showRecipientDropdown: Boolean = false,
    val receivers: List<TimeLetterReceiver> = emptyList(),
    val showRegisteredPopUp: Boolean = false,
    val showDraftSavePopUp: Boolean = false,
    val showWaitingAgainPopUp: Boolean = false,
    val draftId: Long? = null
)
