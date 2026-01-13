package com.kuit.afternote.feature.onboarding.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.ui.theme.AfternoteTheme

@Composable
fun EmailInputContent(email: TextFieldState) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlineTextField(
            textFieldState = email,
            label = "이메일"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmailInputContentPreview() {
    AfternoteTheme {
        EmailInputContent(
            email = rememberTextFieldState()
        )
    }
}
