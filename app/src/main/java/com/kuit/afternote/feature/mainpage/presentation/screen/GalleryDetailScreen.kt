package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
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
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.core.Header
import com.kuit.afternote.feature.mainpage.presentation.component.detail.DeleteConfirmDialogContent
import com.kuit.afternote.feature.mainpage.presentation.component.detail.EditDropdownMenu
import com.kuit.afternote.feature.mainpage.presentation.component.detail.InfoCard
import com.kuit.afternote.feature.mainpage.presentation.component.detail.ProcessingMethodItem
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Recipient
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Black
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray8
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.Spacing

/**
 * 갤러리 상세 화면의 데이터 상태
 */
@Immutable
data class GalleryDetailState(
    val serviceName: String = "갤러리",
    val userName: String = "서영",
    val finalWriteDate: String = "2025.11.26.",
    val recipients: List<Recipient> = listOf(
        Recipient(id = "1", name = "김지은", label = "친구"),
        Recipient(id = "2", name = "김지은", label = "친구")
    ),
    val processingMethods: List<String> = listOf(
        "'엽사' 폴더 박선호에게 전송",
        "'흑역사' 폴더 삭제"
    ),
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
    uiState: AfternoteDetailState = rememberAfternoteDetailState()
) {
    GalleryDetailScaffold(
        modifier = modifier,
        detailState = detailState,
        callbacks = callbacks,
        uiState = uiState
    )
}

@Composable
private fun GalleryDetailScaffold(
    modifier: Modifier,
    detailState: GalleryDetailState,
    callbacks: GalleryDetailCallbacks,
    uiState: AfternoteDetailState
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
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
            uiState = uiState
        )
    }
}

@Composable
private fun GalleryDetailContent(
    modifier: Modifier,
    detailState: GalleryDetailState,
    callbacks: GalleryDetailCallbacks,
    uiState: AfternoteDetailState
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(
                title = "",
                onBackClick = callbacks.onBackClick,
                onEditClick = uiState::toggleDropdownMenu
            )
            GalleryDetailScrollableContent(detailState = detailState)
        }

        GalleryDetailDropdownOverlay(
            showDropdown = uiState.showDropdownMenu,
            onDismiss = uiState::hideDropdownMenu,
            onEditClick = {
                uiState.hideDropdownMenu()
                callbacks.onEditClick()
            },
            onDeleteClick = {
                uiState.hideDropdownMenu()
                uiState.showDeleteDialog()
            }
        )

        GalleryDetailDeleteDialog(
            showDialog = uiState.showDeleteDialog,
            serviceName = detailState.serviceName,
            onDismiss = uiState::hideDeleteDialog,
            onConfirm = {
                uiState.hideDeleteDialog()
                callbacks.onDeleteConfirm()
            }
        )
    }
}

@Composable
private fun GalleryDetailDeleteDialog(
    showDialog: Boolean,
    serviceName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (!showDialog) return

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 배경 오버레이 (반투명 검은색)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() }
        )

        // 다이얼로그 컨텐츠 - 피그마 비율에 맞게 weight 사용
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            // 피그마 기준: 네비게이션 바로부터 247dp 비율에 맞게 weight 적용
            Spacer(modifier = Modifier.weight(1f))
            
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                DeleteConfirmDialogContent(
                    serviceName = serviceName,
                    onDismiss = onDismiss,
                    onConfirm = onConfirm
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
        Spacer(modifier = Modifier.height(Spacing.l))
        TitleSection(
            serviceName = detailState.serviceName,
            userName = detailState.userName
        )
        Spacer(modifier = Modifier.height(Spacing.l))
        CardSection(detailState = detailState)
    }
}

@Composable
private fun CardSection(detailState: GalleryDetailState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DateAndMethodCard(finalWriteDate = detailState.finalWriteDate)
        RecipientsCard(recipients = detailState.recipients)
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
    finalWriteDate: String
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
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = B1)) {
                            append("추가 수신자")
                        }
                        append("에게 정보 전달")
                    },
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Medium,
                        color = Gray9
                    )
                )
            }
        }
    )
}

@Composable
private fun RecipientsCard(recipients: List<Recipient>) {
    if (recipients.isEmpty()) return

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
                recipients.forEachIndexed { _, recipient ->
                    RecipientDetailItem(recipient = recipient)
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

@Composable
private fun GalleryDetailDropdownOverlay(
    showDropdown: Boolean,
    onDismiss: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    if (!showDropdown) return

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() }
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-20).dp, y = 48.dp)
        ) {
            EditDropdownMenu(
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

/**
 * 수신자 상세 아이템 컴포넌트
 * 갤러리 상세 화면에서 사용하는 수신자 정보 표시
 */
@Composable
private fun RecipientDetailItem(
    modifier: Modifier = Modifier,
    recipient: Recipient
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
                text = recipient.name,
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    color = Black
                )
            )
            Text(
                text = recipient.label,
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
    AfternoteTheme {
        GalleryDetailScreen(
            detailState = GalleryDetailState(),
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
    AfternoteTheme {
        val uiState = rememberAfternoteDetailState()
        uiState.showDeleteDialog()
        
        GalleryDetailScreen(
            detailState = GalleryDetailState(),
            callbacks = GalleryDetailCallbacks(
                onBackClick = {},
                onEditClick = {}
            ),
            uiState = uiState
        )
    }
}
