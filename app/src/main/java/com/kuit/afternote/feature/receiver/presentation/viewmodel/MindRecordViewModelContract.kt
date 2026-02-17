package com.kuit.afternote.feature.receiver.presentation.viewmodel

import com.kuit.afternote.feature.receiver.presentation.uimodel.MindRecordListUiState
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

/**
 * 마음의 기록 캘린더(목록) 화면 ViewModel 계약.
 * Preview에서 Hilt 없이 Fake로 대체할 수 있도록 합니다.
 */
interface MindRecordViewModelContract {
    val uiState: StateFlow<MindRecordListUiState>
    fun loadMindRecords()
    fun setSelectedDate(date: LocalDate)
    fun setShowDatePicker(show: Boolean)
    fun clearError()
}
