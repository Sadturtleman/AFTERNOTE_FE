package com.kuit.afternote.feature.receiver.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedAfternoteDetail
import com.kuit.afternote.feature.receiver.domain.usecase.GetAfternoteDetailByAuthCodeUseCase
import com.kuit.afternote.feature.receiverauth.session.ReceiverAuthSessionHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신자 애프터노트 상세 화면 ViewModel.
 *
 * GET /api/receiver-auth/after-notes/{afternoteId} (X-Auth-Code)로 상세를 조회합니다.
 */
private const val TAG = "ReceiverAfternoteDetailVM"

@HiltViewModel
class ReceiverAfternoteDetailViewModel
    @Inject
    constructor(
        private val receiverAuthSessionHolder: ReceiverAuthSessionHolder,
        private val getAfternoteDetailByAuthCodeUseCase: GetAfternoteDetailByAuthCodeUseCase
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(ReceiverAfternoteDetailUiState())
    val uiState: StateFlow<ReceiverAfternoteDetailUiState> = _uiState.asStateFlow()

    fun loadDetail(itemId: String?) {
        Log.d(TAG, "loadDetail called: itemId=$itemId")
        if (itemId.isNullOrBlank()) {
            Log.d(TAG, "loadDetail: early return (itemId null or blank)")
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "잘못된 항목 ID입니다."
                )
            }
            return
        }
        val afternoteId = itemId.toLongOrNull()
        if (afternoteId == null) {
            Log.d(TAG, "loadDetail: early return (afternoteId parse failed for itemId=$itemId)")
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "잘못된 항목 ID입니다."
                )
            }
            return
        }
        val authCode = receiverAuthSessionHolder.getAuthCode()
        if (authCode.isNullOrBlank()) {
            Log.d(TAG, "loadDetail: early return (authCode null or blank)")
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "인증 정보가 없습니다."
                )
            }
            return
        }
        Log.d(TAG, "loadDetail: calling API authCode=*** afternoteId=$afternoteId")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getAfternoteDetailByAuthCodeUseCase(authCode, afternoteId)
                .onSuccess { detail ->
                    _uiState.update {
                        it.copy(
                            detail = detail,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "상세 정보를 불러오지 못했습니다."
                        )
                    }
                }
        }
    }
}

data class ReceiverAfternoteDetailUiState(
    val detail: ReceivedAfternoteDetail? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
