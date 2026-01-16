package com.kuit.afternote.feature.timeletter.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R


@Composable
fun DraftDeleteBottomBar(
    onDeleteAll: () -> Unit,
    onDeleteSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(52.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = Color.Black.copy(alpha = 0.15f),
                spotColor = Color.Black.copy(alpha = 0.15f)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFAFAFA)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable { onDeleteAll() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "전체 삭제",
                color = Color(0xFF212121),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily(Font(R.font.sansneomedium))
            )
        }

        Box(
            modifier = Modifier
                .border(width = 0.5.dp, color = Color(0xFFBDBDBD))
                .padding(0.5.dp)
                .width(0.dp)
                .height(16.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable { onDeleteSelected() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "선택 삭제",
                color = Color(0xFF212121),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily(Font(R.font.sansneomedium))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DraftBottomPrev() {
    DraftDeleteBottomBar(
        onDeleteAll = {},
        onDeleteSelected = {}
    )
}
