package com.kuit.afternote.feature.timeletter.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R

@Composable
fun TimeLetterWritingPopUp(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .shadow(elevation = 10.dp, spotColor = Color(0x26000000), ambientColor = Color(0x26000000))
            .width(350.dp)
            .height(196.dp)
            .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 16.dp))
            .padding(start = 24.dp, top = 32.dp, end = 24.dp, bottom = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "타임레터를 작성 중입니다.\n작성 도중 취소시면 레터는 저장되지 않습니다.\n작성을 취소할까요?",
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = FontFamily(Font(R.font.sansneomedium)),
                fontWeight = FontWeight(500),
                color = Color(0xFF212121),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.padding(top = 20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 11.dp)
            ) {
                Box(
                    modifier = Modifier
                        .shadow(elevation = 5.dp, spotColor = Color(0x0D000000), ambientColor = Color(0x0D000000))
                        .width(136.dp)
                        .height(46.dp)
                        .background(color = Color(0xFFE0E0E0), shape = RoundedCornerShape(size = 8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "아니요"
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .shadow(elevation = 5.dp, spotColor = Color(0x0D000000), ambientColor = Color(0x0D000000))
                        .width(136.dp)
                        .height(46.dp)
                        .background(color = Color(0xFFE0E0E0), shape = RoundedCornerShape(size = 8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "예"
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun PopUpPreview() {
    TimeLetterWritingPopUp()
}
