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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.button.AddFloatingActionButton
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordListItem
import com.kuit.afternote.feature.dailyrecord.presentation.component.RecordListSort
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray1
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordDailyQuestionScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onPlusRecordClick: () -> Unit
) {
    val today = LocalDate.now()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(color = Gray1),
        bottomBar = {
            BottomNavigationBar(
                selectedItem = BottomNavItem.HOME,
                onItemSelected = { }
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
                item {
                    RecordListItem(
                        title = "가",
                        content = "나"
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun RecordDailyQuestionScreenPreview() {
    AfternoteTheme {
        RecordDailyQuestionScreen(
            onBackClick = {},
            onPlusRecordClick = {}
        )
    }
}
