package com.kuit.afternote.feature.onboarding.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kuit.afternote.feature.onboarding.presentation.component.ClickButton
import com.kuit.afternote.feature.onboarding.presentation.component.OutlineTextField
import com.kuit.afternote.feature.onboarding.presentation.component.ProfileAddAvatar
import com.kuit.afternote.feature.onboarding.presentation.component.TopBar
import com.kuit.afternote.ui.theme.B3

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
            modifier = modifier
        ) {
            Spacer(modifier = Modifier.weight(0.8f))

            ProfileAddAvatar {

            }

            OutlineTextField(
                textFieldState = name,
                label = "이름을 지정해주세요."
            )

            ClickButton(
                color = B3,
                title = "회원 가입 완료",
                onButtonClick = onFinishClick
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun ProfileSettingScreenPreview(){
    ProfileSettingScreen(
        onFinishClick = {},
        onBackClick = {}
    )
}
