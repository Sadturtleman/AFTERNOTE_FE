package com.kuit.afternote.feature.user.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.user.domain.usecase.GetMyProfileUseCase
import com.kuit.afternote.feature.user.domain.usecase.GetUserIdUseCase
import com.kuit.afternote.feature.user.domain.usecase.UpdateMyProfileUseCase
import com.kuit.afternote.feature.user.presentation.uimodel.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 프로필 화면 ViewModel.
 */
@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        private val getMyProfileUseCase: GetMyProfileUseCase,
        private val updateMyProfileUseCase: UpdateMyProfileUseCase,
        private val getUserIdUseCase: GetUserIdUseCase
    ) : ViewModel(), ProfileEditViewModelContract {
        private val _uiState = MutableStateFlow(ProfileUiState())
        override val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

        /**
         * 프로필 조회.
         * JWT 토큰에서 userId를 자동으로 추출합니다.
         */
        override fun loadProfile() {
            viewModelScope.launch {
                val userId = getUserIdUseCase()
                Log.d(TAG, "loadProfile: userId=$userId")
                if (userId == null) {
                    Log.w(TAG, "loadProfile: no userId, skipping API call")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "사용자 정보를 가져올 수 없습니다. 다시 로그인해주세요."
                        )
                    }
                    return@launch
                }

                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                getMyProfileUseCase(userId = userId)
                    .onSuccess { profile ->
                        Log.d(TAG, "loadProfile: success name='${profile.name}' email='${profile.email}'")
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                name = profile.name,
                                email = profile.email,
                                phone = profile.phone,
                                savedProfileImageUrl = profile.profileImageUrl,
                                errorMessage = null
                            )
                        }
                        Log.d(TAG, "loadProfile: uiState updated with name='${profile.name}' email='${profile.email}'")
                    }.onFailure { e ->
                        Log.e(TAG, "loadProfile: failed", e)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = e.message ?: "프로필 조회에 실패했습니다."
                            )
                        }
                    }
            }
        }

        /**
         * 프로필 수정.
         * JWT 토큰에서 userId를 자동으로 추출합니다.
         */
        override fun updateProfile(
            name: String?,
            phone: String?,
            profileImageUrl: String?
        ) {
            viewModelScope.launch {
                val userId = getUserIdUseCase()
                if (userId == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "사용자 정보를 가져올 수 없습니다. 다시 로그인해주세요.",
                            updateSuccess = false
                        )
                    }
                    return@launch
                }

                _uiState.update { it.copy(isLoading = true, errorMessage = null, updateSuccess = false) }
                updateMyProfileUseCase(
                    userId = userId,
                    name = name,
                    phone = phone,
                    profileImageUrl = profileImageUrl  // API param name unchanged
                ).onSuccess { profile ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            name = profile.name,
                            email = profile.email,
                            phone = profile.phone,
                            savedProfileImageUrl = profile.profileImageUrl,
                            errorMessage = null,
                            updateSuccess = true
                        )
                    }
                }.onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "프로필 수정에 실패했습니다.",
                            updateSuccess = false
                        )
                    }
                }
            }
        }

        /**
         * 프로필 이미지 선택 시 호출 (갤러리 등에서 선택한 URI 저장).
         */
        override fun setSelectedProfileImageUri(uri: android.net.Uri?) {
            _uiState.update { it.copy(pickedProfileImageUri = uri?.toString()) }
        }

        /**
         * updateSuccess 소비 후 호출 (네비게이션 후).
         */
        override fun clearUpdateSuccess() {
            _uiState.update { it.copy(updateSuccess = false) }
        }

        /**
         * 에러 메시지 초기화.
         */
        fun clearError() {
            _uiState.update { it.copy(errorMessage = null) }
        }

        companion object {
            private const val TAG = "ProfileViewModel"
        }
    }
