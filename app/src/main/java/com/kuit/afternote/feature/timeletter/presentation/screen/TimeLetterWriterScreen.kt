package com.kuit.afternote.feature.timeletter.presentation.screen

import android.R.attr.icon
import android.R.color.white
import android.R.id.icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.WhitePoint
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
    Column {
        Row(
            modifier = Modifier
                .height(43.dp)
                .fillMaxWidth()
        ) {
            Image(
                painterResource(R.drawable.vending),
                contentDescription = "자판기",
                modifier = Modifier
                    .padding(start = 24.dp, top = 10.dp)
                    .width(20.dp)
                    .height(22.dp)
            )
            Text(
                text = "박채연님께",
                modifier = Modifier.padding(start = 100.dp, end = 10.dp, top = 9.dp)
            )
            Image(
                painter = painterResource(R.drawable.ic_down),
                contentDescription = "아래 열기",
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "등록",
                color = Color(0xFF9E9E9E),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.sansneoregular)),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp,
                modifier = Modifier.padding(start = 100.dp, top = 10.dp)
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            color = Color(0xFF9E9E9E),
            thickness = 0.4.dp
        )


        Spacer(modifier = Modifier.padding(top = 24.dp))

        Row(
            modifier = Modifier
                .height(62.dp)
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Column(
                modifier = Modifier.width(228.dp)
            ) {
                Text(
                    text = "발송 날짜",
                    color = Color(0xFF000000),
                    fontFamily = FontFamily(Font(R.font.sansneoregular)),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 18.sp
                )
                Image(
                    painterResource(R.drawable.ic_down_vector),
                    "아래 벡터",
                    modifier = Modifier
                        .padding(start = 208.dp, top = 15.dp)
                        .width(8.dp)
                        .height(4.dp)
                )

                Divider(
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .width(228.dp),
                    color = Color(0xFF9E9E9E),
                    thickness = 0.4.dp
                )
            }

            Column(
                modifier = Modifier.width(106.dp).padding(start = 26.dp)
            ) {
                Text(
                    text = "발송 시간",
                    color = Color(0xFF000000),
                    fontFamily = FontFamily(Font(R.font.sansneoregular)),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 18.sp
                )
                Image(
                    painterResource(R.drawable.ic_down_vector),
                    "아래 벡터",
                    modifier = Modifier
                        .padding(start = 86.dp, top = 15.dp)
                        .width(8.dp)
                        .height(4.dp)
                )
                Divider(
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .width(106.dp),
                    color = Color(0xFF9E9E9E),
                    thickness = 0.4.dp
                )
            }
        }
        Column(modifier = Modifier
            .width(350.dp)
            .height(50.dp)
            .padding(start = 20.dp)
        ) {
            Text(
                text = "제목"
            )
            Divider(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .width(350.dp),
                color = Color(0xFF9E9E9E),
                thickness = 0.4.dp
            )
        }
        Spacer(modifier= Modifier.padding(top=37.dp))
        LazyColumn {
            item {
                Text(
                    text = "소중한 사람에게 타임 레터를 작성하세요.",
                    color = Color(0xFF9E9E9E),
                    fontFamily = FontFamily(Font(R.font.sansneoregular)),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun TimeLetterWriterPrev() {
    TimeLetterWriterScreen()
}
