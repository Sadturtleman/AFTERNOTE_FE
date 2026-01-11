package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray6

// --- Colors ---
val PrimaryBlue = Color(0xFFA3C9FF)
val TextDark = Color(0xFF1A1A1A)
val TextGray = Color(0xFF666666)
val LightBlueBg = Color(0xFFEAF4FF) // 프로필 배경색

@Composable
fun AfterNoteMainScreen(title: String) {

    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.TIME_LETTER) }

    Scaffold(
        topBar = {
            TopBar("故${title}님의 애프터노트") { }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { selectedBottomNavItem = it }
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp)
        ) {
            // 1. 프로필 영역
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(B3.copy(alpha = 0.3f)), // 연한 파랑 배경
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(60.dp),
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "故 박서연님의 애프터노트입니다.",
                        color = Gray6,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            // 2. 추모 플레이리스트
            item {
                SectionHeader(title = "추모 플레이리스트")

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "현재 16개의 노래가 담겨 있습니다.",
                        fontSize = 12.sp,
                        color = TextGray
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "More",
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(4) { // 가짜 데이터 4개
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray) // 앨범 아트 플레이스홀더
                        ) {
                            // 실제 이미지 사용 시:
                            // Image(painter = ..., contentScale = ContentScale.Crop)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            // 3. 남기고 싶은 당부 (Message Bubble)
            item {
                SectionHeader(title = "남기고 싶은 당부")
                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    // 텍스트 박스
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp), // 아이콘 공간 확보
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue),
                        color = Color.White
                    ) {
                        Text(
                            text = "끼니 거르지 말고 건강 챙기고 지내.",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    // 데코레이션 아이콘 (꽃/별 모양)
                    Icon(
                        imageVector = Icons.Default.Star, // 적절한 꽃 아이콘 리소스가 없다면 Star 사용
                        contentDescription = null,
                        tint = Color(0xFFCCFF90), // 연두색
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-20).dp, y = 0.dp) // 박스 위로 살짝 걸치게
                            .size(24.dp)
                            .background(Color.White, CircleShape) // 아이콘 뒤 배경 가림
                            .border(1.dp, Color(0xFFEEEEEE), CircleShape) // 아이콘 테두리
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            // 4. 장례식에 남길 영상
            item {
                SectionHeader(title = "장례식에 남길 영상")
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray), // 영상 썸네일 플레이스홀더
                    contentAlignment = Alignment.Center
                ) {
                    // 플레이 버튼 아이콘
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                            .padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            // 5. 하단 버튼
            item {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text(
                        text = "애프터노트 전체 확인하기",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White // 이미지상 텍스트는 흰색 또는 검정, 여기선 흰색 적용
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

// --- Helper Components ---

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = TextDark,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewAfterNoteMain() {
    MaterialTheme {
        AfterNoteMainScreen("박서연")
    }
}
