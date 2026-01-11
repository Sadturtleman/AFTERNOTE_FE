package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9


@Composable
fun TimeLetterScreen() {

    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.TIME_LETTER) }

    Scaffold(
        topBar = {
            TopBar(
                title = "타임 레터"
            ) { }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { selectedBottomNavItem = it }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp)
        ) {
            // 1. 오늘의 타임 레터 헤더
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "오늘의 타임 레터",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Gray9
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Go",
                        tint = B3
                    )
                }
            }

            // 2. 오늘의 타임 레터 카드 (Hero Image)
            item {
                TodayTimeLetterCard()
                Spacer(modifier = Modifier.height(32.dp))
            }

            // 3. 날짜 순서로 확인하기 헤더 & 카드
            item {
                Text(
                    text = "날짜 순서로 확인하기",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Gray9,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                TimeLetterActionCard(
                    desc = "고인이 작성한 편지를 날짜 순서로 확인합니다.",
                    subDesc = "9개의 레터가 있습니다.",
                    btnText = "타임 레터 확인하러 가기",
                    iconTint = Color(0xFFE3F2FD) // Light Blue tint
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            // 4. 읽지 않은 타임 레터 헤더 & 카드
            item {
                Text(
                    text = "읽지 않은 타임 레터 확인하기",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Gray9,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                TimeLetterActionCard(
                    desc = "고인이 남긴 편지 중 아직 읽지 못한 편지입니다.",
                    subDesc = "5개의 읽지 않은 타임 레터가 있습니다.",
                    btnText = "타임 레터 확인하러 가기",
                    iconTint = Color(0xFFFFF3E0) // Light Orange tint
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

// --- Components ---

@Composable
fun TodayTimeLetterCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            // Placeholder for Image (실제 이미지가 있다면 Image 컴포넌트 사용)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray)
            ) {
                // Image(painter = painterResource(id = R.drawable.your_image), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            }

            // Gradient Overlay (텍스트 가독성을 위해 아래쪽 어둡게)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 100f
                        )
                    )
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "수신인: 박채연",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "발송 예정일: 2027. 11. 24",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp
                    )
                }

                // Bottom Text
                Column {
                    Text(
                        text = "채연아 20번째 생일을 축하해",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "너가 태어난 게 엊그제같은데 벌써 스무살이라니.. 엄마가 없어도 씩씩하게 자랄 채연이를 상상하면 너무 기특해서 안아주고 싶...",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

val CardBgGradientStart = Color(0xFFFFFFFF)
val CardBgGradientEnd = Color(0xFFF0F7FF)

@Composable
fun TimeLetterActionCard(
    desc: String,
    subDesc: String,
    btnText: String,
    iconTint: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent) // Gradient를 위해 투명 처리
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(CardBgGradientStart, CardBgGradientEnd)
                    )
                )
        ) {
            // Background Decoration Icon (편지 모양)
            Icon(
                imageVector = Icons.Outlined.Email,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 20.dp, y = 20.dp) // 살짝 잘리게 배치
                    .size(120.dp)
                    .rotate(-15f)
                    .alpha(0.1f), // 투명도 조절
                tint = Color.Black
            )

            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Gray9,
                    fontSize = 15.sp
                )
                Text(
                    text = subDesc,
                    style = MaterialTheme.typography.bodySmall,
                    color = Gray6,
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
                        fontWeight = FontWeight.Medium
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


@Preview(showBackground = true)
@Composable
fun PreviewTimeLetter() {
    MaterialTheme {
        TimeLetterScreen()
    }
}
