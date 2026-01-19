package com.kuit.afternote.feature.dailyrecord.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Gray5

@Composable
fun RecordListSort(modifier: Modifier = Modifier) {
    var isClicked by remember { mutableStateOf(true) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 28.dp, top = 8.dp ),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(40.dp))
                .size(96.dp, 36.dp)
                .background(Gray2),
                contentAlignment = Alignment.Center // 가운데 정렬
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_list_fore_foreground),
                    contentDescription = "리스트 정렬",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { isClicked = !isClicked } ,
                    colorFilter = if (isClicked) {
                        ColorFilter.tint(Color(0xFF328BFF))   // 클릭되었을 때 빨간색
                    } else {
                        ColorFilter.tint(Color.Gray)  // 기본 색상
                    }
                )
                Text(
                    text = "|",
                    color = Gray5,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_calendar_fore_foreground),
                    contentDescription = "달력 선택 정렬",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { isClicked = !isClicked },
                    colorFilter = if (!isClicked) {
                        ColorFilter.tint(Color(0xFF328BFF))   // 클릭되었을 때 빨간색
                    } else {
                        ColorFilter.tint(Color.Gray)  // 기본 색상
                    }
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun RecordListSortPrev() {
    RecordListSort()
}
