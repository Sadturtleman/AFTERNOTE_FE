package com.kuit.afternote.feature.mainpage.presentation.component.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun EditDropdownMenu(
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    val shape = RoundedCornerShape(8.dp)
    Column(
        modifier = modifier
            .width(91.dp)
            .shadow(elevation = 4.dp, shape = shape, clip = false)
            .clip(shape)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "수정하기",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight(500),
                color = Gray9,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .clickable(onClick = onEditClick)
                .padding(all = 16.dp)
        )

        HorizontalDivider(
            color = Gray3,
            thickness = 1.dp
        )

        Text(
            text = "삭제하기",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight(500),
                color = Gray9,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .clickable(onClick = onDeleteClick)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditDropdownMenuPreview() {
    AfternoteTheme {
        EditDropdownMenu()
    }
}
