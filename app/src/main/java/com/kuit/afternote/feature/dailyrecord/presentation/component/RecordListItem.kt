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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.list.RecordQnAListItem
import com.kuit.afternote.feature.dailyrecord.presentation.uimodel.MindRecordUiModel
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White
import java.time.LocalDate

/**
 * 미리보기 창
 */
@Composable
fun RecordListItem(
    record: MindRecordUiModel,
    onDeleteClick: (Long) -> Unit,
    onEditClick: (Long, String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    RecordQnAListItem(
        question = record.title,
        answer = (record.content ?: record.title).ifBlank { "-" },
        dateText = record.formattedDate,
        trailing = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_three_jum_fore_foreground),
                        contentDescription = "더보기",
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
                        text = { Text("수정하기", fontFamily = Sansneo) },
                        onClick = {
                            expanded = false
                            onEditClick(record.id, record.type)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("삭제하기", fontFamily = Sansneo) },
                        onClick = {
                            expanded = false
                            onDeleteClick(record.id) //삭제 클릭 시 recordId 전달
                        }
                    )
                }
            }
        }
    )
}
