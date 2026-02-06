package com.kuit.afternote.feature.timeletter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterStatus
import com.kuit.afternote.feature.timeletter.presentation.mapper.toTimeLetterReceivers
import com.kuit.afternote.feature.timeletter.domain.usecase.CreateTimeLetterUseCase
import com.kuit.afternote.feature.timeletter.domain.usecase.GetTemporaryTimeLettersUseCase
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterWriterUiState
import com.kuit.afternote.feature.user.domain.usecase.GetReceiversUseCase
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
 * 편지 작성에 필요한 상태를 관리하고, 임시저장(DRAFT)/정식등록(SCHEDULED)을
 * CreateTimeLetterUseCase로 처리합니다.
 * 수신자 목록은 설정 화면과 동일한 GET /users/receivers를 사용합니다.
 */
@HiltViewModel
class TimeLetterWriterViewModel
    @Inject
    constructor(
        private val createTimeLetterUseCase: CreateTimeLetterUseCase,
        private val getTemporaryTimeLettersUseCase: GetTemporaryTimeLettersUseCase,
        private val getReceiversUseCase: GetReceiversUseCase
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(TimeLetterWriterUiState())
        val uiState: StateFlow<TimeLetterWriterUiState> = _uiState.asStateFlow()

        init {
            loadDraftCount()
            loadReceivers()
        }

        /**
         * 수신자 목록 로드 (GET /users/receivers, 설정 수신자 목록과 동일 소스)
         */
        fun loadReceivers() {
            viewModelScope.launch {
                getReceiversUseCase()
                    .onSuccess { list ->
                        _uiState.update { it.copy(receivers = list.toTimeLetterReceivers()) }
                    }
                    .onFailure {
                        _uiState.update { it.copy(receivers = emptyList()) }
                    }
            }
        }

        /**
         * 임시저장된 레터 개수 로드 (GET /time-letters/temporary)
         */
        private fun loadDraftCount() {
            viewModelScope.launch {
                getTemporaryTimeLettersUseCase()
                    .onSuccess { list ->
                        _uiState.update { it.copy(draftCount = list.totalCount) }
                    }.onFailure {
                        _uiState.update { it.copy(draftCount = 0) }
                    }
            }
        }

        /**
         * 임시저장 목록 변경 후 개수 갱신 (예: 임시저장 성공 시)
         */
        fun refreshDraftCount() {
            loadDraftCount()
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
         * @param date 발송 날짜 (yyyy. MM. dd 형식)
         */
        fun updateSendDate(date: String) {
            _uiState.update { it.copy(sendDate = date) }
            validateSaveEnabled()
        }

        /**
         * 더보기(작성 플러스) 메뉴 표시
         */
        fun showPlusMenu() {
            _uiState.update { it.copy(showWritingPlusMenu = true) }
        }

        /**
         * 더보기(작성 플러스) 메뉴 숨김
         */
        fun hidePlusMenu() {
            _uiState.update { it.copy(showWritingPlusMenu = false) }
        }

        /**
         * 수신자 선택 드롭다운 표시 (열 때마다 목록 갱신)
         */
        fun showRecipientDropdown() {
            loadReceivers()
            _uiState.update { it.copy(showRecipientDropdown = true) }
        }

        /**
         * 수신자 선택 드롭다운 숨김
         */
        fun hideRecipientDropdown() {
            _uiState.update { it.copy(showRecipientDropdown = false) }
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
         * sendAt 문자열 생성 (yyyy. MM. dd + HH:mm -> yyyy-MM-ddTHH:mm:00)
         */
        private fun buildSendAt(
            sendDate: String,
            sendTime: String
        ): String? {
            if (sendDate.isBlank() || sendTime.isBlank()) return null
            val normalizedDate = sendDate.replace(". ", "-").trim()
            return "$normalizedDate" + "T" + "$sendTime" + ":00"
        }

        /**
         * 타임레터 정식등록 (status = SCHEDULED)
         *
         * @param onSuccess 저장 성공 시 콜백
         */
        fun saveTimeLetter(onSuccess: () -> Unit) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                val state = _uiState.value
                val sendAt = buildSendAt(state.sendDate, state.sendTime)
                val result = createTimeLetterUseCase(
                    title = state.title.ifBlank { null },
                    content = state.content.ifBlank { null },
                    sendAt = sendAt,
                    status = TimeLetterStatus.SCHEDULED,
                    mediaList = null
                )
                _uiState.update { it.copy(isLoading = false) }
                result.onSuccess { _ -> onSuccess() }
                result.onFailure {
                    // TODO: 에러 메시지 UiState에 반영
                }
            }
        }

        /**
         * 임시저장 (status = DRAFT)
         *
         * 현재 작성 중인 편지를 임시저장합니다. (POST /time-letters, status=DRAFT)
         *
         * @param onSuccess 저장 성공 시 콜백
         */
        fun saveDraft(onSuccess: () -> Unit) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                val state = _uiState.value
                val sendAt = buildSendAt(state.sendDate, state.sendTime)
                val result = createTimeLetterUseCase(
                    title = state.title.ifBlank { null },
                    content = state.content.ifBlank { null },
                    sendAt = sendAt,
                    status = TimeLetterStatus.DRAFT,
                    mediaList = null
                )
                _uiState.update { it.copy(isLoading = false) }
                result.onSuccess {
                    refreshDraftCount()
                    onSuccess()
                }
                result.onFailure {
                    // TODO: 에러 메시지 UiState에 반영
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
