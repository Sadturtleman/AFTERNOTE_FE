package com.kuit.afternote.feature.onboarding.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.feature.onboarding.presentation.component.ClickButton
import com.kuit.afternote.feature.onboarding.presentation.component.OutlineTextField
import com.kuit.afternote.feature.onboarding.presentation.component.ProfileAddAvatar
import com.kuit.afternote.feature.onboarding.presentation.component.TopBar
import com.kuit.afternote.ui.theme.B2

@Composable
fun ProfileSettingScreen(
    modifier: Modifier = Modifier,
    onFinishClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val name = rememberTextFieldState()

    Scaffold(
        topBar = {
            TopBar(
                title = "프로필 설정"
            ) {
                onBackClick()
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.weight(0.8f))

            ProfileAddAvatar {
            }

            Spacer(modifier = Modifier.height(28.dp))

            OutlineTextField(
                textFieldState = name,
                label = "이름을 지정해주세요."
            )

            Spacer(modifier = Modifier.height(8.dp))

            ClickButton(
                color = B2,
                title = "회원 가입 완료",
                onButtonClick = onFinishClick
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun ProfileSettingScreenPreview() {
    ProfileSettingScreen(
        onFinishClick = { },
        onBackClick = { }
    )
}
