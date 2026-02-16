package com.kuit.afternote.feature.afternote.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.afternote.domain.usecase.SearchMusicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SEARCH_DEBOUNCE_MS = 300L

@HiltViewModel
class AddSongViewModel
    @Inject
    constructor(
        private val searchMusicUseCase: SearchMusicUseCase
    ) : ViewModel(), AddSongViewModelContract {

    private val _uiState = MutableStateFlow(AddSongUiState())
    override val uiState: StateFlow<AddSongUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    override fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query, errorMessage = null) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            val trimmed = query.trim()
            if (trimmed.isEmpty()) {
                _uiState.update { it.copy(songs = emptyList(), isLoading = false) }
                return@launch
            }
            _uiState.update { it.copy(isLoading = true) }
            searchMusicUseCase(trimmed)
                .onSuccess { list ->
                    _uiState.update {
                        it.copy(songs = list, isLoading = false, errorMessage = null)
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            songs = emptyList(),
                            isLoading = false,
                            errorMessage = e.message ?: "검색 실패"
                        )
                    }
                }
        }
    }
}
