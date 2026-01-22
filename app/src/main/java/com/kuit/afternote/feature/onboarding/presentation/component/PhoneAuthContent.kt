package com.kuit.afternote.feature.onboarding.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.ui.theme.AfternoteTheme

@Composable
fun PhoneAuthContent(
    phone: TextFieldState,
    authCode: TextFieldState,
    onAuthClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlineTextField(
            textFieldState = phone,
            label = "이메일 주소",
            keyboardType = KeyboardType.Email,
            onAuthClick = onAuthClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlineTextField(
            textFieldState = authCode,
            label = "인증번호",
            keyboardType = KeyboardType.Number
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PhoneAuthContentPreview() {
    AfternoteTheme {
        PhoneAuthContent(
            phone = rememberTextFieldState(),
            authCode = rememberTextFieldState(),
            onAuthClick = {}
        )
    }
}
