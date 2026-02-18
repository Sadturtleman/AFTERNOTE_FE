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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordDiaryCategorySectionParams
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordDiaryContentItem
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordDiaryContentItemParams
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordDiaryDateSectionParams
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordSubTopbar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordDeepMindScreen(
    modifier: Modifier = Modifier,
    params: RecordDeepMindScreenParams
) {
    val content = params.contentState
    val date = params.dateState
    val category = params.categoryState
    Scaffold { _ ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            item {
                RecordSubTopbar(
                    text = "깊은 생각 기록하기",
                    onLeftClock = params.onLeftClick,
                    onRightClick = params.onRegisterClick
                )
            }

            item {
                RecordDiaryContentItem(
                    params = RecordDiaryContentItemParams(
                        standard = "깊은 생각 기록하기",
                        onDateSelected = { _, _, _ -> },
                        title = content.title,
                        onTitleChange = content.onTitleChange,
                        content = content.content,
                        onContentChange = content.onContentChange,
                        dateSection = RecordDiaryDateSectionParams(
                            onDateClick = {},
                            sendDate = date.sendDate,
                            showDatePicker = date.showDatePicker,
                            onDatePickerDismiss = date.onDatePickerDismiss
                        ),
                        categorySection = RecordDiaryCategorySectionParams(
                            selectedCategory = category.selectedCategory,
                            onCategoryChange = category.onCategoryChange,
                            showCategoryDropdown = category.showCategoryDropdown,
                            onCategoryClick = category.onCategoryClick,
                            onCategoryDropdownDismiss = category.onCategoryDropdownDismiss
                        )
                    )
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
