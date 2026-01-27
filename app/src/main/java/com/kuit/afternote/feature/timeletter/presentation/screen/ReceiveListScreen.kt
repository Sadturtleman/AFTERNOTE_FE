package com.kuit.afternote.feature.timeletter.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import com.kuit.afternote.core.ui.component.BottomNavItem
import com.kuit.afternote.core.ui.component.BottomNavigationBar
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(44.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = 23.dp)
                        .clickable { onBackClick() }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.vector),
                        contentDescription = "뒤로가기",
                        modifier = Modifier.size(width = 6.dp, height = 12.dp)
                    )
                }
                Text(
                    text = "수신자 목록",
                    color = Color(0xFF212121),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 131.dp)
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = BottomNavItem.TIME_LETTER,
                onItemSelected = onNavItemSelected
            )
        }
    ) { innerPadding ->
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
