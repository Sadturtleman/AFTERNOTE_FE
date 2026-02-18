package com.kuit.afternote.core.ui.screen.afternotedetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.detail.DeleteConfirmDialog
import com.kuit.afternote.core.ui.component.detail.EditDropdownMenu
import com.kuit.afternote.core.ui.component.detail.InfoCard
import com.kuit.afternote.core.ui.component.detail.ProcessingMethodItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiver
import com.kuit.afternote.feature.afternote.presentation.navgraph.AfternoteLightTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Black
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray8
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 갤러리 상세 화면의 데이터 상태
 */
@Immutable
data class GalleryDetailState(
    val serviceName: String = "갤러리",
    val userName: String = "서영",
    val finalWriteDate: String = "2025.11.26.",
    val afternoteEditReceivers: List<AfternoteEditReceiver> = emptyList(),
    val informationProcessingMethod: String = "",
    val processingMethods: List<String> = emptyList(),
    val message: String = ""
)

/**
 * 갤러리 상세 화면의 콜백 모음
 */
@Immutable
data class GalleryDetailCallbacks(
    val onBackClick: () -> Unit,
    val onEditClick: () -> Unit,
    val onDeleteConfirm: () -> Unit = {}
)

/**
 * 갤러리 상세 화면
 *
 * 피그마 디자인 기반:
 * - 헤더 (뒤로가기, 타이틀, 편집 버튼)
 * - 제목
 * - 최종 작성일 및 정보 처리 방법 카드
 * - 수신자 정보 카드 (있는 경우)
 * - 처리 방법 리스트 카드
 * - 남기신 말씀 카드
 * - 편집 드롭다운 메뉴 (수정하기/삭제하기)
 * - 삭제 확인 다이얼로그
 */
@Composable
fun GalleryDetailScreen(
    modifier: Modifier = Modifier,
    detailState: GalleryDetailState,
    callbacks: GalleryDetailCallbacks,
    isEditable: Boolean = true,
    uiState: AfternoteDetailState = rememberAfternoteDetailState()
) {
    GalleryDetailScaffold(
        modifier = modifier,
        detailState = detailState,
        callbacks = callbacks,
        isEditable = isEditable,
        uiState = uiState
    )
}

@Composable
private fun GalleryDetailScaffold(
    modifier: Modifier,
    detailState: GalleryDetailState,
    callbacks: GalleryDetailCallbacks,
    isEditable: Boolean,
    uiState: AfternoteDetailState
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (isEditable) {
                TopBar(
                    onBackClick = callbacks.onBackClick,
                    onEditClick = uiState::toggleDropdownMenu
                )
            } else {
                TopBar(
                    title = "",
                    onBackClick = callbacks.onBackClick
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = uiState.selectedBottomNavItem,
                onItemSelected = uiState::onBottomNavItemSelected
            )
        }
    ) { paddingValues ->
        GalleryDetailContent(
            modifier = Modifier.padding(paddingValues),
            detailState = detailState,
            callbacks = callbacks,
            isEditable = isEditable,
            uiState = uiState
        )
    }
}

@Composable
private fun GalleryDetailContent(
    modifier: Modifier,
    detailState: GalleryDetailState,
    callbacks: GalleryDetailCallbacks,
    isEditable: Boolean,
    uiState: AfternoteDetailState
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            GalleryDetailScrollableContent(detailState = detailState)
        }

        if (isEditable) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 20.dp)
            ) {
                EditDropdownMenu(
                    expanded = uiState.showDropdownMenu,
                    onDismissRequest = uiState::hideDropdownMenu,
                    onEditClick = { callbacks.onEditClick() },
                    onDeleteClick = { uiState.showDeleteDialog() }
                )
            }

            if (uiState.showDeleteDialog) {
                DeleteConfirmDialog(
                    serviceName = detailState.serviceName,
                    onDismiss = uiState::hideDeleteDialog,
                    onConfirm = {
                        uiState.hideDeleteDialog()
                        callbacks.onDeleteConfirm()
                    }
                )
            }
        }
    }
}

@Composable
private fun GalleryDetailScrollableContent(detailState: GalleryDetailState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        TitleSection(
            serviceName = detailState.serviceName,
            userName = detailState.userName
        )
        Spacer(modifier = Modifier.height(24.dp))
        CardSection(detailState = detailState)
    }
}

@Composable
private fun CardSection(detailState: GalleryDetailState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DateAndMethodCard(
            finalWriteDate = detailState.finalWriteDate,
            informationProcessingMethod = detailState.informationProcessingMethod
        )
        AfternoteEditReceiversCard(afternoteEditReceivers = detailState.afternoteEditReceivers)
        ProcessingMethodsCard(processingMethods = detailState.processingMethods)
        MessageCard(message = detailState.message)
    }
}

@Composable
private fun TitleSection(
    serviceName: String,
    userName: String
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = B1)) {
                append(serviceName)
            }
            append("에 대한 ${userName}님의 기록")
        },
        style = TextStyle(
            fontSize = 18.sp,
            lineHeight = 24.sp,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Bold,
            color = Gray9
        )
    )
}

@Composable
private fun DateAndMethodCard(
    finalWriteDate: String,
    informationProcessingMethod: String
) {
    InfoCard(
        modifier = Modifier.fillMaxWidth(),
        content = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "최종 작성일 $finalWriteDate",
                    style = TextStyle(
                        fontSize = 10.sp,
                        lineHeight = 16.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Normal,
                        color = Gray6
                    )
                )
                InformationProcessingMethodText(
                    informationProcessingMethod = informationProcessingMethod
                )
            }
        }
    )
}

/**
 * 정보 처리 방법 표시 텍스트
 * informationProcessingMethod enum name → 사용자에게 보여줄 텍스트로 변환
 */
@Composable
private fun InformationProcessingMethodText(informationProcessingMethod: String) {
    val annotatedText =
        when (informationProcessingMethod) {
            "TRANSFER",
            "TRANSFER_TO_AFTERNOTE_EDIT_RECEIVER" -> buildAnnotatedString {
                withStyle(style = SpanStyle(color = B1)) { append("수신자") }
                append("에게 정보 전달")
            }
            "ADDITIONAL",
            "TRANSFER_TO_ADDITIONAL_AFTERNOTE_EDIT_RECEIVER" -> buildAnnotatedString {
                withStyle(style = SpanStyle(color = B1)) { append("추가 수신자") }
                append("에게 정보 전달")
            }
            else -> buildAnnotatedString {
                append(informationProcessingMethod)
            }
        }
    Text(
        text = annotatedText,
        style = TextStyle(
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Medium,
            color = Gray9
        )
    )
}

@Composable
private fun AfternoteEditReceiversCard(afternoteEditReceivers: List<AfternoteEditReceiver>) {
    if (afternoteEditReceivers.isEmpty()) return

    InfoCard(
        modifier = Modifier.fillMaxWidth(),
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    space = 8.dp
                )
            ) {
                Text(
                    text = "추가 수신자",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Medium,
                        color = Gray9
                    )
                )
                afternoteEditReceivers.forEachIndexed { _, receiver ->
                    AfternoteEditReceiverDetailItem(receiver = receiver)
                }
            }
        }
    )
}

@Composable
private fun ProcessingMethodsCard(processingMethods: List<String>) {
    if (processingMethods.isEmpty()) return

    InfoCard(
        modifier = Modifier.fillMaxWidth(),
        content = {
            Column {
                Text(
                    text = "처리 방법",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Medium,
                        color = Gray9
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                processingMethods.forEachIndexed { index, method ->
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    ProcessingMethodItem(text = method)
                }
            }
        }
    )
}

@Composable
private fun MessageCard(message: String) {
    val displayMessage = message.ifEmpty { "남기신 말씀이 없습니다." }
    val textColor = if (message.isNotEmpty()) Gray9 else Gray5

    InfoCard(
        modifier = Modifier.fillMaxWidth(),
        content = {
            Column {
                Text(
                    text = "남기신 말씀",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Medium,
                        color = Gray9
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = displayMessage,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Normal,
                        color = textColor
                    )
                )
            }
        }
    )
}

/**
 * 수신자 상세 아이템 컴포넌트
 * 갤러리 상세 화면에서 사용하는 수신자 정보 표시
 */
@Composable
private fun AfternoteEditReceiverDetailItem(
    modifier: Modifier = Modifier,
    receiver: AfternoteEditReceiver
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(R.drawable.img_recipient_profile),
                contentDescription = "프로필 사진",
                modifier = Modifier.fillMaxSize()
            )
        }

        Column {
            Text(
                text = receiver.name,
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    color = Black
                )
            )
            Text(
                text = receiver.label,
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray8
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
private fun GalleryDetailScreenPreview() {
    AfternoteLightTheme {
        GalleryDetailScreen(
            detailState = GalleryDetailState(
                informationProcessingMethod = "TRANSFER_TO_ADDITIONAL_AFTERNOTE_EDIT_RECEIVER",
                processingMethods = listOf("'엽사' 폴더 박선호에게 전송", "'흑역사' 폴더 삭제")
            ),
            callbacks = GalleryDetailCallbacks(
                onBackClick = {},
                onEditClick = {}
            )
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false",
    name = "Gallery Detail Screen with Delete Dialog"
)
@Composable
private fun GalleryDetailScreenWithDialogPreview() {
    AfternoteLightTheme {
        val uiState = rememberAfternoteDetailState()
        uiState.showDeleteDialog()

        GalleryDetailScreen(
            detailState = GalleryDetailState(
                informationProcessingMethod = "TRANSFER_TO_ADDITIONAL_AFTERNOTE_EDIT_RECEIVER",
                processingMethods = listOf("'엽사' 폴더 박선호에게 전송", "'흑역사' 폴더 삭제")
            ),
            callbacks = GalleryDetailCallbacks(
                onBackClick = {},
                onEditClick = {}
            ),
            uiState = uiState
        )
    }
}
