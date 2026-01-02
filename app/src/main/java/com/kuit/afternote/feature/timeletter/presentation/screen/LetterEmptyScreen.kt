package com.kuit.afternote.feature.timeletter.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.core.BottomNavigationBar
import kotlinx.coroutines.selects.select

val SansNeoRegular = FontFamily(
    Font(R.font.sansneoregular, FontWeight.Normal)
)

@Composable
fun LetterEmptyScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(29.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 23.dp, top = 5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vector),
                    contentDescription = "뒤로가기",
                    modifier = Modifier
                        .size(width = 6.dp, height = 12.dp)
                )
            }

            Text(
                text = "타임레터",
                color = Color(0xFF212121),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(
                        start = 131.dp,
                        top = 5.dp
                    )
            )
        }
        Spacer(modifier = Modifier.height(287.38.dp))
        Column {
            Image(
                painter = painterResource(id = R.drawable.letter),
                contentDescription = "편지이미지",
                modifier = Modifier
                    .padding(start = 150.5.dp)
                    .width(88.dp)
                    .height(57.57.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "아직 등록된 타임 레터가 없습니다.\n" +
                        "타임 레터를 작성하여\n" +
                        "소중한 사람에게 마음을 전하세요",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                    fontFamily = SansNeoRegular,
                    color = Color(0xFF9E9E9E),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.padding(top = 186.43.dp))
            Image(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "add",
                modifier = Modifier
                    .padding(start = 302.dp)
                    .size(56.dp)
            )
            Spacer(modifier = Modifier.padding(top = 21.dp))
            BottomNavigationBar(
                modifier = Modifier,
                selectedItem = BottomNavItem.TIME_LETTER,
                onItemSelected = { }
            )
        }
    }
}


@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
fun EmptyPrev() {
    LetterEmptyScreen()
}
