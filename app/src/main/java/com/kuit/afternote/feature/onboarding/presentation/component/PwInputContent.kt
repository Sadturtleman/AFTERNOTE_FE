package com.kuit.afternote.feature.onboarding.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.feature.onboarding.presentation.util.PasswordValidator
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun PwInputContent(
    pw: TextFieldState,
    pwRe: TextFieldState,
) {
    val passwordText by remember { derivedStateOf { pw.text.toString() } }
    val passwordConfirmText by remember { derivedStateOf { pwRe.text.toString() } }

    val passwordError = remember(passwordText) {
        if (passwordText.isNotBlank()) {
            PasswordValidator.validate(passwordText)
        } else {
            null
        }
    }

    val passwordConfirmError = remember(passwordText, passwordConfirmText) {
        if (passwordConfirmText.isNotBlank() && passwordText.isNotBlank()) {
            if (!PasswordValidator.matches(passwordText, passwordConfirmText)) {
                "비밀번호가 일치하지 않습니다."
            } else {
                null
            }
        } else {
            null
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlineTextField(
            textFieldState = pw,
            label = "비밀번호",
            keyboardType = KeyboardType.Password
        )

        if (passwordError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = passwordError,
                color = Gray9,
                fontSize = 14.sp,
                fontFamily = Sansneo
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlineTextField(
            textFieldState = pwRe,
            label = "비밀번호 확인",
            keyboardType = KeyboardType.Password
        )

        if (passwordConfirmError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = passwordConfirmError,
                color = Gray9,
                fontSize = 14.sp,
                fontFamily = Sansneo
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PwInputContentPreview() {
    AfternoteTheme {
        PwInputContent(
            pw = rememberTextFieldState(),
            pwRe = rememberTextFieldState()
        )
    }
}
