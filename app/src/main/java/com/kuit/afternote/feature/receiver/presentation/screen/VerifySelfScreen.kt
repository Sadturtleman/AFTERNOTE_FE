package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import com.kuit.afternote.feature.onboarding.presentation.component.EmailInputContent
import com.kuit.afternote.feature.onboarding.presentation.component.SignUpContentButton
import com.kuit.afternote.feature.onboarding.presentation.component.TopBar
import com.kuit.afternote.feature.receiver.presentation.component.MasterKeyInputContent
import com.kuit.afternote.feature.receiver.presentation.uimodel.VerifyStep

@Composable
fun VerifySelfScreen(
    onBackClick: () -> Unit
) {
    var step by remember { mutableStateOf(VerifyStep.EMAIL_AUTH) }
    val email = rememberTextFieldState()
    val masterKey = rememberTextFieldState()

    Scaffold(
        topBar = {
            TopBar(
                title = "수신자 인증",
                onBackClick = {
                    step.previous()?.let {
                        step = it
                    } ?: onBackClick()
                },
                step = step
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            when (step) {
                VerifyStep.EMAIL_AUTH -> {
                    SignUpContentButton(
                        onNextClick = { step = VerifyStep.MASTER_KEY_AUTH }
                    ) {
                        EmailInputContent(
                            email = email
                        )
                    }
                }
                VerifyStep.MASTER_KEY_AUTH -> {

                    SignUpContentButton(
                        onNextClick = {step = VerifyStep.UPLOAD_PDF_AUTH}
                    ) {
                        MasterKeyInputContent(
                            masterKey = masterKey
                        )
                    }
                }
                VerifyStep.UPLOAD_PDF_AUTH -> {

                }
                VerifyStep.END -> TODO()
            }
        }
    }
}
