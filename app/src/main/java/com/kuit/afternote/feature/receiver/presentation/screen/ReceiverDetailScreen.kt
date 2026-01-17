package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.ClickButton
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.feature.receiver.presentation.uimodel.Receiver
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Sansneo
import java.time.LocalDate

@Composable
fun ReceiverDetailScreen(
    receiver: Receiver,
    onBackClick: () -> Unit,
    onOpenClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "받은 기록함",
                onBackClick = { onBackClick() }
            )
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.img_profile_placeholder),
                contentDescription = null,
                modifier = Modifier.size(130.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = receiver.name,
                fontSize = 32.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "기록",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Sansneo
                )

                Text(
                    text = receiver.record,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }

            HorizontalDivider(thickness = 1.dp, color = Gray4)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "상태",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Sansneo
                )

                Text(
                    text = if (receiver.state) "열람 가능" else "열람 불가",
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }

            HorizontalDivider(thickness = 1.dp, color = Gray4)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "신청일",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Sansneo
                )

                Text(
                    text = receiver.subDate.toString(),
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }

            HorizontalDivider(thickness = 1.dp, color = Gray4)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "승인일",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Sansneo
                )

                Text(
                    text = receiver.acceptDate.toString(),
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }

            HorizontalDivider(thickness = 1.dp, color = Gray4)

            Spacer(modifier = Modifier.weight(0.9f))

            ClickButton(
                color = B3,
                onButtonClick = { onOpenClick() },
                title = "열람하기"
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun ReceiverDetailScreenPreview() {
    ReceiverDetailScreen(
        Receiver(
            name = "미진이",
            acceptDate = LocalDate.now(),
            subDate = LocalDate.now(),
            record = "완료",
            state = true
        ),
        onBackClick = { }
    ) { }
}
