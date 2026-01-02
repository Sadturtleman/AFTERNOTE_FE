package com.kuit.afternote.feature.timeletter.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R

@Composable
fun TimeLetterWriterScreen(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .height(43.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.padding(start = 24.dp)
        ) {
            Image(
                painterResource(R.drawable.vending),
                contentDescription = "자판기"
            )
        }
        Box(
            modifier = Modifier.padding(start = 100.dp, end = 100.dp)
        ) {
            Text(
                text = "박채연님께"
            )
            Image(
                painter = painterResource(R.drawable.ic_down),
                contentDescription = "아래 열기"
            )
            Text(
                text = "텍스트",
                color = Color(0xFF9E9E9E),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.sansneoregular)),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp
            )
        }

    }
    Row(
        modifier = Modifier.padding(20.dp)
    ) {
        Text(
            text = "발송 날짜",
            color = Color(0xFF000000),
            fontFamily = FontFamily(Font(R.font.sansneoregular)),
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 18.sp
        )
        Text(
            text = "발송 시간",
            color = Color(0xFF000000),
            fontFamily = FontFamily(Font(R.font.sansneoregular)),
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 18.sp
        )
    }
    Spacer(modifier = Modifier.padding(top = 24.dp))
    Row(
        modifier = Modifier.padding(start = 221.dp)
    ) {
        Image(
            painterResource(R.drawable.ic_down_vector),
            "아래 벡터",
            modifier = Modifier
                .width(8.dp)
                .height(4.dp)
        )
        Image(
            painterResource(R.drawable.ic_down_vector),
            "아래 벡터",
            modifier = Modifier
                .padding(start = 110.dp)
                .width(8.dp)
                .height(4.dp)
        )
    }


    LazyColumn { }
}

@Preview
@Composable
private fun TimeLetterWriterPrev() {
    TimeLetterWriterScreen()
}
