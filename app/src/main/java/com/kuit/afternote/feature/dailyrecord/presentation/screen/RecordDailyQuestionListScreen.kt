package com.kuit.afternote.feature.dailyrecord.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.button.AddFloatingActionButton
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordListItem
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordListSort
import com.kuit.afternote.feature.dailyrecord.presentation.viewmodel.MindRecordViewModel
import com.kuit.afternote.ui.theme.Gray1
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordDailyQuestionListScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onPlusRecordClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    onBottomNavTabSelected: (BottomNavItem) -> Unit = {},
    viewModel: MindRecordViewModel // ViewModel 주입
) {
    val today = LocalDate.now()
    val records by viewModel.records.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadRecords("DAILY_QUESTION")
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(color = Gray1),
        bottomBar = {
            BottomNavigationBar(
                selectedItem = BottomNavItem.RECORD,
                onItemSelected = { onBottomNavTabSelected(it) }
            )
        },
        floatingActionButton = {
            Box {
                AddFloatingActionButton(
                    onClick = onPlusRecordClick
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(top = 10.dp)
        ) {
            TopBar(
                title = "데일리 질문 답변",
                onBackClick = onBackClick,
            )
            LazyColumn {
                item {
                    RecordListSort(
                        today = today
                    )
                }
                items(records) { record ->
                    RecordListItem(
                        record = record,
                        onDeleteClick = {
                            viewModel.deleteRecord(
                                recordId = record.id,
                                recordType = record.type ?: "DAILY_QUESTION"
                            )
                        },
                        onEditClick = { recordId -> onEditClick(recordId) }
                    ) // 이제 UIModel을 그대로 넘김
                }
            }
        }
    }
}


