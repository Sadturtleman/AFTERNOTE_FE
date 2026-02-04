package com.kuit.afternote.feature.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.user.domain.usecase.RegisterReceiverUseCase
import com.kuit.afternote.feature.user.presentation.uimodel.RegisterReceiverUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신자 등록 화면 ViewModel.
 * POST /users/receivers
 */
@HiltViewModel
class RegisterReceiverViewModel
    @Inject
    constructor(
        private val registerReceiverUseCase: RegisterReceiverUseCase
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(RegisterReceiverUiState())
        val uiState: StateFlow<RegisterReceiverUiState> = _uiState.asStateFlow()

        fun registerReceiver(
            name: String,
            relation: String,
            phone: String? = null,
            email: String? = null
        ) {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(isLoading = true, errorMessage = null, registeredReceiverId = null)
                }
                registerReceiverUseCase(
                    name = name,
                    relation = relation,
                    phone = phone,
                    email = email
                )
                    .onSuccess { receiverId ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null,
                                registeredReceiverId = receiverId
                            )
                        }
                    }
                    .onFailure { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = e.message ?: "수신자 등록에 실패했습니다.",
                                registeredReceiverId = null
                            )
                        }
                    }
            }
        }

        fun clearRegisteredReceiverId() {
            _uiState.update { it.copy(registeredReceiverId = null) }
        }

        fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }
    }
