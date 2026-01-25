package com.kuit.afternote.feature.dailyrecord.presentation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordDiaryContentItem
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordSubTopbar

/**
 * 애프터노트 일기 기록하기 화면
 *
 * 피그마 디자인 기반 구현
 * - 상단 제목
 * - 텍스트 필드 컴포넌트
 * - 하단 FAB 버튼
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordDiaryScreen(
    modifier: Modifier = Modifier,
    onLeftClick: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    Scaffold(

    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)//상태바 만큼 패딩을 줘서 겹치지 않도록

        ) {
            item {
                RecordSubTopbar(
                    text = "일기 기록하기",
                    onLeftClock = onLeftClick,
                    onRightClick = {}
                    )
            }

            item {
                RecordDiaryContentItem(
                    standard = "일기 기록하기",
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
private fun RecordDailyScreenPrev() {
    //RecordDiaryScreen()
}
