package com.kuit.afternote.feature.setting.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.data.local.TokenManager
import com.kuit.afternote.feature.user.domain.usecase.WithdrawAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

/**
 * 회원 탈퇴 확인 문장.
 * 사용자가 이 문장을 정확히 입력해야 탈퇴 API를 호출한다.
 */
private const val CONFIRMATION_SENTENCE = "탈퇴하겠습니다."

/**
 * UI state for the withdrawal confirmation screen.
 */
data class WithdrawalPasswordUiState(
    val showSentenceError: Boolean = false,
    val withdrawalComplete: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

/**
 * ViewModel for the withdrawal confirmation screen.
 *
 * 사용자가 "탈퇴하겠습니다."를 정확히 입력한 경우에만 DELETE /users/me (회원 탈퇴)를 호출한다.
 * 문장이 일치하지 않으면 [WithdrawalPasswordUiState.showSentenceError]를 설정한다.
 * 성공 시 토큰을 삭제하고 [WithdrawalPasswordUiState.withdrawalComplete]를 설정한다.
 */
@HiltViewModel
class WithdrawalPasswordViewModel
    @Inject
    constructor(
        private val withdrawAccountUseCase: WithdrawAccountUseCase,
        private val tokenManager: TokenManager,
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(WithdrawalPasswordUiState())
    val uiState: StateFlow<WithdrawalPasswordUiState> = _uiState.asStateFlow()

    /**
     * Called when the user taps "탈퇴하기".
     * 입력된 문장이 확인 문장과 일치하는지 검증한 후, API를 호출한다.
     *
     * @param confirmationText 사용자가 입력한 확인 문장
     */
    fun submitWithdrawal(confirmationText: String) {
        if (confirmationText.trim() != CONFIRMATION_SENTENCE) {
            _uiState.update { it.copy(showSentenceError = true) }
            return
        }
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, showSentenceError = false, errorMessage = null)
            }
            withdrawAccountUseCase()
                .onSuccess {
                    tokenManager.clearTokens()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            withdrawalComplete = true,
                        )
                    }
                }
                .onFailure { e ->
                    val message = when (e) {
                        is HttpException -> "HTTP ${e.code()} ${e.message()}"
                        else -> e.message ?: "회원 탈퇴에 실패했습니다."
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = message,
                        )
                    }
                }
        }
    }

    /**
     * Clears the sentence mismatch error when the user edits the input.
     */
    fun clearSentenceError() {
        _uiState.update { it.copy(showSentenceError = false) }
    }

    /**
     * Clears the withdrawal-complete state after the user taps "확인하기" on the completion dialog.
     */
    fun clearWithdrawalComplete() {
        _uiState.update { it.copy(withdrawalComplete = false) }
    }
}
