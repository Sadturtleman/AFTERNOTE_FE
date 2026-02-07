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
 * 현재 18개의 public 함수가 있으며, detekt threshold(20)를 초과하지 않지만
 * 향후 확장 시 Manager 클래스로 책임 분리를 고려해야 합니다.
 * (예: AfternoteEditReceiverManager, CategoryManager, ProcessingMethodManager)
 *
 * TODO: 함수가 20개를 초과하면 Manager 클래스로 분리 고려
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
    var selectedBottomNavItem by mutableStateOf(BottomNavItem.HOME)
        private set

    // Category & Service
    var selectedCategory by mutableStateOf("소셜네트워크")
        private set
    var selectedService by mutableStateOf("인스타그램")
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

    // Processing Method Lists (empty until loaded; use dummies only in Previews)
    var processingMethods by mutableStateOf<List<ProcessingMethodItem>>(emptyList())
        private set
    var galleryProcessingMethods by mutableStateOf<List<ProcessingMethodItem>>(emptyList())
        private set

    // Memorial Guideline
    var selectedLastWish by mutableStateOf<String?>(null)
        private set
    var pickedMemorialPhotoUri by mutableStateOf<String?>(null)
        private set
    var funeralVideoUrl by mutableStateOf<String?>(null)
        private set
    var playlistSongCount by mutableIntStateOf(16)
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

    // Constants
    val categories = listOf("소셜네트워크", "비즈니스", CATEGORY_GALLERY_AND_FILE, "재산 처리", "추모 가이드라인")
    val services = listOf("인스타그램", "페이스북")
    val galleryServices = listOf("갤러리", "파일")
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
            onItemMoreClick = {
                // 드롭다운 메뉴는 ProcessingMethodList 내부에서 처리
            },
            onItemEditClick = { _ ->
                // TODO: 처리 방법 수정 로직
            },
            onItemDeleteClick = ::onGalleryProcessingMethodDelete,
            onItemAdded = ::onGalleryProcessingMethodAdded,
            onTextFieldVisibilityChanged = { _ ->
                // 텍스트 필드 표시 상태 변경 처리
            }
        )
    }

    val socialProcessingCallbacks: ProcessingMethodCallbacks by lazy {
        ProcessingMethodCallbacks(
            onItemMoreClick = {
                // 드롭다운 메뉴는 ProcessingMethodList 내부에서 처리
            },
            onItemEditClick = { _ ->
                // TODO: 처리 방법 수정 로직
            },
            onItemDeleteClick = ::onProcessingMethodDelete,
            onItemAdded = ::onProcessingMethodAdded,
            onTextFieldVisibilityChanged = { _ ->
                // 텍스트 필드 표시 상태 변경 처리
            }
        )
    }

    // Actions (Line 279 해결: 람다 내부 중첩 조건문 제거)
    fun onCategorySelected(category: String) {
        selectedCategory = category
        // 카테고리 변경 시 서비스를 해당 카테고리의 기본값으로 초기화
        selectedService = if (category == CATEGORY_GALLERY_AND_FILE) {
            "갤러리"
        } else {
            "인스타그램"
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

    /**
     * 영정사진 선택 시 호출 (갤러리 등에서 선택한 URI 저장).
     */
    fun onMemorialPhotoSelected(uri: Uri?) {
        pickedMemorialPhotoUri = uri?.toString()
    }

    /**
     * 추모 영상 선택 시 호출 (갤러리 등에서 선택한 URI 저장).
     */
    fun onFuneralVideoSelected(uri: Uri?) {
        funeralVideoUrl = uri?.toString()
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

    fun onGalleryProcessingMethodDelete(itemId: String) {
        galleryProcessingMethods = galleryProcessingMethods.filter { it.id != itemId }
    }

    fun onGalleryProcessingMethodAdded(text: String) {
        val newItem = ProcessingMethodItem(
            id = (galleryProcessingMethods.size + 1).toString(),
            text = text
        )
        galleryProcessingMethods = galleryProcessingMethods + newItem
    }

    fun onProcessingMethodDelete(itemId: String) {
        processingMethods = processingMethods.filter { it.id != itemId }
    }

    fun onProcessingMethodAdded(text: String) {
        val newItem = ProcessingMethodItem(
            id = (processingMethods.size + 1).toString(),
            text = text
        )
        processingMethods = processingMethods + newItem
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
                "category=${AfternoteItemMapper.categoryStringForEditScreen(params.serviceName)}, " +
                "accountId=${params.accountId}, message=${params.message.take(20)}, " +
                "accountPM=${params.accountProcessingMethodName}, infoPM=${params.informationProcessingMethodName}, " +
                "processingMethods=${params.processingMethodsList.size}, " +
                "galleryProcessingMethods=${params.galleryProcessingMethodsList.size}"
        )
        loadedItemId = params.itemId
        selectedService = params.serviceName
        selectedCategory = AfternoteItemMapper.categoryStringForEditScreen(params.serviceName)

        idState.edit { replace(0, length, params.accountId) }
        passwordState.edit { replace(0, length, params.password) }
        messageState.edit { replace(0, length, params.message) }

        if (params.accountProcessingMethodName.isNotEmpty()) {
            selectedProcessingMethod = runCatching {
                AccountProcessingMethod.valueOf(params.accountProcessingMethodName)
            }.getOrDefault(AccountProcessingMethod.MEMORIAL_ACCOUNT)
        }

        if (params.informationProcessingMethodName.isNotEmpty()) {
            selectedInformationProcessingMethod = runCatching {
                InformationProcessingMethod.valueOf(params.informationProcessingMethodName)
            }.getOrDefault(InformationProcessingMethod.TRANSFER_TO_AFTERNOTE_EDIT_RECEIVER)
        }

        processingMethods = params.processingMethodsList
        galleryProcessingMethods = params.galleryProcessingMethodsList
    }
}

/**
 * Parameters for pre-filling edit state from an existing afternote item.
 */
data class LoadFromExistingParams(
    val itemId: String,
    val serviceName: String,
    val accountId: String,
    val password: String,
    val message: String,
    val accountProcessingMethodName: String,
    val informationProcessingMethodName: String,
    val processingMethodsList: List<ProcessingMethodItem>,
    val galleryProcessingMethodsList: List<ProcessingMethodItem>
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
