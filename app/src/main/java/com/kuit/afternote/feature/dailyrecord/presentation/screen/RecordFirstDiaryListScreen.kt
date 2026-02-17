package com.kuit.afternote.feature.dailyrecord.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordListItem
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordListSort
import com.kuit.afternote.feature.dailyrecord.presentation.viewmodel.MindRecordViewModel
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
    onBackClick: () -> Unit,
    onPlusRecordClick: () -> Unit,
    onEditClick: (Long, String?) -> Unit,
    onBottomNavTabSelected: (BottomNavItem) -> Unit = {},
    viewModel: MindRecordViewModel // ViewModel 주입
) {
    val today = LocalDate.now()
    val records by viewModel.records.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadRecordsForDiaryList()
    }
    Scaffold(
        modifier = modifier.fillMaxWidth(),
        bottomBar = {
            BottomNavigationBar(
                selectedItem = BottomNavItem.RECORD,
                onItemSelected = { onBottomNavTabSelected(it) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onPlusRecordClick,
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
                                    Color(0xFFBDE0FF),
                                    Color(0xFFFFE1CC)
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
            modifier = Modifier.padding(paddingValues)
        ) {
            TopBar(
                title = "일기",
                onBackClick = onBackClick,
            )
            LazyColumn {
                item {
                    RecordListSort(today = today)
                }
                items(records) { record ->
                    RecordListItem(
                        record = record,
                        onDeleteClick = {
                            viewModel.deleteRecord(
                                recordId = record.id,
                                recordType = record.type ?: "DIARY",
                                onReload = { viewModel.loadRecordsForDiaryList() }
                            )
                        },
                        onEditClick = { recordId, recordType ->
                            onEditClick(recordId, recordType)
                        }
                        ) // 이제 UIModel을 그대로 넘김
                }
            }

        }
    }
}
