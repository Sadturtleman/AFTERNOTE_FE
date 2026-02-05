package com.kuit.afternote.feature.timeletter.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterReceiver
import com.kuit.afternote.feature.timeletter.presentation.component.ReceiverInfoItem
import com.kuit.afternote.feature.timeletter.presentation.component.chosungGroupedItems
import com.kuit.afternote.feature.timeletter.presentation.component.groupByChosung

/**
 * 수신자 목록 화면
 *
 * @param receivers 수신자 목록
 * @param onBackClick 뒤로가기 클릭 콜백
 * @param onNavItemSelected 하단 네비게이션 아이템 선택 콜백
 * @param modifier Modifier
 */
@Composable
fun ReceiveListScreen(
    receivers: List<TimeLetterReceiver>,
    onBackClick: () -> Unit = {},
    onNavItemSelected: (BottomNavItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // 초성별로 그룹화
    val groupedReceivers = groupByChosung(receivers) { it.receiver_name }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(44.dp)
            ) {
                // 1. 왼쪽 뒤로가기 버튼 영역
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 23.dp)
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center // 아이콘을 박스 중앙에 배치
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_vector),
                        contentDescription = "뒤로가기",
                        modifier = Modifier.size(width = 6.dp, height = 12.dp)
                    )
                }

                // 2. 중앙 타이틀 영역 (부모 Box의 중앙에 위치)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "수신자 목록",
                        color = Color(0xFF212121),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = BottomNavItem.TIME_LETTER,
                onItemSelected = onNavItemSelected
            )
        }
    ) { innerPadding ->
        // 초성별 그룹화된 수신자 리스트 (스크롤 가능)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            chosungGroupedItems(groupedReceivers) { receiver ->
                ReceiverInfoItem(receiver = receiver)
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun ReceiveListPreview() {
    val testReceivers = listOf(
        TimeLetterReceiver(1L, "김지은", "2025-01-01", "제목", "내용", null, "딸"),
        TimeLetterReceiver(2L, "김혜성", "2025-01-01", "제목", "내용", null, "아들"),
        TimeLetterReceiver(3L, "박채연", "2025-01-01", "제목", "내용", null, "조카"),
        TimeLetterReceiver(4L, "황은주", "2025-01-01", "제목", "내용", null, "언니"),
        TimeLetterReceiver(5L, "황은경", "2025-01-01", "제목", "내용", null, "동생"),
    )
    ReceiveListScreen(
        receivers = testReceivers,
        onBackClick = {},
        onNavItemSelected = {}
    )
}
