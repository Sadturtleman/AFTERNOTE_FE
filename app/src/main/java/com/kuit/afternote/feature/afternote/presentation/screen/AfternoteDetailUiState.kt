package com.kuit.afternote.feature.afternote.presentation.screen

import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetail

/**
 * 애프터노트 상세 화면 UI 상태.
 */
data class AfternoteDetailUiState(
    val isLoading: Boolean = false,
    val detail: AfternoteDetail? = null,
    val error: String? = null,
    val isDeleting: Boolean = false,
    val deleteSuccess: Boolean = false,
    val deleteError: String? = null
)
