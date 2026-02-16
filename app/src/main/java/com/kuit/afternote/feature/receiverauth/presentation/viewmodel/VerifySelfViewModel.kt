package com.kuit.afternote.feature.receiverauth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.data.remote.ApiException
import com.kuit.afternote.feature.receiverauth.domain.usecase.VerifyReceiverAuthUseCase
import com.kuit.afternote.feature.receiverauth.uimodel.VerifyStep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

/**
 * 수신자 본인 인증(VerifySelf) 화면 ViewModel.
 *
 * 마스터 키 입력 후 verify API를 호출하고, 200이면 다음 단계로, 400 등 실패 시 에러 메시지를 표시합니다.
 */
@HiltViewModel
class VerifySelfViewModel
    @Inject
    constructor(
        private val verifyReceiverAuthUseCase: VerifyReceiverAuthUseCase
    ) : ViewModel(), VerifySelfViewModelContract {

    private val _uiState = MutableStateFlow(VerifySelfUiState())
    override val uiState: StateFlow<VerifySelfUiState> = _uiState.asStateFlow()

    /**
     * 마스터 키 입력값 갱신. 에러 메시지 초기화.
     */
    override fun updateMasterKey(text: String) {
        _uiState.update {
            it.copy(masterKeyInput = text, verifyError = null)
        }
    }

    /**
     * 마스터 키 검증 요청.
     *
     * 성공(200) 시 다음 단계(UPLOAD_PDF_AUTH)로 이동, 실패(400 등) 시 errorMessage 설정 후 재입력.
     */
    override fun verifyMasterKey() {
        val authCode = _uiState.value.masterKeyInput.trim()
        if (authCode.isBlank()) {
            _uiState.update {
                it.copy(verifyError = VerifyErrorType.Required)
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, verifyError = null) }
            verifyReceiverAuthUseCase(authCode)
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            currentStep = VerifyStep.UPLOAD_PDF_AUTH,
                            isLoading = false,
                            verifyError = null,
                            verifiedReceiverId = result.receiverId
                        )
                    }
                }
                .onFailure { e ->
                    Log.e(TAG, "Verify master key failed", e)
                    val errorType = mapVerifyErrorToType(e)
                    _uiState.update {
                        it.copy(isLoading = false, verifyError = errorType)
                    }
                }
        }
    }

    /**
     * 다음 단계로 이동 (PDF 업로드 → 완료 등).
     */
    override fun goToNextStep() {
        _uiState.update { state ->
            val next = when (state.currentStep) {
                VerifyStep.MASTER_KEY_AUTH -> VerifyStep.UPLOAD_PDF_AUTH
                VerifyStep.UPLOAD_PDF_AUTH -> VerifyStep.END
                VerifyStep.END -> VerifyStep.END
            }
            state.copy(currentStep = next)
        }
    }

    /**
     * 이전 단계로 이동.
     */
    override fun goToPreviousStep(): VerifyStep? {
        val current = _uiState.value.currentStep
        val previous = current.previous()
        if (previous != null) {
            _uiState.update { it.copy(currentStep = previous, verifyError = null) }
        }
        return previous
    }

    /**
     * 에러 메시지 초기화.
     */
    override fun clearVerifyError() {
        _uiState.update { it.copy(verifyError = null) }
    }

    private fun mapVerifyErrorToType(e: Throwable): VerifyErrorType {
        return when (e) {
            is ApiException -> e.message?.takeIf { it.isNotBlank() }?.let { VerifyErrorType.Server(it) }
                ?: VerifyErrorType.Unknown
            is IOException -> VerifyErrorType.Network
            else -> e.message?.takeIf { it.isNotBlank() }?.let { VerifyErrorType.Server(it) }
                ?: VerifyErrorType.Unknown
        }
    }

    companion object {
        private const val TAG = "VerifySelfViewModel"
    }
}
