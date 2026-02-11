package com.kuit.afternote.feature.afternote.presentation.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentialsDto
import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylistDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRefDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteSongDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteUpdateRequestDto
import com.kuit.afternote.feature.afternote.domain.usecase.CreateGalleryAfternoteUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.CreatePlaylistAfternoteUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.CreateSocialAfternoteUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.GetAfternoteDetailUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.UpdateAfternoteUseCase
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiver
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.ProcessingMethodItem
import com.kuit.afternote.feature.user.domain.usecase.GetReceiversUseCase
import com.kuit.afternote.feature.user.domain.usecase.GetUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AfternoteEditVM"
private const val CATEGORY_SOCIAL = "소셜네트워크"
private const val CATEGORY_BUSINESS = "비즈니스"
private const val CATEGORY_GALLERY = "갤러리 및 파일"
private const val CATEGORY_MEMORIAL = "추모 가이드라인"

/**
 * 애프터노트 생성/수정 ViewModel.
 *
 * 카테고리에 따라 적절한 create UseCase를 호출하거나,
 * 기존 항목 수정 시 update UseCase를 호출합니다.
 */
private const val INFO_METHOD_TRANSFER = "TRANSFER_TO_AFTERNOTE_EDIT_RECEIVER"
private const val INFO_METHOD_ADDITIONAL = "TRANSFER_TO_ADDITIONAL_AFTERNOTE_EDIT_RECEIVER"

@HiltViewModel
class AfternoteEditViewModel
    @Inject
    constructor(
        private val createSocialUseCase: CreateSocialAfternoteUseCase,
        private val createGalleryUseCase: CreateGalleryAfternoteUseCase,
        private val createPlaylistUseCase: CreatePlaylistAfternoteUseCase,
        private val updateUseCase: UpdateAfternoteUseCase,
        private val getDetailUseCase: GetAfternoteDetailUseCase,
        private val getReceiversUseCase: GetReceiversUseCase,
        private val getUserIdUseCase: GetUserIdUseCase
    ) : ViewModel() {

        private val _saveState = MutableStateFlow(AfternoteSaveState())
        val saveState: StateFlow<AfternoteSaveState> = _saveState.asStateFlow()

        /**
         * 애프터노트 저장 (생성 또는 수정).
         *
         * @param editingId null이면 신규 생성, non-null이면 수정
         * @param category 선택된 카테고리 한국어 문자열
         * @param payload 편집 화면에서 수집된 데이터
         * @param receivers 갤러리 카테고리 시 "추가 수신자에게 정보 전달"일 때만 사용(수정 화면 수신자 추가 목록).
         *                  "수신자에게 정보 전달"일 때는 수신자 목록(GET /users/receivers) ID를 사용함.
         * @param playlistStateHolder 추모 가이드라인의 플레이리스트 상태
         */
        fun saveAfternote(
            editingId: Long?,
            category: String,
            payload: RegisterAfternotePayload,
            receivers: List<AfternoteEditReceiver>,
            playlistStateHolder: MemorialPlaylistStateHolder?
        ) {
            if (_saveState.value.isSaving) {
                Log.w(TAG, "saveAfternote: already saving, ignoring duplicate call")
                return
            }

            val validationError = validateRequiredFieldsSync(
                category = category,
                payload = payload,
                receivers = receivers,
                playlistStateHolder = playlistStateHolder
            )
            if (validationError != null) {
                Log.w(TAG, "saveAfternote: validation failed: $validationError")
                _saveState.update { it.copy(validationError = validationError) }
                return
            }

            Log.d(
                TAG,
                "saveAfternote: editingId=$editingId, category=$category, " +
                    "serviceName=${payload.serviceName}, " +
                    "accountProcessingMethod=${payload.accountProcessingMethod}, " +
                    "informationProcessingMethod=${payload.informationProcessingMethod}, " +
                    "processingMethods=${payload.processingMethods}, " +
                    "galleryProcessingMethods=${payload.galleryProcessingMethods}, " +
                    "receivers=${receivers.map { it.id }}, " +
                    "hasPlaylist=${playlistStateHolder != null}"
            )

            viewModelScope.launch {
                _saveState.update {
                    it.copy(isSaving = true, error = null, validationError = null)
                }
                val result = if (editingId != null) {
                    performUpdate(
                        afternoteId = editingId,
                        category = category,
                        payload = payload,
                        receivers = receivers,
                        playlistStateHolder = playlistStateHolder
                    )
                } else {
                    performCreate(
                        category = category,
                        payload = payload,
                        receivers = receivers,
                        playlistStateHolder = playlistStateHolder
                    )
                }
                result
                    .onSuccess { id ->
                        Log.d(TAG, "saveAfternote: SUCCESS, savedId=$id")
                        _saveState.update {
                            it.copy(isSaving = false, saveSuccess = true, savedId = id)
                        }
                    }
                    .onFailure { e ->
                        Log.e(TAG, "saveAfternote: FAILURE, category=$category", e)
                        when (e) {
                            is AfternoteValidationException -> _saveState.update {
                                it.copy(
                                    isSaving = false,
                                    validationError = e.validationError
                                )
                            }
                            else -> _saveState.update {
                                it.copy(
                                    isSaving = false,
                                    error = e.message ?: "저장에 실패했습니다."
                                )
                            }
                        }
                    }
            }
        }

        /**
         * 상세 API에서 조회한 데이터를 기반으로 편집 화면 상태를 채웁니다.
         *
         * 리스트 아이템에는 계정 정보/처리 방법/메시지가 없기 때문에,
         * 편집 화면 진입 시 GET /api/afternotes/{id} 결과를 사용해
         * [AfternoteEditState.loadFromExisting]를 호출합니다.
         */
        fun loadForEdit(
            afternoteId: Long,
            state: AfternoteEditState
        ) {
            viewModelScope.launch {
                getDetailUseCase(afternoteId = afternoteId)
                    .onSuccess { detail ->
                        val processingMethods =
                            detail.actions.mapIndexed { index, text ->
                                ProcessingMethodItem(
                                    id = (index + 1).toString(),
                                    text = text
                                )
                            }

                        val processMethod = detail.processMethod ?: ""
                        val categoryUpper = detail.category.uppercase()
                        val isSocialCategory =
                            categoryUpper == "SOCIAL" ||
                                categoryUpper == "BUSINESS" ||
                                categoryUpper == "OTHER"
                        val accountProcessingMethodName =
                            if (isSocialCategory) serverProcessMethodToAccountEnum(processMethod)
                            else ""
                        val informationProcessingMethodName =
                            if (!isSocialCategory) serverProcessMethodToInfoEnum(processMethod)
                            else ""

                        val params =
                            LoadFromExistingParams(
                                itemId = detail.id.toString(),
                                serviceName = detail.title,
                                accountId = detail.credentialsId ?: "",
                                password = detail.credentialsPassword ?: "",
                                message = detail.leaveMessage ?: "",
                                accountProcessingMethodName = accountProcessingMethodName,
                                informationProcessingMethodName = informationProcessingMethodName,
                                processingMethodsList = processingMethods,
                                galleryProcessingMethodsList = emptyList()
                            )
                        state.loadFromExisting(params)
                    }
            }
        }

        /**
         * 갤러리 카테고리 저장 시 사용할 수신자 ID 목록.
         * "수신자에게 정보 전달"(TRANSFER)이면 수신자 목록(GET /users/receivers)의 ID 사용,
         * "추가 수신자에게 정보 전달"(ADDITIONAL)이면 편집 화면에서 추가한 수신자 ID 사용.
         */
        private suspend fun resolveGalleryReceiverIds(
            informationProcessingMethod: String,
            editReceivers: List<AfternoteEditReceiver>
        ): List<Long> {
            if (informationProcessingMethod == INFO_METHOD_TRANSFER) {
                val userId = getUserIdUseCase() ?: return emptyList()
                return getReceiversUseCase(userId = userId)
                    .getOrNull()
                    ?.map { it.receiverId }
                    ?: emptyList()
            }
            return editReceivers.mapNotNull { it.id.toLongOrNull() }
        }

        /**
         * Validates required fields for SOCIAL-like categories (소셜네트워크, 비즈니스, 재산 처리).
         * Returns the first validation error found, or null if valid.
         */
        private fun validateSocialLikeRequiredFields(
            payload: RegisterAfternotePayload
        ): AfternoteValidationError? {
            if (payload.accountId.isBlank() || payload.password.isBlank()) {
                return AfternoteValidationError.SOCIAL_CREDENTIALS_REQUIRED
            }
            val validProcessMethods = setOf("MEMORIAL_ACCOUNT", "PERMANENT_DELETE", "TRANSFER_TO_RECEIVER")
            if (payload.accountProcessingMethod !in validProcessMethods) {
                return AfternoteValidationError.SOCIAL_PROCESS_METHOD_REQUIRED
            }
            if (payload.processingMethods.isEmpty()) {
                return AfternoteValidationError.SOCIAL_ACTIONS_REQUIRED
            }
            return null
        }

        /**
         * Validates required fields per category before save (create).
         * Returns the first validation error found, or null if valid.
         */
        private fun validateRequiredFieldsSync(
            category: String,
            payload: RegisterAfternotePayload,
            receivers: List<AfternoteEditReceiver>,
            playlistStateHolder: MemorialPlaylistStateHolder?
        ): AfternoteValidationError? {
            if (payload.serviceName.trim().isEmpty()) {
                return AfternoteValidationError.TITLE_REQUIRED
            }
            return when (category) {
                CATEGORY_SOCIAL, CATEGORY_BUSINESS -> validateSocialLikeRequiredFields(payload)
                CATEGORY_GALLERY -> validateGalleryRequiredFields(payload, receivers)
                CATEGORY_MEMORIAL -> validateMemorialRequiredFields(playlistStateHolder)
                else -> validateSocialLikeRequiredFields(payload)
            }
        }

        private fun validateGalleryRequiredFields(
            payload: RegisterAfternotePayload,
            receivers: List<AfternoteEditReceiver>
        ): AfternoteValidationError? {
            if (payload.galleryProcessingMethods.isEmpty()) {
                return AfternoteValidationError.GALLERY_ACTIONS_REQUIRED
            }
            if (payload.informationProcessingMethod == INFO_METHOD_ADDITIONAL &&
                receivers.isEmpty()
            ) {
                return AfternoteValidationError.GALLERY_RECEIVERS_REQUIRED
            }
            return null
        }

        private fun validateMemorialRequiredFields(
            playlistStateHolder: MemorialPlaylistStateHolder?
        ): AfternoteValidationError? {
            if (playlistStateHolder == null || playlistStateHolder.songs.isEmpty()) {
                return AfternoteValidationError.PLAYLIST_SONGS_REQUIRED
            }
            return null
        }

        private suspend fun performCreate(
            category: String,
            payload: RegisterAfternotePayload,
            receivers: List<AfternoteEditReceiver>,
            playlistStateHolder: MemorialPlaylistStateHolder?
        ): Result<Long> {
            val actions = payload.processingMethods.map { it.text } +
                payload.galleryProcessingMethods.map { it.text }
            val isSocialOrBusiness =
                category == CATEGORY_SOCIAL || category == CATEGORY_BUSINESS
            val processMethod = toServerProcessMethod(
                accountProcessingMethod =
                    if (isSocialOrBusiness) payload.accountProcessingMethod else "",
                informationProcessingMethod =
                    if (!isSocialOrBusiness) payload.informationProcessingMethod else ""
            )
            val leaveMessage = payload.message.ifEmpty { null }

            Log.d(
                TAG,
                "performCreate: category=$category, title=${payload.serviceName}, " +
                    "processMethod=$processMethod, actions=$actions, " +
                    "leaveMessage=$leaveMessage"
            )

                return when (category) {
                CATEGORY_GALLERY -> {
                    val receiverIds = resolveGalleryReceiverIds(
                        informationProcessingMethod = payload.informationProcessingMethod,
                        editReceivers = receivers
                    )
                    if (receiverIds.isEmpty()) {
                        return Result.failure(
                            AfternoteValidationException(
                                AfternoteValidationError.GALLERY_RECEIVERS_REQUIRED
                            )
                        )
                    }
                    // API requires non-empty actions for GALLERY; use default when none added
                    val galleryActions =
                        if (actions.isEmpty()) listOf("정보 전달") else actions
                    Log.d(TAG, "performCreate GALLERY: receiverIds=$receiverIds, actions=$galleryActions")
                    createGalleryUseCase(
                        title = payload.serviceName,
                        processMethod = processMethod,
                        actions = galleryActions,
                        leaveMessage = leaveMessage,
                        receiverIds = receiverIds
                    )
                }
                CATEGORY_MEMORIAL -> {
                    val playlistDto = buildPlaylistDto(playlistStateHolder)
                    createPlaylistUseCase(
                        title = payload.serviceName,
                        playlist = playlistDto
                    )
                }
                else -> createSocialUseCase(
                    title = payload.serviceName,
                    processMethod = processMethod,
                    actions = actions,
                    leaveMessage = leaveMessage,
                    credentialsId = payload.accountId.takeIf { it.isNotEmpty() },
                    credentialsPassword = payload.password.takeIf { it.isNotEmpty() }
                )
            }
        }

        private suspend fun performUpdate(
            afternoteId: Long,
            category: String,
            payload: RegisterAfternotePayload,
            receivers: List<AfternoteEditReceiver>,
            playlistStateHolder: MemorialPlaylistStateHolder?
        ): Result<Long> {
            val actions = payload.processingMethods.map { it.text } +
                payload.galleryProcessingMethods.map { it.text }
            val isSocialOrBusiness =
                category == CATEGORY_SOCIAL || category == CATEGORY_BUSINESS
            val processMethod = toServerProcessMethod(
                accountProcessingMethod =
                    if (isSocialOrBusiness) payload.accountProcessingMethod else "",
                informationProcessingMethod =
                    if (!isSocialOrBusiness) payload.informationProcessingMethod else ""
            )

            val serverCategory =
                when (category) {
                    CATEGORY_SOCIAL -> "SOCIAL"
                    CATEGORY_BUSINESS -> "BUSINESS"
                    CATEGORY_GALLERY -> "GALLERY"
                    CATEGORY_MEMORIAL -> "PLAYLIST"
                    else -> null
                }

            val body = AfternoteUpdateRequestDto(
                category = serverCategory,
                title = payload.serviceName,
                processMethod = processMethod.ifEmpty { null },
                actions = actions.ifEmpty { null },
                leaveMessage = payload.message.ifEmpty { null },
                credentials = when (category) {
                    CATEGORY_SOCIAL, CATEGORY_BUSINESS -> {
                        val id = payload.accountId.takeIf { it.isNotEmpty() }
                        val pw = payload.password.takeIf { it.isNotEmpty() }
                        if (id != null || pw != null) AfternoteCredentialsDto(id = id, password = pw)
                        else null
                    }
                    else -> null
                },
                receivers = when (category) {
                    CATEGORY_GALLERY -> {
                        val ids = resolveGalleryReceiverIds(
                            informationProcessingMethod = payload.informationProcessingMethod,
                            editReceivers = receivers
                        )
                        ids.map { AfternoteReceiverRefDto(receiverId = it) }.ifEmpty { null }
                    }
                    else -> null
                },
                playlist = when (category) {
                    CATEGORY_MEMORIAL -> buildPlaylistDto(playlistStateHolder)
                    else -> null
                }
            )
            return updateUseCase(afternoteId = afternoteId, body = body)
        }

        /**
         * 클라이언트 enum 이름을 서버 processMethod 코드로 변환.
         *
         * - "사망 후 추모 계정으로 전환" 옵션 → MEMORIAL
         * - "사망 후 데이터 보관 요청" 옵션 → DELETE
         * - "수신자에게 정보 전달" 옵션 → TRANSFER
         * - "추가 수신자에게 정보 전달" 옵션 → ADDITIONAL
         *
         * 서버에서 내려오는 processMethod 의미에 맞춰 매핑합니다.
         */
        /** 서버 processMethod → 계정 처리 방법 enum 이름 (소셜/비즈니스 편집용). */
        private fun serverProcessMethodToAccountEnum(processMethod: String): String =
            when (processMethod.uppercase()) {
                "MEMORIAL" -> "MEMORIAL_ACCOUNT"
                "DELETE" -> "PERMANENT_DELETE"
                "TRANSFER", "RECEIVER" -> "TRANSFER_TO_RECEIVER"
                else -> processMethod
            }

        /** 서버 processMethod → 정보 처리 방법 enum 이름 (갤러리 편집용). */
        private fun serverProcessMethodToInfoEnum(processMethod: String): String =
            when (processMethod.uppercase()) {
                "TRANSFER", "RECEIVER" -> "TRANSFER_TO_AFTERNOTE_EDIT_RECEIVER"
                "ADDITIONAL" -> "TRANSFER_TO_ADDITIONAL_AFTERNOTE_EDIT_RECEIVER"
                else -> processMethod
            }

        private fun toServerProcessMethod(
            accountProcessingMethod: String,
            informationProcessingMethod: String
        ): String {
            val fromAccount =
                when (accountProcessingMethod) {
                    "MEMORIAL_ACCOUNT" -> "MEMORIAL"
                    "PERMANENT_DELETE" -> "DELETE"
                    "TRANSFER_TO_RECEIVER" -> "TRANSFER"
                    else -> null
                }
            if (fromAccount != null) return fromAccount
            val fallback = accountProcessingMethod.ifEmpty { informationProcessingMethod }
            return when (fallback) {
                "TRANSFER_TO_AFTERNOTE_EDIT_RECEIVER" -> "TRANSFER"
                "TRANSFER_TO_ADDITIONAL_AFTERNOTE_EDIT_RECEIVER" -> "ADDITIONAL"
                else -> fallback
            }
        }

        private fun buildPlaylistDto(
            playlistStateHolder: MemorialPlaylistStateHolder?
        ): AfternotePlaylistDto {
            val songs = playlistStateHolder?.songs?.map { song ->
                AfternoteSongDto(
                    id = song.id.toLongOrNull(),
                    title = song.title,
                    artist = song.artist,
                    coverUrl = song.albumCoverUrl
                )
            } ?: emptyList()
            return AfternotePlaylistDto(songs = songs)
        }
    }
