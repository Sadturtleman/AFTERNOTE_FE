package com.kuit.afternote.feature.setting.presentation.screen.password

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.setting.presentation.component.KeyPad
import com.kuit.afternote.feature.setting.presentation.component.PasscodeIndicator
import com.kuit.afternote.feature.setting.presentation.uimodel.KeyAction
import com.kuit.afternote.feature.setting.presentation.uimodel.PasswordStep
import com.kuit.afternote.feature.setting.presentation.viewmodel.KeyPadModifyViewModel
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun PasswordModifyScreen(keyPadViewModel: KeyPadModifyViewModel = hiltViewModel()) {
    val password by keyPadViewModel.inputCode.collectAsStateWithLifecycle()
    val step by keyPadViewModel.currentStep.collectAsStateWithLifecycle()
    val onKeyAction: (KeyAction) -> Unit = remember {
        { action -> keyPadViewModel.handleKeyAction(action) }
    }

    Scaffold(
        topBar = {
            TopBar(title = "앱 잠금 비밀번호 변경") {}
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Image(
                painter = painterResource(R.drawable.lock),
                contentDescription = null,
                modifier = Modifier.size(56.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = when (step) {
                    is PasswordStep.Confirm ->
                        "비밀번호를 다시 한 번 입력해 주세요"

                    is PasswordStep.Setup ->
                        "변경할 비밀번호를 입력해 주세요"

                    else -> ""
                },
                fontFamily = Sansneo,
                fontSize = 18.sp,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            PasscodeIndicator(totalDots = 4, filledDots = password.length)

            Spacer(modifier = Modifier.height(100.dp))

            KeyPad(
                onAction = onKeyAction
            )
        }
    }
}

@Preview
@Composable
private fun PasswordModifyScreenPreview() {
    PasswordModifyScreen()
}
