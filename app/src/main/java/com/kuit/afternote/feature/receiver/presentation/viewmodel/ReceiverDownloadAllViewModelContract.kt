package com.kuit.afternote.feature.receiver.presentation.viewmodel

import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverDownloadAllUiState
import kotlinx.coroutines.flow.StateFlow

/**
 * 모든 기록 내려받기 ViewModel 계약.
 * Preview에서 Hilt 없이 Fake로 대체할 수 있도록 합니다.
 */
interface ReceiverDownloadAllViewModelContract {
    val uiState: StateFlow<ReceiverDownloadAllUiState>
    fun confirmDownloadAll(receiverId: Long)
    fun clearDownloadSuccess()
    fun clearError()
}
