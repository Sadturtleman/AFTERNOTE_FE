package com.kuit.afternote.feature.afternote.presentation.screen

import com.kuit.afternote.core.uimodel.PlaylistSongDisplay
import kotlinx.coroutines.flow.StateFlow

/**
 * Contract for Add Song screen ViewModel (production uses [AddSongViewModel], Preview uses Fake).
 */
interface AddSongViewModelContract {

    val uiState: StateFlow<AddSongUiState>

    fun onSearchQueryChange(query: String)
}

/**
 * UI state for Add Song screen (search-driven list from API).
 */
data class AddSongUiState(
    val songs: List<PlaylistSongDisplay> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
