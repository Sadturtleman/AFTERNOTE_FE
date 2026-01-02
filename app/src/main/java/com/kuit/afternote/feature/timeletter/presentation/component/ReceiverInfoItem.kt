package com.kuit.afternote.feature.timeletter.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterReceiver

@Composable
fun ReceiverInfoItem(
    receiver: TimeLetterReceiver
) {
    Row (
        modifier= Modifier.
        padding(start = 20.dp, top=8.dp)
    ){
        Image(
            painter = painterResource(id = R.drawable.ic_default_profile),
            contentDescription = "default_profile",
            modifier = Modifier
                .padding(2.54.dp)
                .size(52.91.dp)
        )

        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                modifier = Modifier.padding(top = 13.dp),
                text = receiver.receiver_name,
                color = Color(0xFF000000),
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontFamily = FontFamily(
                    Font(R.font.sansneomedium, FontWeight.Medium)
                )
            )

            Text(
                text = receiver.relation,
                color = Color(0xFF424242),
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontFamily = FontFamily(
                    Font(R.font.sansneoregular, FontWeight.Normal)
                )
            )
        }
    }
}


@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false")
@Composable
private fun Receiveritemprev() {
    ReceiverInfoItem(
        receiver = TimeLetterReceiver(
        id = 1L,
        receiver_name = "김지은",
        send_at = "2025-01-01",
        title = "제목",
        content = "내용",
        image_url = null,
        relation = "딸"
    ))
}
