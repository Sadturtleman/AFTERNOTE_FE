package com.kuit.afternote.feature.dailyrecord.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.Black
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun RecordDiaryQuestionContentItem(
    modifier: Modifier = Modifier,
    questionText: String,
    title: String,
    onTitleChange: (String) -> Unit,
    content: String,
    onContentChange: (String) -> Unit
) {
    Column(
        modifier = modifier.padding(top = 24.dp, start = 20.dp, end = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(start = 8.dp, bottom = 8.dp)
        ) {
            Text(
                text = questionText.ifBlank { "질문을 불러오는 중..." },
                color = Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Sansneo
            )
        }
        HorizontalDivider(color = Color.LightGray, thickness = 0.8.dp)

        BasicTextField(
            value = title,
            onValueChange = onTitleChange,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 16.dp, bottom = 8.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 18.sp,
                color = Black
            ),
            decorationBox = { innerTextField ->
                if (title.isEmpty()) {
                    Text(
                        text = "제목",
                        color = Gray5,
                        fontSize = 16.sp
                    )
                }
                innerTextField()
            }
        )

        HorizontalDivider(color = Color.LightGray, thickness = 0.8.dp)

        BasicTextField(
            value = content,
            onValueChange = onContentChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 18.sp,
                color = Black
            ),
            decorationBox = { innerTextField ->
                if (content.isEmpty()) {
                    Text(
                        text = "당신의 오늘을 기록해보세요",
                        color = Gray5,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                innerTextField()
            }
        )
    }
}
