package com.kuit.afternote.feature.timeletter.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
fun WaitingAgainPopUp(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 10.dp,
                spotColor = Color(0x26000000),
                ambientColor = Color(0x26000000)
            )
            .width(350.dp)
            .background(
                color = Color(0xFFFFFFFF),
                shape = RoundedCornerShape(size = 16.dp)
            )
            .padding(start = 24.dp, top = 16.dp, end = 24.dp, bottom = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "타임레터를 등록할 수 없습니다.",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.sansneomedium)),
                fontWeight = FontWeight(500),
                color = Color(0xFF212121),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "발송일자 및 시간을 다시 한번 확인해주세요.",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.sansneolight)),
                fontWeight = FontWeight(400),
                color = Color(0xFF9E9E9E),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWaitingAgainPopUp() {
    WaitingAgainPopUp()
}
