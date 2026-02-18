package com.kuit.afternote.core.ui.screen.afternotedetail

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.detail.DeleteConfirmDialog
import com.kuit.afternote.core.ui.component.detail.EditDropdownMenu
import com.kuit.afternote.core.ui.component.detail.InfoCard
import com.kuit.afternote.core.ui.component.detail.InfoRow
import com.kuit.afternote.core.ui.component.detail.ProcessingMethodItem
import com.kuit.afternote.core.ui.component.detail.ReceiversCard
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiver
import com.kuit.afternote.ui.theme.Sansneo

/**
 * Display data for [SocialNetworkDetailScreen].
 *
 * Used for 소셜 네트워크 category afternote detail.
 */
data class SocialNetworkDetailContent(
    val serviceName: String = "인스타그램",
    val userName: String = "서영",
    val accountId: String = "",
    val password: String = "",
    val accountProcessingMethod: String = "",
    val processingMethods: List<String> = emptyList(),
    val message: String = "",
    val finalWriteDate: String = "2025.11.26.",
    val afternoteEditReceivers: List<AfternoteEditReceiver> = emptyList()
)

/**
 * 소셜 네트워크 애프터노트 상세 화면.
 *
 * 피그마 디자인 기반:
 * - 헤더 (뒤로가기, 타이틀, 편집 버튼)
 * - 제목
 * - 최종 작성일 및 처리 방법 카드
 * - 개인 정보 카드
 * - 처리 방법 리스트 카드
 * - 남기신 말씀 카드
 * - 편집 드롭다운 메뉴 (수정하기/삭제하기)
 * - 삭제 확인 다이얼로그
 *
 * @param content Display data (service name, user name, account info, message, etc.)
 * @param isEditable true이면 편집/삭제 기능 표시 (작성자 모드), false이면 읽기 전용 (수신자 모드)
 */
@Composable
fun SocialNetworkDetailScreen(
    modifier: Modifier = Modifier,
    content: SocialNetworkDetailContent = SocialNetworkDetailContent(),
    isEditable: Boolean = true,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit = {},
    onDeleteConfirm: () -> Unit = {},
    state: AfternoteDetailState = rememberAfternoteDetailState()
) {
    if (isEditable && state.showDeleteDialog) {
        DeleteConfirmDialog(
            serviceName = content.serviceName,
            onDismiss = state::hideDeleteDialog,
            onConfirm = {
                state.hideDeleteDialog()
                onDeleteConfirm()
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (isEditable) {
                TopBar(
                    onBackClick = onBackClick,
                    onEditClick = state::toggleDropdownMenu
                )
            } else {
                TopBar(
                    title = "",
                    onBackClick = onBackClick
                )
            }
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
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                SocialNetworkDetailScrollContent(content = content)
            }
            if (isEditable) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 20.dp)
                ) {
                    EditDropdownMenu(
                        expanded = state.showDropdownMenu,
                        onDismissRequest = state::hideDropdownMenu,
                        onEditClick = onEditClick,
                        onDeleteClick = { state.showDeleteDialog() }
                    )
                }
            }
        }
    }
}

@Composable
private fun SocialNetworkDetailScrollContent(content: SocialNetworkDetailContent) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = B1)) {
                    append(content.serviceName)
                }
                append("에 대한 ${content.userName}님의 기록")
            },
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Bold,
                color = Gray9
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        InfoCard(
            modifier = Modifier.fillMaxWidth(),
            content = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "최종 작성일 ${content.finalWriteDate}",
                        style = TextStyle(
                            fontSize = 10.sp,
                            lineHeight = 16.sp,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Normal,
                            color = Gray6
                        )
                    )
                    AccountProcessingMethodText(
                        accountProcessingMethod = content.accountProcessingMethod
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        ReceiversCard(receivers = content.afternoteEditReceivers)
        Spacer(modifier = Modifier.height(8.dp))
        InfoCard(
            modifier = Modifier.fillMaxWidth(),
            content = {
                Column {
                    Text(
                        text = "기록에 대한 개인 정보",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Medium,
                            color = Gray9
                        )
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    InfoRow(label = "아이디", value = content.accountId)
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoRow(label = "비밀번호", value = content.password)
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (content.processingMethods.isNotEmpty()) {
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
                        content.processingMethods.forEachIndexed { index, method ->
                            if (index > 0) Spacer(modifier = Modifier.height(6.dp))
                            ProcessingMethodItem(text = method)
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
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
                    val displayMessage = content.message.ifEmpty { "남기신 말씀이 없습니다." }
                    val textColor = if (content.message.isNotEmpty()) Gray9 else Gray5
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
}

/**
 * 계정 처리 방법 표시 텍스트
 * accountProcessingMethod enum name → 사용자에게 보여줄 텍스트로 변환
 */
@Composable
private fun AccountProcessingMethodText(accountProcessingMethod: String) {
    // 서버 processMethod 코드(MEMORIAL / DELETE / TRANSFER / ADDITIONAL)와
    // 클라이언트 enum 이름을 모두 처리한다.
    val annotatedText =
        when (accountProcessingMethod) {
            "MEMORIAL" -> buildAnnotatedString {
                append("사망 후 ")
                withStyle(style = SpanStyle(color = B1)) { append("추모 계정") }
                append("으로 전환")
            }
            "DELETE" -> buildAnnotatedString {
                append("사망 후 ")
                withStyle(style = SpanStyle(color = B1)) { append("계정 영구 삭제") }
            }
            "TRANSFER", "RECEIVER" -> buildAnnotatedString {
                withStyle(style = SpanStyle(color = B1)) { append("수신자") }
                append("에게 정보 전달")
            }
            "ADDITIONAL" -> buildAnnotatedString {
                withStyle(style = SpanStyle(color = B1)) { append("추가 수신자") }
                append("에게 정보 전달")
            }
            else -> buildAnnotatedString {
                append(accountProcessingMethod)
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

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun SocialNetworkDetailScreenPreview() {
    AfternoteTheme {
        SocialNetworkDetailScreen(
            content = SocialNetworkDetailContent(
                accountId = "qwerty123",
                password = "qwerty123",
                accountProcessingMethod = "MEMORIAL_ACCOUNT",
                processingMethods = listOf("게시물 내리기", "추모 게시물 올리기", "추모 계정으로 전환하기"),
                message = "이 계정에는 우리 가족 여행 사진이 많아.\n계정 삭제하지 말고 꼭 추모 계정으로 남겨줘!"
            ),
            onBackClick = {},
            onEditClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false",
    name = "SocialNetworkDetailScreen with Delete Dialog"
)
@Composable
private fun SocialNetworkDetailScreenWithDeleteDialogPreview() {
    AfternoteTheme {
        val stateWithDialog = remember {
            AfternoteDetailState().apply {
                showDeleteDialog()
            }
        }
        SocialNetworkDetailScreen(
            content = SocialNetworkDetailContent(),
            onBackClick = {},
            onEditClick = {},
            state = stateWithDialog
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false",
    name = "SocialNetworkDetailScreen with Edit Dropdown Menu"
)
@Composable
private fun SocialNetworkDetailScreenWithEditDropdownMenuPreview() {
    AfternoteTheme {
        val stateWithDropdown = remember {
            AfternoteDetailState().apply {
                toggleDropdownMenu()
            }
        }
        SocialNetworkDetailScreen(
            content = SocialNetworkDetailContent(),
            onBackClick = {},
            onEditClick = {},
            state = stateWithDropdown
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false",
    name = "SocialNetworkDetailScreen - Receiver Mode (Read Only)"
)
@Composable
private fun SocialNetworkDetailScreenReceiverModePreview() {
    AfternoteTheme {
        SocialNetworkDetailScreen(
            content = SocialNetworkDetailContent(),
            isEditable = false,
            onBackClick = {},
            state = rememberAfternoteDetailState(
                defaultBottomNavItem = BottomNavItem.AFTERNOTE
            )
        )
    }
}
