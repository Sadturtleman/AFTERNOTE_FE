package com.kuit.afternote.feature.timeletter.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterMediaType
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterStatus
import com.kuit.afternote.feature.timeletter.domain.usecase.CreateTimeLetterUseCase
import com.kuit.afternote.feature.timeletter.domain.usecase.GetTimeLetterUseCase
import com.kuit.afternote.feature.timeletter.domain.usecase.GetTemporaryTimeLettersUseCase
import com.kuit.afternote.feature.timeletter.domain.usecase.UpdateTimeLetterUseCase
import com.kuit.afternote.feature.timeletter.domain.usecase.UploadTimeLetterAudioUseCase
import com.kuit.afternote.feature.timeletter.domain.usecase.UploadTimeLetterImageUseCase
import com.kuit.afternote.feature.timeletter.presentation.mapper.toTimeLetterReceivers
import com.kuit.afternote.feature.timeletter.presentation.navgraph.TimeLetterRoute
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterWriterUiState
import com.kuit.afternote.feature.user.domain.usecase.GetReceiversUseCase
import com.kuit.afternote.feature.user.domain.usecase.GetUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 타임레터 작성 화면의 ViewModel
 *
 * 편지 작성에 필요한 상태를 관리하고, 임시저장(DRAFT)/정식등록(SCHEDULED)을
 * CreateTimeLetterUseCase, UpdateTimeLetterUseCase로 처리합니다.
 * 수신자 목록은 설정 화면과 동일한 GET /users/receivers를 사용합니다.
 * 라우트에 draftId가 있으면 해당 임시저장 편지를 불러와 수정 모드로 진입합니다.
 */
@HiltViewModel
class TimeLetterWriterViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val createTimeLetterUseCase: CreateTimeLetterUseCase,
        private val updateTimeLetterUseCase: UpdateTimeLetterUseCase,
        private val getTimeLetterUseCase: GetTimeLetterUseCase,
        private val getTemporaryTimeLettersUseCase: GetTemporaryTimeLettersUseCase,
        private val uploadTimeLetterImageUseCase: UploadTimeLetterImageUseCase,
        private val uploadTimeLetterAudioUseCase: UploadTimeLetterAudioUseCase,
        private val getReceiversUseCase: GetReceiversUseCase,
        private val getUserIdUseCase: GetUserIdUseCase
    ) : ViewModel() {
        private companion object {
            private const val TAG = "TimeLetterWriterVM"
            private const val WAITING_AGAIN_POPUP_DURATION_MS = 2000L
            private const val MAX_IMAGE_COUNT = 6
        }
        private val draftIdFromRoute: Long? =
            savedStateHandle.toRoute<TimeLetterRoute.TimeLetterWriterRoute>().draftId

        private val _uiState = MutableStateFlow(TimeLetterWriterUiState())
        val uiState: StateFlow<TimeLetterWriterUiState> = _uiState.asStateFlow()
        private var waitingAgainPopUpJob: Job? = null

        init {
            loadDraftCount()
            loadReceivers()
            if (draftIdFromRoute != null) {
                loadDraft(draftIdFromRoute)
            }
        }

        /**
         * 임시저장된 편지 불러오기 (GET /time-letters/{timeLetterId})
         */
        private fun loadDraft(timeLetterId: Long) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                getTimeLetterUseCase(timeLetterId)
                    .onSuccess { letter ->
                        val (date, time) = parseSendAtToDateAndTime(letter.sendAt)
                        val existingUrls = letter.mediaList
                            .filter { it.mediaType == TimeLetterMediaType.IMAGE }
                            .map { it.mediaUrl }
                        _uiState.update {
                            it.copy(
                                draftId = letter.id,
                                title = letter.title ?: "",
                                content = letter.content ?: "",
                                sendDate = date,
                                sendTime = time,
                                existingMediaUrls = existingUrls,
                                selectedImageUriStrings = emptyList(),
                                selectedVoiceUriStrings = emptyList(),
                                isLoading = false
                            )
                        }
                        validateSaveEnabled()
                    }
                    .onFailure {
                        _uiState.update { it.copy(isLoading = false) }
                    }
            }
        }

        /**
         * sendAt (yyyy-MM-ddTHH:mm:ss) -> (날짜, 시간) 쌍으로 변환
         */
        private fun parseSendAtToDateAndTime(sendAt: String?): Pair<String, String> {
            if (sendAt.isNullOrBlank()) return "" to ""
            val parts = sendAt.split("T")
            val date = parts.getOrNull(0)?.replace("-", ". ")?.trim() ?: ""
            val timePart = parts.getOrNull(1) ?: ""
            val time = timePart.take(5) // HH:mm
            return date to time
        }

        /**
         * 수신자 목록 로드 (GET /users/receivers, 설정 수신자 목록과 동일 소스)
         */
        fun loadReceivers() {
            viewModelScope.launch {
                val userId = getUserIdUseCase()
                if (userId == null) {
                    _uiState.update { it.copy(receivers = emptyList()) }
                    return@launch
                }
                getReceiversUseCase(userId = userId)
                    .onSuccess { list ->
                        _uiState.update { it.copy(receivers = list.toTimeLetterReceivers()) }
                    }
                    .onFailure {
                        _uiState.update { it.copy(receivers = emptyList()) }
                    }
            }
        }

        /**
         * 임시저장된 레터 개수 로드 (GET /time-letters/temporary)
         */
        private fun loadDraftCount() {
            viewModelScope.launch {
                getTemporaryTimeLettersUseCase()
                    .onSuccess { list ->
                        _uiState.update { it.copy(draftCount = list.totalCount) }
                    }.onFailure {
                        _uiState.update { it.copy(draftCount = 0) }
                    }
            }
        }

        /**
         * 임시저장 목록 변경 후 개수 갱신 (예: 임시저장 성공 시)
         */
        fun refreshDraftCount() {
            loadDraftCount()
        }

        /**
         * 선택된 수신자 ID 목록 업데이트 (TimeLetterCreateRequest.receiverIds와 동일 소스)
         *
         * @param ids 선택된 수신자 ID 목록
         */
        fun updateSelectedReceiverIds(ids: List<Long>) {
            _uiState.update { it.copy(receiverIds = ids) }
            validateSaveEnabled()
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
         * 첨부 이미지 추가. 최대 [MAX_IMAGE_COUNT]장까지 (기존 + 새로 추가 합계).
         *
         * @param uris 갤러리에서 선택한 이미지 URI 목록
         */
        fun addImageUris(uris: List<Uri>) {
            val newStrings = uris.map { it.toString() }
            _uiState.update { state ->
                val current = state.selectedImageUriStrings + state.existingMediaUrls
                val toAdd = newStrings.take((MAX_IMAGE_COUNT - current.size).coerceAtLeast(0))
                state.copy(selectedImageUriStrings = state.selectedImageUriStrings + toAdd)
            }
        }

        /**
         * 첨부 이미지 1장 제거
         *
         * @param uriString 제거할 이미지 URI 문자열
         */
        fun removeImageUri(uriString: String) {
            _uiState.update {
                it.copy(selectedImageUriStrings = it.selectedImageUriStrings - uriString)
            }
        }

        /**
         * 첨부 음성/오디오 추가
         *
         * @param uris 문서 선택기에서 선택한 오디오 URI 목록
         */
        fun addVoiceUris(uris: List<Uri>) {
            val newStrings = uris.map { it.toString() }
            _uiState.update { state ->
                state.copy(selectedVoiceUriStrings = state.selectedVoiceUriStrings + newStrings)
            }
        }

        /**
         * 첨부 음성/오디오 1개 제거
         *
         * @param uriString 제거할 오디오 URI 문자열
         */
        fun removeVoiceUri(uriString: String) {
            _uiState.update {
                it.copy(selectedVoiceUriStrings = it.selectedVoiceUriStrings - uriString)
            }
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
         * 더보기(작성 플러스) 메뉴 표시
         */
        fun showPlusMenu() {
            _uiState.update { it.copy(showWritingPlusMenu = true) }
        }

        /**
         * 더보기(작성 플러스) 메뉴 숨김
         */
        fun hidePlusMenu() {
            _uiState.update { it.copy(showWritingPlusMenu = false) }
        }

        /**
         * 수신자 선택 드롭다운 표시 (열 때마다 목록 갱신)
         */
        fun showRecipientDropdown() {
            loadReceivers()
            _uiState.update { it.copy(showRecipientDropdown = true) }
        }

        /**
         * 수신자 선택 드롭다운 숨김
         */
        fun hideRecipientDropdown() {
            _uiState.update { it.copy(showRecipientDropdown = false) }
        }

        /**
         * 발송 시간 업데이트
         *
         * @param time 발송 시간 (HH:mm 형식)
         */
        fun updateSendTime(time: String) {
            _uiState.update { it.copy(sendTime = time) }
            validateSaveEnabled()
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
         * TimePicker 표시
         */
        fun showTimePicker() {
            _uiState.update { it.copy(showTimePicker = true) }
        }

        /**
         * TimePicker 숨기기
         */
        fun hideTimePicker() {
            _uiState.update { it.copy(showTimePicker = false) }
        }

        /**
         * 저장 버튼 활성화 여부 검증
         *
         * 수신자, 제목, 내용, 날짜, 시간이 모두 입력되어야 저장 가능
         */
        private fun validateSaveEnabled() {
            val state = _uiState.value
            val isEnabled = state.receiverIds.isNotEmpty() &&
                state.title.isNotBlank() &&
                state.content.isNotBlank() &&
                state.sendDate.isNotBlank() &&
                state.sendTime.isNotBlank()
            _uiState.update { it.copy(isSaveEnabled = isEnabled) }
        }

        /**
         * sendAt 문자열 생성 (yyyy. MM. dd + HH:mm -> yyyy-MM-ddTHH:mm:00)
         */
        private fun buildSendAt(
            sendDate: String,
            sendTime: String
        ): String? {
            if (sendDate.isBlank() || sendTime.isBlank()) return null
            val normalizedDate = sendDate.replace(". ", "-").trim()
            return "$normalizedDate" + "T" + "$sendTime" + ":00"
        }

        /**
         * 현재 상태 기준으로 mediaList 구성. 선택 이미지·오디오 업로드 후 (타입, url) 쌍 목록 반환.
         * 이미지·오디오가 하나도 없으면 null 반환.
         */
        private suspend fun buildMediaList(state: TimeLetterWriterUiState): Result<List<Pair<TimeLetterMediaType, String>>?> {
            val existingImages = state.existingMediaUrls
            val pendingImages = state.selectedImageUriStrings
            val pendingVoices = state.selectedVoiceUriStrings
            if (existingImages.isEmpty() && pendingImages.isEmpty() && pendingVoices.isEmpty()) {
                return Result.success(null)
            }
            val mediaList = mutableListOf<Pair<TimeLetterMediaType, String>>()

            val newImageUrls = mutableListOf<String>()
            for (uriString in pendingImages) {
                uploadTimeLetterImageUseCase(uriString)
                    .onSuccess { newImageUrls.add(it) }
                    .onFailure { return Result.failure(it) }
            }
            val allImageUrls = existingImages + newImageUrls
            mediaList.addAll(allImageUrls.map { TimeLetterMediaType.IMAGE to it })

            for (uriString in pendingVoices) {
                uploadTimeLetterAudioUseCase(uriString)
                    .onSuccess { url -> mediaList.add(TimeLetterMediaType.AUDIO to url) }
                    .onFailure { return Result.failure(it) }
            }

            return Result.success(mediaList.ifEmpty { null })
        }

        /**
         * 등록 버튼 클릭 시: 등록 완료 팝업을 잠깐 보여준 뒤 실제 타임레터 정식등록을 수행합니다.
         *
         * @param onSuccess 저장 성공 시 콜백
         */
        fun registerWithPopUpThenSave(onSuccess: () -> Unit) {
            saveTimeLetter(onSuccess = onSuccess)
        }

        /**
         * 타임레터 정식등록 (status = SCHEDULED)
         *
         * draftId가 있으면 PATCH /time-letters/{id}, 없으면 POST /time-letters
         *
         * @param showPopUpAfterSuccess 저장 성공 후 등록 완료 팝업 표시 여부
         * @param onSuccess 저장 성공 시 콜백
         */
        fun saveTimeLetter(
            showPopUpAfterSuccess: Boolean = true,
            onSuccess: () -> Unit
        ) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                val state = _uiState.value
                val sendAt = buildSendAt(state.sendDate, state.sendTime)
                hideWaitingAgainPopUp()
                val mediaListResult = buildMediaList(state)
                val result = mediaListResult.fold(
                    onSuccess = { mediaList ->
                        if (state.draftId != null) {
                            updateTimeLetterUseCase(
                                timeLetterId = state.draftId,
                                title = state.title.ifBlank { null },
                                content = state.content.ifBlank { null },
                                sendAt = sendAt,
                                status = TimeLetterStatus.SCHEDULED,
                                mediaList = mediaList
                            )
                        } else {
                            createTimeLetterUseCase(
                                title = state.title.ifBlank { null },
                                content = state.content.ifBlank { null },
                                sendAt = sendAt,
                                status = TimeLetterStatus.SCHEDULED,
                                mediaList = mediaList,
                                receiverIds = state.receiverIds,
                                deliveredAt = sendAt
                            )
                        }
                    },
                    onFailure = { Result.failure(it) }
                )
                _uiState.update { it.copy(isLoading = false) }
                result.onSuccess { _ ->
                    Log.d(TAG, "saveTimeLetter onSuccess")
                    hideWaitingAgainPopUp()
                    if (showPopUpAfterSuccess) {
                        _uiState.update { it.copy(showRegisteredPopUp = true) }
                        delay(2000L)
                        onSuccess()
                        _uiState.update { it.copy(showRegisteredPopUp = false) }
                    } else {
                        onSuccess()
                    }
                }
                result.onFailure { error ->
                    Log.e(TAG, "saveTimeLetter failed", error)
                    showWaitingAgainPopUp()
                    _uiState.update {
                        it.copy(
                            errorMessage = error.message ?: error.localizedMessage ?: "저장에 실패했습니다."
                        )
                    }
                }
            }
        }

        /**
         * 임시저장 (status = DRAFT)
         *
         * draftId가 있으면 PATCH /time-letters/{id}, 없으면 POST /time-letters
         *
         * @param onSuccess 저장 성공 시 콜백
         */
        fun saveDraft(onSuccess: () -> Unit) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                val state = _uiState.value
                val sendAt = buildSendAt(state.sendDate, state.sendTime)
                val mediaListResult = buildMediaList(state)
                val result = mediaListResult.fold(
                    onSuccess = { mediaList ->
                        if (state.draftId != null) {
                            updateTimeLetterUseCase(
                                timeLetterId = state.draftId,
                                title = state.title.ifBlank { null },
                                content = state.content.ifBlank { null },
                                sendAt = sendAt,
                                status = TimeLetterStatus.DRAFT,
                                mediaList = mediaList
                            )
                        } else {
                            createTimeLetterUseCase(
                                title = state.title.ifBlank { null },
                                content = state.content.ifBlank { null },
                                sendAt = sendAt,
                                status = TimeLetterStatus.DRAFT,
                                mediaList = mediaList,
                                receiverIds = state.receiverIds,
                                deliveredAt = sendAt
                            )
                        }
                    },
                    onFailure = { Result.failure(it) }
                )
                _uiState.update { it.copy(isLoading = false) }
                result.onSuccess { _ ->
                    refreshDraftCount()
                    _uiState.update { it.copy(showDraftSavePopUp = true) }
                    delay(2000L)
                    _uiState.update { it.copy(showDraftSavePopUp = false) }
                    onSuccess()
                }
                result.onFailure { error ->
                    _uiState.update {
                        it.copy(
                            errorMessage = error.message ?: error.localizedMessage ?: "임시저장에 실패했습니다."
                        )
                    }
                }
            }
        }

        /**
         * 상태 초기화
         */
        fun resetState() {
            hideWaitingAgainPopUp()
            _uiState.value = TimeLetterWriterUiState()
        }

        private fun showWaitingAgainPopUp() {
            _uiState.update { it.copy(showWaitingAgainPopUp = true) }
            waitingAgainPopUpJob?.cancel()
            waitingAgainPopUpJob = viewModelScope.launch {
                delay(WAITING_AGAIN_POPUP_DURATION_MS)
                _uiState.update { it.copy(showWaitingAgainPopUp = false) }
            }
        }

        private fun hideWaitingAgainPopUp() {
            waitingAgainPopUpJob?.cancel()
            waitingAgainPopUpJob = null
            _uiState.update { it.copy(showWaitingAgainPopUp = false) }
        }
    }
