package com.kuit.afternote.feature.dailyrecord.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordDiaryContentItem
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordSubTopbar
import com.kuit.afternote.feature.dailyrecord.presentation.viewmodel.DailyRecordViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordDeepMindScreen(
    modifier: Modifier = Modifier,
    onLeftClick: () -> Unit,
    viewModel: DailyRecordViewModel
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
                    text = "깊은 생각 기록하기",
                    onLeftClock = onLeftClick,
                    onRightClick = {
                        viewModel.createPost(title, content)
                    }
                )
            }

            item {
                RecordDiaryContentItem(
                    standard = "깊은 생각 기록하기",
                    onDateSelected = { _, _, _ -> },
                    title = title,
                    onTitleChange = { title = it },
                    content = content,
                    onContentChange = { content = it }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun RecordDeepMindPrev() {
    // RecordDeepMindScreen()
}
