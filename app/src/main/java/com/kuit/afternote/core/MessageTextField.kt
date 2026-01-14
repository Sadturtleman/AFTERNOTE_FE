package com.kuit.afternote.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

/**
 * 남기실 말씀 멀티라인 텍스트 필드 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 라벨: 16sp, Medium, Gray9
 * - 텍스트 필드: 흰색 배경, 16dp 모서리, 160dp 높이
 */
@Composable
fun MessageTextField(
    modifier: Modifier = Modifier,
    label: String = "남기실 말씀",
    textFieldState: TextFieldState,
    placeholder: String = "Text Field"
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray9
            )
        )

        OutlinedTextField(
            state = textFieldState,
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray4
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedContainerColor = White,
                focusedContainerColor = White
            ),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions.Default,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(White, RoundedCornerShape(16.dp)),
            contentPadding = PaddingValues(all = 16.dp),
            textStyle = TextStyle(
                color = Gray9
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MessageTextFieldPreview() {
    AfternoteTheme {
        MessageTextField(
            textFieldState = rememberTextFieldState()
        )
    }
}
