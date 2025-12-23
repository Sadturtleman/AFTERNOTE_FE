package com.kuit.afternote.feature.onboarding.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
