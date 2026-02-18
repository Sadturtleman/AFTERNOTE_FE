package com.kuit.afternote.feature.afternote.presentation.screen

import androidx.annotation.StringRes
import com.kuit.afternote.R

/**
 * Validation failure type for required-field checks before save.
 * UI should show [messageResId] via stringResource().
 */
/**
 * Exception carrying a validation error for async validation (e.g. GALLERY receivers from API).
 */
class AfternoteValidationException(val validationError: AfternoteValidationError) : Exception()

enum class AfternoteValidationError(@StringRes val messageResId: Int) {
    TITLE_REQUIRED(R.string.afternote_validation_title_required),
    SOCIAL_CREDENTIALS_REQUIRED(R.string.afternote_validation_social_credentials_required),
    SOCIAL_PROCESS_METHOD_REQUIRED(R.string.afternote_validation_social_process_method_required),
    SOCIAL_ACTIONS_REQUIRED(R.string.afternote_validation_social_actions_required),
    GALLERY_ACTIONS_REQUIRED(R.string.afternote_validation_gallery_actions_required),
    /** 수신자 최소 1명 필요 (모든 카테고리). API 400/475와 동일 메시지. */
    RECEIVERS_REQUIRED(R.string.afternote_validation_receivers_required),
    GALLERY_RECEIVERS_REQUIRED(R.string.afternote_validation_gallery_receivers_required),
    PLAYLIST_SONGS_REQUIRED(R.string.afternote_validation_playlist_songs_required)
}

/**
 * 애프터노트 저장(생성/수정) 상태.
 */
data class AfternoteSaveState(
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val savedId: Long? = null,
    val error: String? = null,
    val validationError: AfternoteValidationError? = null
)
