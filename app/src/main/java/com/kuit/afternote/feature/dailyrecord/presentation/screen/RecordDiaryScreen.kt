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
fun RecordDiaryScreen(modifier: Modifier = Modifier) {
    Scaffold(

    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)//상태바 만큼 패딩을 줘서 겹치지 않도록

        ) {
            item {
                RecordSubTopbar(text = "일기 기록하기")
            }

            item {
                RecordDiaryContentItem(
                    standard = "일기 기록하기",
                    onTitleChange = {},
                    onDateSelected = { _, _, _ -> }
                )
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun RecordDailyScreenPrev() {
    RecordDiaryScreen()
}
