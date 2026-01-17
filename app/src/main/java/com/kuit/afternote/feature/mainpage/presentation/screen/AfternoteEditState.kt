package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kuit.afternote.core.ui.component.BottomNavItem
import com.kuit.afternote.feature.mainpage.presentation.component.edit.AlbumCover
import com.kuit.afternote.feature.mainpage.presentation.component.edit.LastWishOption
import com.kuit.afternote.feature.mainpage.presentation.component.edit.SelectionDropdownState
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.AccountProcessingMethod
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.InformationProcessingMethod
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.ProcessingMethodCallbacks
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.ProcessingMethodItem
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Recipient
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.RecipientCallbacks

private const val CATEGORY_GALLERY_AND_FILE = "갤러리 및 파일"
private const val CUSTOM_ADD_OPTION = "직접 추가하기"

/**
 * 다이얼로그 타입
 */
enum class DialogType {
    ADD_RECIPIENT,
    CUSTOM_SERVICE
}

/**
 * AfternoteEditScreen의 상태를 관리하는 State Holder
 *
 * Note: State Holder 패턴으로 인해 많은 함수가 필요합니다.
 * 현재 18개의 public 함수가 있으며, detekt threshold(20)를 초과하지 않지만
 * 향후 확장 시 Manager 클래스로 책임 분리를 고려해야 합니다.
 * (예: RecipientManager, CategoryManager, ProcessingMethodManager)
 *
 * TODO: 함수가 20개를 초과하면 Manager 클래스로 분리 고려
 */
@Stable
class AfternoteEditState(
    // TextFieldState는 Composable에서 생성하여 전달
    val idState: TextFieldState,
    val passwordState: TextFieldState,
    val messageState: TextFieldState,
    val recipientNameState: TextFieldState,
    val phoneNumberState: TextFieldState,
    val customServiceNameState: TextFieldState
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
    var selectedInformationProcessingMethod by mutableStateOf(InformationProcessingMethod.TRANSFER_TO_RECIPIENT)
        private set

    // Recipients
    private val initialRecipients = listOf(
        Recipient(id = "1", name = "김지은", label = "친구"),
        Recipient(id = "2", name = "박선호", label = "가족")
    )
    var recipients by mutableStateOf(initialRecipients)
        private set

    // Dialog States
    var relationshipSelectedValue by mutableStateOf("친구")
        private set
    var activeDialog by mutableStateOf<DialogType?>(null)
        private set

    // Processing Method Lists
    private val defaultProcessingMethods = listOf(
        ProcessingMethodItem("1", "게시물 내리기"),
        ProcessingMethodItem("2", "추모 게시물 올리기"),
        ProcessingMethodItem("3", "추모 계정으로 전환하기")
    )
    private val initialGalleryProcessingMethods = listOf(
        ProcessingMethodItem("1", "'엽사' 폴더 박선호에게 전송"),
        ProcessingMethodItem("2", "'흑역사' 폴더 삭제")
    )
    var processingMethods by mutableStateOf(defaultProcessingMethods)
        private set
    var galleryProcessingMethods by mutableStateOf(initialGalleryProcessingMethods)
        private set

    // Memorial Guideline
    var selectedLastWish by mutableStateOf<String?>(null)
        private set
    var memorialPhotoUrl by mutableStateOf<String?>(null)
        private set
    var funeralVideoUrl by mutableStateOf<String?>(null)
        private set
    var playlistSongCount by mutableIntStateOf(16)
        private set

    // Dropdown States
    var categoryDropdownState by mutableStateOf(
        com.kuit.afternote.feature.mainpage.presentation.component.edit
            .SelectionDropdownState()
    )
        private set
    var serviceDropdownState by mutableStateOf(
        com.kuit.afternote.feature.mainpage.presentation.component.edit
            .SelectionDropdownState()
    )
        private set
    var relationshipDropdownState by mutableStateOf(
        com.kuit.afternote.feature.mainpage.presentation.component.edit
            .SelectionDropdownState()
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
    val playlistAlbumCovers = listOf(
        AlbumCover("1"),
        AlbumCover("2"),
        AlbumCover("3"),
        AlbumCover("4")
    )

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
    val galleryRecipientCallbacks: RecipientCallbacks by lazy {
        RecipientCallbacks(
            onAddClick = ::showAddRecipientDialog,
            onItemEditClick = { _ ->
                // TODO: 수신자 수정 로직
            },
            onItemDeleteClick = ::onRecipientDelete,
            onItemAdded = ::onRecipientItemAdded,
            onTextFieldVisibilityChanged = { _ ->
                // 텍스트 필드 표시 상태 변경 처리
            }
        )
    }

    val galleryProcessingCallbacks: ProcessingMethodCallbacks by lazy {
        ProcessingMethodCallbacks(
            onAddClick = {
                // TODO: 처리 방법 추가 로직
            },
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
            onAddClick = {
                // TODO: 처리 방법 추가 로직
            },
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
        if (category == CATEGORY_GALLERY_AND_FILE) {
            selectedService = "갤러리"
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

    fun showAddRecipientDialog() {
        activeDialog = DialogType.ADD_RECIPIENT
    }

    fun showCustomServiceDialog() {
        activeDialog = DialogType.CUSTOM_SERVICE
    }

    fun dismissDialog() {
        activeDialog = null
        recipientNameState.edit { replace(0, length, "") }
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
    fun onAddRecipient() {
        val name = recipientNameState.text.toString().trim()
        if (name.isEmpty()) return

        val newRecipient = Recipient(
            id = (recipients.size + 1).toString(),
            name = name,
            label = relationshipSelectedValue
        )
        recipients = recipients + newRecipient
        dismissDialog()
    }

    fun onRelationshipSelected(relationship: String) {
        relationshipSelectedValue = relationship
    }

    fun onRecipientDelete(recipientId: String) {
        recipients = recipients.filter { it.id != recipientId }
    }

    fun onRecipientItemAdded(text: String) {
        val newRecipient = Recipient(
            id = (recipients.size + 1).toString(),
            name = text,
            label = "친구"
        )
        recipients = recipients + newRecipient
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
}

@Composable
fun rememberAfternoteEditState(): AfternoteEditState {
    val idState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()
    val messageState = rememberTextFieldState()
    val recipientNameState = rememberTextFieldState()
    val phoneNumberState = rememberTextFieldState()
    val customServiceNameState = rememberTextFieldState()

    return remember {
        AfternoteEditState(
            idState = idState,
            passwordState = passwordState,
            messageState = messageState,
            recipientNameState = recipientNameState,
            phoneNumberState = phoneNumberState,
            customServiceNameState = customServiceNameState
        )
    }
}
