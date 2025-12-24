package com.kuit.afternote.feature.onboarding.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun IdentifyInputContent(memberCode: TextFieldState) {
    Column(modifier = Modifier.fillMaxSize()) {
        OutlineTextField(
            textFieldState = memberCode,
            label = "주민등록번호 (- 없이 입력)",
            keyboardType = KeyboardType.Number
        )
    }
}
