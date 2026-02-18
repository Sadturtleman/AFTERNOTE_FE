package com.kuit.afternote.feature.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.user.domain.usecase.GetMyProfileUseCase
import com.kuit.afternote.feature.user.domain.usecase.GetUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Exposes the current logged-in user's name for use in nav graph / detail screens.
 * Loads profile once so the Afternote detail title shows "{serviceName}에 대한 {userName}님의 기록"
 * with the actual user name, not a receiver title.
 */
@HiltViewModel
class CurrentUserNameViewModel
    @Inject
    constructor(
        private val getUserIdUseCase: GetUserIdUseCase,
        private val getMyProfileUseCase: GetMyProfileUseCase
    ) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    /** Loads the current user's name from profile so detail screens show the correct name. */
    fun loadUserName() {
        viewModelScope.launch {
            val userId = getUserIdUseCase() ?: return@launch
            getMyProfileUseCase(userId = userId)
                .onSuccess { profile -> _userName.value = profile.name }
        }
    }

    init {
        loadUserName()
    }
}
