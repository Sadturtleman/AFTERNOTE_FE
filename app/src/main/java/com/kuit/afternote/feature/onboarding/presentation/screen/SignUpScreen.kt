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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.EmailInputContent
import com.kuit.afternote.core.ui.component.SignUpContentButton
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.feature.onboarding.presentation.component.IdentifyInputContent
import com.kuit.afternote.feature.onboarding.presentation.component.PhoneAuthContent
import com.kuit.afternote.feature.onboarding.presentation.component.PwInputContent
import com.kuit.afternote.feature.onboarding.presentation.component.SignUpEndContent
import com.kuit.afternote.feature.onboarding.presentation.uimodel.SignUpStep
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    onSettingClick: () -> Unit
) {
    val phone = rememberTextFieldState()
    val authCode = rememberTextFieldState()
    var firstPart by remember { mutableStateOf("") }
    var secondPart by remember { mutableStateOf("") }
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
                Spacer(modifier = Modifier.height(40.dp))

                when (step) {
                    SignUpStep.PHONE_AUTH -> {
                        SignUpContentButton(
                            onNextClick = { step = SignUpStep.IDENTIFY_INPUT }
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
                                onAuthClick = {}
                            )
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
                                firstPart = firstPart,
                                onFirstPartChange = { firstPart = it },
                                secondPart = secondPart,
                                onSecondPartChange = { secondPart = it }
                            )
                        }
                    }

                    SignUpStep.PW_INPUT -> {
                        SignUpContentButton(
                            onNextClick = { step = SignUpStep.END }
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
