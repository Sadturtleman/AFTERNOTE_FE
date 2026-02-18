package com.kuit.afternote.feature.user.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kuit.afternote.feature.setting.presentation.navgraph.SettingRoute
import com.kuit.afternote.feature.user.domain.usecase.GetReceiverDetailUseCase
import com.kuit.afternote.feature.user.domain.usecase.UpdateReceiverUseCase
import com.kuit.afternote.feature.user.presentation.uimodel.EditReceiverUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신자 수정 화면 ViewModel.
 * GET /users/receivers/{receiverId} 로 상세 조회, PATCH /users/receivers/{receiverId} 로 수정.
 */
@HiltViewModel
class EditReceiverViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getReceiverDetailUseCase: GetReceiverDetailUseCase,
        private val updateReceiverUseCase: UpdateReceiverUseCase
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(EditReceiverUiState())
        val uiState: StateFlow<EditReceiverUiState> = _uiState.asStateFlow()

        private val receiverId: Long? = savedStateHandle.toRoute<SettingRoute.ReceiverEditRoute>()
            .receiverId.toLongOrNull()

        init {
            receiverId?.let { loadReceiverDetail(it) }
        }

        fun loadReceiverDetail(receiverId: Long) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                getReceiverDetailUseCase(receiverId = receiverId)
                    .onSuccess { detail ->
                        _uiState.update {
                            it.copy(
                                receiverId = detail.receiverId,
                                name = detail.name,
                                relation = detail.relation,
                                phone = detail.phone.orEmpty(),
                                email = detail.email.orEmpty(),
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                    .onFailure { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = e.message ?: "수신자 정보를 불러오지 못했습니다."
                            )
                        }
                    }
            }
        }

        fun updateReceiver(
            name: String,
            relation: String,
            phone: String?,
            email: String?
        ) {
            val id = _uiState.value.receiverId
            if (id == 0L) return
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null, updateSuccess = false) }
                updateReceiverUseCase(
                    receiverId = id,
                    name = name,
                    relation = relation,
                    phone = phone?.takeIf { it.isNotBlank() },
                    email = email?.takeIf { it.isNotBlank() }
                )
                    .onSuccess {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null,
                                updateSuccess = true
                            )
                        }
                    }
                    .onFailure { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = e.message ?: "수신자 수정에 실패했습니다.",
                                updateSuccess = false
                            )
                        }
                    }
            }
        }

        fun clearUpdateSuccess() {
            _uiState.update { it.copy(updateSuccess = false) }
        }

        fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }
    }
