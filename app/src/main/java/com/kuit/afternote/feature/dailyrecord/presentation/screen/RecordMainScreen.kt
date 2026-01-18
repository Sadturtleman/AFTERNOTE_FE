package com.kuit.afternote.feature.dailyrecord.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.kuit.afternote.core.ui.component.BottomNavItem
import com.kuit.afternote.core.ui.component.BottomNavigationBar
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordItem
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordMainTopbar
import com.kuit.afternote.ui.theme.Gray1

/**
 * 에프터노트 마음기록 메인 화면
 *
 * 피그마 디자인 기반으로 구현
 * - 상단 텍스트
 * - 마음기록 리스트
 * - 하단 FAB 버튼
 */
@Composable
fun RecordMainScreen(
    modifier: Modifier = Modifier,
    onDiaryClick: () -> Unit,
    onDeepMindClick: () -> Unit,
    onWeekendReportClick: () -> Unit,
    onQuestionClick: () -> Unit
) {

    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.RECORD) }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Gray1,
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { selectedBottomNavItem = it }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: 새 애프터노트 생성 */ },
                modifier = Modifier.size(56.dp),
                containerColor = Color.Transparent,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFBDE0FF), // B3
                                    Color(0xFFFFE1CC) // 주황색 계열
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "새 애프터노트 추가",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)//상태바 만큼 패딩을 줘서 겹치지 않도록
        ) {
            //샹단 제목
            RecordMainTopbar(modifier = modifier,
                "나의 모든 기록",
                showLeftArrow = false
            )
            //리스트
            val allItems = listOf(
                "데일리 질문답변" to "매일 다른 질문들에 나를 남겨보세요",
                "일기" to "나의 매일을 기록하세요.",
                "깊은 생각" to "오늘은 무엇을 생각하고 있나요?",
                "주간 리포트" to "이번주 나의 기록들을 확인해보세요."
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 8.dp,
                    end = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)

            ) {
                items(allItems){ (title, subtitle) ->
                    RecordItem(
                        title = title,
                        subtitle = subtitle,
                        modifier = Modifier.clickable {
                            when (title) {
                                "데일리 질문답변" -> onQuestionClick()
                                "일기" -> onDiaryClick()
                                "깊은 생각" -> onDeepMindClick()
                                "주간 리포트" -> onWeekendReportClick()
                            }
                        }
                    )
                }
            }
        }



    }
}

@Preview(
    showBackground = true
)
@Composable
private fun RecordMainScreenPrev() {

}
