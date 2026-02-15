package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.icon.RightArrowIcon
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverTimeLetterUiState
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun TimeLetterScreen(
    uiState: ReceiverTimeLetterUiState,
    onBackClick: () -> Unit,
    onLetterClick: (ReceivedTimeLetter) -> Unit,
    onBottomNavSelected: (BottomNavItem) -> Unit,
    onSortByDate: () -> Unit,
    onSortByUnread: () -> Unit,
    showBottomBar: Boolean = true
) {
    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                TopBar(
                    title = "타임 레터",
                    onBackClick = { onBackClick() }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    selectedItem = uiState.selectedBottomNavItem,
                    onItemSelected = onBottomNavSelected
                )
            }
        }
    ) { innerPadding ->
        when {
            uiState.isLoading && uiState.timeLetters.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null && uiState.timeLetters.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.errorMessage,
                        color = Gray9,
                        fontFamily = Sansneo
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp)
                ) {
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
                                color = Gray9,
                                fontFamily = Sansneo
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "이동",
                                tint = B3
                            )
                        }
                    }

                    item {
                        TodayTimeLetterCard(
                            letter = uiState.timeLetters.firstOrNull(),
                            onLetterClick = onLetterClick
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    item {
                        Text(
                            text = "날짜 순서로 확인하기",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Gray9,
                            modifier = Modifier.padding(bottom = 16.dp),
                            fontFamily = Sansneo
                        )
                        TimeLetterActionCard(
                            desc = "고인이 작성한 편지를 날짜 순서로 확인합니다.",
                            subDesc = "${uiState.totalCount}개의 레터가 있습니다.",
                            btnText = "타임 레터 확인하러 가기",
                            onClick = onSortByDate
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    item {
                        Text(
                            text = "읽지 않은 타임 레터 확인하기",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Gray9,
                            modifier = Modifier.padding(bottom = 16.dp),
                            fontFamily = Sansneo
                        )
                        TimeLetterActionCard(
                            desc = "고인이 남긴 편지 중 아직 읽지 못한 편지입니다.",
                            subDesc = "0개의 읽지 않은 타임 레터가 있습니다.",
                            btnText = "타임 레터 확인하러 가기",
                            onClick = onSortByUnread
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    items(
                        items = uiState.timeLetters,
                        key = { it.timeLetterId }
                    ) { letter ->
                        TimeLetterListItem(
                            letter = letter,
                            onClick = { onLetterClick(letter) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

// --- Components ---

@Composable
fun TodayTimeLetterCard(
    letter: ReceivedTimeLetter? = null,
    onLetterClick: (ReceivedTimeLetter) -> Unit = {}
) {
    val enabled = letter != null
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .then(
                if (enabled) Modifier.clickable { letter?.let(onLetterClick) }
                else Modifier
            ),
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "발신인: ${letter?.senderName ?: "-"}",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "발송 예정일: ${letter?.sendAt ?: "-"}",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp
                    )
                }

                Column {
                    Text(
                        text = letter?.title ?: "타임 레터가 없습니다",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = letter?.content?.take(80)?.plus("...").orEmpty().ifEmpty { " " },
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

@Composable
private fun TimeLetterListItem(
    letter: ReceivedTimeLetter,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = letter.title.orEmpty().ifEmpty { "제목 없음" },
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = Gray9,
                    fontFamily = Sansneo,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = letter.sendAt.orEmpty(),
                    fontSize = 12.sp,
                    color = Gray6,
                    fontFamily = Sansneo,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "상세 보기",
                tint = B3
            )
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
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent) // Gradient를 위해 투명 처리
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(CardBgGradientStart, CardBgGradientEnd)
                    )
                )
        ) {
            // Background Decoration Icon (편지 모양)
            Image(
                painter = painterResource(R.drawable.img_timeletter),
                contentDescription = "편지 장식",
                modifier = Modifier
                    .align(Alignment.BottomEnd) // 살짝 잘리게 배치
                    .size(width = 160.dp, height = 100.dp)
            )

            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = desc,
                    fontWeight = FontWeight.Medium,
                    color = Gray9,
                    fontSize = 15.sp,
                    fontFamily = Sansneo
                )
                Text(
                    text = subDesc,
                    fontFamily = Sansneo,
                    color = Gray6,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                Button(
                    onClick = onClick,
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
                    RightArrowIcon(B1)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimeLetter() {
    MaterialTheme {
        val sampleState = ReceiverTimeLetterUiState(
            timeLetters = listOf(
                ReceivedTimeLetter(
                    timeLetterId = 1L,
                    title = "채연아 20번째 생일을 축하해",
                    content = "너가 태어난 게 엊그제같은데 벌써 스무살이라니..",
                    sendAt = "2027. 11. 24",
                    status = "DRAFT",
                    senderName = "박채연",
                    deliveredAt = null,
                    createdAt = null
                )
            ),
            totalCount = 1
        )
        TimeLetterScreen(
            uiState = sampleState,
            onBackClick = { },
            onLetterClick = { },
            onBottomNavSelected = { },
            onSortByDate = {},
            onSortByUnread = {},
        )
    }
}
