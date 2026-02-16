package com.kuit.afternote.feature.dailyrecord.presentation.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordResponse
import com.kuit.afternote.feature.dailyrecord.domain.usecase.CreateMindRecordUseCase
import com.kuit.afternote.feature.dailyrecord.domain.usecase.DeleteMindRecordUseCase
import com.kuit.afternote.feature.dailyrecord.domain.usecase.EditMindRecordUseCase
import com.kuit.afternote.feature.dailyrecord.domain.usecase.GetMindRecordUseCase
import com.kuit.afternote.feature.dailyrecord.domain.usecase.GetMindRecordsUseCase
import com.kuit.afternote.feature.dailyrecord.presentation.uimodel.MindRecordUiModel
import com.kuit.afternote.feature.dailyrecord.presentation.uimodel.MindRecordUiState
import com.kuit.afternote.feature.home.presentation.component.CalendarDay
import com.kuit.afternote.feature.home.presentation.component.CalendarDayStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MindRecordViewModel @Inject constructor(
    private val getMindRecordsUseCase: GetMindRecordsUseCase,
    private val createMindRecordUseCase: CreateMindRecordUseCase,
    private val deleteMindRecordUseCase: DeleteMindRecordUseCase,
    private val getMindRecordUseCase: GetMindRecordUseCase,
    private val editMindRecordUseCase: EditMindRecordUseCase

    ) : ViewModel() {

    private val _records = MutableStateFlow<List<MindRecordUiModel>>(emptyList())
    val records: StateFlow<List<MindRecordUiModel>> = _records
    private val weekDates: List<LocalDate> = run {
        val now = LocalDate.now()
        val monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        (0..6).map { monday.plusDays(it.toLong()) }
    }

    val calendarDays: StateFlow<List<CalendarDay>> = _records
        .map { recordList ->
            // 성능 최적화: 기록된 날짜들을 Set으로 변환하여 검색 속도 O(1) 확보
            // MindRecordUiModel에 원본 날짜(yyyy-MM-dd)가 포함되어 있다고 가정합니다.
            val recordedDates = recordList.map { it.originalDate }.toSet()
            val today = LocalDate.now()

            weekDates.map { date ->
                val dateString = date.toString() // "yyyy-MM-dd" format

                val style = when {
                    date.isEqual(today) -> CalendarDayStyle.TODAY
                    recordedDates.contains(dateString) -> CalendarDayStyle.FILLED
                    else -> CalendarDayStyle.OUTLINED // 기본값
                }

                CalendarDay(
                    dayLabel = date.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN),
                    date = date.dayOfMonth,
                    style = style
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private fun formatDate(date: String): String {
        // "2026-02-06" → "2월 6일" 같은 변환
        val parts = date.split("-")
        return "${parts[1].toInt()}월 ${parts[2].toInt()}일"
    }
    fun loadRecords() {
        viewModelScope.launch {
            try {
                val response = getMindRecordsUseCase()
                val domainRecords = response ?: emptyList() // null 방어

                _records.value = domainRecords.map { summary ->
                    MindRecordUiModel(
                        id = summary.recordId,
                        title = summary.title,
                        formattedDate = runCatching { formatDate(summary.date) }
                            .getOrElse { summary.date }, // 포맷 실패 시 원본 날짜 사용
                        draftLabel = if (summary.isDraft) "임시저장" else "완료",
                        originalDate = summary.date
                    )
                }
            } catch (e: Exception) {
                // 예외 로그 찍기
                Log.e("MindRecordViewModel", "loadRecords 실패", e)
                _records.value = emptyList()
            }
        }
    }

    // UI 상태를 관리할 StateFlow
    private val _uiState = MutableStateFlow(MindRecordUiState())
    val uiState: StateFlow<MindRecordUiState> = _uiState

    fun onCreateRecord(
        type: String,
        title: String,
        content: String,
        date: String,
        isDraft: Boolean,
        questionId: Long? = null,
        category: String? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val result = createMindRecordUseCase(
                    type = type,
                    title = title,
                    content = content,
                    date = date,
                    isDraft = isDraft,
                    questionId = questionId,
                    category = category
                )
                //_uiState.update { it.copy(isCreated = true, newRecord = result) }
                loadRecords()
                onSuccess() // 성공 시 콜백 실행
            } catch (e: Exception) {
               // _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun deleteRecord(recordId: Long, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                deleteMindRecordUseCase(recordId)
                loadRecords()
                onSuccess()
            } catch (e: Exception) {
                Log.e("MindRecordViewModel", "deleteRecord 실패", e)
            }
        }
    }

    private val _selectedRecord = MutableStateFlow<MindRecordUiModel?>(null)
    val selectedRecord: StateFlow<MindRecordUiModel?> = _selectedRecord

    fun loadRecord(recordId: Long) {
        viewModelScope.launch {
            try {
                // UseCase 호출 → Result<MindRecordDetailResponse>
                val response = getMindRecordUseCase(recordId).getOrThrow()
                val detail = response.data // 실제 기록 정보는 여기 들어있음

                if (detail != null) {
                    _selectedRecord.value = MindRecordUiModel(
                        id = detail.recordId,
                        title = detail.title,
                        formattedDate = runCatching { formatDate(detail.date) }
                            .getOrElse { detail.date },
                        draftLabel = if (detail.isDraft) "임시저장" else "완료",
                        content = detail.content,
                        type = detail.type,
                        category = detail.category,
                        originalDate = detail.date
                    )
                } else {
                    _selectedRecord.value = null
                }
            } catch (e: Exception) {
                Log.e("MindRecordViewModel", "loadRecord 실패", e)
                _selectedRecord.value = null
            }
        }
    }

    fun editRecord(
        recordId: Long,
        title: String,
        content: String,
        date: String,
        type: String,
        category: String?,
        isDraft: Boolean,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val request = PostMindRecordRequest(title, content, date, type, isDraft, questionId = null,category)
                val result = editMindRecordUseCase(recordId, request).getOrThrow()
                loadRecords()
                onSuccess()
            } catch (e: Exception) {
                Log.e("MindRecordViewModel", "editRecord 실패", e)
            }
        }
    }


}





