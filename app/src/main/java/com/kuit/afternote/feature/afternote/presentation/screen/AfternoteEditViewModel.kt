package com.kuit.afternote.feature.afternote.presentation.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentialsDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteMemorialVideoDto
import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylistDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRefDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteSongDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteUpdateRequestDto
import com.kuit.afternote.feature.afternote.domain.model.AfternoteDetail
import com.kuit.afternote.feature.afternote.domain.usecase.CreateGalleryAfternoteUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.CreatePlaylistAfternoteUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.CreateSocialAfternoteUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.GetAfternoteDetailUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.UpdateAfternoteUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.UploadMemorialThumbnailUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.UploadMemorialVideoUseCase
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiver
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.ProcessingMethodItem
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.Song
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
private const val CATEGORY_GALLERY = "갤러리 및 파일"
private const val CATEGORY_MEMORIAL = "추모 가이드라인"

/** S3 presigned URLs contain this; we must not send them back on PATCH or the server overwrites the stored key. */
private const val PRESIGNED_URL_MARKER = "X-Amz-"

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
        private val getUserIdUseCase: GetUserIdUseCase,
        private val uploadMemorialThumbnailUseCase: UploadMemorialThumbnailUseCase,
        private val uploadMemorialVideoUseCase: UploadMemorialVideoUseCase
    ) : ViewModel() {

        private val _saveState = MutableStateFlow(AfternoteSaveState())
        val saveState: StateFlow<AfternoteSaveState> = _saveState.asStateFlow()

        /** Set when memorial thumbnail upload (presigned URL) succeeds; consumed by screen to set state. */
        private val _uploadedThumbnailUrl = MutableStateFlow<String?>(null)
        val uploadedThumbnailUrl: StateFlow<String?> = _uploadedThumbnailUrl.asStateFlow()

        /**
         * Category from the server when loading for edit. Used for update requests because the API
         * does not allow changing category; we must send the original category.
         */
        private var loadedCategoryForEdit: String? = null

        /**
         * Uploads memorial thumbnail via POST /files/presigned-url and S3. On success emits URL to
         * [uploadedThumbnailUrl]; screen should apply it to state then call [clearUploadedThumbnailUrl].
         */
        fun uploadMemorialThumbnail(jpegBytes: ByteArray) {
            viewModelScope.launch {
                uploadMemorialThumbnailUseCase(jpegBytes)
                    .onSuccess { url ->
                        Log.d(TAG, "uploadMemorialThumbnail: success, url=$url")
                        _uploadedThumbnailUrl.value = url
                    }
                    .onFailure { e ->
                        Log.e(TAG, "uploadMemorialThumbnail: failed", e)
                    }
            }
        }

        fun clearUploadedThumbnailUrl() {
            _uploadedThumbnailUrl.value = null
        }

        /**
         * 애프터노트 저장 (생성 또는 수정).
         *
         * @param editingId null이면 신규 생성, non-null이면 수정
         * @param category 선택된 카테고리 한국어 문자열
         * @param payload 편집 화면에서 수집된 데이터
         * @param receivers 갤러리 카테고리 시 "추가 수신자에게 정보 전달"일 때만 사용(수정 화면 수신자 추가 목록).
         *                  "수신자에게 정보 전달"일 때는 수신자 목록(GET /users/receivers) ID를 사용함.
         * @param playlistStateHolder 추모 가이드라인의 플레이리스트 상태
         * @param funeralVideoUrl 추모 가이드라인 전용: 장례식에 남길 영상 URL. 있으면 요청에 memorialVideo 포함.
         * @param funeralThumbnailUrl 추모 가이드라인 전용: 썸네일 URL (API 응답 또는 업로드 API 반환 시). 없으면 null.
         */
        fun saveAfternote(
            editingId: Long?,
            category: String,
            payload: RegisterAfternotePayload,
            receivers: List<AfternoteEditReceiver>,
            playlistStateHolder: MemorialPlaylistStateHolder?,
            funeralVideoUrl: String? = null,
            funeralThumbnailUrl: String? = null
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

            // API does not allow editing category; when updating, use the category from the loaded detail.
            val categoryForApi =
                if (editingId != null) (loadedCategoryForEdit ?: category) else category

            Log.d(
                TAG,
                "saveAfternote: editingId=$editingId, category=$categoryForApi, " +
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
                val resolvedVideoUrl = resolveVideoUrlForSave(funeralVideoUrl)
                if (resolvedVideoUrl == null && funeralVideoUrl != null && funeralVideoUrl.startsWith("content://")) {
                    return@launch
                }
                val videoUrlForUpdate = videoUrlForUpdateRequest(editingId != null, resolvedVideoUrl)
                val thumbnailForUpdate =
                    if (videoUrlForUpdate == null) null else funeralThumbnailUrl
                (if (editingId != null) {
                    performUpdate(
                        afternoteId = editingId,
                        category = categoryForApi,
                        payload = payload,
                        receivers = receivers,
                        playlistStateHolder = playlistStateHolder,
                        funeralVideoUrl = videoUrlForUpdate,
                        funeralThumbnailUrl = thumbnailForUpdate
                    )
                } else {
                    performCreate(
                        category = categoryForApi,
                        payload = payload,
                        receivers = receivers,
                        playlistStateHolder = playlistStateHolder,
                        funeralVideoUrl = resolvedVideoUrl,
                        funeralThumbnailUrl = funeralThumbnailUrl
                    )
                })
                    .onSuccess { id ->
                        Log.d(TAG, "saveAfternote: SUCCESS, savedId=$id")
                        _saveState.update {
                            it.copy(isSaving = false, saveSuccess = true, savedId = id)
                        }
                    }
                    .onFailure { e ->
                        handleSaveFailure(e, categoryForApi)
                    }
            }
        }

        /**
         * Resolves [funeralVideoUrl] for save: returns as-is if non-blank and not content://;
         * uploads and returns file URL if content://; on upload failure updates [saveState] and returns null.
         */
        private suspend fun resolveVideoUrlForSave(funeralVideoUrl: String?): String? {
            when {
                funeralVideoUrl.isNullOrBlank() -> return null
                !funeralVideoUrl.startsWith("content://") -> return funeralVideoUrl
            }
            return uploadMemorialVideoUseCase(funeralVideoUrl).fold(
                onSuccess = { it },
                onFailure = { e ->
                    Log.e(TAG, "saveAfternote: video upload failed", e)
                    _saveState.update {
                        it.copy(
                            isSaving = false,
                            error = e.message ?: "영상 업로드에 실패했습니다."
                        )
                    }
                    null
                }
            )
        }

        /**
         * For update (PATCH), do not send presigned URLs so the server does not overwrite the stored key.
         */
        private fun videoUrlForUpdateRequest(isUpdate: Boolean, resolvedVideoUrl: String?): String? {
            if (!isUpdate || resolvedVideoUrl == null) return resolvedVideoUrl
            if (resolvedVideoUrl.contains(PRESIGNED_URL_MARKER)) {
                Log.d(TAG, "saveAfternote: skipping videoUrl in PATCH (presigned URL)")
                return null
            }
            return resolvedVideoUrl
        }

        private fun handleSaveFailure(e: Throwable, categoryForApi: String) {
            Log.e(TAG, "saveAfternote: FAILURE, category=$categoryForApi", e)
            _saveState.update {
                it.copy(
                    isSaving = false,
                    validationError = (e as? AfternoteValidationException)?.validationError,
                    error = if (e is AfternoteValidationException) it.error else (e.message ?: "저장에 실패했습니다.")
                )
            }
        }

        /**
         * 상세 API에서 조회한 데이터를 기반으로 편집 화면 상태를 채웁니다.
         *
         * 리스트 아이템에는 계정 정보/처리 방법/메시지가 없기 때문에,
         * 편집 화면 진입 시 GET /api/afternotes/{id} 결과를 사용해
         * [AfternoteEditState.loadFromExisting]를 호출합니다.
         * 추모 가이드라인(PLAYLIST)인 경우 [playlistStateHolder]에 상세 플레이리스트 곡을 채워
         * 편집 화면과 상세 화면의 곡 수가 일치하도록 합니다.
         */
        fun loadForEdit(
            afternoteId: Long,
            state: AfternoteEditState,
            playlistStateHolder: MemorialPlaylistStateHolder? = null
        ) {
            viewModelScope.launch {
                getDetailUseCase(afternoteId = afternoteId)
                    .onSuccess { detail ->
                        populatePlaylistFromDetail(detail, playlistStateHolder)
                        val params = buildLoadFromExistingParams(detail)
                        loadedCategoryForEdit = params.categoryDisplayString
                        state.loadFromExisting(params)
                    }
            }
        }

        private fun populatePlaylistFromDetail(
            detail: AfternoteDetail,
            playlistStateHolder: MemorialPlaylistStateHolder?
        ) {
            if (detail.category.uppercase() != "PLAYLIST" ||
                detail.playlist == null ||
                playlistStateHolder == null
            ) return
            playlistStateHolder.clearAllSongs()
            detail.playlist.songs.mapIndexed { index, s ->
                Song(
                    id = (s.id ?: index.toLong()).toString(),
                    title = s.title,
                    artist = s.artist,
                    albumCoverUrl = s.coverUrl
                )
            }.forEach { playlistStateHolder.addSong(it) }
        }

        private fun buildLoadFromExistingParams(detail: AfternoteDetail): LoadFromExistingParams {
            val actionItems =
                detail.actions.mapIndexed { index, text ->
                    ProcessingMethodItem(
                        id = (index + 1).toString(),
                        text = text
                    )
                }
            val processMethod = detail.processMethod ?: ""
            val categoryUpper = detail.category.uppercase()
            val isGalleryCategory = categoryUpper == "GALLERY"
            val isSocialCategory = categoryUpper == "SOCIAL"
            val accountProcessingMethodName =
                if (isSocialCategory) serverProcessMethodToAccountEnum(processMethod)
                else ""
            val informationProcessingMethodName =
                if (isGalleryCategory) serverProcessMethodToInfoEnum(processMethod)
                else ""
            return LoadFromExistingParams(
                itemId = detail.id.toString(),
                serviceName = detail.title,
                categoryDisplayString = serverCategoryToEditScreenCategory(detail.category),
                accountId = detail.credentialsId ?: "",
                password = detail.credentialsPassword ?: "",
                message = detail.leaveMessage ?: "",
                accountProcessingMethodName = accountProcessingMethodName,
                informationProcessingMethodName = informationProcessingMethodName,
                processingMethodsList = if (!isGalleryCategory) actionItems else emptyList(),
                galleryProcessingMethodsList = if (isGalleryCategory) actionItems else emptyList(),
                atmosphere = detail.playlist?.atmosphere,
                memorialVideoUrl = detail.playlist?.memorialVideoUrl,
                memorialThumbnailUrl = detail.playlist?.memorialThumbnailUrl
            )
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
         * Validates required fields for SOCIAL category (소셜네트워크).
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
                CATEGORY_SOCIAL -> validateSocialLikeRequiredFields(payload)
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
            playlistStateHolder: MemorialPlaylistStateHolder?,
            funeralVideoUrl: String? = null,
            funeralThumbnailUrl: String? = null
        ): Result<Long> {
            val actions = payload.processingMethods.map { it.text } +
                payload.galleryProcessingMethods.map { it.text }
            val isSocial = category == CATEGORY_SOCIAL
            val processMethod = toServerProcessMethod(
                accountProcessingMethod =
                    if (isSocial) payload.accountProcessingMethod else "",
                informationProcessingMethod =
                    if (!isSocial) payload.informationProcessingMethod else ""
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
                        actions.ifEmpty { listOf("정보 전달") }
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
                    val playlistDto = buildPlaylistDto(
                        playlistStateHolder = playlistStateHolder,
                        atmosphere = payload.atmosphere,
                        funeralVideoUrl = funeralVideoUrl,
                        funeralThumbnailUrl = funeralThumbnailUrl
                    )
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
            playlistStateHolder: MemorialPlaylistStateHolder?,
            funeralVideoUrl: String? = null,
            funeralThumbnailUrl: String? = null
        ): Result<Long> {
            val body =
                if (category == CATEGORY_MEMORIAL) {
                    buildMemorialUpdateBody(
                        title = payload.serviceName,
                        atmosphere = payload.atmosphere,
                        playlistStateHolder = playlistStateHolder,
                        funeralVideoUrl = funeralVideoUrl,
                        funeralThumbnailUrl = funeralThumbnailUrl
                    )
                } else {
                    buildNonMemorialUpdateBody(
                        category = category,
                        payload = payload,
                        receivers = receivers
                    )
                }
            return updateUseCase(afternoteId = afternoteId, body = body)
        }

        /**
         * PLAYLIST category allows only title and playlist to be updated (API spec).
         * Title and category are mandatory for the edit API.
         * [atmosphere] is "남기고 싶은 당부" (playlist.atmosphere).
         */
        private fun buildMemorialUpdateBody(
            title: String,
            atmosphere: String,
            playlistStateHolder: MemorialPlaylistStateHolder?,
            funeralVideoUrl: String? = null,
            funeralThumbnailUrl: String? = null
        ): AfternoteUpdateRequestDto =
            AfternoteUpdateRequestDto(
                category = "PLAYLIST",
                title = title,
                playlist = buildPlaylistDto(
                    playlistStateHolder = playlistStateHolder,
                    atmosphere = atmosphere,
                    funeralVideoUrl = funeralVideoUrl,
                    funeralThumbnailUrl = funeralThumbnailUrl
                )
            )

        private suspend fun buildNonMemorialUpdateBody(
            category: String,
            payload: RegisterAfternotePayload,
            receivers: List<AfternoteEditReceiver>
        ): AfternoteUpdateRequestDto {
            val actions = payload.processingMethods.map { it.text } +
                payload.galleryProcessingMethods.map { it.text }
            val isSocial =
                category == CATEGORY_SOCIAL
            val processMethod = toServerProcessMethod(
                accountProcessingMethod =
                    if (isSocial) payload.accountProcessingMethod else "",
                informationProcessingMethod =
                    if (!isSocial) payload.informationProcessingMethod else ""
            )
            val serverCategory =
                when (category) {
                    CATEGORY_SOCIAL -> "SOCIAL"
                    CATEGORY_GALLERY -> "GALLERY"
                    else -> null
                }
            // Title and category are mandatory for the edit API; fallback for unknown display category.
            return AfternoteUpdateRequestDto(
                category = serverCategory ?: "SOCIAL",
                title = payload.serviceName,
                processMethod = processMethod.ifEmpty { null },
                actions = actions.ifEmpty { null },
                leaveMessage = payload.message.ifEmpty { null },
                credentials = when (category) {
                    CATEGORY_SOCIAL -> {
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
                playlist = null
            )
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

        /** API category (e.g. GALLERY) → edit screen dropdown value so Gallery processMethod loads. */
        private fun serverCategoryToEditScreenCategory(serverCategory: String): String =
            when (serverCategory.uppercase()) {
                "SOCIAL" -> CATEGORY_SOCIAL
                "GALLERY" -> CATEGORY_GALLERY
                "PLAYLIST", "MUSIC" -> CATEGORY_MEMORIAL
                else -> CATEGORY_SOCIAL
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

        /**
         * Builds playlist DTO for create/update. memorialVideo is included whenever the user has
         * selected a video. thumbnailUrl is from POST /files/presigned-url upload or API on edit.
         */
        private fun buildPlaylistDto(
            playlistStateHolder: MemorialPlaylistStateHolder?,
            atmosphere: String = "",
            funeralVideoUrl: String? = null,
            funeralThumbnailUrl: String? = null
        ): AfternotePlaylistDto {
            val songs = playlistStateHolder?.songs?.map { song ->
                AfternoteSongDto(
                    id = song.id.toLongOrNull(),
                    title = song.title,
                    artist = song.artist,
                    coverUrl = song.albumCoverUrl
                )
            } ?: emptyList()
            val memorialVideo =
                if (funeralVideoUrl.isNullOrBlank()) null
                else AfternoteMemorialVideoDto(
                    videoUrl = funeralVideoUrl,
                    thumbnailUrl = funeralThumbnailUrl.takeIf { !it.isNullOrBlank() }
                )
            return AfternotePlaylistDto(
                atmosphere = atmosphere.ifEmpty { null },
                songs = songs,
                memorialVideo = memorialVideo
            )
        }
    }
