package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.core.ui.component.ClickButton
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun AfterNoteScreen() {
    var showDialog by remember { mutableStateOf(false) }

    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.HOME) }

    // 다이얼로그 (오른쪽 화면 구현)
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 타이틀 텍스트
                    Text(
                        text = "모든 기록을 내려받으시겠습니까?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Gray9, // 또는 Color.Black
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 버튼 영역 (Row로 배치하여 반반 나누기)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp) // 버튼 사이 간격
                    ) {
                        // '예' 버튼
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier
                                .weight(1f) // 1:1 비율
                                .height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFCBE5FF), // 이미지와 유사한 연한 파랑
                                contentColor = Color.Black
                            ),
                            elevation = ButtonDefaults.buttonElevation(0.dp)
                        ) {
                            Text(text = "예", fontWeight = FontWeight.SemiBold)
                        }

                        // '아니요' 버튼
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier
                                .weight(1f) // 1:1 비율
                                .height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE0E0E0), // 연한 회색
                                contentColor = Color.Black
                            ),
                            elevation = ButtonDefaults.buttonElevation(0.dp)
                        ) {
                            Text(text = "아니요", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
    Scaffold(
        topBar = { TopHeader() },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { selectedBottomNavItem = it }
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            // 헤더 텍스트
            item {
                Text(
                    text = "故 박서연님이 남기신 기록",
                    fontWeight = FontWeight.Bold,
                    color = Gray9,
                    fontFamily = Sansneo,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            item {
                HeroCard()
            }
            // 1. Hero Card (그라데이션 배경)

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                ContentSection(
                    title = "마음의 기록",
                    desc = "고인의 일상적인 생각과 감정, 일기들입니다.",
                    subDesc = "150개 마음의 기록이 있습니다.",
                    btnText = "마음의 기록 확인하러 가기",
                    iconPlaceholderColor = Color(0xFFE3F2FD) // Light Blue
                )
            }
            // 2. 마음의 기록 섹션


            // 3. 타임레터 섹션
            item {
                ContentSection(
                    title = "타임레터",
                    desc = "고인이 특별한 날에 작성한 편지입니다.",
                    subDesc = "30개 라이프 이벤트 레터가 있습니다.",
                    btnText = "라이프 이벤트 레터 확인하러 가기",
                    iconPlaceholderColor = Color(0xFFFFF3E0) // Light Orange
                )
            }


            // 4. 애프터노트 섹션
            item {
                ContentSection(
                    title = "애프터노트",
                    desc = "고인이 사후 정리하고자 하는 데이터입니다.",
                    subDesc = "10개의 애프터노트가 있습니다.",
                    btnText = "애프터노트 확인하러 가기",
                    iconPlaceholderColor = Color(0xFFF3E5F5) // Light Purple
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }


            // 5. 하단 버튼 (다운로드)
            item {
                ClickButton(
                    color = B3,
                    onButtonClick = {
                        showDialog = true
                    },
                    title = "모든 기록 내려받기"
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

        }
    }
}

// --- Components ---

@Composable
fun TopHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "AFTERNOTE",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User",
                modifier = Modifier.size(24.dp),
                tint = Gray9
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                modifier = Modifier.size(24.dp),
                tint = Gray9
            )
        }
    }
}

@Composable
fun HeroCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFD0E4FF), // Light Blue
                        Color(0xFFF0F4F8), // White-ish
                        Color(0xFFFFE0CC)  // Light Peach
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.CenterStart)
        ) {
            Text(
                text = "가족들에게...\n내가 없어도 너무 슬퍼하지마.",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                lineHeight = 28.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "고인이 남긴 마지막 인사말",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ContentSection(
    title: String,
    desc: String,
    subDesc: String,
    btnText: String,
    iconPlaceholderColor: Color
) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Flat style as per image
        ) {
            Box {
                // Background decoration (Simulating the 3D icons in the image)
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 10.dp, y = 10.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(iconPlaceholderColor.copy(alpha = 0.8f), Color.Transparent)
                            )
                        )
                )

                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = desc,
                        fontSize = 16.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Medium,
                        color = Gray9
                    )
                    Text(
                        text = subDesc,
                        fontSize = 12.sp,
                        fontFamily = Sansneo,
                        color = Gray9,
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = B3),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            text = btnText,
                            fontSize = 12.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium,
                            fontFamily = Sansneo
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAfterNote() {
    MaterialTheme {
        AfterNoteScreen()
    }
}
