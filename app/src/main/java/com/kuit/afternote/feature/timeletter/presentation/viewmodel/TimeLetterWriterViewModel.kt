package com.kuit.afternote.feature.timeletter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.timeletter.domain.entity.DraftLetter
import com.kuit.afternote.feature.timeletter.domain.usecase.SaveDraftLetterUseCase
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterWriterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 타임레터 작성 화면의 ViewModel
 *
 * 편지 작성에 필요한 상태를 관리하고 저장 로직을 처리합니다.
 */
@HiltViewModel
class TimeLetterWriterViewModel
    @Inject
    constructor(
        private val saveDraftLetterUseCase: SaveDraftLetterUseCase
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(TimeLetterWriterUiState())
        val uiState: StateFlow<TimeLetterWriterUiState> = _uiState.asStateFlow()

        init {
            loadDraftCount()
        }

        /**
         * 임시저장된 레터 개수 로드
         *
         * TODO: 실제 Repository/UseCase에서 가져오도록 변경 필요
         */
        private fun loadDraftCount() {
            viewModelScope.launch {
                // TODO: 실제 API/DB에서 임시저장 개수 가져오기
                // val count = getDraftCountUseCase.invoke()
                val count = MOCK_DRAFT_COUNT
                _uiState.update { it.copy(draftCount = count) }
            }
        }

        companion object {
            private const val MOCK_DRAFT_COUNT = 10
        }

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
         * 임시저장
         *
         * 현재 작성 중인 편지를 임시저장합니다.
         *
         * @param onSuccess 저장 성공 시 콜백
         */
        fun saveDraft(onSuccess: () -> Unit) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }

                try {
                    val state = _uiState.value
                    saveDraftLetterUseCase.invoke(
                        DraftLetter(
                            receiverName = state.recipientName,
                            sendDate = state.sendDate,
                            title = state.title,
                            content = state.content
                        )
                    )
                    onSuccess()
                } catch (e: Exception) {
                    // TODO: 에러 처리
                } finally {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }

        /**
         * 상태 초기화
         */
        fun resetState() {
            _uiState.value = TimeLetterWriterUiState()
        }
    }
