package com.kuit.afternote.feature.afternote.presentation.screen

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.kuit.afternote.app.compositionlocal.DataProviderLocals
import com.kuit.afternote.core.ui.component.LastWishOption
import com.kuit.afternote.core.ui.component.list.AlbumCover
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.feature.afternote.presentation.component.edit.dropdown.SelectionDropdownState
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AccountProcessingMethod
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiver
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiverCallbacks
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.InformationProcessingMethod
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.ProcessingMethodCallbacks
import com.kuit.afternote.core.domain.model.AfternoteServiceCatalog
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.ProcessingMethodItem
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.Song

/**
 * 추모 플레이리스트 상태 홀더
 */
@Stable
class MemorialPlaylistStateHolder {
    val songs: SnapshotStateList<Song> = mutableStateListOf()

    var onSongCountChanged: (() -> Unit)? = null

    fun initializeSongs(initialSongs: List<Song>) {
        if (songs.isEmpty()) {
            songs.addAll(initialSongs)
        }
    }

    fun addSong(song: Song) {
        songs.add(song)
        onSongCountChanged?.invoke()
    }

    @Suppress("UNUSED")
    fun removeSong(songId: String) {
        songs.removeAll { it.id == songId }
        onSongCountChanged?.invoke()
    }

    /**
     * 선택된 곡 ID 집합에 해당하는 곡들을 일괄 삭제합니다.
     */
    fun removeSongs(ids: Set<String>) {
        songs.removeAll { it.id in ids }
        onSongCountChanged?.invoke()
    }

    fun clearAllSongs() {
        songs.clear()
        onSongCountChanged?.invoke()
    }
}

private const val TAG = "AfternoteEditState"
private const val CATEGORY_GALLERY_AND_FILE = "갤러리 및 파일"
private const val CUSTOM_ADD_OPTION = "직접 추가하기"
private const val LAST_WISH_DEFAULT_CALM = "차분하고 조용하게 보내주세요."
private const val LAST_WISH_DEFAULT_BRIGHT = "슬퍼 하지 말고 밝고 따뜻하게 보내주세요."

/**
 * 다이얼로그 타입
 */
enum class DialogType {
    ADD_AFTERNOTE_EDIT_RECEIVER,
    CUSTOM_SERVICE
}

/**
 * AfternoteEditScreen의 상태를 관리하는 State Holder
 *
 * Note: State Holder 패턴으로 인해 많은 함수가 필요합니다.
 * [ProcessingMethodManager]로 처리 방법 목록 책임을 분리하여 함수 수를 20 이하로 유지합니다.
 * 추가 확장 시 AfternoteEditReceiverManager, CategoryManager 등 분리 고려.
 */
@Stable
class AfternoteEditState(
    // TextFieldState는 Composable에서 생성하여 전달
    val idState: TextFieldState,
    val passwordState: TextFieldState,
    val messageState: TextFieldState,
    val afternoteEditReceiverNameState: TextFieldState,
    val phoneNumberState: TextFieldState,
    val customServiceNameState: TextFieldState,
    initialAfternoteEditReceivers: List<AfternoteEditReceiver>,
    albumCovers: List<AlbumCover>
) {
    // Navigation
    var selectedBottomNavItem by mutableStateOf(BottomNavItem.AFTERNOTE)
        private set

    // Category & Service
    var selectedCategory by mutableStateOf("소셜네트워크")
        private set
    var selectedService by mutableStateOf(AfternoteServiceCatalog.defaultSocialService)
        private set

    // Processing Methods
    var selectedProcessingMethod by mutableStateOf(AccountProcessingMethod.MEMORIAL_ACCOUNT)
        private set
    var selectedInformationProcessingMethod by mutableStateOf(InformationProcessingMethod.TRANSFER_TO_AFTERNOTE_EDIT_RECEIVER)
        private set

    // AfternoteEditReceivers
    var afternoteEditReceivers by mutableStateOf(initialAfternoteEditReceivers)
        private set

    // Dialog States
    var relationshipSelectedValue by mutableStateOf("친구")
        private set
    var activeDialog by mutableStateOf<DialogType?>(null)
        private set

    // Which item we last loaded (so we don't overwrite when returning from sub-route)
    var loadedItemId: String? by mutableStateOf(null)
        private set

    // Processing Method Lists (delegated to manager to keep function count under threshold)
    private val processingMethodManager = ProcessingMethodManager()
    val processingMethods: List<ProcessingMethodItem>
        get() = processingMethodManager.processingMethods
    val galleryProcessingMethods: List<ProcessingMethodItem>
        get() = processingMethodManager.galleryProcessingMethods

    // Memorial Guideline
    var selectedLastWish by mutableStateOf<String?>(null)
        private set
    var pickedMemorialPhotoUri by mutableStateOf<String?>(null)
        private set
    var funeralVideoUrl by mutableStateOf<String?>(null)
        private set
    /** Memorial video thumbnail URL when available (e.g. from API on edit or future upload). */
    var funeralThumbnailUrl by mutableStateOf<String?>(null)
        private set
    var playlistSongCount by mutableIntStateOf(16)
        private set

    var customLastWishText by mutableStateOf("")
        private set

    // Memorial Playlist State Holder (옵셔널 - 공유 상태)
    var playlistStateHolder: MemorialPlaylistStateHolder? = null
        private set

    /**
     * 플레이리스트 상태 홀더 설정
     */
    fun setPlaylistStateHolder(stateHolder: MemorialPlaylistStateHolder) {
        playlistStateHolder = stateHolder
        // 상태 홀더가 설정되면 실제 노래 개수로 업데이트
        updatePlaylistSongCount()
    }

    /**
     * 플레이리스트 노래 개수 업데이트
     */
    fun updatePlaylistSongCount() {
        playlistSongCount = playlistStateHolder?.songs?.size ?: 16
    }

    // Dropdown States
    var categoryDropdownState by mutableStateOf(
        SelectionDropdownState()
    )
        private set
    var serviceDropdownState by mutableStateOf(
        SelectionDropdownState()
    )
        private set

    @Suppress("UNUSED")
    var relationshipDropdownState by mutableStateOf(
        SelectionDropdownState()
    )
        private set

    // Constants (service names from single source: AfternoteServiceCatalog)
    val categories = listOf("소셜네트워크", CATEGORY_GALLERY_AND_FILE, "추모 가이드라인")
    val services: List<String>
        get() = AfternoteServiceCatalog.socialServices
    val galleryServices: List<String>
        get() = AfternoteServiceCatalog.galleryServices
    val relationshipOptions = listOf("친구", "가족", "연인")
    val lastWishOptions = listOf(
        LastWishOption(
            text = "차분하고 조용하게 보내주세요.",
            value = "calm"
        ),
        LastWishOption(
            text = "슬퍼 하지 말고 밝고 따뜻하게 보내주세요.",
            value = "bright"
        ),
        LastWishOption(
            text = "기타(직접 입력)",
            value = "other"
        )
    )
    val playlistAlbumCovers = albumCovers

    // Computed Properties (Line 295 해결: 삼항 연산자 제거)
    val currentServiceOptions: List<String>
        get() = if (selectedCategory == CATEGORY_GALLERY_AND_FILE) {
            galleryServices
        } else {
            services + CUSTOM_ADD_OPTION
        }

    /**
     * 선택된 서비스가 "직접 추가하기"인지 확인
     */
    fun isCustomAddOption(service: String): Boolean = service == CUSTOM_ADD_OPTION

    // Callbacks (Composable 내부 람다 제거로 인지 복잡도 최소화)
    val galleryAfternoteEditReceiverCallbacks: AfternoteEditReceiverCallbacks by lazy {
        AfternoteEditReceiverCallbacks(
            onAddClick = ::showAddAfternoteEditReceiverDialog,
            onItemDeleteClick = ::onAfternoteEditReceiverDelete,
            onItemAdded = ::onAfternoteEditReceiverItemAdded,
            onTextFieldVisibilityChanged = { _ ->
                // 텍스트 필드 표시 상태 변경 처리
            }
        )
    }

    val galleryProcessingCallbacks: ProcessingMethodCallbacks by lazy {
        ProcessingMethodCallbacks(
            onItemDeleteClick = processingMethodManager::deleteGalleryProcessingMethod,
            onItemAdded = processingMethodManager::addGalleryProcessingMethod,
            onTextFieldVisibilityChanged = { _ ->
                // 텍스트 필드 표시 상태 변경 처리
            },
            onItemEdited = processingMethodManager::editGalleryProcessingMethod
        )
    }

    val socialProcessingCallbacks: ProcessingMethodCallbacks by lazy {
        ProcessingMethodCallbacks(
            onItemDeleteClick = processingMethodManager::deleteProcessingMethod,
            onItemAdded = processingMethodManager::addProcessingMethod,
            onTextFieldVisibilityChanged = { _ ->
                // 텍스트 필드 표시 상태 변경 처리
            },
            onItemEdited = processingMethodManager::editProcessingMethod
        )
    }

    // Actions (Line 279 해결: 람다 내부 중첩 조건문 제거)
    fun onCategorySelected(category: String) {
        selectedCategory = category
        // 카테고리 변경 시 서비스를 해당 카테고리의 기본값으로 초기화
        selectedService = if (category == CATEGORY_GALLERY_AND_FILE) {
            AfternoteServiceCatalog.defaultGalleryService
        } else {
            AfternoteServiceCatalog.defaultSocialService
        }
    }

    fun onServiceSelected(service: String) {
        if (isCustomAddOption(service)) {
            showCustomServiceDialog()
        } else {
            selectedService = service
        }
    }

    fun onProcessingMethodSelected(method: AccountProcessingMethod) {
        selectedProcessingMethod = method
    }

    fun onInformationProcessingMethodSelected(method: InformationProcessingMethod) {
        selectedInformationProcessingMethod = method
    }

    fun onLastWishSelected(wish: String?) {
        selectedLastWish = wish
    }

    fun onCustomLastWishChanged(text: String) {
        customLastWishText = text
    }

    /**
     * Resolves the "남기고 싶은 당부" value to send as playlist.atmosphere (Memorial only).
     */
    fun getAtmosphereForSave(): String =
        when (selectedLastWish) {
            "calm" -> LAST_WISH_DEFAULT_CALM
            "bright" -> LAST_WISH_DEFAULT_BRIGHT
            "other" -> customLastWishText.trim()
            else -> ""
        }

    /**
     * 영정사진 선택 시 호출 (갤러리 등에서 선택한 URI 저장).
     */
    fun onMemorialPhotoSelected(uri: Uri?) {
        pickedMemorialPhotoUri = uri?.toString()
    }

    /**
     * 추모 영상 선택 시 호출 (갤러리 등에서 선택한 URI 저장).
     * 새 영상 선택 시 썸네일 URL은 초기화 (FuneralVideoUpload가 생성 후 콜백으로 설정).
     */
    fun onFuneralVideoSelected(uri: Uri?) {
        funeralVideoUrl = uri?.toString()
        funeralThumbnailUrl = null
    }

    /**
     * 장례식 영상 썸네일이 준비되면 호출 (앱이 생성한 프레임을 data URL로 인코딩한 값).
     */
    fun onFuneralThumbnailDataUrlReady(dataUrl: String?) {
        funeralThumbnailUrl = dataUrl
    }

    fun showAddAfternoteEditReceiverDialog() {
        activeDialog = DialogType.ADD_AFTERNOTE_EDIT_RECEIVER
    }

    fun showCustomServiceDialog() {
        activeDialog = DialogType.CUSTOM_SERVICE
    }

    fun dismissDialog() {
        activeDialog = null
        afternoteEditReceiverNameState.edit { replace(0, length, "") }
        phoneNumberState.edit { replace(0, length, "") }
        customServiceNameState.edit { replace(0, length, "") }
        relationshipSelectedValue = "친구"
    }

    fun onAddCustomService() {
        val serviceName = customServiceNameState.text.toString().trim()
        if (serviceName.isEmpty()) return

        selectedService = serviceName
        dismissDialog()
    }

    // Line 350 해결: Guard Clause로 중첩 줄이기
    fun onAddAfternoteEditReceiver() {
        val name = afternoteEditReceiverNameState.text.toString().trim()
        if (name.isEmpty()) return

        val newAfternoteEditReceiver = AfternoteEditReceiver(
            id = (afternoteEditReceivers.size + 1).toString(),
            name = name,
            label = relationshipSelectedValue
        )
        afternoteEditReceivers = afternoteEditReceivers + newAfternoteEditReceiver
        dismissDialog()
    }

    fun onRelationshipSelected(relationship: String) {
        relationshipSelectedValue = relationship
    }

    fun onAfternoteEditReceiverDelete(afternoteEditReceiverId: String) {
        afternoteEditReceivers = afternoteEditReceivers.filter { it.id != afternoteEditReceiverId }
    }

    fun onAfternoteEditReceiverItemAdded(text: String) {
        val newAfternoteEditReceiver = AfternoteEditReceiver(
            id = (afternoteEditReceivers.size + 1).toString(),
            name = text,
            label = "친구"
        )
        afternoteEditReceivers = afternoteEditReceivers + newAfternoteEditReceiver
    }

    fun onBottomNavItemSelected(item: BottomNavItem) {
        selectedBottomNavItem = item
    }

    /**
     * Pre-fill state from an existing afternote item (when editing).
     * Sets category, service, text fields, and processing method lists.
     * `params.itemId` is stored so we do not overwrite the form when returning from a sub-route.
     */
    fun loadFromExisting(params: LoadFromExistingParams) {
        Log.d(
            TAG,
            "loadFromExisting: itemId=${params.itemId}, serviceName=${params.serviceName}, " +
                "category=${params.categoryDisplayString}, " +
                "accountPM=${params.accountProcessingMethodName}, infoPM=${params.informationProcessingMethodName}, " +
                "processingMethods=${params.processingMethodsList.size}, " +
                "galleryProcessingMethods=${params.galleryProcessingMethodsList.size}"
        )
        loadedItemId = params.itemId
        selectedService = params.serviceName
        selectedCategory = params.categoryDisplayString

        idState.edit { replace(0, length, params.accountId) }
        passwordState.edit { replace(0, length, params.password) }
        messageState.edit { replace(0, length, params.message) }

        if (params.accountProcessingMethodName.isNotEmpty()) {
            selectedProcessingMethod = runCatching {
                AccountProcessingMethod.valueOf(params.accountProcessingMethodName)
            }.getOrDefault(AccountProcessingMethod.MEMORIAL_ACCOUNT)
        }

        if (params.informationProcessingMethodName.isNotEmpty()) {
            val infoMethodName =
                when (params.informationProcessingMethodName) {
                    "TRANSFER_TO_ADDITIONAL_AFTERNOTE_EDIT_RECEIVER",
                    "ADDITIONAL" -> "TRANSFER_TO_AFTERNOTE_EDIT_RECEIVER"
                    else -> params.informationProcessingMethodName
                }
            selectedInformationProcessingMethod = runCatching {
                InformationProcessingMethod.valueOf(infoMethodName)
            }.getOrDefault(InformationProcessingMethod.TRANSFER_TO_AFTERNOTE_EDIT_RECEIVER)
        }

        processingMethodManager.replaceProcessingMethods(params.processingMethodsList)
        processingMethodManager.replaceGalleryProcessingMethods(params.galleryProcessingMethodsList)

        // Memorial only: when atmosphere does not match a default option, select "기타(직접 입력)" and show saved text.
        params.atmosphere?.let { atmosphereValue ->
            val trimmed = atmosphereValue.trim()
            when {
                trimmed.isEmpty() -> {
                    selectedLastWish = null
                    customLastWishText = ""
                }
                trimmed == LAST_WISH_DEFAULT_CALM -> {
                    selectedLastWish = "calm"
                    customLastWishText = ""
                }
                trimmed == LAST_WISH_DEFAULT_BRIGHT -> {
                    selectedLastWish = "bright"
                    customLastWishText = ""
                }
                else -> {
                    selectedLastWish = "other"
                    customLastWishText = trimmed
                }
            }
        }
        // Memorial only: 장례식에 남길 영상 URL and thumbnail from API.
        funeralVideoUrl = params.memorialVideoUrl
        funeralThumbnailUrl = params.memorialThumbnailUrl
    }
}

/**
 * Parameters for pre-filling edit state from an existing afternote item.
 *
 * @param categoryDisplayString Edit-screen category dropdown value (e.g. "갤러리 및 파일").
 *        Must come from API [detail.category], not inferred from title, so Gallery processMethod loads.
 * @param atmosphere Memorial(PLAYLIST) only: playlist.atmosphere for "남기고 싶은 당부". When non-null and
 *        not matching a default option, edit screen selects "기타(직접 입력)" and shows this text.
 * @param memorialVideoUrl Memorial(PLAYLIST) only: playlist memorial video URL from API (장례식에 남길 영상).
 * @param memorialThumbnailUrl Memorial(PLAYLIST) only: playlist memorial thumbnail URL from API.
 */
data class LoadFromExistingParams(
    val itemId: String,
    val serviceName: String,
    val categoryDisplayString: String,
    val accountId: String,
    val password: String,
    val message: String,
    val accountProcessingMethodName: String,
    val informationProcessingMethodName: String,
    val processingMethodsList: List<ProcessingMethodItem>,
    val galleryProcessingMethodsList: List<ProcessingMethodItem>,
    val atmosphere: String? = null,
    val memorialVideoUrl: String? = null,
    val memorialThumbnailUrl: String? = null
)

@Composable
fun rememberAfternoteEditState(): AfternoteEditState {
    val afternoteProvider = DataProviderLocals.LocalAfternoteEditDataProvider.current
    val idState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()
    val messageState = rememberTextFieldState()
    val afternoteEditReceiverNameState = rememberTextFieldState()
    val phoneNumberState = rememberTextFieldState()
    val customServiceNameState = rememberTextFieldState()

    return remember(afternoteProvider) {
        AfternoteEditState(
            idState = idState,
            passwordState = passwordState,
            messageState = messageState,
            afternoteEditReceiverNameState = afternoteEditReceiverNameState,
            phoneNumberState = phoneNumberState,
            customServiceNameState = customServiceNameState,
            initialAfternoteEditReceivers = afternoteProvider.getAfternoteEditReceivers(),
            albumCovers = afternoteProvider.getAlbumCovers()
        )
    }
}
