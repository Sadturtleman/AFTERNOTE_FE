package com.kuit.afternote.feature.onboarding.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.feature.onboarding.presentation.component.ClickButton
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray6

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onStartClick: () -> Unit,
    onCheckClick: () -> Unit
) {
    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.weight(0.3f))

            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 40.dp, height = 50.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "나의 만약이, 남겨진 이들의\n막막함이 되지 않도록",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = Color.Blue
                        )
                    ) {
                        append("애프터노트")
                    }
                    append("는 당신의 디지털 세상을\n 미리 정리해 가장 소중한 사람들에게\n 안전하고 따뜻한 다리를 제공합니다.")
                },
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            ClickButton(
                color = B2,
                onButtonClick = onStartClick,
                title = "시작하기"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ClickButton(
                color = B3,
                onButtonClick = onCheckClick,
                title = "전달 받은 기록 확인하기"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "이미 가입하셨나요?",
                    color = Gray6
                )

                Spacer(modifier = Modifier.width(11.dp))

                Text(
                    text = "로그인하기",
                    color = Gray6,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable {
                            onLoginClick()
                        }
                )
            }

            Spacer(modifier = Modifier.weight(0.3f))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SplashScreen(
        onLoginClick = {},
        onStartClick = {},
        onCheckClick = {}
    )
}
