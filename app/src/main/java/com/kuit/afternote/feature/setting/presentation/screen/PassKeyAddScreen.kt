package com.kuit.afternote.feature.setting.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.setting.presentation.component.KeychainStatusCard
import com.kuit.afternote.feature.setting.presentation.uimodel.PassKey
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Sansneo
import java.time.LocalDateTime

@Composable
fun PassKeyAddScreen() {
    val passKeyList = listOf<PassKey>(
        PassKey(name = "icloud keychain", createdTime = LocalDateTime.now())
    )
    Scaffold(
        topBar = {
            TopBar("패스키 관리") {}
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "패스키 관리",
                fontWeight = FontWeight.Bold,
                fontFamily = Sansneo,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "패스키를 만들어 비밀번호 대신 지문 및 얼굴 인식으로 \n 쉽고 안전하게 로그인 할 수 있습니다.",
                fontFamily = Sansneo,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "패스키는 본인 소유의 기기에서만 사용해주세요.",
                fontFamily = Sansneo,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            HorizontalDivider(thickness = 1.dp, color = Gray2)

            Spacer(modifier = Modifier.height(40.dp))

            if (passKeyList.isEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.pass_key),
                        contentDescription = null,
                        modifier = Modifier.size(width = 320.dp, height = 260.dp)
                    )
                }
            } else {
                Text(
                    text = "패스키 목록",
                    fontFamily = Sansneo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                passKeyList.forEach { key ->
                    KeychainStatusCard(
                        title = key.name,
                        date = key.createdTime
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            ClickButton(
                color = B3,
                title = "패스키 생성",
                onButtonClick = {}
            )

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Preview
@Composable
private fun PassKeyAddScreenPreview() {
    PassKeyAddScreen()
}
