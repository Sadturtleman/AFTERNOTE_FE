package com.kuit.afternote.feature.afternote.presentation.screen

/**
 * 애프터노트 저장(생성/수정) 상태.
 */
data class AfternoteSaveState(
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val savedId: Long? = null,
    val error: String? = null
)
