package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.BottomNavItem
import com.kuit.afternote.core.ui.component.BottomNavigationBar
import com.kuit.afternote.core.ui.component.RightArrowIcon
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.feature.receiver.presentation.component.AfterNoteListItem
import com.kuit.afternote.feature.receiver.presentation.component.FilterChipItem
import com.kuit.afternote.feature.receiver.presentation.uimodel.AppNoteItem
import com.kuit.afternote.ui.theme.B1

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
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 8.dp),
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

                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(30.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.8f), Color.White),
                                startX = 0f
                            )
                        )
                )

                RightArrowIcon(
                    color = B1,
                    size = 16.dp
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8F9FA)),
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

@Preview(showBackground = true)
@Composable
fun PreviewAfterNoteList() {
    MaterialTheme {
        AfterNoteListScreen()
    }
}
