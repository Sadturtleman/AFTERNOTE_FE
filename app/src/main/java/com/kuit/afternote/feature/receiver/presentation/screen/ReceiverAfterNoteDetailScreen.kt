package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray2

val SectionBg = Color(0xFFF8F9FA) // 회색 박스 배경
val TagBg = Color(0xFFE3F2FD) // 태그 배경 (연한 파랑)
val TagText = Color(0xFF448AFF) // 태그 글자색 (진한 파랑)

@Composable
fun AfterNoteDetailScreen() {
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.AFTERNOTE) }
    Scaffold(
        topBar = {
            TopBar(
                title = ""
            ) { }
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
                    withStyle(style = SpanStyle(color = B2)) {
                        append("인스타그램")
                    }
                    append("에 대한 서연님의 기록")
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // 2. 상태 박스 (최종 작성일 + 추모 계정 전환)
            InfoCard(
                content = {
                    Column {
                        Text(
                            text = "최종 작성일 2025.11.26.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            buildAnnotatedString {
                                append("사망 후 ")
                                withStyle(style = SpanStyle(color = TagText, fontWeight = FontWeight.Bold)) {
                                    append("추모 계정")
                                }
                                append("으로 전환")
                            },
                            fontSize = 16.sp,
                            color = TextDark,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))

            // 3. 기록에 대한 개인 정보
            SectionTitle("기록에 대한 개인 정보")
            InfoCard(
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        CredentialRow(label = "아이디", value = "qwerty123")
                        CredentialRow(label = "비밀번호", value = "qwerty123")
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))

            // 4. 처리 방법 (체크리스트)
            SectionTitle("처리 방법")
            InfoCard(
                content = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        CheckItem("게시물 내리기")
                        CheckItem("추모 게시물 올리기")
                        CheckItem("추모 계정으로 전환하기")
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))

            // 5. 남기신 말씀
            SectionTitle("남기신 말씀")
            InfoCard(
                content = {
                    Text(
                        text = "이 계정에는 우리 가족 여행 사진이 많아.\n계정 삭제하지 말고 꼭 추모 계정으로 남겨줘!",
                        fontSize = 14.sp,
                        color = TextDark,
                        lineHeight = 22.sp
                    )
                }
            )
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// --- Components ---

@Composable
fun InfoCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Gray2),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            content()
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = TextDark,
        modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
    )
}

@Composable
fun CredentialRow(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // 라벨 태그 (타원형 배경)
        Box(
            modifier = Modifier
                .width(70.dp)
                .clip(RoundedCornerShape(50))
                .background(TagBg)
                .padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TagText
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            color = TextDark
        )
    }
}

@Composable
fun CheckItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // 체크 아이콘
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(TagText), // 파란색 배경
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            color = TextDark
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAfterNoteDetail() {
    MaterialTheme {
        AfterNoteDetailScreen()
    }
}
