package com.kuit.afternote.feature.receiver.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverTimeLetterDetailUiState
import com.kuit.afternote.feature.receiverauth.session.ReceiverAuthSessionHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * 수신자 앱 타임레터 상세 화면 ViewModel.
 *
 * receiver-auth 플로우에서는 목록에서 선택한 항목을 [ReceiverAuthSessionHolder]에서 읽어 표시합니다.
 * received API는 사용하지 않습니다.
 */
@HiltViewModel
class ReceiverTimeLetterDetailViewModel
    @Inject
    constructor(
        private val receiverAuthSessionHolder: ReceiverAuthSessionHolder
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(ReceiverTimeLetterDetailUiState())
    val uiState: StateFlow<ReceiverTimeLetterDetailUiState> = _uiState.asStateFlow()

    init {
        receiverAuthSessionHolder.getSelectedTimeLetter()?.let { letter ->
            _uiState.update {
                it.copy(
                    letter = letter,
                    isLoading = false,
                    errorMessage = null
                )
            }
            receiverAuthSessionHolder.setSelectedTimeLetter(null)
        }
    }

    fun updateSelectedBottomNavItem(item: BottomNavItem) {
        _uiState.update { it.copy(selectedBottomNavItem = item) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
