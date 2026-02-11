package com.kuit.afternote.feature.dailyrecord.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuit.afternote.core.ui.component.button.AddFloatingActionButton
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordDiaryContentItem
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordDiaryQuestionContentItem
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordListItem
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordListSort
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordSubTopbar
import com.kuit.afternote.feature.dailyrecord.presentation.uimodel.MindRecordUiModel
import com.kuit.afternote.feature.dailyrecord.presentation.viewmodel.MindRecordViewModel
import com.kuit.afternote.ui.theme.Gray1
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordQuestionScreen(
    record: MindRecordUiModel?,
    modifier: Modifier = Modifier,
    onLeftClick: () -> Unit,
    viewModel: MindRecordViewModel // ViewModel 주입
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars) // 상태바 만큼 패딩을 줘서 겹치지 않도록
        ) {
            item {
                RecordSubTopbar(
                    text = "데일리 질문 답변",
                    onLeftClock = onLeftClick,
                    onRightClick = {}
                )
            }

            item {
                RecordDiaryQuestionContentItem(
                    title = title,
                    onTitleChange = { title = it },
                    content = content,
                    onContentChange = { content = it }
                )
            }
        }
    }
}
