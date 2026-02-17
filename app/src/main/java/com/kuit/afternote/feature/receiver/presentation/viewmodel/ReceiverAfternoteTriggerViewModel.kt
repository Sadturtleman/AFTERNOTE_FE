package com.kuit.afternote.feature.receiver.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.receiver.domain.usecase.GetAfterNotesByAuthCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신자 HOME에서 "애프터노트 확인하러 가기" 버튼을 눌렀을 때 after-notes API만 호출하는 ViewModel.
 *
 * 화면 전환은 하지 않고, 버튼 콜백에서 [loadAfterNotes]만 호출할 때 사용합니다.
 * loadAfterNotes 결과에서 첫 번째 non-null leaveMessage를 [leaveMessage]로 노출합니다 (HeroCard 표시용).
 */
@HiltViewModel
class ReceiverAfternoteTriggerViewModel
    @Inject
    constructor(
        private val getAfterNotesByAuthCodeUseCase: GetAfterNotesByAuthCodeUseCase
    ) : ViewModel() {

    private val _leaveMessage = MutableStateFlow<String?>(null)
    val leaveMessage: StateFlow<String?> = _leaveMessage.asStateFlow()

    /**
     * GET /api/receiver-auth/after-notes (X-Auth-Code) API를 호출합니다.
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
                }
        }
    }
}
