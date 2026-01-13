package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.feature.mainpage.presentation.component.common.header.Header
import com.kuit.afternote.feature.mainpage.presentation.component.edit.AddRecipientDialog
import com.kuit.afternote.feature.mainpage.presentation.component.edit.DropdownMenuStyle
import com.kuit.afternote.feature.mainpage.presentation.component.edit.SelectionDropdown
import com.kuit.afternote.feature.mainpage.presentation.component.edit.content.GalleryAndFileEditContent
import com.kuit.afternote.feature.mainpage.presentation.component.edit.content.MemorialGuidelineEditContent
import com.kuit.afternote.feature.mainpage.presentation.component.edit.content.SocialNetworkEditContent
import com.kuit.afternote.feature.mainpage.presentation.model.AccountSection
import com.kuit.afternote.feature.mainpage.presentation.model.GalleryAndFileEditContentParams
import com.kuit.afternote.feature.mainpage.presentation.model.InfoMethodSection
import com.kuit.afternote.feature.mainpage.presentation.model.InformationProcessingMethod
import com.kuit.afternote.feature.mainpage.presentation.model.MemorialGuidelineEditContentParams
import com.kuit.afternote.feature.mainpage.presentation.model.ProcessingMethodSection
import com.kuit.afternote.feature.mainpage.presentation.model.RecipientSection
import com.kuit.afternote.feature.mainpage.presentation.model.SocialNetworkEditContentParams
import com.kuit.afternote.ui.expand.addFocusCleaner
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray1

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
    state: AfternoteEditState = rememberAfternoteEditState()
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Gray1,
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
                .windowInsetsPadding(WindowInsets.statusBars)
                .addFocusCleaner(focusManager)
        ) {
            EditContent(
                state = state,
                onBackClick = onBackClick,
                onRegisterClick = onRegisterClick
            )

            // Line 336 해결: 조건부 렌더링을 nullable로 변경
            state.activeDialog?.let { dialogType ->
                when (dialogType) {
                    DialogType.ADD_RECIPIENT -> {
                        AddRecipientDialog(
                            recipientNameState = state.recipientNameState,
                            relationshipSelectedValue = state.relationshipSelectedValue,
                            relationshipOptions = state.relationshipOptions,
                            phoneNumberState = state.phoneNumberState,
                            onDismiss = state::dismissDialog,
                            onAddClick = state::onAddRecipient,
                            onRelationshipSelected = state::onRelationshipSelected,
                            onImportContactsClick = {
                                // TODO: 연락처 가져오기 로직
                            }
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
    onBackClick: () -> Unit,
    onRegisterClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Header(
            title = "애프터노트 작성하기",
            onBackClick = onBackClick,
            onActionClick = {
                onRegisterClick(state.selectedService)
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 종류 선택 (Line 279 해결: State의 메서드 사용)
            SelectionDropdown(
                label = "종류",
                selectedValue = state.selectedCategory,
                options = state.categories,
                onValueSelected = state::onCategorySelected,
                menuStyle = DropdownMenuStyle(
                    shadowElevation = 10.dp,
                    tonalElevation = 10.dp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 서비스명 선택 (Line 295 해결: Computed Property 사용)
            SelectionDropdown(
                label = "서비스명",
                selectedValue = state.selectedService,
                options = state.currentServiceOptions,
                onValueSelected = state::onServiceSelected,
                menuStyle = DropdownMenuStyle(
                    shadowElevation = 10.dp,
                    tonalElevation = 10.dp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 종류에 따라 다른 콘텐츠 표시
            CategoryContent(state = state)

            Spacer(modifier = Modifier.height(169.dp))
        }
    }
}

@Composable
private fun CategoryContent(state: AfternoteEditState) {
    when (state.selectedCategory) {
        "추모 가이드라인" -> {
            MemorialGuidelineEditContent(
                params = MemorialGuidelineEditContentParams(
                    memorialPhotoUrl = state.memorialPhotoUrl,
                    playlistSongCount = state.playlistSongCount,
                    playlistAlbumCovers = state.playlistAlbumCovers,
                    selectedLastWish = state.selectedLastWish,
                    lastWishOptions = state.lastWishOptions,
                    funeralVideoUrl = state.funeralVideoUrl,
                    onPhotoAddClick = {
                        // TODO: 사진 선택 로직
                    },
                    onSongAddClick = {
                        // TODO: 노래 추가 로직
                    },
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
    AfternoteTheme {
        AfternoteEditScreen(
            onBackClick = {}
        )
    }
}
