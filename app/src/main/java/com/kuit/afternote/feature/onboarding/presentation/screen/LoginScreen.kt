package com.kuit.afternote.feature.onboarding.presentation.screen

import android.content.Context
import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.LoginUiMode
import com.kuit.afternote.BuildConfig
import com.kuit.afternote.R
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
    val context = LocalContext.current
    val email = rememberTextFieldState()
    val pw = rememberTextFieldState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val kakaoSdkNotInitializedMessage = stringResource(R.string.login_kakao_sdk_not_initialized)
    val kakaoFailedMessage = stringResource(R.string.login_kakao_failed)

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess()
            viewModel.clearLoginSuccess()
        }
    }

    val callbacks = LoginScreenContentCallbacks(
        onBackClick = onBackClick,
        onLoginClick = { emailText, passwordText -> viewModel.login(emailText, passwordText) },
        onKakaoLoginClick = {
            handleKakaoLogin(
                context = context,
                viewModel = viewModel,
                sdkNotInitializedMessage = kakaoSdkNotInitializedMessage,
                failedMessage = kakaoFailedMessage
            )
        },
        onSignUpClick = onSignUpClick,
        onFindIdClick = onFindIdClick
    )

    LoginScreenContent(
        modifier = modifier,
        email = email,
        pw = pw,
        uiState = uiState,
        callbacks = callbacks
    )
}

/**
 * Runs Kakao SDK login and forwards result to [viewModel]. Call from Composable.
 */
private fun handleKakaoLogin(
    context: Context,
    viewModel: LoginViewModel,
    sdkNotInitializedMessage: String,
    failedMessage: String
) {
    if (!KakaoSdk.isInitialized) {
        Log.e("KakaoLogin", "KakaoSdk is NOT initialized. Check KAKAO_NATIVE_APP_KEY/local.properties.")
        viewModel.setErrorMessage(sdkNotInitializedMessage)
        return
    }
    UserApiClient.instance.loginWithKakao(
        context = context,
        uiMode = LoginUiMode.AUTO
    ) { token, error ->
        if (error != null) {
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                Log.e("KakaoLogin", "User cancelled Kakao login.")
                return@loginWithKakao
            }
            Log.e("KakaoLogin", "Kakao SDK login failed. type=${error::class.java.name}", error)
            viewModel.setErrorMessage(error.message ?: failedMessage)
            return@loginWithKakao
        }
        val accessToken = token?.accessToken
        if (accessToken.isNullOrBlank()) {
            Log.e("KakaoLogin", "Kakao SDK returned empty accessToken.")
            viewModel.setErrorMessage(failedMessage)
            return@loginWithKakao
        }
        if (BuildConfig.DEBUG) {
            Log.d("KakaoLogin", "Kakao accessToken=$accessToken")
        }
        Log.d("KakaoLogin", "Kakao SDK login success. Calling /auth/social/login.")
        viewModel.kakaoLogin(accessToken)
    }
}

/** Callbacks for [LoginScreenContent]. Groups events to keep parameter count ≤7. */
private data class LoginScreenContentCallbacks(
    val onBackClick: () -> Unit,
    val onLoginClick: (email: String, password: String) -> Unit,
    val onKakaoLoginClick: () -> Unit,
    val onSignUpClick: () -> Unit,
    val onFindIdClick: () -> Unit
)

@Composable
private fun LoginScreenContent(
    modifier: Modifier = Modifier,
    email: TextFieldState,
    pw: TextFieldState,
    uiState: LoginUiState,
    callbacks: LoginScreenContentCallbacks
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "로그인",
                onBackClick = callbacks.onBackClick
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

            val errorText = uiState.errorMessage?.takeIf { it.isNotBlank() }
            if (errorText != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorText,
                    color = Gray9
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            ClickButton(
                title = "로그인",
                onButtonClick = {
                    callbacks.onLoginClick(
                        email.text.toString().trim(),
                        pw.text.toString()
                    )
                },
                color = B2
            )

            Spacer(modifier = Modifier.height(12.dp))

            ClickButton(
                title = stringResource(id = R.string.login_kakao_button),
                onButtonClick = callbacks.onKakaoLoginClick,
                color = B3
            )

            Spacer(modifier = Modifier.weight(1f))

            ClickButton(
                title = "간편 회원가입하기",
                onButtonClick = callbacks.onSignUpClick,
                color = B3
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "아이디/비밀번호 찾기",
                color = Gray6,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { callbacks.onFindIdClick() }
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
        val callbacks = LoginScreenContentCallbacks(
            onBackClick = {},
            onLoginClick = { _, _ -> },
            onKakaoLoginClick = {},
            onSignUpClick = {},
            onFindIdClick = {}
        )
        LoginScreenContent(
            email = email,
            pw = pw,
            uiState = LoginUiState(errorMessage = "로그인에 실패했습니다."),
            callbacks = callbacks
        )
    }
}
