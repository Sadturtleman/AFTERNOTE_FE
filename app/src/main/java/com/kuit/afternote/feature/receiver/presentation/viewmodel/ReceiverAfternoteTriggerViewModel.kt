package com.kuit.afternote.feature.receiver.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.receiver.domain.usecase.GetAfterNotesByAuthCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신자 HOME에서 "애프터노트 확인하러 가기" 버튼을 눌렀을 때 after-notes API만 호출하는 ViewModel.
 *
 * 화면 전환은 하지 않고, 버튼 콜백에서 [loadAfterNotes]만 호출할 때 사용합니다.
 */
@HiltViewModel
class ReceiverAfternoteTriggerViewModel
    @Inject
    constructor(
        private val getAfterNotesByAuthCodeUseCase: GetAfterNotesByAuthCodeUseCase
    ) : ViewModel() {

    /**
     * GET /api/receiver-auth/after-notes (X-Auth-Code) API를 호출합니다.
     *
     * @param authCode 수신자 인증번호 (마스터키)
     */
    fun loadAfterNotes(authCode: String) {
        viewModelScope.launch {
            getAfterNotesByAuthCodeUseCase(authCode)
            // 결과는 사용하지 않음. 호출만 수행하여 서버/캐시 등 부수 효과 목적.
        }
    }
}
