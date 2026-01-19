package com.kuit.afternote.feature.dailyrecord.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.Black
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun RecordDiaryQuestionContentItem(
    modifier: Modifier = Modifier,

) {
    var isEditing by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding(top = 24.dp, start = 20.dp, end = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(start = 8.dp, bottom = 8.dp)
        ) {
            Text(
                text = "오늘 하루, 누구에게 가장 고마웠나요?",
                color = Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Sansneo
            )
        }
        Divider(color = Color.LightGray, thickness = 0.8.dp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 16.dp, bottom = 8.dp)
                .clickable { isEditing = true } // 클릭하면 편집 모드로 전환
        ) {
            if (isEditing) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            } else {
                Text(
                    text = "제목",
                    color = Gray5,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        Divider(color = Color.LightGray, thickness = 0.8.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .clickable { isEditing = true } // 클릭하면 편집 모드로 전환
        ) {
            if (isEditing) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            } else {
                Text(
                    text = "당신의 오늘을 기록해보세요",
                    color = Gray5,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
