package com.kuit.afternote.feature.receiver.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedMindRecord
import com.kuit.afternote.feature.receiver.domain.usecase.GetReceivedMindRecordsUseCase
import com.kuit.afternote.feature.receiver.presentation.uimodel.MindRecordItemUiModel
import com.kuit.afternote.feature.receiver.presentation.uimodel.MindRecordListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

/**
 * 마음의 기록 캘린더(목록) 화면 ViewModel.
 *
 * GET /api/received/{receiverId}/mind-records 목록을 받아
 * 오늘의 기록, 날짜별 기록, 캘린더 표시에 활용합니다.
 */
@HiltViewModel
class MindRecordViewModel
    @Inject
    constructor(
        private val getReceivedMindRecordsUseCase: GetReceivedMindRecordsUseCase
    ) : ViewModel(), MindRecordViewModelContract {

    private val _uiState = MutableStateFlow(MindRecordListUiState())
    override val uiState: StateFlow<MindRecordListUiState> = _uiState.asStateFlow()

    private var allMindRecords: List<ReceivedMindRecord> = emptyList()

    override fun loadMindRecords(receiverId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getReceivedMindRecordsUseCase(receiverId = receiverId)
                .onSuccess { list ->
                    allMindRecords = list
                    _uiState.update { state ->
                        state.copy(
                            daysWithRecords = computeDaysWithRecords(
                                list,
                                YearMonth.from(state.selectedDate)
                            ),
                            selectedDateRecords = filterAndMapToUiModels(list, state.selectedDate),
                            todayRecord = findTodayRecord(list),
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

    override fun setSelectedDate(date: LocalDate) {
        _uiState.update { state ->
            state.copy(
                selectedDate = date,
                daysWithRecords = computeDaysWithRecords(
                    allMindRecords,
                    YearMonth.from(date)
                ),
                selectedDateRecords = filterAndMapToUiModels(allMindRecords, date)
            )
        }
    }

    override fun setShowDatePicker(show: Boolean) {
        _uiState.update { it.copy(showDatePicker = show) }
    }

    override fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun computeDaysWithRecords(
        list: List<ReceivedMindRecord>,
        displayYearMonth: YearMonth
    ): Set<Int> =
        list
            .mapNotNull { record ->
                record.recordDate?.let { dateStr ->
                    try {
                        LocalDate.parse(dateStr)
                    } catch (_: DateTimeParseException) {
                        null
                    }
                }
            }
            .filter { it.year == displayYearMonth.year && it.month == displayYearMonth.month }
            .map { it.dayOfMonth }
            .toSet()

    private fun filterAndMapToUiModels(
        list: List<ReceivedMindRecord>,
        selectedDate: LocalDate
    ): List<MindRecordItemUiModel> {
        val dateStr = selectedDate.toString()
        return list
            .filter { it.recordDate == dateStr }
            .map(::toMindRecordItemUiModel)
    }

    private fun findTodayRecord(list: List<ReceivedMindRecord>): MindRecordItemUiModel? {
        val todayStr = LocalDate.now().toString()
        return list
            .firstOrNull { it.recordDate == todayStr }
            ?.let(::toMindRecordItemUiModel)
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
