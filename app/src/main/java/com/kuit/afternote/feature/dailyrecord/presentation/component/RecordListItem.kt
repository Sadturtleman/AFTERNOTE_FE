package com.kuit.afternote.feature.dailyrecord.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 미리보기 창
 */
@Composable
fun RecordListItem(
    modifier: Modifier = Modifier
        .background(Gray1)
    ,
    title: String,
    content: String,
    onRightClick: () -> Unit = {},

    ) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 36.dp,end = 36.dp, bottom = 16.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Sansneo
        )
        Box(
            modifier= Modifier
                .padding(top = 16.dp)
                .size(width = 318.dp, height = 88.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Gray2)
                .padding(start = 16.dp, top = 24.dp, end = 16.dp)

        ){
            Text(
                text = content,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Sansneo
            )
        }

        Row(
            modifier = Modifier
                //.fillMaxWidth()
                .padding(top = 12.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ){
            Text(
                text = "2026.01.03",
                fontSize = 12.sp,
                color = Gray5,
                fontWeight = FontWeight.Normal,
                fontFamily = Sansneo
            )
            Row{
                Image(
                    painter = painterResource(id = R.drawable.ic_edit_button_fore_foreground),
                    contentDescription = "수정버튼",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { /* 수정 버튼 동작 */ }
                        .offset(x = 175.dp, y = 2.dp)

                )

                Box(
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_three_jum_fore_foreground),
                        contentDescription = "더보기",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(start = 8.dp)
                            .clickable { showDialog = true }
                            .offset(x = 164.dp, y = 2.dp)

                    )
                }

                // 다이얼로그 표시
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("옵션") },
                        text = { Text("여기에 원하는 내용을 넣을 수 있어요.") },
                        confirmButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("수정하기")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("삭제하기")
                            }
                        }
                    )
                }



            }
        }
        Divider(color = Color.LightGray, thickness = 0.8.dp)
    }

}

@Preview(showBackground = true)
@Composable
private fun RecordListItemPrev () {
    Column {
        RecordListItem(
            title = "오능ㄹ",
            content = "내용"
        )
        RecordListItem(
            title = "r",
            content = "r"
        )
    }

}
