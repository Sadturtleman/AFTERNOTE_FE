package com.kuit.afternote.feature.timeletter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterWriterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 타임레터 작성 화면의 ViewModel
 *
 * 편지 작성에 필요한 상태를 관리하고 저장 로직을 처리합니다.
 */
class TimeLetterWriterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TimeLetterWriterUiState())
    val uiState: StateFlow<TimeLetterWriterUiState> = _uiState.asStateFlow()

    /**
     * 수신자 이름 업데이트
     *
     * @param name 수신자 이름
     */
    fun updateRecipientName(name: String) {
        _uiState.update { it.copy(recipientName = name) }
        validateSaveEnabled()
    }

    /**
     * 제목 업데이트
     *
     * @param title 편지 제목
     */
    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title) }
        validateSaveEnabled()
    }

    /**
     * 내용 업데이트
     *
     * @param content 편지 내용
     */
    fun updateContent(content: String) {
        _uiState.update { it.copy(content = content) }
        validateSaveEnabled()
    }

    /**
     * 발송 날짜 업데이트
     *
     * @param date 발송 날짜 (yyyy.MM.dd 형식)
     */
    fun updateSendDate(date: String) {
        _uiState.update { it.copy(sendDate = date) }
        validateSaveEnabled()
    }

    /**
     * 발송 시간 업데이트
     *
     * @param time 발송 시간 (HH:mm 형식)
     */
    fun updateSendTime(time: String) {
        _uiState.update { it.copy(sendTime = time) }
        validateSaveEnabled()
    }

    /**
     * DatePicker 표시
     */
    fun showDatePicker() {
        _uiState.update { it.copy(showDatePicker = true) }
    }

    /**
     * DatePicker 숨기기
     */
    fun hideDatePicker() {
        _uiState.update { it.copy(showDatePicker = false) }
    }

    /**
     * TimePicker 표시
     */
    fun showTimePicker() {
        _uiState.update { it.copy(showTimePicker = true) }
    }

    /**
     * TimePicker 숨기기
     */
    fun hideTimePicker() {
        _uiState.update { it.copy(showTimePicker = false) }
    }

    /**
     * 저장 버튼 활성화 여부 검증
     *
     * 수신자, 제목, 내용, 날짜, 시간이 모두 입력되어야 저장 가능
     */
    private fun validateSaveEnabled() {
        val state = _uiState.value
        val isEnabled = state.recipientName.isNotBlank() &&
            state.title.isNotBlank() &&
            state.content.isNotBlank() &&
            state.sendDate.isNotBlank() &&
            state.sendTime.isNotBlank()
        _uiState.update { it.copy(isSaveEnabled = isEnabled) }
    }

    /**
     * 타임레터 저장
     *
     * @param onSuccess 저장 성공 시 콜백
     */
    fun saveTimeLetter(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // TODO: 실제 API 호출 (UseCase 사용)
            // try {
            //     saveTimeLetterUseCase.invoke(
            //         TimeLetter(
            //             receiverName = _uiState.value.recipientName,
            //             title = _uiState.value.title,
            //             content = _uiState.value.content,
            //             sendDate = _uiState.value.sendDate,
            //             sendTime = _uiState.value.sendTime
            //         )
            //     )
            //     onSuccess()
            // } catch (e: Exception) {
            //     // 에러 처리
            // } finally {
            //     _uiState.update { it.copy(isLoading = false) }
            // }

            // 임시: 저장 성공으로 처리
            _uiState.update { it.copy(isLoading = false) }
            onSuccess()
        }
    }

    /**
     * 상태 초기화
     */
    fun resetState() {
        _uiState.value = TimeLetterWriterUiState()
    }
}
