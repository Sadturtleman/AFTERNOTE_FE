package com.kuit.afternote.feature.timeletter.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.feature.timeletter.presentation.screen.DraftLetterItem

@Composable
fun DraftLetterListItem(
    item: DraftLetterItem,
    isEditMode: Boolean,
    isSelected: Boolean,
    onItemClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp)
        ) {
            // 편집 모드 체크 아이콘
            if (isEditMode) {
                Image(
                    painter = painterResource(
                        if (isSelected) R.drawable.ic_check_circle else R.drawable.ic_draft_circle
                    ),
                    contentDescription = if (isSelected) "선택됨" else "선택 안됨",
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.width(16.dp))
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // 수신인 & 발송 예정일
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "수신인: ${item.receiverName}",
                        color = Color(0xFF757575),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily(Font(R.font.sansneomedium))
                    )
                    Text(
                        text = "발송 예정일: ${item.sendDate}",
                        color = Color(0xFF757575),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily(Font(R.font.sansneomedium))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.title,
                    color = Color(0xFF212121),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                    fontFamily = FontFamily(Font(R.font.sansneoregular))
                )
            }
        }

        Divider(
            modifier = Modifier.padding(start = if (isEditMode) 48.dp else 16.dp),
            color = Color(0xFFE0E0E0),
            thickness = 0.5.dp
        )
    }
}

@Preview(showBackground = true, name = "일반 모드")
@Composable
private fun DraftLetterListItemPreview() {
    DraftLetterListItem(
        item = DraftLetterItem(
            id = "1",
            receiverName = "김지은",
            sendDate = "2029. 11. 20",
            title = "지은아 결혼을 축하해"
        ),
        isEditMode = false,
        isSelected = false,
        onItemClick = {}
    )
}

@Preview(showBackground = true, name = "편집 모드")
@Composable
private fun DraftLetterListItemSelectedPreview() {
    DraftLetterListItem(
        item = DraftLetterItem(
            id = "1",
            receiverName = "김지은",
            sendDate = "2029. 11. 20",
            title = "지은아 결혼을 축하해"
        ),
        isEditMode = true,
        isSelected = true,
        onItemClick = {}
    )
}

@Preview(showBackground = true, name = "편집 모드")
@Composable
private fun DraftLetterListItemUnSelectedPreview() {
    DraftLetterListItem(
        item = DraftLetterItem(
            id = "1",
            receiverName = "김지은",
            sendDate = "2029. 11. 20",
            title = "지은아 결혼을 축하해"
        ),
        isEditMode = true,
        isSelected = false,
        onItemClick = {}
    )
}
