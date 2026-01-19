package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.BottomNavigationBar
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.feature.mainpage.presentation.component.edit.AlbumCover
import com.kuit.afternote.feature.mainpage.presentation.component.edit.AddRecipientDialog
import com.kuit.afternote.feature.mainpage.presentation.component.edit.AddRecipientDialogCallbacks
import com.kuit.afternote.feature.mainpage.presentation.component.edit.AddRecipientDialogParams
import com.kuit.afternote.feature.mainpage.presentation.component.edit.CustomServiceDialog
import com.kuit.afternote.feature.mainpage.presentation.component.edit.CustomServiceDialogCallbacks
import com.kuit.afternote.feature.mainpage.presentation.component.edit.CustomServiceDialogParams
import com.kuit.afternote.feature.mainpage.presentation.component.edit.DropdownMenuStyle
import com.kuit.afternote.feature.mainpage.presentation.component.edit.SelectionDropdown
import com.kuit.afternote.feature.mainpage.presentation.component.edit.content.GalleryAndFileEditContent
import com.kuit.afternote.feature.mainpage.presentation.component.edit.content.GalleryAndFileEditContentParams
import com.kuit.afternote.feature.mainpage.presentation.component.edit.content.MemorialGuidelineEditContent
import com.kuit.afternote.feature.mainpage.presentation.component.edit.content.MemorialGuidelineEditContentParams
import com.kuit.afternote.feature.mainpage.presentation.component.edit.content.SocialNetworkEditContent
import com.kuit.afternote.feature.mainpage.presentation.component.edit.content.SocialNetworkEditContentParams
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.AccountSection
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.InfoMethodSection
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.InformationProcessingMethod
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.ProcessingMethodSection
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.RecipientSection
import com.kuit.afternote.feature.mainpage.presentation.navgraph.MainPageLightTheme
import com.kuit.afternote.ui.expand.addFocusCleaner
import com.kuit.afternote.ui.theme.Spacing

private const val CATEGORY_GALLERY_AND_FILE = "갤러리 및 파일"

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
    onBackClick: () -> Unit,
    onRegisterClick: (String) -> Unit = {},
    onNavigateToAddSong: () -> Unit = {},
    state: AfternoteEditState = rememberAfternoteEditState(),
    playlistStateHolder: MemorialPlaylistStateHolder? = null
) {
    val focusManager = LocalFocusManager.current
    
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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = "애프터노트 작성하기",
                onBackClick = onBackClick,
                onActionClick = {
                    onRegisterClick(state.selectedService)
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = state.selectedBottomNavItem,
                onItemSelected = state::onBottomNavItemSelected
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
                onNavigateToAddSong = onNavigateToAddSong
            )

            // Line 336 해결: 조건부 렌더링을 nullable로 변경
            state.activeDialog?.let { dialogType ->
                when (dialogType) {
                    DialogType.ADD_RECIPIENT -> {
                        AddRecipientDialog(
                            params = AddRecipientDialogParams(
                                recipientNameState = state.recipientNameState,
                                phoneNumberState = state.phoneNumberState,
                                relationshipSelectedValue = state.relationshipSelectedValue,
                                relationshipOptions = state.relationshipOptions,
                                callbacks = AddRecipientDialogCallbacks(
                                    onDismiss = state::dismissDialog,
                                    onAddClick = state::onAddRecipient,
                                    onRelationshipSelected = state::onRelationshipSelected,
                                    onImportContactsClick = {
                                        // TODO: 연락처 가져오기 로직
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
    onNavigateToAddSong: () -> Unit
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
            Spacer(modifier = Modifier.height(Spacing.l))

            // 종류 선택 (Line 279 해결: State의 메서드 사용)
            SelectionDropdown(
                label = "종류",
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
            if (state.selectedCategory != "추모 가이드라인") {
                Spacer(modifier = Modifier.height(Spacing.m))

                SelectionDropdown(
                    label = "서비스명",
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
            Spacer(modifier = Modifier.height(Spacing.l))

            // 종류에 따라 다른 콘텐츠 표시
            // 각 Content 컴포넌트 내부에서 카테고리별 하단 여백 처리
            CategoryContent(
                state = state,
                onNavigateToAddSong = onNavigateToAddSong
            )
        }
    }
}

@Composable
private fun CategoryContent(
    state: AfternoteEditState,
    onNavigateToAddSong: () -> Unit
) {
    when (state.selectedCategory) {
        "추모 가이드라인" -> {
            val albumCoversFromPlaylist = state.playlistStateHolder?.songs?.let { songs ->
                (1..songs.size).map { AlbumCover(id = "$it") }
            } ?: state.playlistAlbumCovers
            MemorialGuidelineEditContent(
                params = MemorialGuidelineEditContentParams(
                    memorialPhotoUrl = state.memorialPhotoUrl,
                    playlistSongCount = state.playlistSongCount,
                    playlistAlbumCovers = albumCoversFromPlaylist,
                    selectedLastWish = state.selectedLastWish,
                    lastWishOptions = state.lastWishOptions,
                    funeralVideoUrl = state.funeralVideoUrl,
                    onPhotoAddClick = {
                        // TODO: 사진 선택 로직
                    },
                    onSongAddClick = onNavigateToAddSong,
                    onLastWishSelected = state::onLastWishSelected,
                    onVideoAddClick = {
                        // TODO: 영상 선택 로직
                    }
                )
            )
        }

        CATEGORY_GALLERY_AND_FILE -> {
            GalleryAndFileEditContent(
                params = GalleryAndFileEditContentParams(
                    messageState = state.messageState,
                    infoMethodSection = InfoMethodSection(
                        selectedMethod = state.selectedInformationProcessingMethod,
                        onMethodSelected = state::onInformationProcessingMethodSelected
                    ),
                    recipientSection = if (
                        state.selectedInformationProcessingMethod ==
                        InformationProcessingMethod.TRANSFER_TO_ADDITIONAL_RECIPIENT
                    ) {
                        RecipientSection(
                            recipients = state.recipients,
                            callbacks = state.galleryRecipientCallbacks
                        )
                    } else {
                        null
                    },
                    processingMethodSection = ProcessingMethodSection(
                        items = state.galleryProcessingMethods,
                        callbacks = state.galleryProcessingCallbacks
                    )
                )
            )
        }

        else -> {
            SocialNetworkEditContent(
                params = SocialNetworkEditContentParams(
                    messageState = state.messageState,
                    accountSection = AccountSection(
                        idState = state.idState,
                        passwordState = state.passwordState,
                        selectedMethod = state.selectedProcessingMethod,
                        onMethodSelected = state::onProcessingMethodSelected
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
    MainPageLightTheme {
        AfternoteEditScreen(
            onBackClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false",
    name = "갤러리 및 파일"
)
@Composable
private fun AfternoteEditScreenGalleryAndFilePreview() {
    MainPageLightTheme {
        val state = rememberAfternoteEditState()
        LaunchedEffect(Unit) {
            state.onCategorySelected(CATEGORY_GALLERY_AND_FILE)
        }
        AfternoteEditScreen(
            onBackClick = {},
            state = state
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false",
    name = "추모 가이드라인"
)
@Composable
private fun AfternoteEditScreenMemorialGuidelinePreview() {
    MainPageLightTheme {
        val state = rememberAfternoteEditState()
        LaunchedEffect(Unit) {
            state.onCategorySelected("추모 가이드라인")
        }
        AfternoteEditScreen(
            onBackClick = {},
            state = state
        )
    }
}
