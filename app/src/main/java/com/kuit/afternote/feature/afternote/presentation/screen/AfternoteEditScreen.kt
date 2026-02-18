package com.kuit.afternote.feature.afternote.presentation.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import android.util.Log
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.list.AlbumCover
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.afternote.presentation.component.edit.content.GalleryAndFileEditContent
import com.kuit.afternote.feature.afternote.presentation.component.edit.content.GalleryAndFileEditContentParams
import com.kuit.afternote.feature.afternote.presentation.component.edit.content.MemorialGuidelineEditContent
import com.kuit.afternote.feature.afternote.presentation.component.edit.content.MemorialGuidelineEditContentParams
import com.kuit.afternote.feature.afternote.presentation.component.edit.content.SocialNetworkEditContent
import com.kuit.afternote.feature.afternote.presentation.component.edit.content.SocialNetworkEditContentParams
import com.kuit.afternote.feature.afternote.presentation.component.edit.dropdown.DropdownMenuStyle
import com.kuit.afternote.feature.afternote.presentation.component.edit.dropdown.SelectionDropdown
import com.kuit.afternote.feature.afternote.presentation.component.edit.dropdown.SelectionDropdownLabelParams
import com.kuit.afternote.feature.afternote.presentation.component.edit.afternoteeditreceiver.AddAfternoteEditReceiverDialog
import com.kuit.afternote.feature.afternote.presentation.component.edit.afternoteeditreceiver.AddAfternoteEditReceiverDialogCallbacks
import com.kuit.afternote.feature.afternote.presentation.component.edit.afternoteeditreceiver.AddAfternoteEditReceiverDialogParams
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AccountSection
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiverSection
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.ProcessingMethodItem
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.ProcessingMethodSection
import com.kuit.afternote.feature.afternote.presentation.component.edit.processingmethod.CustomServiceDialog
import com.kuit.afternote.feature.afternote.presentation.component.edit.processingmethod.CustomServiceDialogCallbacks
import com.kuit.afternote.feature.afternote.presentation.component.edit.processingmethod.CustomServiceDialogParams
import com.kuit.afternote.app.compositionlocal.DataProviderLocals
import com.kuit.afternote.data.provider.FakeAfternoteEditDataProvider
import com.kuit.afternote.feature.afternote.domain.model.AfternoteProcessingMethod
import com.kuit.afternote.feature.afternote.presentation.navgraph.AfternoteLightTheme
import com.kuit.afternote.ui.expand.addFocusCleaner
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "AfternoteEditScreen"
private const val CATEGORY_GALLERY_AND_FILE = "갤러리 및 파일"
private const val CATEGORY_MEMORIAL_GUIDELINE = "추모 가이드라인"

/**
 * 콜백 그룹 (S107: 파라미터 7개 이하 유지).
 */
data class AfternoteEditScreenCallbacks(
    val onBackClick: () -> Unit = {},
    val onRegisterClick: (RegisterAfternotePayload) -> Unit = {},
    val onNavigateToAddSong: () -> Unit = {},
    val onBottomNavTabSelected: (BottomNavItem) -> Unit = {},
    val onThumbnailBytesReady: (ByteArray?) -> Unit = {}
)

/**
 * Message to show when save fails (validation or API error).
 * When non-null, the screen shows a Snackbar with this text.
 */
data class AfternoteEditSaveError(val message: String)

/**
 * 애프터노트 수정/작성 화면
 *
 * 피그마 디자인 기반:
 * - 헤더 (뒤로가기, 타이틀, 등록 버튼)
 * - 종류 선택 드롭다운
 * - 서비스명 선택 드롭다운
 * - 계정 정보 입력 (아이디, 비밀번호)
 * - 계정 처리 방법 선택 (라디오 버튼)
 * - 처리 방법 리스트 (체크박스)
 * - 남기실 말씀 (멀티라인 텍스트 필드)
 */
@Composable
fun AfternoteEditScreen(
    modifier: Modifier = Modifier,
    callbacks: AfternoteEditScreenCallbacks = AfternoteEditScreenCallbacks(),
    state: AfternoteEditState = rememberAfternoteEditState(),
    playlistStateHolder: MemorialPlaylistStateHolder? = null,
    initialItem: com.kuit.afternote.feature.afternote.domain.model.AfternoteItem? = null,
    saveError: AfternoteEditSaveError? = null
) {
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(saveError) {
        saveError?.let { err ->
            snackbarHostState.showSnackbar(
                message = err.message,
                withDismissAction = true
            )
        }
    }

    LaunchedEffect(initialItem?.id) {
        val item = initialItem ?: run {
            Log.d(TAG, "LaunchedEffect: initialItem is null, skipping loadFromExisting")
            return@LaunchedEffect
        }
        Log.d(
            TAG,
            "LaunchedEffect: item.id=${item.id}, state.loadedItemId=${state.loadedItemId}, " +
                "needsLoad=${state.loadedItemId != item.id}"
        )
        if (state.loadedItemId != item.id) {
            state.loadFromExisting(
                LoadFromExistingParams(
                    itemId = item.id,
                    serviceName = item.serviceName,
                    categoryDisplayString = AfternoteItemMapper.categoryStringForEditScreen(item.type),
                    accountId = item.accountId,
                    password = item.password,
                    message = item.message,
                    accountProcessingMethodName = item.accountProcessingMethod,
                    informationProcessingMethodName = item.informationProcessingMethod,
                    processingMethodsList = item.processingMethods.map { ProcessingMethodItem(it.id, it.text) },
                    galleryProcessingMethodsList = item.galleryProcessingMethods.map { ProcessingMethodItem(it.id, it.text) }
                )
            )
        }
    }

    // 플레이리스트 상태 홀더가 전달되면 설정
    LaunchedEffect(playlistStateHolder) {
        playlistStateHolder?.let { state.setPlaylistStateHolder(it) }
    }

    // 플레이리스트 노래 개수 변경 감지 및 동기화
    val songCount by remember {
        playlistStateHolder?.songs?.let {
            androidx.compose.runtime.derivedStateOf { it.size }
        } ?: androidx.compose.runtime.derivedStateOf { state.playlistSongCount }
    }

    LaunchedEffect(songCount) {
        if (playlistStateHolder != null) {
            state.updatePlaylistSongCount()
        }
    }

    val memorialPhotoPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            state.onMemorialPhotoSelected(uri)
        }
    val memorialVideoPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            state.onFuneralVideoSelected(uri)
        }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopBar(
                title = "애프터노트 작성하기",
                onBackClick = callbacks.onBackClick,
                onActionClick = {
                    val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                    val date = dateFormat.format(Date())
                    val processingMethods = state.processingMethods.map {
                        AfternoteProcessingMethod(it.id, it.text)
                    }
                    val galleryProcessingMethods = state.galleryProcessingMethods.map {
                        AfternoteProcessingMethod(it.id, it.text)
                    }
                    callbacks.onRegisterClick(
                        RegisterAfternotePayload(
                            serviceName =
                                if (state.selectedCategory == CATEGORY_MEMORIAL_GUIDELINE)
                                    CATEGORY_MEMORIAL_GUIDELINE
                                else
                                    state.selectedService,
                            date = date,
                            accountId = state.idState.text.toString(),
                            password = state.passwordState.text.toString(),
                            message = state.messageState.text.toString(),
                            accountProcessingMethod = state.selectedProcessingMethod.name,
                            informationProcessingMethod = state.selectedInformationProcessingMethod.name,
                            processingMethods = processingMethods,
                            galleryProcessingMethods = galleryProcessingMethods,
                            atmosphere = state.getAtmosphereForSave()
                        )
                    )
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = state.selectedBottomNavItem,
                onItemSelected = { item ->
                    state.onBottomNavItemSelected(item)
                    callbacks.onBottomNavTabSelected(item)
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .addFocusCleaner(focusManager)
        ) {
            EditContent(
                state = state,
                onNavigateToAddSong = callbacks.onNavigateToAddSong,
                onPhotoAddClick = {
                    memorialPhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onVideoAddClick = {
                    memorialVideoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                    )
                },
                onThumbnailBytesReady = callbacks.onThumbnailBytesReady,
                bottomPadding = paddingValues
            )

            // Line 336 해결: 조건부 렌더링을 nullable로 변경
            state.activeDialog?.let { dialogType ->
                when (dialogType) {
                    DialogType.ADD_AFTERNOTE_EDIT_RECEIVER -> {
                        AddAfternoteEditReceiverDialog(
                            params = AddAfternoteEditReceiverDialogParams(
                                afternoteEditReceiverNameState = state.afternoteEditReceiverNameState,
                                phoneNumberState = state.phoneNumberState,
                                relationshipSelectedValue = state.relationshipSelectedValue,
                                relationshipOptions = state.relationshipOptions,
                                callbacks = AddAfternoteEditReceiverDialogCallbacks(
                                    onDismiss = state::dismissDialog,
                                    onAddClick = state::onAddAfternoteEditReceiver,
                                    onRelationshipSelected = state::onRelationshipSelected,
                                    onImportContactsClick = {
                                        // 연락처 가져오기 기능은 추후 구현 예정
                                    }
                                )
                            )
                        )
                    }

                    DialogType.CUSTOM_SERVICE -> {
                        CustomServiceDialog(
                            params = CustomServiceDialogParams(
                                serviceNameState = state.customServiceNameState,
                                callbacks = CustomServiceDialogCallbacks(
                                    onDismiss = state::dismissDialog,
                                    onAddClick = state::onAddCustomService
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EditContent(
    state: AfternoteEditState,
    onNavigateToAddSong: () -> Unit,
    onPhotoAddClick: () -> Unit,
    onVideoAddClick: () -> Unit,
    onThumbnailBytesReady: (ByteArray?) -> Unit,
    bottomPadding: PaddingValues
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 메인 콘텐츠 (스크롤 가능, 남은 공간 차지)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 종류 선택 (Line 279 해결: State의 메서드 사용)
            SelectionDropdown(
                labelParams = SelectionDropdownLabelParams(label = "종류"),
                selectedValue = state.selectedCategory,
                options = state.categories,
                onValueSelected = state::onCategorySelected,
                menuStyle = DropdownMenuStyle(
                    shadowElevation = 10.dp,
                    tonalElevation = 10.dp
                ),
                state = state.categoryDropdownState
            )

            // 서비스명 선택 (추모 가이드라인 선택 시 숨김)
            if (state.selectedCategory != CATEGORY_MEMORIAL_GUIDELINE) {
                Spacer(modifier = Modifier.height(16.dp))

                SelectionDropdown(
                    labelParams = SelectionDropdownLabelParams(label = "서비스명"),
                    selectedValue = state.selectedService,
                    options = state.currentServiceOptions,
                    onValueSelected = state::onServiceSelected,
                    menuStyle = DropdownMenuStyle(
                        shadowElevation = 10.dp,
                        tonalElevation = 10.dp
                    ),
                    state = state.serviceDropdownState
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // 종류에 따라 다른 콘텐츠 표시
            // 각 Content 컴포넌트 내부에서 카테고리별 하단 여백 처리
            CategoryContent(
                state = state,
                onNavigateToAddSong = onNavigateToAddSong,
                onPhotoAddClick = onPhotoAddClick,
                onVideoAddClick = onVideoAddClick,
                onThumbnailBytesReady = onThumbnailBytesReady,
                bottomPadding = bottomPadding
            )
        }
    }
}

@Composable
private fun CategoryContent(
    state: AfternoteEditState,
    onNavigateToAddSong: () -> Unit,
    onPhotoAddClick: () -> Unit,
    onVideoAddClick: () -> Unit,
    onThumbnailBytesReady: (ByteArray?) -> Unit,
    bottomPadding: PaddingValues
) {
    when (state.selectedCategory) {
        CATEGORY_MEMORIAL_GUIDELINE -> {
            val albumCoversFromPlaylist = state.playlistStateHolder?.songs?.let { songs ->
                songs.mapIndexed { _, s ->
                    AlbumCover(
                        id = s.id,
                        imageUrl = s.albumCoverUrl,
                        title = s.title
                    )
                }
            } ?: state.playlistAlbumCovers
            val livePlaylistSongCount =
                state.playlistStateHolder?.songs?.size ?: state.playlistSongCount
            MemorialGuidelineEditContent(
                bottomPadding = bottomPadding,
                params = MemorialGuidelineEditContentParams(
                    displayMemorialPhotoUri = state.pickedMemorialPhotoUri,
                    playlistSongCount = livePlaylistSongCount,
                    playlistAlbumCovers = albumCoversFromPlaylist,
                    selectedLastWish = state.selectedLastWish,
                    lastWishOptions = state.lastWishOptions,
                    funeralVideoUrl = state.funeralVideoUrl,
                    funeralThumbnailUrl = state.funeralThumbnailUrl,
                    customLastWishText = state.customLastWishText,
                    recipientSection = AfternoteEditReceiverSection(
                        afternoteEditReceivers = state.afternoteEditReceivers,
                        callbacks = state.galleryAfternoteEditReceiverCallbacks
                    ),
                    onSongAddClick = onNavigateToAddSong,
                    onLastWishSelected = state::onLastWishSelected,
                    onCustomLastWishChanged = state::onCustomLastWishChanged,
                    onPhotoAddClick = onPhotoAddClick,
                    onVideoAddClick = onVideoAddClick,
                    onThumbnailBytesReady = onThumbnailBytesReady
                )
            )
        }

        CATEGORY_GALLERY_AND_FILE -> {
            GalleryAndFileEditContent(
                bottomPadding = bottomPadding,
                params = GalleryAndFileEditContentParams(
                    messageState = state.messageState,
                    recipientSection = AfternoteEditReceiverSection(
                        afternoteEditReceivers = state.afternoteEditReceivers,
                        callbacks = state.galleryAfternoteEditReceiverCallbacks
                    ),
                    processingMethodSection = ProcessingMethodSection(
                        items = state.galleryProcessingMethods,
                        callbacks = state.galleryProcessingCallbacks
                    )
                )
            )
        }

        else -> {
            SocialNetworkEditContent(
                bottomPadding = bottomPadding,
                params = SocialNetworkEditContentParams(
                    messageState = state.messageState,
                    accountSection = AccountSection(
                        idState = state.idState,
                        passwordState = state.passwordState,
                        selectedMethod = state.selectedProcessingMethod,
                        onMethodSelected = state::onProcessingMethodSelected
                    ),
                    recipientSection = AfternoteEditReceiverSection(
                        afternoteEditReceivers = state.afternoteEditReceivers,
                        callbacks = state.galleryAfternoteEditReceiverCallbacks
                    ),
                    processingMethodSection = ProcessingMethodSection(
                        items = state.processingMethods,
                        callbacks = state.socialProcessingCallbacks
                    )
                )
            )
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun AfternoteEditScreenPreview() {
    AfternoteLightTheme {
        CompositionLocalProvider(
            DataProviderLocals.LocalAfternoteEditDataProvider provides FakeAfternoteEditDataProvider()
        ) {
            AfternoteEditScreen(
                callbacks = AfternoteEditScreenCallbacks(onBackClick = {})
            )
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false",
    name = "갤러리 및 파일"
)
@Composable
private fun AfternoteEditScreenGalleryAndFilePreview() {
    AfternoteLightTheme {
        CompositionLocalProvider(
            DataProviderLocals.LocalAfternoteEditDataProvider provides FakeAfternoteEditDataProvider()
        ) {
            val state = rememberAfternoteEditState().apply {
                onCategorySelected(CATEGORY_GALLERY_AND_FILE)
            }
            AfternoteEditScreen(
                callbacks = AfternoteEditScreenCallbacks(onBackClick = {}),
                state = state
            )
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false",
    name = "추모 가이드라인"
)
@Composable
private fun AfternoteEditScreenMemorialGuidelinePreview() {
    AfternoteLightTheme {
        CompositionLocalProvider(
            DataProviderLocals.LocalAfternoteEditDataProvider provides FakeAfternoteEditDataProvider()
        ) {
            val state = rememberAfternoteEditState().apply {
                onCategorySelected(CATEGORY_MEMORIAL_GUIDELINE)
            }
            AfternoteEditScreen(
                callbacks = AfternoteEditScreenCallbacks(onBackClick = {}),
                state = state
            )
        }
    }
}
