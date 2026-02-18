package com.kuit.afternote.feature.dailyrecord.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.dailyrecord.domain.usecase.CreateMindRecordUseCase
import com.kuit.afternote.feature.dailyrecord.domain.usecase.DeleteMindRecordUseCase
import com.kuit.afternote.feature.dailyrecord.domain.usecase.EditMindRecordUseCase
import com.kuit.afternote.feature.dailyrecord.domain.usecase.GetMindRecordUseCase
import com.kuit.afternote.feature.dailyrecord.domain.usecase.GetMindRecordsUseCase
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordRequest
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
    private val getMindRecordUseCase: GetMindRecordUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MindRecordUiState())
    val uiState: StateFlow<MindRecordUiState> = _uiState.asStateFlow()
    private var waitingAgainPopUpJob: Job? = null

    /** 수정 모드일 때의 기록 ID. null이면 신규 작성. */
    private var recordIdForEdit: Long? = null

    /** 수정 모드일 때의 기록 유형 (DIARY, DEEP_THOUGHT). 저장 시 사용. */
    private var recordTypeForEdit: String? = null

    init {
        val today = java.time.LocalDate.now()
        val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
        _uiState.update { it.copy(sendDate = today.format(formatter)) }
    }

    /**
     * 수정 모드: 기존 기록을 불러와 UI 상태를 채웁니다.
     * EditDiaryRoute/EditDeepMindRoute 진입 시 호출됩니다.
     */
    fun loadRecordForEdit(recordId: Long) {
        viewModelScope.launch {
            getMindRecordUseCase(recordId).fold(
                onSuccess = { response ->
                    val detail = response.data
                    if (detail != null) {
                        recordIdForEdit = recordId
                        recordTypeForEdit = detail.type
                        val sendDateFormatted = formatApiDateToDisplay(detail.date)
                        _uiState.update {
                            it.copy(
                                title = detail.title,
                                content = detail.content,
                                sendDate = sendDateFormatted.ifBlank { it.sendDate },
                                category = detail.category
                            )
                        }
                        validateSaveEnabled()
                    }
                },
                onFailure = { e ->
                    Log.e(TAG, "loadRecordForEdit failed recordId=$recordId", e)
                }
            )
        }
    }

    /** "yyyy-MM-dd" → "yyyy년 MM월 dd일" 변환 */
    private fun formatApiDateToDisplay(apiDate: String): String {
        if (apiDate.isBlank()) return ""
        return try {
            val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val parsed = java.time.LocalDate.parse(apiDate, formatter)
            val displayFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
            parsed.format(displayFormatter)
        } catch (e: Exception) {
            Log.e(TAG, "formatApiDateToDisplay failed: $apiDate", e)
            ""
        }
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
     * 깊은 생각 카테고리 업데이트 (나의 가치관 등)
     */
    fun updateCategory(category: String?) {
        _uiState.update { it.copy(category = category) }
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
     * 등록 버튼 클릭 시: 등록 완료 팝업을 잠깐 보여준 뒤 실제 등록을 수행합니다.
     *
     * @param type 기록 유형 (DIARY, DEEP_THOUGHT)
     * @param onSuccess 저장 성공 시 콜백
     */
    fun registerWithPopUpThenSave(type: String, onSuccess: () -> Unit) {
        saveMindRecord(type = type, onSuccess = onSuccess)
    }

    fun saveMindRecord(
        type: String,
        showPopUpAfterSuccess: Boolean = true,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, saveErrorMessage = null) }
            val state = _uiState.value
            val dateStr = parseSendDateToApiFormat(state.sendDate) ?: return@launch.also {
                _uiState.update { it.copy(isLoading = false, saveErrorMessage = "날짜 형식이 올바르지 않습니다.") }
            }

            val effectiveType = recordTypeForEdit ?: type
            val categoryForRequest =
                if (effectiveType == "DEEP_THOUGHT") (state.category ?: "나의 가치관") else null

            val result = if (recordIdForEdit != null) {
                val request = PostMindRecordRequest(
                    type = effectiveType,
                    title = state.title.ifBlank { "" },
                    content = state.content.ifBlank { "" },
                    date = dateStr,
                    isDraft = false,
                    questionId = null,
                    category = categoryForRequest
                )
                editMindRecordUseCase(recordIdForEdit!!, request)
            } else {
                createMindRecordUseCase(
                    type = type,
                    title = state.title.ifBlank { null },
                    content = state.content.ifBlank { null },
                    date = dateStr,
                    isDraft = false,
                    questionId = null,
                    category = categoryForRequest
                )
            }

            _uiState.update { it.copy(isLoading = false) }
            result.onSuccess { _ ->
                Log.d(TAG, "saveMindRecord onSuccess type=$effectiveType edit=${recordIdForEdit != null}")
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
     * "yyyy년 MM월 dd일" 형식을 API 요구 형식 "yyyy-MM-dd"로 변환합니다.
     */
    private fun parseSendDateToApiFormat(sendDate: String): String? {
        if (sendDate.isBlank()) return null
        return try {
            val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
            val parsed = java.time.LocalDate.parse(sendDate, formatter)
            parsed.toString()
        } catch (e: Exception) {
            Log.e(TAG, "parseSendDateToApiFormat failed: $sendDate", e)
            null
        }
    }
}
