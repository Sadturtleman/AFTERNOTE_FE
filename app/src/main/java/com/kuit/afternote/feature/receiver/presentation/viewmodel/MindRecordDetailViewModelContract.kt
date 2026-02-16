package com.kuit.afternote.feature.receiver.presentation.viewmodel

import com.kuit.afternote.feature.receiver.presentation.uimodel.MindRecordDetailUiState
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

/**
 * 마음의 기록 상세 화면에서 사용하는 ViewModel 계약.
 * Preview에서 Hilt 없이 Fake로 대체할 수 있도록 합니다.
 */
interface MindRecordDetailViewModelContract {
    val uiState: StateFlow<MindRecordDetailUiState>
    fun loadMindRecords()
    fun setSelectedDate(date: LocalDate)
    fun clearError()
}
