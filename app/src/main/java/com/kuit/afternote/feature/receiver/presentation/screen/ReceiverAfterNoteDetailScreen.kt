package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.BottomNavItem
import com.kuit.afternote.core.ui.component.BottomNavigationBar
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.feature.receiver.presentation.component.CheckItem
import com.kuit.afternote.feature.receiver.presentation.component.CredentialRow
import com.kuit.afternote.feature.receiver.presentation.component.InfoCard
import com.kuit.afternote.feature.receiver.presentation.component.SectionTitle
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun AfterNoteDetailScreen() {
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.AFTERNOTE) }
    Scaffold(
        topBar = {
            TopBar(
                title = "",
                onBackClick = { }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { selectedBottomNavItem = it }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 10.dp)
        ) {
            // 1. 메인 타이틀
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = B1)) {
                        append("인스타그램")
                    }
                    append("에 대한 서연님의 기록")
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Gray9,
                fontFamily = Sansneo,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            InfoCard(
                content = {
                    Column {
                        Text(
                            text = "최종 작성일 2025.11.26.",
                            fontSize = 10.sp,
                            color = Gray6,
                            fontFamily = Sansneo,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            buildAnnotatedString {
                                append("사망 후 ")
                                withStyle(style = SpanStyle(color = B1, fontWeight = FontWeight.Bold)) {
                                    append("추모 계정")
                                }
                                append("으로 전환")
                            },
                            fontSize = 16.sp,
                            color = Gray9,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            InfoCard(
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SectionTitle("기록에 대한 개인 정보")

                        CredentialRow(label = "아이디", value = "qwerty123")
                        CredentialRow(label = "비밀번호", value = "qwerty123")
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoCard(
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SectionTitle("처리 방법")

                        CheckItem("게시물 내리기")
                        CheckItem("추모 게시물 올리기")
                        CheckItem("추모 계정으로 전환하기")
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            InfoCard(
                content = {
                    SectionTitle("남기신 말씀")

                    Text(
                        text = "이 계정에는 우리 가족 여행 사진이 많아.\n계정 삭제하지 말고 꼭 추모 계정으로 남겨줘!",
                        fontSize = 14.sp,
                        color = TextDark,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(top = 30.dp)
                    )
                }
            )
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAfterNoteDetail() {
    MaterialTheme {
        AfterNoteDetailScreen()
    }
}
