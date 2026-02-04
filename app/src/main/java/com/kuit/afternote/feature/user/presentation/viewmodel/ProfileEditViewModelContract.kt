package com.kuit.afternote.feature.user.presentation.viewmodel

import com.kuit.afternote.feature.user.presentation.uimodel.ProfileUiState
import kotlinx.coroutines.flow.StateFlow

/**
 * Contract for ProfileEditScreen so a fake can be used in Previews (no Hilt).
 */
interface ProfileEditViewModelContract {
    val uiState: StateFlow<ProfileUiState>
    fun loadProfile()
    fun updateProfile(name: String?, phone: String?, profileImageUrl: String?)
    fun setSelectedProfileImageUri(uri: android.net.Uri?)
    fun clearUpdateSuccess()
}
