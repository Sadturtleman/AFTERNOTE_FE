package com.kuit.afternote.feature.dailyrecord.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem
import com.kuit.afternote.feature.afternote.domain.usecase.GetAfternotesUseCase
import com.kuit.afternote.feature.dailyrecord.data.dto.Emotion
import com.kuit.afternote.feature.dailyrecord.data.dto.EmotionResponse
import com.kuit.afternote.feature.dailyrecord.data.dto.PostMindRecordRequest
import com.kuit.afternote.feature.dailyrecord.domain.usecase.*
import com.kuit.afternote.feature.dailyrecord.presentation.uimodel.MindRecordUiModel
import com.kuit.afternote.feature.dailyrecord.presentation.uimodel.MindRecordUiState
import com.kuit.afternote.feature.home.presentation.component.CalendarDay
import com.kuit.afternote.feature.home.presentation.component.CalendarDayStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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
    private val editMindRecordUseCase: EditMindRecordUseCase,
    private val getAfternotesUseCase: GetAfternotesUseCase,
    private val getEmotionsUseCase: GetEmotionsUseCase
) : ViewModel() {

    // --- State 정의 ---
    private val _records = MutableStateFlow<List<MindRecordUiModel>>(emptyList())
    val records: StateFlow<List<MindRecordUiModel>> = _records

    private val _emotions = MutableStateFlow<EmotionResponse>(EmotionResponse(emotions = emptyList()))
    val emotions: StateFlow<EmotionResponse> = _emotions
    fun getEmotions() {
        viewModelScope.launch {
            Log.d("TRACE_VM", "1. getEmotions 시작")
            try {
                val response = getEmotionsUseCase()
                _emotions.value = response
                Log.d("TRACE_VM", "2. 성공: ${response.emotions}")
            } catch (e: Exception) {
                // 이 로그가 찍혀야 범인을 잡을 수 있습니다.
                Log.e("TRACE_VM", "3. 실패: ${e.message}", e)
            }
        }
    }

    // [추가] 리포트용 독립적 데이터 소스 (UI 데이터 스트림 분리)
    private val _dailyQuestionRecords = MutableStateFlow<List<MindRecordUiModel>>(emptyList())
    private val _totalRecords = MutableStateFlow<List<MindRecordUiModel>>(emptyList())
    private val _afternoteRecords = MutableStateFlow<List<MindRecordUiModel>>(emptyList())

    private val _markedDates = MutableStateFlow<Set<String>>(emptySet())
    val markedDates: StateFlow<Set<String>> = _markedDates

    private val weekDates: List<LocalDate> = run {
        val now = LocalDate.now()
        val monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        (0..6).map { monday.plusDays(it.toLong()) }
    }

    // --- 리포트용 UI State ---
    data class WeeklySummaryUiState(
        val calendarDays: List<CalendarDay> = emptyList(),
        val totalWeeklyCount: Int = 0,
        val dayCounts: Map<String, Int> = emptyMap()
    )
    fun loadWeeklyReportData() {
        viewModelScope.launch {
            // 모든 리포트 데이터를 병렬 혹은 순차적으로 로드
            launch { loadRecords("DAILY_QUESTION") }
            launch { loadRecordsForDiaryList() }
            launch { loadAfternoteRecords() }
            launch { getEmotions() }
            Log.d("emotion", emotions.value.emotions.toString())
        }
    }
    // [추가] 데일리 질문 주간 통계 스트림
    val dailyQuestionSummary: StateFlow<WeeklySummaryUiState> = _dailyQuestionRecords
        .map { calculateWeeklySummary(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WeeklySummaryUiState())

    val afternoteSummary: StateFlow<WeeklySummaryUiState> = _afternoteRecords
        .map { calculateWeeklySummary(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WeeklySummaryUiState())
    // [추가] 전체 기록 주간 통계 스트림
    val totalSummary: StateFlow<WeeklySummaryUiState> = _totalRecords
        .map { calculateWeeklySummary(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WeeklySummaryUiState())

    // 기존 캘린더 데이 (메인 화면용 유지)
    val calendarDays: StateFlow<List<CalendarDay>> = _records
        .map { recordList ->
            val recordedDates = recordList.map { it.originalDate }.toSet()
            val today = LocalDate.now()
            weekDates.map { date ->
                val dateString = date.toString()
                val style = when {
                    date.isEqual(today) -> CalendarDayStyle.TODAY
                    recordedDates.contains(dateString) -> CalendarDayStyle.FILLED
                    else -> CalendarDayStyle.OUTLINED
                }
                CalendarDay(
                    dayLabel = date.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN),
                    date = date.dayOfMonth,
                    style = style
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Helper Functions ---

    private fun loadAfternoteRecords() {
        viewModelScope.launch {
            getAfternotesUseCase(category = null, page = 0, size = 50).fold(
                onSuccess = { pagedData ->
                    // 매핑 함수 사용
                    _afternoteRecords.value = pagedData.items.map { it.toMindRecordUiModel() }
                },
                onFailure = { Log.e(TAG, "Afternote 로드 실패", it) }
            )
        }
    }
    // [추가] 주간 통계 계산 로직 (Pure Function)
    private fun calculateWeeklySummary(recordList: List<MindRecordUiModel>): WeeklySummaryUiState {
        val today = LocalDate.now()
        val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val sunday = monday.plusDays(6)

        val weekRange = (0..6).map { monday.plusDays(it.toLong()) }

        // 이번 주 범위 내의 기록만 필터링
        val recordsInThisWeek = recordList.filter {
            val d = LocalDate.parse(it.originalDate)
            !d.isBefore(monday) && !d.isAfter(sunday)
        }

        val countsByDate = recordsInThisWeek
            .groupBy { it.originalDate }
            .mapValues { it.value.size }

        val calendarDays = weekRange.map { date ->
            val dateString = date.toString()
            CalendarDay(
                dayLabel = date.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN),
                date = date.dayOfMonth,
                style = when {
                    countsByDate.containsKey(dateString) -> CalendarDayStyle.FILLED
                    date.isEqual(today) -> CalendarDayStyle.TODAY
                    else -> CalendarDayStyle.OUTLINED
                }
            )
        }

        return WeeklySummaryUiState(
            calendarDays = calendarDays,
            totalWeeklyCount = recordsInThisWeek.size,
            dayCounts = countsByDate
        )
    }

    private fun formatDate(date: String): String {
        val parts = date.split("-")
        return "${parts[1].toInt()}월 ${parts[2].toInt()}일"
    }

    // --- Public Functions (Business Logic) ---

    fun loadRecords(
        type: String,
        view: String = "LIST",
        year: Int? = null,
        month: Int? = null
    ) {
        viewModelScope.launch {
            try {
                val result = getMindRecordsUseCase(type, view, year, month)
                val domainRecords = result.records
                val uiModels = domainRecords.map { summary ->
                    MindRecordUiModel(
                        id = summary.recordId,
                        title = summary.title ?: "",
                        formattedDate = runCatching { formatDate(summary.date) }.getOrElse { summary.date },
                        draftLabel = if (summary.isDraft) "임시저장" else "완료",
                        content = summary.content,
                        originalDate = summary.date,
                        type = summary.type
                    )
                }

                _records.value = uiModels

                // [추가] 데일리 질문인 경우 리포트용 전용 상태 업데이트
                if (type == "DAILY_QUESTION") {
                    _dailyQuestionRecords.value = uiModels
                }

                val apiMarked = result.markedDates.toSet()
                _markedDates.value = if (apiMarked.isNotEmpty()) apiMarked else uiModels.map { it.originalDate }.toSet()
            } catch (e: Exception) {
                Log.e(TAG, "loadRecords 실패", e)
                _records.value = emptyList()
            }
        }
    }

    fun loadRecordsForDiaryList() {
        viewModelScope.launch {
            try {
                val diaryResult = getMindRecordsUseCase("DIARY", "LIST", null, null)
                val deepResult = getMindRecordsUseCase("DEEP_THOUGHT", "LIST", null, null)
                val combined = diaryResult.records + deepResult.records
                val sorted = combined.sortedByDescending { it.date }

                val uiModels = sorted.map { summary ->
                    MindRecordUiModel(
                        id = summary.recordId,
                        title = summary.title ?: "",
                        formattedDate = runCatching { formatDate(summary.date) }.getOrElse { summary.date },
                        draftLabel = if (summary.isDraft) "임시저장" else "완료",
                        content = summary.content,
                        originalDate = summary.date,
                        type = summary.type
                    )
                }

                _records.value = uiModels
                // [추가] 전체 기록 리포트용 상태 업데이트
                _totalRecords.value = uiModels

                val apiMarkedDates = (diaryResult.markedDates + deepResult.markedDates).toSet()
                _markedDates.value = if (apiMarkedDates.isNotEmpty()) apiMarkedDates else uiModels.map { it.originalDate }.toSet()
            } catch (e: Exception) {
                Log.e(TAG, "loadRecordsForDiaryList 실패", e)
                _records.value = emptyList()
            }
        }
    }

    // [추가] 두 가지 리포트 데이터를 한 번에 갱신하는 함수
    fun loadAllReportData() {
        loadRecords("DAILY_QUESTION")
        loadRecordsForDiaryList()
    }

    // --- 나머지 기존 함수 유지 ---

    private val _uiState = MutableStateFlow(MindRecordUiState())
    val uiState: StateFlow<MindRecordUiState> = _uiState

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
                        _uiState.update { it.copy(createErrorMessage = "오늘의 질문을 불러올 수 없습니다.") }
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
                    // 기록 생성 후 리포트 데이터도 함께 갱신하여 정합성 유지
                    loadAllReportData()
                    onSuccess()
                },
                onFailure = { e ->
                    Log.e(TAG, "onCreateRecord 실패", e)
                    val message = if (e is HttpException && e.code() == 401) "인증 만료" else e.message ?: "등록 실패"
                    _uiState.update { it.copy(createErrorMessage = message) }
                }
            )
        }
    }

    fun clearCreateError() {
        _uiState.update { it.copy(createErrorMessage = null) }
    }

    fun deleteRecord(recordId: Long, recordType: String, onReload: (() -> Unit)? = null, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            deleteMindRecordUseCase(recordId).fold(
                onSuccess = {
                    if (onReload != null) onReload() else loadRecords(recordType)
                    loadAllReportData() // 삭제 후 통계 갱신
                    onSuccess()
                },
                onFailure = { e -> Log.e(TAG, "deleteRecord failed", e) }
            )
        }
    }

    private val _selectedRecord = MutableStateFlow<MindRecordUiModel?>(null)
    val selectedRecord: StateFlow<MindRecordUiModel?> = _selectedRecord

    fun loadRecord(recordId: Long) {
        viewModelScope.launch {
            try {
                val response = getMindRecordUseCase(recordId).getOrThrow()
                val detail = response.data
                if (detail != null) {
                    _selectedRecord.value = MindRecordUiModel(
                        id = detail.recordId,
                        title = detail.title,
                        formattedDate = runCatching { formatDate(detail.date) }.getOrElse { detail.date },
                        draftLabel = if (detail.isDraft) "임시저장" else "완료",
                        content = detail.content,
                        type = detail.type,
                        category = detail.category,
                        originalDate = detail.date
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "loadRecord failed", e)
            }
        }
    }

    fun editRecord(params: EditRecordParams, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val request = PostMindRecordRequest(
                    params.title, params.content, params.date, params.type, params.isDraft, null, params.category
                )
                editMindRecordUseCase(params.recordId, request).getOrThrow()
                loadRecords(params.type)
                loadAllReportData() // 수정 후 통계 갱신
                onSuccess()
            } catch (e: Exception) {
                Log.e(TAG, "editRecord failed", e)
            }
        }
    }
}

fun AfternoteItem.toMindRecordUiModel(): MindRecordUiModel {
    // 1. 날짜 포맷팅 로직 (안정성을 위해 java.time 사용)
    val inputFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd")
    val isoFormatter = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE // yyyy-MM-dd

    val parsedDate = runCatching {
        java.time.LocalDate.parse(this.date, inputFormatter)
    }.getOrElse {
        java.time.LocalDate.now() // 파싱 실패 시 오늘 날짜로 폴백 (시스템 안정성)
    }

    val originalDateStr = parsedDate.format(isoFormatter) // "2026-02-06"
    val formattedDateStr = "${parsedDate.monthValue}월 ${parsedDate.dayOfMonth}일"

    return MindRecordUiModel(
        // String ID를 Long으로 변환 (UI용 unique key 확보)
        id = this.id.hashCode().toLong(),
        title = this.serviceName,
        formattedDate = formattedDateStr,
        draftLabel = "완료", // 애프터노트는 도메인상 완료된 데이터로 간주
        content = this.message,
        type = this.type.name,
        category = null, // 필요 시 mapping 로직 추가
        originalDate = originalDateStr
    )
}
