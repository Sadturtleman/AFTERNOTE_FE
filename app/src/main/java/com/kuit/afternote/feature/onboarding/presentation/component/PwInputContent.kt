package com.kuit.afternote.feature.onboarding.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.OutlineTextField

@Composable
fun PwInputContent(
    pw: TextFieldState,
    pwRe: TextFieldState,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlineTextField(
            textFieldState = pw,
            label = "비밀번호"
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlineTextField(
            textFieldState = pwRe,
            label = "비밀번호 확인"
        )
    }
}
