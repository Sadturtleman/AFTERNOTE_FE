package com.kuit.afternote.feature.onboarding.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.kuit.afternote.feature.onboarding.presentation.viewmodel.SendEmailCodeViewModel
import com.kuit.afternote.feature.onboarding.presentation.viewmodel.VerifyEmailViewModel
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    onSettingClick: (email: String, password: String) -> Unit,
    sendEmailCodeViewModel: SendEmailCodeViewModel = hiltViewModel(),
    verifyEmailViewModel: VerifyEmailViewModel = hiltViewModel()
) {
    val phone = rememberTextFieldState()
    val authCode = rememberTextFieldState()
    val memberCode = rememberTextFieldState()
    val pw = rememberTextFieldState()
    val pwRe = rememberTextFieldState()

    var step by remember { mutableStateOf(SignUpStep.PHONE_AUTH) }
    val sendEmailCodeUiState by sendEmailCodeViewModel.uiState.collectAsStateWithLifecycle()
    val verifyEmailUiState by verifyEmailViewModel.uiState.collectAsStateWithLifecycle()
    var isAuthCodeEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                title = "회원 가입",
                onBackClick = {
                    step.previous()?.let {
                        step = it
                    } ?: onBackClick()
                },
                step = step
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
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
                                val emailText = phone.text.toString().trim()
                                val codeText = authCode.text.toString().trim()

                                android.util.Log.d("SignUpScreen", "다음 버튼 클릭: email=$emailText, code=$codeText")

                                if (emailText.isBlank() || codeText.isBlank()) {
                                    android.util.Log.d("SignUpScreen", "이메일 또는 인증번호가 비어있음")
                                    return@SignUpContentButton
                                }

                                android.util.Log.d("SignUpScreen", "verifyEmail 호출")
                                verifyEmailViewModel.verifyEmail(emailText, codeText)
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
                                phone = phone,
                                authCode = authCode,
                                onAuthClick = {
                                    val emailText = phone.text.toString().trim()
                                    // Optimistic UI update: enable field immediately
                                    isAuthCodeEnabled = true
                                    sendEmailCodeViewModel.sendEmailCode(emailText)
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

                            // Disable field if API call fails (rollback optimistic update)
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

                            LaunchedEffect(verifyEmailUiState) {
                                if (verifyEmailUiState.verifySuccess) {
                                    android.util.Log.d("SignUpScreen", "인증번호 검증 성공, 다음 단계로 이동")
                                    verifyEmailViewModel.clearVerifySuccess()
                                    step = SignUpStep.IDENTIFY_INPUT
                                }
                            }
                        }
                    }

                    SignUpStep.IDENTIFY_INPUT -> {
                        SignUpContentButton(
                            onNextClick = { step = SignUpStep.PW_INPUT }
                        ) {
                            Text(
                                text = "주민등록번호",
                                fontSize = 16.sp,
                                fontFamily = Sansneo,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            IdentifyInputContent(
                                memberCode
                            )
                        }
                    }

                    SignUpStep.PW_INPUT -> {
                        SignUpContentButton(
                            onNextClick = {
                                val passwordText = pw.text.toString().trim()
                                val passwordConfirmText = pwRe.text.toString().trim()

                                val passwordError = PasswordValidator.validate(passwordText)
                                val isPasswordMatch = PasswordValidator.matches(
                                    passwordText,
                                    passwordConfirmText
                                )

                                if (passwordError == null && isPasswordMatch) {
                                    step = SignUpStep.END
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
                                pw = pw,
                                pwRe = pwRe
                            )
                        }
                    }

                    SignUpStep.END -> {
                        SignUpEndContent {
                            onSettingClick(
                                phone.text.toString().trim(),
                                pw.text.toString()
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
    SignUpScreen(onBackClick = {}, onSettingClick = { _, _ -> })
}
