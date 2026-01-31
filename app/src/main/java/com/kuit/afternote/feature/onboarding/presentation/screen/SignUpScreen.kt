package com.kuit.afternote.feature.onboarding.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.core.ui.component.button.SignUpContentButton
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.onboarding.presentation.component.IdentifyInputContent
import com.kuit.afternote.feature.onboarding.presentation.component.PhoneAuthContent
import com.kuit.afternote.feature.onboarding.presentation.component.PwInputContent
import com.kuit.afternote.feature.onboarding.presentation.component.SignUpEndContent
import com.kuit.afternote.feature.onboarding.presentation.uimodel.SignUpStep
import com.kuit.afternote.feature.onboarding.presentation.util.PasswordValidator
import com.kuit.afternote.feature.onboarding.presentation.viewmodel.SendEmailCodeUiState
import com.kuit.afternote.feature.onboarding.presentation.viewmodel.SendEmailCodeViewModel
import com.kuit.afternote.feature.onboarding.presentation.viewmodel.VerifyEmailUiState
import com.kuit.afternote.feature.onboarding.presentation.viewmodel.VerifyEmailViewModel
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

private data class SignUpFieldStates(
    val phone: TextFieldState,
    val authCode: TextFieldState,
    val memberCode: TextFieldState,
    val pw: TextFieldState,
    val pwRe: TextFieldState
)

@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    onSettingClick: (email: String, password: String) -> Unit,
    sendEmailCodeViewModel: SendEmailCodeViewModel = hiltViewModel(),
    verifyEmailViewModel: VerifyEmailViewModel = hiltViewModel()
) {
    val fieldStates = SignUpFieldStates(
        phone = rememberTextFieldState(),
        authCode = rememberTextFieldState(),
        memberCode = rememberTextFieldState(),
        pw = rememberTextFieldState(),
        pwRe = rememberTextFieldState()
    )

    var step by remember { mutableStateOf(SignUpStep.PHONE_AUTH) }
    val sendEmailCodeUiState by sendEmailCodeViewModel.uiState.collectAsStateWithLifecycle()
    val verifyEmailUiState by verifyEmailViewModel.uiState.collectAsStateWithLifecycle()
    var isAuthCodeEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(sendEmailCodeUiState.errorMessage) {
        if (sendEmailCodeUiState.errorMessage != null) {
            isAuthCodeEnabled = false
        }
    }

    LaunchedEffect(sendEmailCodeUiState.sendSuccess) {
        if (sendEmailCodeUiState.sendSuccess) {
            sendEmailCodeViewModel.clearSendSuccess()
            // Field is already enabled from optimistic update
        }
    }

    LaunchedEffect(verifyEmailUiState) {
        if (verifyEmailUiState.verifySuccess) {
            verifyEmailViewModel.clearVerifySuccess()
            step = SignUpStep.IDENTIFY_INPUT
        }
    }

    SignUpScreenContent(
        step = step,
        fieldStates = fieldStates,
        isAuthCodeEnabled = isAuthCodeEnabled,
        sendEmailCodeUiState = sendEmailCodeUiState,
        verifyEmailUiState = verifyEmailUiState,
        onBackClick = {
            step.previous()?.let {
                step = it
            } ?: onBackClick()
        },
        onSettingClick = onSettingClick,
        onSendEmailCodeClick = { email ->
            // Optimistic UI update: enable field immediately
            isAuthCodeEnabled = true
            sendEmailCodeViewModel.sendEmailCode(email)
        },
        onVerifyEmailClick = { email, code ->
            verifyEmailViewModel.verifyEmail(email, code)
        },
        onStepChange = { step = it }
    )
}

@Composable
private fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    step: SignUpStep,
    fieldStates: SignUpFieldStates,
    isAuthCodeEnabled: Boolean,
    sendEmailCodeUiState: SendEmailCodeUiState,
    verifyEmailUiState: VerifyEmailUiState,
    onBackClick: () -> Unit,
    onSettingClick: (email: String, password: String) -> Unit,
    onSendEmailCodeClick: (email: String) -> Unit,
    onVerifyEmailClick: (email: String, code: String) -> Unit,
    onStepChange: (SignUpStep) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "회원 가입",
                onBackClick = onBackClick,
                step = step
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                when (step) {
                    SignUpStep.PHONE_AUTH -> {
                        SignUpContentButton(
                            onNextClick = {
                                val emailText =
                                    fieldStates.phone.text
                                        .toString()
                                        .trim()
                                val codeText =
                                    fieldStates.authCode.text
                                        .toString()
                                        .trim()

                                android.util.Log.d("SignUpScreen", "다음 버튼 클릭: email=$emailText, code=$codeText")

                                if (emailText.isBlank() || codeText.isBlank()) {
                                    android.util.Log.d("SignUpScreen", "이메일 또는 인증번호가 비어있음")
                                    return@SignUpContentButton
                                }

                                android.util.Log.d("SignUpScreen", "verifyEmail 호출")
                                onVerifyEmailClick(emailText, codeText)
                            }
                        ) {
                            Text(
                                text = "이메일",
                                fontSize = 16.sp,
                                fontFamily = Sansneo,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            PhoneAuthContent(
                                phone = fieldStates.phone,
                                authCode = fieldStates.authCode,
                                onAuthClick = {
                                    val emailText =
                                        fieldStates.phone.text
                                            .toString()
                                            .trim()
                                    onSendEmailCodeClick(emailText)
                                },
                                isAuthCodeEnabled = isAuthCodeEnabled
                            )

                            if (sendEmailCodeUiState.errorMessage != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = sendEmailCodeUiState.errorMessage!!,
                                    color = Gray9,
                                    fontSize = 14.sp,
                                    fontFamily = Sansneo
                                )
                            }

                            if (sendEmailCodeUiState.sendSuccess) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "인증번호가 발송되었습니다.",
                                    color = Gray9,
                                    fontSize = 14.sp,
                                    fontFamily = Sansneo
                                )
                            }

                            if (verifyEmailUiState.errorMessage != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = verifyEmailUiState.errorMessage!!,
                                    color = Gray9,
                                    fontSize = 14.sp,
                                    fontFamily = Sansneo
                                )
                            }
                        }
                    }

                    SignUpStep.IDENTIFY_INPUT -> {
                        SignUpContentButton(
                            onNextClick = { onStepChange(SignUpStep.PW_INPUT) }
                        ) {
                            Text(
                                text = "주민등록번호",
                                fontSize = 16.sp,
                                fontFamily = Sansneo,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            IdentifyInputContent(
                                fieldStates.memberCode
                            )
                        }
                    }

                    SignUpStep.PW_INPUT -> {
                        SignUpContentButton(
                            onNextClick = {
                                val passwordText =
                                    fieldStates.pw.text
                                        .toString()
                                        .trim()
                                val passwordConfirmText =
                                    fieldStates.pwRe.text
                                        .toString()
                                        .trim()

                                val passwordError = PasswordValidator.validate(passwordText)
                                val isPasswordMatch = PasswordValidator.matches(
                                    passwordText,
                                    passwordConfirmText
                                )

                                if (passwordError == null && isPasswordMatch) {
                                    onStepChange(SignUpStep.END)
                                }
                            }
                        ) {
                            Text(
                                text = "비밀번호 입력",
                                fontSize = 16.sp,
                                fontFamily = Sansneo,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            PwInputContent(
                                pw = fieldStates.pw,
                                pwRe = fieldStates.pwRe
                            )
                        }
                    }

                    SignUpStep.END -> {
                        SignUpEndContent {
                            onSettingClick(
                                fieldStates.phone.text
                                    .toString()
                                    .trim(),
                                fieldStates.pw.text.toString()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SignUpScreenPreview() {
    AfternoteTheme {
        SignUpScreenContent(
            step = SignUpStep.PHONE_AUTH,
            fieldStates = SignUpFieldStates(
                phone = rememberTextFieldState(),
                authCode = rememberTextFieldState(),
                memberCode = rememberTextFieldState(),
                pw = rememberTextFieldState(),
                pwRe = rememberTextFieldState()
            ),
            isAuthCodeEnabled = true,
            sendEmailCodeUiState = SendEmailCodeUiState(sendSuccess = true),
            verifyEmailUiState = VerifyEmailUiState(),
            onBackClick = {},
            onSettingClick = { _, _ -> },
            onSendEmailCodeClick = {},
            onVerifyEmailClick = { _, _ -> },
            onStepChange = {}
        )
    }
}
