package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9

val TextLightGray = Color(0xFF999999)

@Composable
fun TimeLetterDetailScreen() {
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.TIME_LETTER) }

    Scaffold(
        topBar = {
            TopBar(
                title = "타임레터"
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
                .verticalScroll(rememberScrollState()) // 긴 텍스트를 대비해 스크롤 가능하게 설정
                .padding(horizontal = 24.dp, vertical = 20.dp) // 좌우 여백 넉넉하게
        ) {
            // 1. 날짜
            Text(
                text = "2027년 11월 24일",
                color = TextLightGray,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 2. 제목
            Text(
                text = "채연아 20번째 생일을 축하해",
                color = Gray9,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // 3. 이미지 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.6f) // 이미지 비율 (약 16:10 느낌)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray) // 실제 이미지가 없을 때 회색 배경
            ) {
                // 실제 앱에서는 아래 주석을 해제하고 리소스 이미지를 사용하세요
                /*
                Image(
                    painter = painterResource(id = R.drawable.sample_family_photo),
                    contentDescription = "Family Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                */

                // 플레이스홀더 텍스트 (시각적 확인용, 실제 사용시 제거)
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
                fontSize = 16.sp,
                lineHeight = 26.sp, // 줄 간격을 넓혀서 가독성 확보
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(40.dp)) // 하단 여백 추가
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
