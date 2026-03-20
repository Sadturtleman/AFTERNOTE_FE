package com.kuit.afternote.feature.afternote.presentation.detail

import com.kuit.afternote.feature.afternote.domain.model.Detail

/**
 * 애프터노트 상세 화면 UI 상태.
 */
data class AfternoteDetailUiState(
    val isLoading: Boolean = false,
    val detail: Detail? = null,
    val error: String? = null,
    val isDeleting: Boolean = false,
    val deleteSuccess: Boolean = false,
    val deleteError: String? = null,
)
