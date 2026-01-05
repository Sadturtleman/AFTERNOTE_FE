package com.kuit.afternote.feature.dailyrecord.presentation.component

import RecordCustomDateSelector
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
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.Black9
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Sansneo
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordDiaryContentItem(
    modifier: Modifier = Modifier,
    standard: String,
    onTitleChange: (String) -> Unit,
    onDateSelected: (Int, Int, Int) -> Unit
    ) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }

    //오늘 날짜 기준으로 받아오기
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
    val formattedDate = today.format(formatter)

    //아래로 화살표 누르면 날짜 선택 가능하도록
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }

    var s = " "
    if (standard.equals("일기 기록하기")) s = "작성 날짜"
    else s = "기록 주제"
    Column(
        modifier = Modifier
            .padding(top = 24.dp, start = 20.dp, end = 20.dp)

    ) {
        Text(
            text = s,
            color = Black9,
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
                if (s.equals("작성 날짜")) {
                    Text(
                        text = formattedDate,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else if (s.equals("기록 주제")) {
                    Text(
                        text = "나의 가치관",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }


                Image(
                    painter = painterResource(id = R.drawable.ic_under),
                    contentDescription = "밑 화살표",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { showDialog.value = true }
                )
                //다이얼로그
                if (showDialog.value) {
                    LaunchedEffect(Unit) {
                        android.app.DatePickerDialog(
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
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun RecordView () {
    RecordDiaryContentItem(
        standard = "일기 기록하기",
        onTitleChange = {},
        onDateSelected = { _, _, _ -> }
    )
}
