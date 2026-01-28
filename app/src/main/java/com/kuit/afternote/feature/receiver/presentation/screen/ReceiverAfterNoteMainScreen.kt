package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.BottomNavItem
import com.kuit.afternote.core.ui.component.BottomNavigationBar
import com.kuit.afternote.core.ui.component.ClickButton
import com.kuit.afternote.core.ui.component.RightArrowIcon
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

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
            TopBar(
                title = "故${title}님의 애프터노트",
                onBackClick = { }
            )
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
                .padding(20.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            // 1. 프로필 영역
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.img_profile_placeholder),
                        contentDescription = null,
                        modifier = Modifier.size(140.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            item {
                Text(
                    text = "故 ${title}님의 애프터노트입니다.",
                    color = Gray9,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Sansneo
                )
                Spacer(modifier = Modifier.height(30.dp))
            }
            item {
                SectionHeader(title = "추모 플레이리스트")
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "현재 16개의 노래가 담겨 있습니다.",
                            fontSize = 14.sp,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Normal
                        )
                        RightArrowIcon(
                            color = B1,
                            size = 16.dp
                        )
                    }

                    LazyRow(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(4) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.LightGray)
                            ) {
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
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
                            color = B1,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            fontFamily = Sansneo
                        )
                    }
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
                        .background(Color.LightGray),
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
                ClickButton(
                    color = B3,
                    title = "애프터노트 전체 확인하기",
                    onButtonClick = {}
                )
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
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        color = Gray9,
        fontFamily = Sansneo,
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
