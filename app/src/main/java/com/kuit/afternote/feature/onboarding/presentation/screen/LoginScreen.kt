package com.kuit.afternote.feature.onboarding.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.ClickButton
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray6

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onFindIdClick: () -> Unit,
) {
    val email = rememberTextFieldState()
    val pw = rememberTextFieldState()

    Scaffold(
        topBar = {
            TopBar(
                title = "로그인",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.15f))

            OutlineTextField(
                textFieldState = email,
                label = "아이디(이메일)"
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlineTextField(
                textFieldState = pw,
                label = "비밀번호"
            )

            Spacer(modifier = Modifier.height(24.dp))

            ClickButton(
                title = "로그인",
                onButtonClick = onLoginClick,
                color = B2
            )

            Spacer(modifier = Modifier.weight(1f))

            ClickButton(
                title = "간편 회원가입하기",
                onButtonClick = onSignUpClick,
                color = B3
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "아이디/비밀번호 찾기",
                color = Gray6,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable {
                        onFindIdClick()
                    }
            )

            Spacer(modifier = Modifier.weight(0.3f))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        onBackClick = {},
        onFindIdClick = {},
        onLoginClick = {},
        onSignUpClick = {}
    )
}
