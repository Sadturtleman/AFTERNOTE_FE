package com.kuit.afternote.feature.dailyrecord.presentation.component

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.Black
import com.kuit.afternote.ui.theme.Gray3
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Sansneo
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val DIARY_STANDARD_TEXT = "일기 기록하기"
private const val DATE_LABEL_TEXT = "작성 날짜"
private const val TOPIC_LABEL_TEXT = "기록 주제"

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordDiaryContentItem(
    modifier: Modifier = Modifier,
    standard: String,
    onDateSelected: (Int, Int, Int) -> Unit,
    title: String,
    onTitleChange: (String) -> Unit,
    content: String,
    onContentChange: (String) -> Unit
) {
    // 오늘 날짜 기준으로 받아오기
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
    val formattedDate = today.format(formatter)

    // 아래로 화살표 누르면 날짜 선택 가능하도록
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }

    val isDiary = standard == DIARY_STANDARD_TEXT
    val labelText = if (isDiary) DATE_LABEL_TEXT else TOPIC_LABEL_TEXT
    val displayText = if (isDiary) formattedDate else "나의 가치관"

    Column(
        modifier = modifier
            .padding(top = 24.dp, start = 20.dp, end = 20.dp)
    ) {
        Text(
            text = labelText,
            color = Black,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Sansneo
        )

        Box(
            modifier = Modifier
                .padding(all = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayText,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_under),
                    contentDescription = "밑 화살표",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { showDialog.value = true }
                )

                // 다이얼로그
                if (showDialog.value) {
                    LaunchedEffect(Unit) {
                        DatePickerDialog(
                                context,
                                R.style.SpinnerDatePickerStyle,
                                { _, year, month, dayOfMonth ->
                                    onDateSelected(year, month + 1, dayOfMonth)
                                    showDialog.value = false
                                },
                                today.year,
                                today.monthValue - 1,
                                today.dayOfMonth
                            ).show()
                    }
                }
            }
            HorizontalDivider(
                color = Gray3,
                thickness = 1.dp
            )
        }

        BasicTextField(
            value = title,
            onValueChange = onTitleChange,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 16.dp, bottom = 8.dp),
            textStyle = TextStyle(
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

        HorizontalDivider(
            color = Gray3,
            thickness = 1.dp
        )

        BasicTextField(
            value = content,
            onValueChange = onContentChange,
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            textStyle = TextStyle(
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
