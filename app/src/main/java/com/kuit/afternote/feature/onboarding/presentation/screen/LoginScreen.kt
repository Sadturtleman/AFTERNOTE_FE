package com.kuit.afternote.feature.onboarding.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.onboarding.presentation.viewmodel.LoginUiState
import com.kuit.afternote.feature.onboarding.presentation.viewmodel.LoginViewModel
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onFindIdClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val email = rememberTextFieldState()
    val pw = rememberTextFieldState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess()
            viewModel.clearLoginSuccess()
        }
    }

    LoginScreenContent(
        modifier = modifier,
        email = email,
        pw = pw,
        uiState = uiState,
        onBackClick = onBackClick,
        onLoginClick = { emailText, passwordText ->
            viewModel.login(emailText, passwordText)
        },
        onSignUpClick = onSignUpClick,
        onFindIdClick = onFindIdClick
    )
}

@Composable
private fun LoginScreenContent(
    modifier: Modifier = Modifier,
    email: TextFieldState,
    pw: TextFieldState,
    uiState: LoginUiState,
    onBackClick: () -> Unit,
    onLoginClick: (email: String, password: String) -> Unit,
    onSignUpClick: () -> Unit,
    onFindIdClick: () -> Unit
) {
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
                label = "아이디(이메일)",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlineTextField(
                textFieldState = pw,
                label = "비밀번호",
                keyboardType = KeyboardType.Password
            )

            if (uiState.errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.errorMessage!!,
                    color = Gray9
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            ClickButton(
                title = "로그인",
                onButtonClick = {
                    onLoginClick(
                        email.text.toString().trim(),
                        pw.text.toString()
                    )
                },
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
    AfternoteTheme {
        val email = rememberTextFieldState()
        val pw = rememberTextFieldState()
        LoginScreenContent(
            email = email,
            pw = pw,
            uiState = LoginUiState(errorMessage = "로그인에 실패했습니다."),
            onBackClick = {},
            onLoginClick = { _, _ -> },
            onSignUpClick = {},
            onFindIdClick = {}
        )
    }
}
