package com.kuit.afternote.feature.dailyrecord.presentation.screen

import android.icu.util.LocaleData
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordListItem
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordListSort
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordMainTopbar
import com.kuit.afternote.feature.mainpage.presentation.model.AfternoteTab
import java.time.LocalDate

/**
 * 일기, 깊은 생각하기로 넘어가면 먼저뜨는 리스트형 창
 * - 상단 : 제목
 * - 중간 : 리스트
 * - 하단 : FAB 바
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordFirstDiaryListScreen(
    modifier: Modifier = Modifier,
    onLeftClick: () -> Unit,
    onPlusRecordClick: () -> Unit,
) {
    var selectedTab by remember { mutableStateOf(AfternoteTab.ALL) }
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.HOME) }
    val today = LocalDate.now()

    Scaffold(
        modifier = Modifier
            .fillMaxWidth(),
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { selectedBottomNavItem = it }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onPlusRecordClick ,
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

            ) {
            RecordMainTopbar(
                text = "일기",
                showLeftArrow = true,
                onLeftClock = onLeftClick,

            )
            LazyColumn{
                item {
                    RecordListSort(
                        today = today
                    )
                }
                item {
                    RecordListItem(
                        title = "오늘 하루, 누구에게 가장 고마웠나요?",
                        content = "아무 말 없이 그저 나의 곁을 지켜주는 아내가 너무 고맙다."
                    )
                }
                item {
                    RecordListItem(
                        title = "다",
                        content = "라"
                    )
                }
                item {
                    RecordListItem(
                        title = "마",
                        content = "바"
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun RePrev() {
//    RecordFirstDiaryListScreen(
//        onLeftClick = {},
//        onPlusRecordClick = {}
//    )
//}
