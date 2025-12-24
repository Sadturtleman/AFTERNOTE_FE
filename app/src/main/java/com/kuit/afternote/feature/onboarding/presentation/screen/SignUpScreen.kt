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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.feature.onboarding.presentation.component.EmailInputContent
import com.kuit.afternote.feature.onboarding.presentation.component.IdentifyInputContent
import com.kuit.afternote.feature.onboarding.presentation.component.PhoneAuthContent
import com.kuit.afternote.feature.onboarding.presentation.component.PwInputContent
import com.kuit.afternote.feature.onboarding.presentation.component.SignUpContentButton
import com.kuit.afternote.feature.onboarding.presentation.component.SignUpEndContent
import com.kuit.afternote.feature.onboarding.presentation.component.TopBar
import com.kuit.afternote.feature.onboarding.presentation.uimodel.SignUpStep

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onSettingClick: () -> Unit
) {
    val phone = rememberTextFieldState()
    val authCode = rememberTextFieldState()
    val memberCode = rememberTextFieldState()
    val email = rememberTextFieldState()
    val pw = rememberTextFieldState()
    val pwRe = rememberTextFieldState()

    var step by remember { mutableStateOf(SignUpStep.PHONE_AUTH) }

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
                Spacer(modifier = modifier.height(40.dp))

                when (step) {
                    SignUpStep.PHONE_AUTH -> {
                        SignUpContentButton(
                            onNextClick = { step = SignUpStep.IDENTIFY_INPUT }
                        ) {
                            PhoneAuthContent(
                                phone = phone,
                                authCode = authCode,
                                onAuthClick = {}
                            )
                        }
                    }

                    SignUpStep.IDENTIFY_INPUT -> {
                        SignUpContentButton(
                            onNextClick = { step = SignUpStep.EMAIL_INPUT }
                        ) {
                            IdentifyInputContent(
                                memberCode = memberCode
                            )
                        }
                    }

                    SignUpStep.EMAIL_INPUT -> {
                        SignUpContentButton(
                            onNextClick = { step = SignUpStep.PW_INPUT }
                        ) {
                            EmailInputContent(
                                email = email
                            )
                        }
                    }

                    SignUpStep.PW_INPUT -> {
                        SignUpContentButton(
                            onNextClick = { step = SignUpStep.END }
                        ) {
                            PwInputContent(
                                pw = pw,
                                pwRe = pwRe
                            )
                        }
                    }

                    SignUpStep.END -> {
                        SignUpEndContent {
                            onSettingClick()
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
    SignUpScreen(onBackClick = {}, onSettingClick = {})
}
