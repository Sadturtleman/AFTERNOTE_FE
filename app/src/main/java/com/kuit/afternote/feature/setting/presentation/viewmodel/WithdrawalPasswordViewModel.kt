package com.kuit.afternote.feature.setting.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * UI state for the withdrawal password confirmation screen.
 */
data class WithdrawalPasswordUiState(
    val showPasswordError: Boolean = false,
    val withdrawalComplete: Boolean = false
)

/**
 * ViewModel for the withdrawal password screen. Handles submit and wrong-password error state.
 * When the withdrawal API is available, [submitWithdrawal] should call it and set
 * [WithdrawalPasswordUiState.showPasswordError] on wrong-password response.
 */
@HiltViewModel
class WithdrawalPasswordViewModel
@Inject
constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(WithdrawalPasswordUiState())
    val uiState: StateFlow<WithdrawalPasswordUiState> = _uiState.asStateFlow()

    /**
     * Called when the user taps "탈퇴하기" with the entered password.
     * Call withdrawal API; on success set [WithdrawalPasswordUiState.withdrawalComplete] true
     * to show completion dialog. On wrong-password response set
     * [WithdrawalPasswordUiState.showPasswordError] true.
     *
     * @param password User-entered password; will be sent to withdrawal API when implemented.
     */
    @Suppress("UNUSED_PARAMETER")
    fun submitWithdrawal(password: String) {
        // TODO: Call withdrawal API; on success set withdrawalComplete = true, on wrong password set showPasswordError = true
        _uiState.update { it.copy(showPasswordError = true) }
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
