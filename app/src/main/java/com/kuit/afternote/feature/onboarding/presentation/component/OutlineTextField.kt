package com.kuit.afternote.feature.onboarding.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OutlineTextField(textFieldState: TextFieldState, label: String){
    OutlinedTextField(
        state = textFieldState,
        lineLimits = TextFieldLineLimits.SingleLine,
        label = { Text(
            text = label,
            fontSize = 16.sp
        ) },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun OutlineTextFieldPreview(){
    OutlineTextField(rememberTextFieldState("시작"), "시작")
}
