package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

/**
 * 계정 정보 입력 필드 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 라벨: 12sp, Regular, Gray9
 * - 텍스트 필드: 흰색 배경, 8dp 모서리, 56dp 높이
 */
@Composable
fun AccountInfoTextField(
    modifier: Modifier = Modifier,
    label: String,
    textFieldState: TextFieldState,
    placeholder: String = "Text Field",
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = 6.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Normal,
                color = Gray9
            )
        )

        OutlinedTextField(
            contentPadding = PaddingValues(
                vertical = 16.dp,
                horizontal = 24.dp
            ),
            state = textFieldState,
            lineLimits = TextFieldLineLimits.SingleLine,
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 16.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray4,
                    lineHeight = 20.sp
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedContainerColor = White,
                focusedContainerColor = White
            ),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(White, RoundedCornerShape(8.dp))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountInfoTextFieldPreview() {
    AfternoteTheme {
        AccountInfoTextField(
            label = "아이디",
            textFieldState = androidx.compose.foundation.text.input
                .rememberTextFieldState()
        )
    }
}
