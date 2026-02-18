package com.kuit.afternote.feature.receiver.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.core.uimodel.PlaylistSongDisplay
import com.kuit.afternote.feature.receiver.domain.usecase.GetAfternoteDetailByAuthCodeUseCase
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverMemorialPlaylistUiState
import com.kuit.afternote.feature.receiverauth.session.ReceiverAuthSessionHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 수신자 추모 플레이리스트 화면 ViewModel.
 *
 * GET /api/receiver-auth/after-notes/{afternoteId} (X-Auth-Code)로 상세를 조회한 뒤
 * playlist.songs를 [PlaylistSongDisplay]로 변환하여 표시합니다.
 */
@HiltViewModel
class ReceiverMemorialPlaylistViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val receiverAuthSessionHolder: ReceiverAuthSessionHolder,
        private val getAfternoteDetailByAuthCodeUseCase: GetAfternoteDetailByAuthCodeUseCase
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(ReceiverMemorialPlaylistUiState())
    val uiState: StateFlow<ReceiverMemorialPlaylistUiState> = _uiState.asStateFlow()

    init {
        val afternoteId = (savedStateHandle["afternoteId"] as? String)?.toLongOrNull()
        val authCode = receiverAuthSessionHolder.getAuthCode()

        when {
            authCode == null || authCode.isBlank() -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "인증 정보가 없습니다."
                    )
                }
            }
            afternoteId == null -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "애프터노트 정보를 찾을 수 없습니다."
                    )
                }
            }
            else -> loadPlaylist(authCode = authCode, afternoteId = afternoteId)
        }
    }

    private fun loadPlaylist(authCode: String, afternoteId: Long) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            getAfternoteDetailByAuthCodeUseCase(
                authCode = authCode,
                afternoteId = afternoteId
            )
                .onSuccess { detail ->
                    val songs = detail.playlist?.songs?.mapIndexed { index, song ->
                        PlaylistSongDisplay(
                            id = index.toString(),
                            title = song.title,
                            artist = song.artist,
                            albumImageUrl = song.coverUrl
                        )
                    }.orEmpty()
                    _uiState.update {
                        it.copy(
                            songs = songs,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "플레이리스트를 불러오는데 실패했습니다."
                        )
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
