package com.kuit.afternote.feature.receiver.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.receiver.domain.usecase.GetAfterNotesByAuthCodeUseCase
import com.kuit.afternote.feature.receiver.domain.usecase.GetMindRecordsByAuthCodeUseCase
import com.kuit.afternote.feature.receiver.domain.usecase.GetTimeLettersByAuthCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신자 HOME에서 콘텐츠 개수(마음의 기록, 타임레터, 애프터노트) 및 leaveMessage를 로드하는 ViewModel.
 *
 * [loadHomeSummary]로 세 API를 병렬 호출하여 totalCount를 수집합니다.
 * [loadAfterNotes]는 "애프터노트 확인하러 가기" 버튼 클릭 시 leaveMessage 갱신용으로 호출됩니다.
 */
@HiltViewModel
class ReceiverAfternoteTriggerViewModel
    @Inject
    constructor(
        private val getAfterNotesByAuthCodeUseCase: GetAfterNotesByAuthCodeUseCase,
        private val getMindRecordsByAuthCodeUseCase: GetMindRecordsByAuthCodeUseCase,
        private val getTimeLettersByAuthCodeUseCase: GetTimeLettersByAuthCodeUseCase
    ) : ViewModel() {

    private val _leaveMessage = MutableStateFlow<String?>(null)
    val leaveMessage: StateFlow<String?> = _leaveMessage.asStateFlow()

    private val _mindRecordTotalCount = MutableStateFlow(0)
    val mindRecordTotalCount: StateFlow<Int> = _mindRecordTotalCount.asStateFlow()

    private val _timeLetterTotalCount = MutableStateFlow(0)
    val timeLetterTotalCount: StateFlow<Int> = _timeLetterTotalCount.asStateFlow()

    private val _afternoteTotalCount = MutableStateFlow(0)
    val afternoteTotalCount: StateFlow<Int> = _afternoteTotalCount.asStateFlow()

    /**
     * 홈 화면용 요약 데이터 로드. 마음의 기록, 타임레터, 애프터노트 totalCount와 leaveMessage를 가져옵니다.
     *
     * @param authCode 수신자 인증번호 (마스터키)
     */
    fun loadHomeSummary(authCode: String) {
        viewModelScope.launch {
            val afternotesDeferred = async { getAfterNotesByAuthCodeUseCase(authCode) }
            val mindRecordsDeferred = async { getMindRecordsByAuthCodeUseCase(authCode) }
            val timeLettersDeferred = async { getTimeLettersByAuthCodeUseCase(authCode) }
            awaitAll(afternotesDeferred, mindRecordsDeferred, timeLettersDeferred)

            afternotesDeferred.await().onSuccess { result ->
                val first =
                    result.items.firstOrNull { it.leaveMessage?.isNotBlank() == true }
                _leaveMessage.value = first?.leaveMessage
                _afternoteTotalCount.value = result.totalCount
            }
            mindRecordsDeferred.await().onSuccess { result ->
                _mindRecordTotalCount.value = result.totalCount
            }
            timeLettersDeferred.await().onSuccess { result ->
                _timeLetterTotalCount.value = result.totalCount
            }
        }
    }

    /**
     * GET /api/receiver-auth/after-notes (X-Auth-Code) API를 호출합니다.
     * leaveMessage 갱신 및 afternoteTotalCount 업데이트.
     *
     * @param authCode 수신자 인증번호 (마스터키)
     */
    fun loadAfterNotes(authCode: String) {
        viewModelScope.launch {
            getAfterNotesByAuthCodeUseCase(authCode)
                .onSuccess { result ->
                    val first =
                        result.items.firstOrNull { it.leaveMessage?.isNotBlank() == true }
                    _leaveMessage.value = first?.leaveMessage
                    _afternoteTotalCount.value = result.totalCount
                }
        }
    }
}
