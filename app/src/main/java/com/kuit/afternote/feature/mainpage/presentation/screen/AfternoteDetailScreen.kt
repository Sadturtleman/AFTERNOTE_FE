package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import com.kuit.afternote.core.ui.component.BottomNavigationBar
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.feature.mainpage.presentation.component.detail.DeleteConfirmDialog
import com.kuit.afternote.feature.mainpage.presentation.component.detail.EditDropdownMenu
import com.kuit.afternote.feature.mainpage.presentation.component.detail.InfoCard
import com.kuit.afternote.feature.mainpage.presentation.component.detail.InfoRow
import com.kuit.afternote.feature.mainpage.presentation.component.detail.ProcessingMethodItem
import com.kuit.afternote.feature.mainpage.presentation.navgraph.MainPageLightTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.Spacing

/**
 * 애프터노트 상세 화면
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
 */
@Composable
fun AfternoteDetailScreen(
    modifier: Modifier = Modifier,
    serviceName: String = "인스타그램",
    userName: String = "서영",
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteConfirm: () -> Unit = {},
    state: AfternoteDetailState = rememberAfternoteDetailState()
) {
    if (state.showDeleteDialog) {
        DeleteConfirmDialog(
            serviceName = serviceName,
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
            TopBar(
                onBackClick = onBackClick,
                onEditClick = state::toggleDropdownMenu
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
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(Spacing.l))

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
                    Spacer(modifier = Modifier.height(Spacing.l))

                    InfoCard(
                        modifier = Modifier.fillMaxWidth(),
                        content = {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "최종 작성일 2025.11.26.",
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
                                        append("사망 후 ")
                                        withStyle(style = SpanStyle(color = B1)) {
                                            append("추모 계정")
                                        }
                                        append("으로 전환")
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
                                    ),
                                )
                                Spacer(modifier = Modifier.height(7.dp))

                                InfoRow(
                                    label = "아이디",
                                    value = "qwerty123"
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                InfoRow(
                                    label = "비밀번호",
                                    value = "qwerty123"
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

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

                                ProcessingMethodItem(text = "게시물 내리기")
                                Spacer(modifier = Modifier.height(6.dp))
                                ProcessingMethodItem(text = "추모 게시물 올리기")
                                Spacer(modifier = Modifier.height(6.dp))
                                ProcessingMethodItem(text = "추모 계정으로 전환하기")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

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
                                    text = "이 계정에는 우리 가족 여행 사진이 많아.\n" +
                                        "계정 삭제하지 말고 꼭 추모 계정으로 남겨줘!",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp,
                                        fontFamily = Sansneo,
                                        fontWeight = FontWeight.Normal,
                                        color = Gray9
                                    )
                                )
                            }
                        }
                    )
                }
            }

            if (state.showDropdownMenu) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { state.hideDropdownMenu() }
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-20).dp, y = 48.dp)
                ) {
                    EditDropdownMenu(
                        onEditClick = {
                            state.hideDropdownMenu()
                            onEditClick()
                        },
                        onDeleteClick = {
                            state.hideDropdownMenu()
                            state.showDeleteDialog()
                        }
                    )
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
private fun AfternoteDetailScreenPreview() {
    MainPageLightTheme {
        AfternoteDetailScreen(
            onBackClick = {},
            onEditClick = {}
        )
    }
}
