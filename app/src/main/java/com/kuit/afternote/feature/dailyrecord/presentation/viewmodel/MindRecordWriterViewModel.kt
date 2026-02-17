package com.kuit.afternote.feature.dailyrecord.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.dailyrecord.domain.usecase.CreateMindRecordUseCase
import com.kuit.afternote.feature.dailyrecord.domain.usecase.DeleteMindRecordUseCase
import com.kuit.afternote.feature.dailyrecord.domain.usecase.EditMindRecordUseCase
import com.kuit.afternote.feature.dailyrecord.domain.usecase.GetMindRecordsUseCase
import com.kuit.afternote.feature.dailyrecord.presentation.uimodel.MindRecordUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MindRecordWriterViewModel"

@HiltViewModel
class MindRecordWriterViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val createMindRecordUseCase: CreateMindRecordUseCase,
    private val deleteMindRecordUseCase: DeleteMindRecordUseCase,
    private val editMindRecordUseCase: EditMindRecordUseCase,
    private val getMindRecordsUseCase: GetMindRecordsUseCase,
    private val getMindRecordUseCase: EditMindRecordUseCase,
) : ViewModel(){
    private val _uiState = MutableStateFlow(MindRecordUiState())
    val uiState: StateFlow<MindRecordUiState> = _uiState.asStateFlow()
    private var waitingAgainPopUpJob: Job? = null

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
     * 저장 버튼 활성화 여부 검증
     *
     * 수신자, 제목, 내용, 날짜, 시간이 모두 입력되어야 저장 가능
     */
    private fun validateSaveEnabled() {
        val state = _uiState.value
        val isEnabled =
            state.title.isNotBlank() &&
            state.content.isNotBlank() &&
            state.sendDate.isNotBlank()
        _uiState.update { it.copy(isSaveEnabled = isEnabled) }
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
     * 등록 버튼 클릭 시: 등록 완료 팝업을 잠깐 보여준 뒤 실제 타임레터 정식등록을 수행합니다.
     *
     * @param onSuccess 저장 성공 시 콜백
     */
    fun registerWithPopUpThenSave(onSuccess: () -> Unit) {
        saveMindRecord(onSuccess = onSuccess)
    }

    fun saveMindRecord(
        showPopUpAfterSuccess: Boolean = true,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, saveErrorMessage = null) }
            val state = _uiState.value
            val sendAt = buildSendAt(state.sendDate)
            val result = createMindRecordUseCase(
                type = "DIARY", // 혹은 "MIND_RECORD" 등 서버에서 요구하는 타입
                title = state.title.ifBlank { null },
                content = state.content.ifBlank { null },
                date = sendAt,
                isDraft = false
            )

            _uiState.update { it.copy(isLoading = false) }
            result.onSuccess { _ ->
                Log.d(TAG, "saveMindRecord onSuccess")
                _uiState.update { it.copy(saveErrorMessage = null) }
                if (showPopUpAfterSuccess) {
                    _uiState.update { it.copy(showRegisteredPopUp = true) }
                    delay(2000L)
                    onSuccess()
                    _uiState.update { it.copy(showRegisteredPopUp = false) }
                } else {
                    onSuccess()
                }
            }
            result.onFailure { e ->
                Log.e(TAG, "saveMindRecord failed", e)
                _uiState.update {
                    it.copy(saveErrorMessage = e.message ?: "저장에 실패했습니다.")
                }
            }
        }
    }
    /**
     * sendAt 문자열 생성 (yyyy. MM. dd + HH:mm -> yyyy-MM-ddTHH:mm:00)
     */
    private fun buildSendAt(
        sendDate: String,
    ): String? {
        if (sendDate.isBlank()) return null
        val normalizedDate = sendDate.replace(". ", "-").trim()
        return "$normalizedDate"
    }
}
