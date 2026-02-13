package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.receiver.presentation.component.OtpInputField
import com.kuit.afternote.feature.receiver.presentation.uimodel.VerifySelfStep
import com.kuit.afternote.feature.receiver.presentation.viewmodel.FakeVerifyReceiverViewModel
import com.kuit.afternote.feature.receiver.presentation.viewmodel.VerifyReceiverUiState
import com.kuit.afternote.feature.receiver.presentation.viewmodel.VerifyReceiverViewModel
import com.kuit.afternote.feature.receiver.presentation.viewmodel.VerifyReceiverViewModelContract
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun VerifyReceiverScreen(
    onBackClick: () -> Unit,
    onVerifySuccess: () -> Unit,
    viewModel: VerifyReceiverViewModelContract = hiltViewModel<VerifyReceiverViewModel>()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val emailState = rememberTextFieldState()
    var authCode by remember { mutableStateOf("") }

    LaunchedEffect(uiState.verifySuccess) {
        if (uiState.verifySuccess) {
            onVerifySuccess()
            viewModel.clearVerifySuccess()
        }
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                TopBar(
                    title = stringResource(R.string.receiver_verify_title),
                    onBackClick = {
                        uiState.step.previous()?.let { viewModel.setStep(it) } ?: onBackClick()
                    },
                    step = uiState.step
                )
            }
        }
    ) { paddingValues ->
        AuthBaseScreen(
            paddingValues = paddingValues,
            buttonContent = {
                val (title, action, enabled) = when (uiState.step) {
                    VerifySelfStep.START -> Triple(
                        stringResource(R.string.receiver_verify_start_button),
                        { viewModel.onStartAuth() },
                        true
                    )
                    VerifySelfStep.EMAIL_AUTH -> Triple(
                        stringResource(R.string.receiver_verify_next_button),
                        { viewModel.onSendCode(emailState.text.toString().trim()) },
                        emailState.text.isNotEmpty()
                    )
                    VerifySelfStep.EMAIL_CODE -> Triple(
                        stringResource(R.string.receiver_verify_confirm_button),
                        { viewModel.onVerifyCode(emailState.text.toString().trim(), authCode) },
                        authCode.length == 6
                    )
                }

                ClickButton(
                    title = title,
                    onButtonClick = action,
                    isTrue = enabled && !uiState.isLoading
                )
            }
        ) {
            when (uiState.step) {
                VerifySelfStep.START -> {
                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = stringResource(R.string.receiver_verify_self_title),
                        fontSize = 24.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(15.dp))
                    Text(
                        text = stringResource(R.string.receiver_verify_intro),
                        fontSize = 16.sp,
                        color = Gray6,
                        fontFamily = Sansneo
                    )
                }
                VerifySelfStep.EMAIL_AUTH -> {
                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = stringResource(R.string.receiver_verify_self_title),
                        fontSize = 24.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(15.dp))
                    Text(
                        text = stringResource(R.string.receiver_verify_email_description),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Gray6,
                        fontFamily = Sansneo
                    )
                    Spacer(Modifier.height(25.dp))
                    OutlineTextField(
                        textFieldState = emailState,
                        label = stringResource(R.string.receiver_verify_email_label),
                        keyboardType = KeyboardType.Uri
                    )
                }
                VerifySelfStep.EMAIL_CODE -> {
                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = stringResource(R.string.receiver_verify_self_title),
                        fontSize = 24.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(15.dp))
                    Text(
                        text = stringResource(R.string.receiver_verify_code_description),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Gray6,
                        fontFamily = Sansneo
                    )
                    Spacer(Modifier.height(25.dp))
                    OtpInputField(
                        otpText = authCode,
                        onOtpTextChange = { newValue, _ -> authCode = newValue }
                    )
                }
            }
            uiState.errorMessage?.let { message ->
                Spacer(Modifier.height(16.dp))
                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = Gray6,
                    fontFamily = Sansneo
                )
            }
        }
    }
}

/**
 * 재사용 가능한 인증 화면 템플릿
 * - content: 상단에 배치될 텍스트나 입력 필드
 * - buttonContent: 화면 정중앙에 배치될 버튼
 */
@Composable
private fun AuthBaseScreen(
    paddingValues: PaddingValues,
    buttonContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 20.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.TopStart)) {
            content()
        }
        Box(modifier = Modifier.align(Alignment.Center)) {
            buttonContent()
        }
    }
}

@Preview(showBackground = true, name = "START")
@Composable
private fun VerifyReceiverScreenStartPreview() {
    AfternoteTheme {
        VerifyReceiverScreen(
            onBackClick = {},
            onVerifySuccess = {},
            viewModel = remember { FakeVerifyReceiverViewModel() }
        )
    }
}

@Preview(showBackground = true, name = "EMAIL_AUTH")
@Composable
private fun VerifyReceiverScreenEmailAuthPreview() {
    AfternoteTheme {
        VerifyReceiverScreen(
            onBackClick = {},
            onVerifySuccess = {},
            viewModel =
                remember {
                    FakeVerifyReceiverViewModel(
                        VerifyReceiverUiState(step = VerifySelfStep.EMAIL_AUTH)
                    )
                }
        )
    }
}

@Preview(showBackground = true, name = "EMAIL_CODE")
@Composable
private fun VerifyReceiverScreenEmailCodePreview() {
    AfternoteTheme {
        VerifyReceiverScreen(
            onBackClick = {},
            onVerifySuccess = {},
            viewModel =
                remember {
                    FakeVerifyReceiverViewModel(
                        VerifyReceiverUiState(step = VerifySelfStep.EMAIL_CODE)
                    )
                }
        )
    }
}
