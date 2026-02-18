package com.kuit.afternote.feature.receiverauth.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.data.remote.ApiException
import com.kuit.afternote.feature.receiverauth.domain.usecase.GetDeliveryVerificationStatusUseCase
import com.kuit.afternote.feature.receiverauth.domain.usecase.SubmitDeliveryVerificationUseCase
import com.kuit.afternote.feature.receiverauth.domain.usecase.UploadReceiverDocumentUseCase
import com.kuit.afternote.feature.receiverauth.domain.usecase.VerifyReceiverAuthUseCase
import com.kuit.afternote.feature.receiverauth.uimodel.VerifyStep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
        private val verifyReceiverAuthUseCase: VerifyReceiverAuthUseCase,
        private val uploadReceiverDocumentUseCase: UploadReceiverDocumentUseCase,
        private val submitDeliveryVerificationUseCase: SubmitDeliveryVerificationUseCase,
        private val getDeliveryVerificationStatusUseCase: GetDeliveryVerificationStatusUseCase
    ) : ViewModel(), VerifySelfViewModelContract {

    private val _uiState = MutableStateFlow(VerifySelfUiState())
    override val uiState: StateFlow<VerifySelfUiState> = _uiState.asStateFlow()

    private var deliveryStatusPollingJob: Job? = null

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
                            verifiedReceiverId = result.receiverId,
                            verifiedSenderName = result.senderName
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
            if (current == VerifyStep.END) {
                deliveryStatusPollingJob?.cancel()
                deliveryStatusPollingJob = null
            }
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

    /**
     * 증빙 서류(사망진단서, 가족관계증명서) 업로드 후 delivery-verification 제출.
     *
     * 두 URI가 모두 있을 때만 업로드·제출하고, 성공 시 END 단계로 이동한 뒤 인증 상태를 조회합니다.
     */
    override fun submitDocuments(deathCertUri: String?, familyCertUri: String?) {
        if (deathCertUri.isNullOrBlank() || familyCertUri.isNullOrBlank()) {
            _uiState.update {
                it.copy(submitError = VerifyErrorType.Required)
            }
            return
        }
        val authCode = _uiState.value.masterKeyInput.trim()
        if (authCode.isBlank()) {
            _uiState.update { it.copy(submitError = VerifyErrorType.Unknown) }
            return
        }
        viewModelScope.launch {
            _uiState.update {
                it.copy(isSubmitting = true, submitError = null)
            }
            val deathUrlResult = uploadReceiverDocumentUseCase(authCode, deathCertUri)
            val deathUrl = deathUrlResult.getOrElse { e ->
                Log.e(TAG, "Upload death certificate failed", e)
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        submitError = mapVerifyErrorToType(e)
                    )
                }
                return@launch
            }
            val familyUrlResult = uploadReceiverDocumentUseCase(authCode, familyCertUri)
            val familyUrl = familyUrlResult.getOrElse { e ->
                Log.e(TAG, "Upload family relation certificate failed", e)
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        submitError = mapVerifyErrorToType(e)
                    )
                }
                return@launch
            }
            submitDeliveryVerificationUseCase(authCode, deathUrl, familyUrl)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            currentStep = VerifyStep.END,
                            isSubmitting = false,
                            submitError = null
                        )
                    }
                    loadDeliveryVerificationStatus(authCode)
                    deliveryStatusPollingJob?.cancel()
                    deliveryStatusPollingJob = viewModelScope.launch {
                        while (_uiState.value.currentStep == VerifyStep.END) {
                            delay(POLLING_INTERVAL_MS)
                            if (_uiState.value.currentStep != VerifyStep.END) break
                            loadDeliveryVerificationStatus(authCode)
                        }
                    }
                }
                .onFailure { e ->
                    Log.e(TAG, "Submit delivery verification failed", e)
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            submitError = mapVerifyErrorToType(e)
                        )
                    }
                }
        }
    }

    private fun loadDeliveryVerificationStatus(authCode: String) {
        viewModelScope.launch {
            getDeliveryVerificationStatusUseCase(authCode)
                .onSuccess { status ->
                    val isApproved = status.status.equals("APPROVED", ignoreCase = true)
                    if (isApproved) {
                        deliveryStatusPollingJob?.cancel()
                        deliveryStatusPollingJob = null
                    }
                    _uiState.update {
                        it.copy(deliveryVerificationStatus = status)
                    }
                }
                .onFailure { e ->
                    Log.e(TAG, "Load delivery verification status failed", e)
                }
        }
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
        private const val POLLING_INTERVAL_MS = 5000L
    }
}
