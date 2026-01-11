package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.ui.theme.Gray6


// --- Data Model ---
data class AppNoteItem(
    val name: String,
    val date: String,
    val icon: ImageVector,
    val iconColor: Color,
    val iconBgColor: Color
)

@Composable
fun AfterNoteListScreen() {
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.AFTERNOTE) }

    // 필터 카테고리 데이터
    val categories = listOf("전체", "소셜네트워크", "비즈니스", "갤러리 및 클라우드")
    var selectedCategory by remember { mutableStateOf("전체") }

    // 리스트 데이터 (더미)
    val noteItems = listOf(
        AppNoteItem("인스타그램", "최종 작성일 2025.11.26.", Icons.Outlined.Image, Color.White, Color(0xFFE1306C)), // Instagram Pink
        AppNoteItem("갤러리", "최종 작성일 2025.11.26.", Icons.Outlined.Image, Color.Gray, Color(0xFFEEEEEE)), // Gallery Gray
        AppNoteItem("카카오톡", "최종 작성일 2025.11.26.", Icons.Filled.Phone, Color(0xFF381E1F), Color(0xFFFEE500)), // Kakao Yellow
        AppNoteItem("네이버 메일", "최종 작성일 2025.11.26.", Icons.Filled.MailOutline, Color.White, Color(0xFF03C75A)) // Naver Green
    )

    Scaffold(
        topBar = {
            TopBar(
                title = "애프터노트"
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
        ) {
            // 1. 상단 카테고리 필터 (Horizontal Scroll)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChipItem(
                        text = category,
                        isSelected = category == selectedCategory,
                        onClick = { selectedCategory = category }
                    )
                }
            }

            Divider(thickness = 8.dp, color = Color(0xFFF8F9FA)) // 섹션 구분용 두꺼운 회색 선

            // 2. 애프터노트 리스트
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8F9FA)), // 전체 배경 회색 처리
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(noteItems) { item ->
                    AfterNoteListItem(item)
                }
            }
        }
    }
}

// --- Components ---

@Composable
fun FilterChipItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) PrimaryBlue else Gray6)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else TextGray,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun AfterNoteListItem(item: AppNoteItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp), // 높이 고정
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Flat design
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 아이콘 박스
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(item.iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = item.iconColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 텍스트 정보
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.date,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            // 화살표 아이콘
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Detail",
                tint = PrimaryBlue
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAfterNoteList() {
    MaterialTheme {
        AfterNoteListScreen()
    }
}
