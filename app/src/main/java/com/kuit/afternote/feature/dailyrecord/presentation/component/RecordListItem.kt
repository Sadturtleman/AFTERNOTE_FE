package com.kuit.afternote.feature.dailyrecord.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.list.RecordQnAListItem
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

/**
 * 미리보기 창
 */
@Composable
fun RecordListItem(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    onRightClick: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    RecordQnAListItem(
        modifier = modifier,
        question = title,
        answer = content,
        dateText = "2026.01.03",
        trailing = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.End)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit_button_fore_foreground),
                    contentDescription = "수정버튼",
                    modifier = Modifier.size(30.dp),
                    tint = Gray5
                )
                IconButton(
                    onClick = {
                        onRightClick()
                        expanded = true
                    },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_three_jum_fore_foreground),
                        contentDescription = "더보기",
                        modifier = Modifier.size(30.dp),
                        tint = Gray5
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    offset = DpOffset(x = 130.dp, y = 10.dp),
                    containerColor = White,
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "수정하기",
                                fontFamily = Sansneo,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
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
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
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
    )
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
