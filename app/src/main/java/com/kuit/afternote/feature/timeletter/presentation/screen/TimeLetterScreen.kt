package com.kuit.afternote.feature.timeletter.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteMainScreen
import com.kuit.afternote.ui.theme.AfternoteTheme

@Composable
fun TimeLetterScreen(modifier: Modifier = Modifier) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(29.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.vector),
                contentDescription = "뒤로가기",
                modifier = Modifier
                    .padding(
                        start = 23.dp,
                        top= 11.03.dp
                    )
                    .width(6.dp)
                    .height(12.dp)
            )
            Text(
                text = "타임레터",
                color = Color(0xFF212121),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier= Modifier
                    .padding(start= 131.dp)
            )
        }
        Spacer(modifier = Modifier.height(18.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {

        }
    }



}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun TimeLetterScreenPreview() {
    AfternoteTheme {
        TimeLetterScreen()
    }
}
