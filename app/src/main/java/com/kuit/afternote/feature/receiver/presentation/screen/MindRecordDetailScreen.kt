package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
fun MindRecordDetailScreen() {

    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.RECORD) }

    Scaffold(
        topBar = {
            TopBar(
                title = "마음의 기록"
            ) { }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { selectedBottomNavItem = it }
            )
        }, // 하단 네비게이션
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // 상단 날짜 선택 버튼 영역
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                OutlinedButton(
                    onClick = { /* 날짜 변경 로직 */ },
                    shape = RoundedCornerShape(20.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp,
                        brush = androidx.compose.ui.graphics.SolidColor(B3)
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(
                        text = "11월 10일",
                        fontSize = 13.sp,
                        color = Gray9,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = B3,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            HorizontalDivider(color = Gray6, thickness = 1.dp)

            // 리스트 영역
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                // 첫 번째 항목 (사진 + 텍스트)
                item {
                    ExpandableRecordItem(
                        date = "2025년 11월 10일",
                        tags = "#감사 #가족",
                        question = "오늘 하루, 누구에게 가장 고마웠나요?",
                        content = "아무 말 없이 내 옆을 지켜주는 남편이 너무 고맙다. 힘든 내색 없이 묵묵히 함께해주는 것만으로도 큰 힘이 된다.",
                        hasImage = true
                    )
                    HorizontalDivider(color = Gray6, thickness = 1.dp, modifier = Modifier.padding(horizontal = 20.dp))
                }

                // 두 번째 항목 (박스 + 텍스트)
                item {
                    ExpandableRecordItem(
                        date = "2025년 11월 10일",
                        tags = "#일상",
                        question = "오늘의 일기",
                        content = "오늘은 병원에서 검사 결과가 안좋게 나와서 남편이 우울해하는거 같길래 치킨을 시켜줬다.",
                        hasImage = false // 일기 박스 형태
                    )
                    HorizontalDivider(color = Gray6, thickness = 1.dp, modifier = Modifier.padding(horizontal = 20.dp))
                }
            }
        }
    }
}

@Composable
fun ExpandableRecordItem(
    date: String,
    tags: String,
    question: String,
    content: String,
    hasImage: Boolean
) {
    // 상태 관리: 펼쳐짐/닫힘
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .animateContentSize( // 애니메이션 적용
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        // --- Header (항상 보임) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = date,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = tags,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }, // 클릭 시 토글
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = question,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Gray9,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (expanded) "Collapse" else "Expand",
                tint = B3,
                modifier = Modifier.size(24.dp)
            )
        }

        // --- Body (펼쳐졌을 때만 보임) ---
        if (expanded) {
            Spacer(modifier = Modifier.height(16.dp))

            if (hasImage) {
                // 1. 이미지 카드 형태
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray) // 실제 이미지가 없을 경우 회색 배경
                ) {
                    // 실제 이미지 사용 시:
                    // Image(painter = painterResource(id = R.drawable.sample_img), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())

                    // 예시용 플레이스홀더 (그라데이션 효과 흉내)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF555555)) // 이미지 대신 어두운 배경
                    )
                }
            } else {
                // 2. 빈 박스 형태 (일기장)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(1.dp, B3, RoundedCornerShape(12.dp)) // 파란 테두리
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = content,
                fontSize = 14.sp,
                color = Gray9,
                lineHeight = 22.sp
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMindRecordDetail() {
    MaterialTheme {
        MindRecordDetailScreen()
    }
}
