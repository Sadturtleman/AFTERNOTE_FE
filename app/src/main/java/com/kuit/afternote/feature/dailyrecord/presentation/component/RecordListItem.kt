package com.kuit.afternote.feature.dailyrecord.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

/**
 * 미리보기 창
 */
@Composable
fun RecordListItem(
    modifier: Modifier = Modifier
        .background(Gray1),
    title: String,
    content: String,
    onRightClick: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 10.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Sansneo
                )
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(width = 318.dp, height = 88.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Gray2)
                        .padding(start = 16.dp, top = 24.dp, end = 16.dp)
                ) {
                    Text(
                        text = content,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = Sansneo
                    )
                }

                Row(
                    modifier = Modifier
                        // .fillMaxWidth()
                        .padding(top = 7.dp, bottom = 11.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "2026.01.03",
                        fontSize = 12.sp,
                        color = Gray5,
                        fontWeight = FontWeight.Normal,
                        fontFamily = Sansneo
                    )

                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.End) // 4dp 간격
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_edit_button_fore_foreground),
                                contentDescription = "수정버튼",
                                modifier = Modifier.size(30.dp),
                                tint = Gray5
                            )
                            IconButton(
                                onClick = { expanded = true },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_three_jum_fore_foreground),
                                    contentDescription = "더보기",
                                    modifier = Modifier.size(30.dp),
                                    tint = Gray5
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            offset = DpOffset(x = 130.dp, y = 10.dp), // 원하는 만큼 이동
                            containerColor = White,
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "수정하기",
                                        fontFamily = Sansneo,
                                        fontWeight = FontWeight.Medium
                                    )
                                },
                                onClick = {
                                    expanded = false
                                },
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "삭제하기",
                                        fontFamily = Sansneo,
                                        fontWeight = FontWeight.Medium
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    println("삭제")
                                }
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .padding(horizontal = 20.dp)
        ) {
            Divider(color = Color.LightGray, thickness = 0.8.dp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecordListItemPrev() {
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
