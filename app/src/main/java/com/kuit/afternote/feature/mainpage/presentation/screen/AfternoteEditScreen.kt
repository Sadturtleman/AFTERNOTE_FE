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
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.feature.mainpage.presentation.component.common.header.EditHeader
import com.kuit.afternote.feature.mainpage.presentation.component.common.textfield.MessageTextField
import com.kuit.afternote.feature.mainpage.presentation.component.edit.ProcessingMethodList
import com.kuit.afternote.feature.mainpage.presentation.component.edit.SelectionDropdown
import com.kuit.afternote.feature.mainpage.presentation.component.edit.content.GalleryAndFileEditContent
import com.kuit.afternote.feature.mainpage.presentation.component.edit.content.SocialNetworkEditContent
import com.kuit.afternote.feature.mainpage.presentation.model.AccountProcessingMethod
import com.kuit.afternote.feature.mainpage.presentation.model.InformationProcessingMethod
import com.kuit.afternote.feature.mainpage.presentation.model.ProcessingMethodItem
import com.kuit.afternote.ui.expand.addFocusCleaner
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

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
    onRegisterClick: () -> Unit = {}
) {
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.HOME) }

    // 종류 선택
    var selectedCategory by remember { mutableStateOf("소셜네트워크") }
    val categories = listOf("소셜네트워크", "비즈니스", "갤러리 및 파일", "재산 처리", "추모 가이드라인")

    // 서비스명 선택
    var selectedService by remember { mutableStateOf("인스타그램") }
    val services = listOf("인스타그램", "페이스북", "트위터", "카카오톡", "네이버")
    val galleryServices = listOf("갤러리", "파일")

    // 계정 정보
    val idState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()

    // 계정 처리 방법
    var selectedProcessingMethod by remember { mutableStateOf(AccountProcessingMethod.MEMORIAL_ACCOUNT) }

    // 정보 처리 방법 (갤러리 및 파일용)
    var selectedInformationProcessingMethod by remember { mutableStateOf(InformationProcessingMethod.TRANSFER_TO_RECIPIENT) }

    // 처리 방법 리스트
    val defaultProcessingMethods = listOf(
        ProcessingMethodItem("1", "게시물 내리기"),
        ProcessingMethodItem("2", "추모 게시물 올리기"),
        ProcessingMethodItem("3", "추모 계정으로 전환하기")
    )
    val initialGalleryProcessingMethods = listOf(
        ProcessingMethodItem("1", "'엽사' 폴더 박선호에게 전송"),
        ProcessingMethodItem("2", "'흑역사' 폴더 삭제")
    )
    var processingMethods by remember { mutableStateOf(defaultProcessingMethods) }
    var galleryProcessingMethods by remember { mutableStateOf(initialGalleryProcessingMethods) }

    // 남기실 말씀
    val messageState = rememberTextFieldState()

    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Gray1,
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { selectedBottomNavItem = it }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .addFocusCleaner(focusManager)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                EditHeader(
                    onBackClick = onBackClick,
                    onRegisterClick = onRegisterClick
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    // 종류 선택
                    SelectionDropdown(
                        label = "종류",
                        selectedValue = selectedCategory,
                        options = categories,
                        onValueSelected = { category ->
                            selectedCategory = category
                            if (category == "갤러리 및 파일") {
                                selectedService = "갤러리"
                            } else if (selectedCategory == "갤러리 및 파일" && category != "갤러리 및 파일") {
                                selectedService = "인스타그램"
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 서비스명 선택 (갤러리 및 파일일 때는 다른 옵션 표시)
                    SelectionDropdown(
                        label = "서비스명",
                        selectedValue = selectedService,
                        options = if (selectedCategory == "갤러리 및 파일") galleryServices else services,
                        onValueSelected = { selectedService = it }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 종류에 따라 다른 콘텐츠 표시
                    if (selectedCategory != "갤러리 및 파일") {
                        SocialNetworkEditContent(
                            idState = idState,
                            passwordState = passwordState,
                            selectedProcessingMethod = selectedProcessingMethod,
                            onProcessingMethodSelected = { selectedProcessingMethod = it }
                        )
                    } else {
                        GalleryAndFileEditContent(
                            selectedInformationProcessingMethod = selectedInformationProcessingMethod,
                            onInformationProcessingMethodSelected = { selectedInformationProcessingMethod = it }
                        )
                    }

                    // 처리 방법 리스트 섹션
                    Text(
                        text = "처리 방법 리스트",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight(500),
                            color = Gray9
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ProcessingMethodList(
                        items = if (selectedCategory == "갤러리 및 파일") {
                            galleryProcessingMethods
                        } else {
                            processingMethods
                        },
                        onAddClick = {
                            // TODO: 처리 방법 추가 로직
                        },
                        onItemMoreClick = {
                            // 드롭다운 메뉴는 ProcessingMethodList 내부에서 처리
                        },
                        onItemEditClick = { itemId ->
                            // TODO: 처리 방법 수정 로직
                        },
                        onItemDeleteClick = { itemId ->
                            if (selectedCategory == "갤러리 및 파일") {
                                galleryProcessingMethods = galleryProcessingMethods.filter { it.id != itemId }
                            } else {
                                processingMethods = processingMethods.filter { it.id != itemId }
                            }
                        },
                        onItemAdded = { text ->
                            val newItem = if (selectedCategory == "갤러리 및 파일") {
                                ProcessingMethodItem(
                                    id = (galleryProcessingMethods.size + 1).toString(),
                                    text = text
                                )
                            } else {
                                ProcessingMethodItem(
                                    id = (processingMethods.size + 1).toString(),
                                    text = text
                                )
                            }
                            if (selectedCategory == "갤러리 및 파일") {
                                galleryProcessingMethods = galleryProcessingMethods + newItem
                            } else {
                                processingMethods = processingMethods + newItem
                            }
                        },
                        onTextFieldVisibilityChanged = { isOpen ->
                            // 텍스트 필드 표시 상태 변경 처리
                        }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // 남기실 말씀
                    MessageTextField(
                        textFieldState = messageState
                    )

                    Spacer(modifier = Modifier.height(169.dp))
                }
            }
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
