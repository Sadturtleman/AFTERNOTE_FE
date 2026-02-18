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
import com.kuit.afternote.feature.afternote.domain.usecase.UploadMemorialPhotoUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.UploadMemorialThumbnailUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.UploadMemorialVideoUseCase
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.ProcessingMethodItem
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.Song
import com.kuit.afternote.feature.user.domain.model.ReceiverListItem
import com.kuit.afternote.feature.user.domain.usecase.GetReceiversUseCase
import com.kuit.afternote.feature.user.domain.usecase.GetUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import javax.inject.Inject

private const val TAG = "AfternoteEditVM"
private const val CATEGORY_SOCIAL = "소셜네트워크"
private const val CATEGORY_GALLERY = "갤러리 및 파일"
private const val CATEGORY_MEMORIAL = "추모 가이드라인"

/** S3 presigned URLs contain this; we must not send them back on PATCH or the server overwrites the stored key. */
private const val PRESIGNED_URL_MARKER = "X-Amz-"

/** Content URI scheme; used to detect local picks that must be uploaded before save. */
private const val CONTENT_SCHEME = "content://"

/**
 * 애프터노트 생성/수정 ViewModel.
 *
 * 카테고리에 따라 적절한 create UseCase를 호출하거나,
 * 기존 항목 수정 시 update UseCase를 호출합니다.
 */
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
        private val uploadMemorialVideoUseCase: UploadMemorialVideoUseCase,
        private val uploadMemorialPhotoUseCase: UploadMemorialPhotoUseCase
    ) : ViewModel() {

        private val _saveState = MutableStateFlow(AfternoteSaveState())
        val saveState: StateFlow<AfternoteSaveState> = _saveState.asStateFlow()

        /** Set when memorial thumbnail upload (presigned URL) succeeds; consumed by screen to set state. */
        private val _uploadedThumbnailUrl = MutableStateFlow<String?>(null)
        val uploadedThumbnailUrl: StateFlow<String?> = _uploadedThumbnailUrl.asStateFlow()

        /** Cached receiver list (GET /users/receivers) for lookup when returning from receiver selection. */
        private var cachedReceivers: List<ReceiverListItem> = emptyList()

        /**
         * Loads receivers (GET /users/receivers) and caches for [getReceiverById] lookup.
         * Call when entering edit screen so selection-from-navigation can resolve names.
         */
        fun loadReceivers() {
            viewModelScope.launch {
                val userId = getUserIdUseCase() ?: return@launch
                getReceiversUseCase(userId = userId)
                    .getOrNull()
                    ?.let { cachedReceivers = it }
            }
        }

        /**
         * Returns the receiver for the given id from the last [loadReceivers] result, or null.
         */
        fun getReceiverById(id: Long): ReceiverListItem? =
            cachedReceivers.find { it.receiverId == id }

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
         * 수신자: [selectedReceiverIds]에 최소 1명 이상 필요하며, 비어 있으면 검증 오류로 저장하지 않습니다.
         *
         * @param editingId null이면 신규 생성, non-null이면 수정
         * @param category 선택된 카테고리 한국어 문자열
         * @param payload 편집 화면에서 수집된 데이터
         * @param selectedReceiverIds 수신자 지정에서 선택한 수신자 ID 목록 (최소 1명 필요)
         * @param playlistStateHolder 추모 가이드라인의 플레이리스트 상태
         * @param memorialMedia 추모 가이드라인 전용: 영상/썸네일/영정 URL 및 선택한 영정 URI
         */
        fun saveAfternote(
            editingId: Long?,
            category: String,
            payload: RegisterAfternotePayload,
            selectedReceiverIds: List<Long>,
            playlistStateHolder: MemorialPlaylistStateHolder?,
            memorialMedia: SaveAfternoteMemorialMedia
        ) {
            if (_saveState.value.isSaving) {
                Log.w(TAG, "saveAfternote: already saving, ignoring duplicate call")
                return
            }

            val validationError = validateRequiredFieldsSync(
                category = category,
                payload = payload,
                selectedReceiverIds = selectedReceiverIds,
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
                    "hasPlaylist=${playlistStateHolder != null}"
            )

            viewModelScope.launch {
                _saveState.update {
                    it.copy(isSaving = true, error = null, validationError = null)
                }
                val resolvedVideoUrl = resolveVideoUrlForSave(memorialMedia.funeralVideoUrl)
                if (resolvedVideoUrl == null && memorialMedia.funeralVideoUrl != null &&
                    memorialMedia.funeralVideoUrl.startsWith(CONTENT_SCHEME)
                ) {
                    return@launch
                }
                val resolvedMemorialPhotoUrl = resolveMemorialPhotoUrlForSave(
                    memorialPhotoUrl = memorialMedia.memorialPhotoUrl,
                    pickedMemorialPhotoUri = memorialMedia.pickedMemorialPhotoUri
                )
                if (resolvedMemorialPhotoUrl == null && memorialMedia.pickedMemorialPhotoUri != null &&
                    memorialMedia.pickedMemorialPhotoUri.startsWith(CONTENT_SCHEME)
                ) {
                    return@launch
                }
                val videoUrlForUpdate = videoUrlForUpdateRequest(editingId != null, resolvedVideoUrl)
                val thumbnailForUpdate =
                    if (videoUrlForUpdate == null) null else memorialMedia.funeralThumbnailUrl
                val updateMedia = MemorialMediaUrls(
                    funeralVideoUrl = videoUrlForUpdate,
                    funeralThumbnailUrl = thumbnailForUpdate,
                    memorialPhotoUrl = resolvedMemorialPhotoUrl
                )
                val createMedia = MemorialMediaUrls(
                    funeralVideoUrl = resolvedVideoUrl,
                    funeralThumbnailUrl = memorialMedia.funeralThumbnailUrl,
                    memorialPhotoUrl = resolvedMemorialPhotoUrl
                )
                (if (editingId != null) {
                    performUpdate(
                        afternoteId = editingId,
                        category = categoryForApi,
                        payload = payload,
                        selectedReceiverIds = selectedReceiverIds,
                        playlistStateHolder = playlistStateHolder,
                        memorialMedia = updateMedia
                    )
                } else {
                    performCreate(
                        category = categoryForApi,
                        payload = payload,
                        selectedReceiverIds = selectedReceiverIds,
                        playlistStateHolder = playlistStateHolder,
                        funeralVideoUrl = createMedia.funeralVideoUrl,
                        funeralThumbnailUrl = createMedia.funeralThumbnailUrl,
                        memorialPhotoUrl = createMedia.memorialPhotoUrl
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
                !funeralVideoUrl.startsWith(CONTENT_SCHEME) -> return funeralVideoUrl
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
         * Resolves memorial photo URL for save: returns [memorialPhotoUrl] if non-blank (e.g. from API on edit);
         * uploads and returns file URL if [pickedMemorialPhotoUri] is content://; on upload failure updates
         * [saveState] and returns null.
         */
        private suspend fun resolveMemorialPhotoUrlForSave(
            memorialPhotoUrl: String?,
            pickedMemorialPhotoUri: String?
        ): String? {
            if (!pickedMemorialPhotoUri.isNullOrBlank() && pickedMemorialPhotoUri.startsWith(CONTENT_SCHEME)) {
                return uploadMemorialPhotoUseCase(pickedMemorialPhotoUri).fold(
                    onSuccess = { it },
                    onFailure = { e ->
                        Log.e(TAG, "saveAfternote: memorial photo upload failed", e)
                        _saveState.update {
                            it.copy(
                                isSaving = false,
                                error = e.message ?: "영정 사진 업로드에 실패했습니다."
                            )
                        }
                        null
                    }
                )
            }
            return memorialPhotoUrl?.takeIf { it.isNotBlank() }
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
            val validationError = when {
                e is AfternoteValidationException -> e.validationError
                e is HttpException && e.code() == 400 -> parseReceiversRequiredFromBody(e)
                else -> null
            }
            val errorMessage = when {
                validationError != null -> null
                e is AfternoteValidationException -> null
                else -> e.message ?: "저장에 실패했습니다."
            }
            _saveState.update {
                it.copy(
                    isSaving = false,
                    validationError = validationError,
                    error = errorMessage
                )
            }
        }

        /** API 400 + code 475 (수신자 최소 1명) 시 RECEIVERS_REQUIRED로 통일. */
        private fun parseReceiversRequiredFromBody(e: HttpException): AfternoteValidationError? {
            val body = e.response()?.errorBody()?.string() ?: return null
            return runCatching {
                val parsed = Json.decodeFromString<ApiErrorBody>(body)
                if (parsed.code == 475) AfternoteValidationError.RECEIVERS_REQUIRED else null
            }.getOrNull()
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
                memorialThumbnailUrl = detail.playlist?.memorialThumbnailUrl,
                memorialPhotoUrl = detail.playlist?.memorialPhotoUrl ?: detail.playlist?.profilePhoto
            )
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
            selectedReceiverIds: List<Long>,
            playlistStateHolder: MemorialPlaylistStateHolder?
        ): AfternoteValidationError? {
            if (selectedReceiverIds.isEmpty()) {
                return AfternoteValidationError.RECEIVERS_REQUIRED
            }
            if (payload.serviceName.trim().isEmpty()) {
                return AfternoteValidationError.TITLE_REQUIRED
            }
            return when (category) {
                CATEGORY_GALLERY -> validateGalleryRequiredFields(payload)
                CATEGORY_MEMORIAL -> validateMemorialRequiredFields(playlistStateHolder)
                else -> validateSocialLikeRequiredFields(payload)
            }
        }

        private fun validateGalleryRequiredFields(payload: RegisterAfternotePayload): AfternoteValidationError? {
            if (payload.galleryProcessingMethods.isEmpty()) {
                return AfternoteValidationError.GALLERY_ACTIONS_REQUIRED
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
            selectedReceiverIds: List<Long>,
            playlistStateHolder: MemorialPlaylistStateHolder?,
            funeralVideoUrl: String? = null,
            funeralThumbnailUrl: String? = null,
            memorialPhotoUrl: String? = null
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
                    val galleryActions =
                        actions.ifEmpty { listOf("정보 전달") }
                    Log.d(
                        TAG,
                        "performCreate GALLERY: receiverIds=$selectedReceiverIds, actions=$galleryActions"
                    )
                    createGalleryUseCase(
                        title = payload.serviceName,
                        processMethod = processMethod,
                        actions = galleryActions,
                        leaveMessage = leaveMessage,
                        receiverIds = selectedReceiverIds
                    )
                }
                CATEGORY_MEMORIAL -> {
                    val playlistDto = buildPlaylistDto(
                        playlistStateHolder = playlistStateHolder,
                        atmosphere = payload.atmosphere,
                        memorialPhotoUrl = memorialPhotoUrl,
                        funeralVideoUrl = funeralVideoUrl,
                        funeralThumbnailUrl = funeralThumbnailUrl
                    )
                    createPlaylistUseCase(
                        title = payload.serviceName,
                        playlist = playlistDto,
                        receiverIds = selectedReceiverIds
                    )
                }
                else -> {
                    createSocialUseCase(
                        title = payload.serviceName,
                        processMethod = processMethod,
                        actions = actions,
                        leaveMessage = leaveMessage,
                        credentialsId = payload.accountId.takeIf { it.isNotEmpty() },
                        credentialsPassword = payload.password.takeIf { it.isNotEmpty() },
                        receiverIds = selectedReceiverIds
                    )
                }
            }
        }

        private suspend fun performUpdate(
            afternoteId: Long,
            category: String,
            payload: RegisterAfternotePayload,
            selectedReceiverIds: List<Long>,
            playlistStateHolder: MemorialPlaylistStateHolder?,
            memorialMedia: MemorialMediaUrls
        ): Result<Long> {
            val body =
                if (category == CATEGORY_MEMORIAL) {
                    buildMemorialUpdateBody(
                        title = payload.serviceName,
                        atmosphere = payload.atmosphere,
                        playlistStateHolder = playlistStateHolder,
                        funeralVideoUrl = memorialMedia.funeralVideoUrl,
                        funeralThumbnailUrl = memorialMedia.funeralThumbnailUrl,
                        memorialPhotoUrl = memorialMedia.memorialPhotoUrl
                    )
                } else {
                    buildNonMemorialUpdateBody(
                        category = category,
                        payload = payload,
                        selectedReceiverIds = selectedReceiverIds
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
            funeralThumbnailUrl: String? = null,
            memorialPhotoUrl: String? = null
        ): AfternoteUpdateRequestDto =
            AfternoteUpdateRequestDto(
                category = "PLAYLIST",
                title = title,
                playlist = buildPlaylistDto(
                    playlistStateHolder = playlistStateHolder,
                    atmosphere = atmosphere,
                    memorialPhotoUrl = memorialPhotoUrl,
                    funeralVideoUrl = funeralVideoUrl,
                    funeralThumbnailUrl = funeralThumbnailUrl
                )
            )

        private fun buildNonMemorialUpdateBody(
            category: String,
            payload: RegisterAfternotePayload,
            selectedReceiverIds: List<Long>
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
                    CATEGORY_GALLERY ->
                        selectedReceiverIds.map { AfternoteReceiverRefDto(receiverId = it) }
                    else -> null
                },
                playlist = null
            )
        }

        /**
         * 클라이언트 enum 이름을 서버 processMethod 코드로 변환.
         * 갤러리는 수신자 지정만 지원하며 TRANSFER만 전송합니다.
         */
        /** 서버 processMethod → 계정 처리 방법 enum 이름 (소셜/비즈니스 편집용). */
        private fun serverProcessMethodToAccountEnum(processMethod: String): String =
            when (processMethod.uppercase()) {
                "MEMORIAL" -> "MEMORIAL_ACCOUNT"
                "DELETE" -> "PERMANENT_DELETE"
                "TRANSFER", "RECEIVER" -> "TRANSFER_TO_RECEIVER"
                else -> processMethod
            }

        /** 서버 processMethod → 정보 처리 방법 enum 이름 (갤러리 편집용). 갤러리는 수신자 지정만 지원. */
        private fun serverProcessMethodToInfoEnum(processMethod: String): String =
            when (processMethod.uppercase()) {
                "TRANSFER", "RECEIVER", "ADDITIONAL" -> "TRANSFER_TO_AFTERNOTE_EDIT_RECEIVER"
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
                else -> fallback
            }
        }

        /**
         * Builds playlist DTO for create/update. memorialVideo is included whenever the user has
         * selected a video. thumbnailUrl is from POST /files/presigned-url upload or API on edit.
         * memorialPhotoUrl: 영정 사진 URL (from API on edit or after upload).
         */
        private fun buildPlaylistDto(
            playlistStateHolder: MemorialPlaylistStateHolder?,
            atmosphere: String = "",
            memorialPhotoUrl: String? = null,
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
                memorialPhotoUrl = memorialPhotoUrl?.takeIf { it.isNotBlank() },
                songs = songs,
                memorialVideo = memorialVideo
            )
        }
    }

/**
 * Memorial-related media URLs and the picked photo URI for save.
 * Groups 4 params to keep [saveAfternote] under the 7-parameter limit (S107).
 */
data class SaveAfternoteMemorialMedia(
    val funeralVideoUrl: String? = null,
    val funeralThumbnailUrl: String? = null,
    val memorialPhotoUrl: String? = null,
    val pickedMemorialPhotoUri: String? = null
)

/**
 * Resolved memorial media URLs for performUpdate/performCreate.
 * Groups 3 params to keep [performUpdate] under the 7-parameter limit (S107).
 */
private data class MemorialMediaUrls(
    val funeralVideoUrl: String? = null,
    val funeralThumbnailUrl: String? = null,
    val memorialPhotoUrl: String? = null
)

/** API 400 응답 body 파싱용 (code 475 등). */
@Serializable
private data class ApiErrorBody(
    val code: Int? = null
)
