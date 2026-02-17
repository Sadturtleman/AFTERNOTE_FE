package com.kuit.afternote.feature.receiver.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.receiver.domain.repository.iface.ExportReceivedRepository
import com.kuit.afternote.feature.receiver.domain.usecase.DownloadAllReceivedUseCase
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverDownloadAllUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 모든 기록 내려받기 다이얼로그 확인 시 타임레터·마인드레코드·애프터노트를 조회한 뒤 JSON 파일로 저장하는 ViewModel.
 *
 * @param downloadAllReceivedUseCase 인증번호(authCode)로 세 목록을 순차 조회하는 UseCase
 * @param exportReceivedRepository 조회 결과를 JSON 파일로 저장하는 Repository
 */
@HiltViewModel
class ReceiverDownloadAllViewModel
    @Inject
    constructor(
        private val downloadAllReceivedUseCase: DownloadAllReceivedUseCase,
        private val exportReceivedRepository: ExportReceivedRepository
    ) : ViewModel(), ReceiverDownloadAllViewModelContract {

    private val _uiState = MutableStateFlow(ReceiverDownloadAllUiState())
    override val uiState: StateFlow<ReceiverDownloadAllUiState> = _uiState.asStateFlow()

    /**
     * 다이얼로그 "예" 선택 시 호출. 인증번호(마스터키)로 세 API를 순차 호출합니다.
     *
     * @param authCode 수신자 인증번호 (마스터키)
     */
    override fun confirmDownloadAll(authCode: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null, downloadSuccess = false)
            }
            downloadAllReceivedUseCase(authCode)
                .onSuccess { result ->
                    exportReceivedRepository.saveToFile(result)
                        .onSuccess {
                            _uiState.update {
                                it.copy(isLoading = false, downloadSuccess = true)
                            }
                        }
                        .onFailure { e ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = e.message ?: "파일 저장에 실패했습니다."
                                )
                            }
                        }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "모든 기록 내려받기에 실패했습니다."
                        )
                    }
                }
        }
    }

    /** downloadSuccess 플래그를 초기화합니다. 다이얼로그를 닫은 뒤 호출합니다. */
    override fun clearDownloadSuccess() {
        _uiState.update { it.copy(downloadSuccess = false) }
    }

    /** errorMessage를 초기화합니다. 스낵바/토스트 소비 후 호출합니다. */
    override fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
