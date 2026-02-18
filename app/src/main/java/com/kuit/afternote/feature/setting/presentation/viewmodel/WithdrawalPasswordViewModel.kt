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
 * UI state for the withdrawal password confirmation screen.
 */
data class WithdrawalPasswordUiState(
    val showPasswordError: Boolean = false,
    val withdrawalComplete: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel for the withdrawal password screen. Calls DELETE /users/me (회원 탈퇴) on submit.
 * On success clears tokens and sets [WithdrawalPasswordUiState.withdrawalComplete].
 * On 401 or wrong-password response sets [WithdrawalPasswordUiState.showPasswordError].
 */
@HiltViewModel
class WithdrawalPasswordViewModel
    @Inject
    constructor(
        private val withdrawAccountUseCase: WithdrawAccountUseCase,
        private val tokenManager: TokenManager
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(WithdrawalPasswordUiState())
    val uiState: StateFlow<WithdrawalPasswordUiState> = _uiState.asStateFlow()

    /**
     * Called when the user taps "탈퇴하기". Validates password is non-empty, then calls
     * DELETE /users/me. On success clears tokens and sets withdrawalComplete for the completion dialog.
     * On 401 sets showPasswordError; on other errors sets errorMessage.
     *
     * @param password User-entered password; validated locally (non-empty). API has no parameters.
     */
    fun submitWithdrawal(password: String) {
        if (password.isBlank()) {
            _uiState.update { it.copy(showPasswordError = true) }
            return
        }
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, showPasswordError = false, errorMessage = null)
            }
            withdrawAccountUseCase()
                .onSuccess {
                    tokenManager.clearTokens()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            withdrawalComplete = true
                        )
                    }
                }
                .onFailure { e ->
                    val isUnauthorized = (e as? HttpException)?.code() == 401
                    val message = when (e) {
                        is HttpException -> "HTTP ${e.code()} ${e.message()}"
                        else -> e.message ?: "회원 탈퇴에 실패했습니다."
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            showPasswordError = isUnauthorized,
                            errorMessage = if (isUnauthorized) null else message
                        )
                    }
                }
        }
    }

    /**
     * Clears the password error so the message is hidden when the user edits the password.
     */
    fun clearPasswordError() {
        _uiState.update { it.copy(showPasswordError = false) }
    }

    /**
     * Clears the withdrawal-complete state after the user taps "확인하기" on the completion dialog.
     */
    fun clearWithdrawalComplete() {
        _uiState.update { it.copy(withdrawalComplete = false) }
    }
}
