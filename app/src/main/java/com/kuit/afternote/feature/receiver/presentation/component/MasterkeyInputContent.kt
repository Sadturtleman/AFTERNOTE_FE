package com.kuit.afternote.feature.receiver.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.kuit.afternote.feature.onboarding.presentation.component.OutlineTextField

@Composable
fun MasterKeyInputContent(masterKey: TextFieldState) {
    Column(modifier = Modifier.fillMaxSize()) {
        OutlineTextField(
            textFieldState = masterKey,
            label = "마스터 키 입력",
            keyboardType = KeyboardType.Number
        )
    }
}
