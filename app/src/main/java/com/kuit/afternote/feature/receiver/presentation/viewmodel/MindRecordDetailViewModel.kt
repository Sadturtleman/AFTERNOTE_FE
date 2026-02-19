package com.kuit.afternote.feature.receiver.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedMindRecord
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedMindRecordDetail
import com.kuit.afternote.feature.receiver.domain.usecase.GetMindRecordDetailByAuthCodeUseCase
import com.kuit.afternote.feature.receiver.domain.usecase.GetMindRecordsByAuthCodeUseCase
import com.kuit.afternote.feature.receiverauth.session.ReceiverAuthSessionHolder
import com.kuit.afternote.feature.receiver.presentation.uimodel.MindRecordDetailUiState
import com.kuit.afternote.feature.receiver.presentation.uimodel.MindRecordItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
 * 목록 API로 날짜별 ID를 받은 뒤, 항목별 상세 API를 병렬 호출하여 title / imageList / content 를 표시합니다.
 */
@HiltViewModel
class MindRecordDetailViewModel
    @Inject
    constructor(
        private val receiverAuthSessionHolder: ReceiverAuthSessionHolder,
        private val getMindRecordsByAuthCodeUseCase: GetMindRecordsByAuthCodeUseCase,
        private val getMindRecordDetailByAuthCodeUseCase: GetMindRecordDetailByAuthCodeUseCase
    ) : ViewModel(), MindRecordDetailViewModelContract {

    private val _uiState = MutableStateFlow(MindRecordDetailUiState())
    override val uiState: StateFlow<MindRecordDetailUiState> = _uiState.asStateFlow()

    private var allMindRecords: List<ReceivedMindRecord> = emptyList()

    /**
     * 인증번호로 마인드레코드 목록을 로드한 뒤, 선택된 날짜의 항목에 대해 상세 API를 병렬 호출해 [mindRecordItems]를 갱신합니다.
     */
    override fun loadMindRecords() {
        val authCode = receiverAuthSessionHolder.getAuthCode() ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getMindRecordsByAuthCodeUseCase(authCode)
                .onSuccess { data ->
                    allMindRecords = data.items
                    loadDetailsForDate(_uiState.value.selectedDate, authCode)
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
     * 선택 날짜를 변경합니다. 해당 날짜의 항목에 대해 상세 API를 병렬 호출해 [mindRecordItems]를 갱신합니다.
     */
    override fun setSelectedDate(date: LocalDate) {
        _uiState.update { state ->
            state.copy(selectedDate = date, mindRecordItems = emptyList(), isLoading = true)
        }
        val authCode = receiverAuthSessionHolder.getAuthCode() ?: return
        viewModelScope.launch {
            loadDetailsForDate(date, authCode)
        }
    }

    override fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private suspend fun loadDetailsForDate(date: LocalDate, authCode: String) {
        val dateStr = date.toString()
        val ids = allMindRecords.filter { it.recordDate == dateStr }.map { it.mindRecordId }
        val details = coroutineScope {
            ids.map { id ->
                async {
                    getMindRecordDetailByAuthCodeUseCase(authCode, id).getOrNull()
                }
            }.awaitAll()
        }
        val items = details.filterNotNull().map(::toMindRecordItemUiModel)
        _uiState.update {
            it.copy(mindRecordItems = items, isLoading = false, errorMessage = null)
        }
    }

    private fun toMindRecordItemUiModel(detail: ReceivedMindRecordDetail): MindRecordItemUiModel {
        val dateDisplay = formatRecordDate(detail.recordDate)
        val tags = detail.category?.let { "#$it" }.orEmpty()
        val question = (detail.title?.takeIf { it.isNotBlank() }
            ?: detail.questionContent?.takeIf { it.isNotBlank() }).orEmpty()
        return MindRecordItemUiModel(
            mindRecordId = detail.mindRecordId,
            date = dateDisplay,
            tags = tags,
            question = question,
            content = detail.content.orEmpty(),
            hasImage = detail.imageUrls.isNotEmpty(),
            imageUrl = detail.imageUrls.firstOrNull()
        )
    }

    private fun formatRecordDate(recordDate: String?): String {
        if (recordDate.isNullOrBlank()) return ""
        return try {
            val parsed = LocalDate.parse(recordDate)
            parsed.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"))
        } catch (_: DateTimeParseException) {
            recordDate
        }
    }
}
