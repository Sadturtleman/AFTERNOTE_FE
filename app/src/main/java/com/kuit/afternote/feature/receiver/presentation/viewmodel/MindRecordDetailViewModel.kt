package com.kuit.afternote.feature.receiver.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedMindRecord
import com.kuit.afternote.feature.receiver.domain.usecase.GetMindRecordsByAuthCodeUseCase
import com.kuit.afternote.feature.receiverauth.session.ReceiverAuthSessionHolder
import com.kuit.afternote.feature.receiver.presentation.uimodel.MindRecordDetailUiState
import com.kuit.afternote.feature.receiver.presentation.uimodel.MindRecordItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

/**
 * 마음의 기록 상세(날짜별) 화면 ViewModel.
 *
 * GET /api/receiver-auth/mind-records (X-Auth-Code) 목록을 받아 선택한 날짜로 필터 후 표시합니다.
 */
@HiltViewModel
class MindRecordDetailViewModel
    @Inject
    constructor(
        private val receiverAuthSessionHolder: ReceiverAuthSessionHolder,
        private val getMindRecordsByAuthCodeUseCase: GetMindRecordsByAuthCodeUseCase
    ) : ViewModel(), MindRecordDetailViewModelContract {

    private val _uiState = MutableStateFlow(MindRecordDetailUiState())
    override val uiState: StateFlow<MindRecordDetailUiState> = _uiState.asStateFlow()

    private var allMindRecords: List<ReceivedMindRecord> = emptyList()

    /**
     * 인증번호로 마인드레코드 목록을 로드합니다.
     * 로드 후 현재 선택된 날짜로 필터해 [mindRecordItems]를 갱신합니다.
     */
    override fun loadMindRecords() {
        val authCode = receiverAuthSessionHolder.getAuthCode() ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getMindRecordsByAuthCodeUseCase(authCode)
                .onSuccess { data ->
                    allMindRecords = data.items
                    _uiState.update { state ->
                        state.copy(
                            mindRecordItems = filterAndMapToUiModels(
                                data.items,
                                state.selectedDate
                            ),
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "마인드레코드 목록 조회에 실패했습니다."
                        )
                    }
                }
        }
    }

    /**
     * 선택 날짜를 변경합니다. 이미 로드된 목록을 해당 날짜로 필터해 [mindRecordItems]를 갱신합니다.
     */
    override fun setSelectedDate(date: LocalDate) {
        _uiState.update { state ->
            state.copy(
                selectedDate = date,
                mindRecordItems = filterAndMapToUiModels(allMindRecords, date)
            )
        }
    }

    override fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun filterAndMapToUiModels(
        list: List<ReceivedMindRecord>,
        selectedDate: LocalDate
    ): List<MindRecordItemUiModel> {
        val dateStr = selectedDate.toString()
        return list
            .filter { it.recordDate == dateStr }
            .map(::toMindRecordItemUiModel)
    }

    private fun toMindRecordItemUiModel(record: ReceivedMindRecord): MindRecordItemUiModel {
        val dateDisplay = formatRecordDate(record.recordDate)
        val tags = record.sourceType?.let { "#$it" }.orEmpty()
        val question = record.sourceType.orEmpty()
        return MindRecordItemUiModel(
            mindRecordId = record.mindRecordId,
            date = dateDisplay,
            tags = tags,
            question = question,
            content = record.content.orEmpty(),
            hasImage = false
        )
    }

    private fun formatRecordDate(recordDate: String?): String {
        if (recordDate.isNullOrBlank()) return ""
        return try {
            val date = LocalDate.parse(recordDate)
            date.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"))
        } catch (_: DateTimeParseException) {
            recordDate
        }
    }
}
