package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.BottomNavItem
import com.kuit.afternote.core.ui.component.BottomNavigationBar
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun TimeLetterDetailScreen() {
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.TIME_LETTER) }

    Scaffold(
        topBar = {
            TopBar(
                title = "타임레터",
                onBackClick = { }
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(all = 20.dp) // 좌우 여백 넉넉하게
        ) {
            Text(
                text = "2027년 11월 24일",
                color = Gray4,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 8.dp),
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "채연아 20번째 생일을 축하해",
                color = Gray9,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 16.dp),
                fontFamily = Sansneo
            )

            // 3. 이미지 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.6f) // 이미지 비율 (약 16:10 느낌)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray) // 실제 이미지가 없을 때 회색 배경
            ) {
                Text(
                    text = "가족 사진 영역",
                    color = Color.DarkGray,
                    modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. 본문 텍스트
            Text(
                text = "너가 태어난 게 엊그제같은데 벌써 스무살이라니..\n" +
                    "엄마가 없어도 씩씩하게 컸을 채연이를 상상하면\n" +
                    "너무 기특해서 안아주고 싶구나.\n" +
                    "20번째 생일을 축하해.",
                color = Gray9,
                fontSize = 14.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Sansneo
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimeLetterDetail() {
    MaterialTheme {
        TimeLetterDetailScreen()
    }
}
