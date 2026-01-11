package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.core.ui.component.ClickButton
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9


@Composable
fun MindRecordScreen() {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(10) } // 10일 선택됨
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.RECORD) }
    // 날짜 선택 다이얼로그
    if (showDatePicker) {
        WheelDatePickerDialog(
            onDismiss = { showDatePicker = false },
            onConfirm = { showDatePicker = false }
        )
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "마음의 기록",
            ) { }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { selectedBottomNavItem = it }
            )
        }, // 이전 코드의 BottomNavBar 재사용
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            // 1. 오늘의 기록 섹션
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "오늘의 기록",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Go",
                            tint = B3
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    TodayRecordCard()
                }
            }

            // 2. 날짜별 기록 섹션 (캘린더)
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "날짜별 기록",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // 캘린더 컨테이너
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            // 년월 선택 버튼
                            OutlinedButton(
                                onClick = { showDatePicker = true },
                                shape = RoundedCornerShape(20.dp),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    width = 1.dp,
                                    brush = SolidColor(B3)
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(
                                    text = "2025년 11월",
                                    fontSize = 12.sp,
                                    color = Gray9,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // 캘린더 그리드
                            CalendarGrid(selectedDay = selectedDate, onDaySelected = { selectedDate = it })
                        }
                    }
                }
            }

            // 3. 하단 리스트 아이템
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
                    RecordListItem(date = "2025년 11월 10일", content = "오늘 하루, 누구에게 가장 고마웠나요?")
                    Spacer(modifier = Modifier.height(12.dp))
                    RecordListItem(date = "2025년 11월 10일", content = "오늘의 일기")

                    Spacer(modifier = Modifier.height(24.dp))

                    // 확인하기 버튼
                    ClickButton(
                        color = B3,
                        title = "확인하기",
                        onButtonClick = {}
                    )
                }
            }
        }
    }
}

// --- Components ---

@Composable
fun TodayRecordCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            // Placeholder Image (실제 이미지가 없으므로 회색 배경으로 대체하거나 로컬 리소스를 사용하세요)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray)
            ) {
                // 실제 앱에서는 아래 코드를 사용하세요
                // Image(painter = painterResource(id = R.drawable.your_image), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            }

            // Dark Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        )
                    )
            )

            // Content
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "#감사 #가족",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "2025년 11월 10일",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "오늘 하루, 누구에게 가장 고마웠나요?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun CalendarGrid(selectedDay: Int, onDaySelected: (Int) -> Unit) {
    // 11월 가상 데이터 (1일이 토요일이라고 가정하고 패딩 추가)
    val days = (1..30).toList()
    val paddingDays = 6 // 앞쪽 공백 (일~금)

    // LazyVerticalGrid를 Box나 고정 높이 없이 사용하면 Scroll 충돌이 날 수 있으므로
    // 여기서는 간단하게 Column + Row 조합이나, 높이를 계산해서 사용합니다.
    // 간단한 구현을 위해 커스텀 Grid 로직 사용

    val totalSlots = paddingDays + days.size
    val rows = (totalSlots + 6) / 7

    Column {
        for (row in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (col in 0 until 7) {
                    val index = row * 7 + col
                    val day = index - paddingDays + 1

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (day in 1..30) {
                            val isSelected = day == selectedDay
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) B1 else Color.Transparent)
                                    .clickable { onDaySelected(day) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    color = if (isSelected) Color.White else if (day > 25) Color.LightGray else Gray9, // 예시 로직
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecordListItem(date: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp) // 살짝 그림자
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = date,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Gray9
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                fontSize = 14.sp,
                color = Gray6
            )
        }
    }
}

// --- Wheel Date Picker Dialog (Custom Implementation) ---
@Composable
fun WheelDatePickerDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Selection Overlay (파란 테두리 박스)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(horizontal = 20.dp)
                            .border(1.dp, B3, RoundedCornerShape(8.dp))
                    )
                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Year Wheel
                    WheelColumn(items = listOf("2024", "2025", "2026"), initialIndex = 1, width = 100.dp)
                    // Divider
                    Box(modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(Gray6))
                    // Month Wheel
                    WheelColumn(items = listOf("10", "11", "12"), initialIndex = 1, width = 80.dp)
                }

                // 확인 버튼 (이미지에는 없지만 닫기 위해 필요하거나 터치로 닫음)
                // 여기서는 시각적 요소만 구현
            }
        }
    }
}

@Composable
fun WheelColumn(items: List<String>, initialIndex: Int, width: Dp) {
    // 실제 휠 로직은 복잡하므로 간단한 리스트로 시각적 구현
    LazyColumn(
        modifier = Modifier
            .width(width)
            .height(150.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 50.dp) // 중앙 정렬 효과
    ) {
        items(items) { item ->
            Text(
                text = item,
                fontSize = 20.sp,
                color = if (item == "2025" || item == "11") Gray9 else Color.LightGray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewMindRecord() {
    MindRecordScreen()
}
