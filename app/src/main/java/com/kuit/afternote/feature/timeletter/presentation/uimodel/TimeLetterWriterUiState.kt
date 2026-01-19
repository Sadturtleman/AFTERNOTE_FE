package com.kuit.afternote.feature.timeletter.presentation.uimodel

/**
 * 타임레터 작성 화면의 UI 상태
 *
 * @property recipientName 수신자 이름
 * @property title 편지 제목
 * @property content 편지 내용
 * @property sendDate 발송 날짜 (yyyy. MM. dd 형식)
 * @property sendTime 발송 시간 (HH:mm 형식)
 * @property isLoading 로딩 상태
 * @property isSaveEnabled 저장 버튼 활성화 여부
 * @property showDatePicker DatePicker 표시 여부
 * @property showTimePicker TimePicker 표시 여부
 * @property draftCount 임시저장된 레터 개수
 */
data class TimeLetterWriterUiState(
    val recipientName: String = "",
    val title: String = "",
    val content: String = "",
    val sendDate: String = "",
    val sendTime: String = "",
    val isLoading: Boolean = false,
    val isSaveEnabled: Boolean = false,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val draftCount: Int = 0
)
