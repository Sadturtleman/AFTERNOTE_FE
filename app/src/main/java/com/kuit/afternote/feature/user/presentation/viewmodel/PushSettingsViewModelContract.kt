package com.kuit.afternote.feature.user.presentation.viewmodel

import com.kuit.afternote.feature.user.presentation.uimodel.PushSettingsUiState
import kotlinx.coroutines.flow.StateFlow

/**
 * Contract for push notification setting screen so a fake can be used in Previews (no Hilt).
 */
interface PushSettingsViewModelContract {
    val uiState: StateFlow<PushSettingsUiState>
    fun loadPushSettings()
    fun updatePushSettings(
        timeLetter: Boolean?,
        mindRecord: Boolean?,
        afterNote: Boolean?
    )
    fun clearUpdateSuccess()
    fun clearError()
}
