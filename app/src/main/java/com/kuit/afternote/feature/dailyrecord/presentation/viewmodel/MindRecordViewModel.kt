package com.kuit.afternote.feature.dailyrecord.presentation.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.domain.usecase.CreateMindRecordUseCase
import com.kuit.afternote.feature.dailyrecord.domain.usecase.DeleteMindRecordUseCase
import com.kuit.afternote.feature.dailyrecord.domain.usecase.EditMindRecordUseCase
import com.kuit.afternote.feature.dailyrecord.domain.usecase.GetDailyQuestionUseCase
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
import retrofit2.HttpException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import javax.inject.Inject

private const val TAG = "MindRecordViewModel"

/** Params for [MindRecordViewModel.onCreateRecord] to satisfy S107 (max 7 params). */
data class CreateRecordParams(
    val type: String,
    val title: String,
    val content: String,
    val date: String,
    val isDraft: Boolean,
    val questionId: Long? = null,
    val category: String? = null
)

/** Params for [MindRecordViewModel.editRecord] to satisfy S107 (max 7 params). */
data class EditRecordParams(
    val recordId: Long,
    val title: String,
    val content: String,
    val date: String,
    val type: String,
    val category: String?,
    val isDraft: Boolean
)

@HiltViewModel
class MindRecordViewModel @Inject constructor(
    private val getMindRecordsUseCase: GetMindRecordsUseCase,
    private val createMindRecordUseCase: CreateMindRecordUseCase,
    private val getDailyQuestionUseCase: GetDailyQuestionUseCase,
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
    fun loadRecords(
        type: String,
        view: String = "LIST",
        year: Int? = null,
        month: Int? = null
    ) {
        viewModelScope.launch {
            try {
                val response = getMindRecordsUseCase(type, view, year, month)
                val domainRecords = response ?: emptyList() // null 방어

                _records.value = domainRecords.map { summary ->
                    MindRecordUiModel(
                        id = summary.recordId,
                        title = summary.title,
                        formattedDate = runCatching { formatDate(summary.date) }
                            .getOrElse { summary.date },
                        draftLabel = if (summary.isDraft) "임시저장" else "완료",
                        content = summary.content,
                        originalDate = summary.date,
                        type = summary.type
                    )
                }
            } catch (e: Exception) {
                Log.e("MindRecordViewModel", "loadRecords 실패", e)
                _records.value = emptyList()
            }
        }
    }

    /**
     * 일기(DIARY)와 깊은 생각(DEEP_THOUGHT)을 모두 조회하여 합칩니다.
     * RecordFirstDiaryListScreen에서 사용합니다.
     */
    fun loadRecordsForDiaryList() {
        viewModelScope.launch {
            try {
                val diaryList = getMindRecordsUseCase("DIARY", "LIST", null, null)
                val deepThoughtList = getMindRecordsUseCase("DEEP_THOUGHT", "LIST", null, null)
                val combined = (diaryList ?: emptyList()) + (deepThoughtList ?: emptyList())
                val sorted = combined.sortedByDescending { it.date }

                _records.value = sorted.map { summary ->
                    MindRecordUiModel(
                        id = summary.recordId,
                        title = summary.title,
                        formattedDate = runCatching { formatDate(summary.date) }
                            .getOrElse { summary.date },
                        draftLabel = if (summary.isDraft) "임시저장" else "완료",
                        content = summary.content,
                        originalDate = summary.date,
                        type = summary.type
                    )
                }
            } catch (e: Exception) {
                Log.e("MindRecordViewModel", "loadRecordsForDiaryList 실패", e)
                _records.value = emptyList()
            }
        }
    }

    // UI 상태를 관리할 StateFlow
    private val _uiState = MutableStateFlow(MindRecordUiState())
    val uiState: StateFlow<MindRecordUiState> = _uiState

    /**
     * 오늘의 데일리 질문을 조회하여 UI에 표시합니다.
     * RecordQuestionScreen 진입 시(record == null) 호출합니다.
     */
    fun loadDailyQuestion() {
        viewModelScope.launch {
            val data = getDailyQuestionUseCase()
            _uiState.update { it.copy(dailyQuestionText = data?.content) }
        }
    }

    fun onCreateRecord(params: CreateRecordParams, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val resolvedQuestionId = when {
                params.type == "DAILY_QUESTION" && params.questionId == null -> {
                    val data = getDailyQuestionUseCase()
                    if (data == null) {
                        _uiState.update {
                            it.copy(createErrorMessage = "오늘의 질문을 불러올 수 없습니다.")
                        }
                        return@launch
                    }
                    data.questionId
                }
                else -> params.questionId
            }

            createMindRecordUseCase(
                type = params.type,
                title = params.title.ifBlank { null },
                content = params.content,
                date = params.date,
                isDraft = params.isDraft,
                questionId = resolvedQuestionId,
                category = params.category
            ).fold(
                onSuccess = {
                    _uiState.update { it.copy(createErrorMessage = null) }
                    loadRecords(params.type)
                    onSuccess()
                },
                onFailure = { e ->
                    Log.e("MindRecordViewModel", "onCreateRecord 실패", e)
                    val message = when (e) {
                        is HttpException -> when (e.code()) {
                            401 -> "인증이 만료되었습니다. 다시 로그인해 주세요."
                            else -> e.message() ?: "등록에 실패했습니다."
                        }
                        else -> e.message ?: "등록에 실패했습니다."
                    }
                    _uiState.update { it.copy(createErrorMessage = message) }
                }
            )
        }
    }

    fun clearCreateError() {
        _uiState.update { it.copy(createErrorMessage = null) }
    }

    /**
     * 기록을 삭제하고 목록을 갱신합니다.
     *
     * @param recordId 삭제할 기록 ID
     * @param recordType 기록 유형 (갱신 시 loadRecords에 사용)
     * @param onReload 삭제 성공 후 목록 갱신 콜백. null이면 loadRecords(recordType) 호출.
     *                 일기 목록(DIARY+DEEP_THOUGHT 통합)에서는 loadRecordsForDiaryList 전달.
     * @param onSuccess 삭제 성공 시 콜백
     */
    fun deleteRecord(
        recordId: Long,
        recordType: String,
        onReload: (() -> Unit)? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            deleteMindRecordUseCase(recordId).fold(
                onSuccess = {
                    if (onReload != null) {
                        onReload()
                    } else {
                        loadRecords(recordType)
                    }
                    onSuccess()
                },
                onFailure = { e ->
                    Log.e(TAG, "deleteRecord failed recordId=$recordId", e)
                }
            )
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
                Log.e(TAG, "loadRecord failed recordId=$recordId", e)
                _selectedRecord.value = null
            }
        }
    }

    fun editRecord(params: EditRecordParams, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val request = PostMindRecordRequest(
                    params.title,
                    params.content,
                    params.date,
                    params.type,
                    params.isDraft,
                    questionId = null,
                    params.category
                )
                editMindRecordUseCase(params.recordId, request).getOrThrow()
                loadRecords(params.type)
                onSuccess()
            } catch (e: Exception) {
                Log.e(TAG, "editRecord failed recordId=${params.recordId}", e)
            }
        }
    }


}





