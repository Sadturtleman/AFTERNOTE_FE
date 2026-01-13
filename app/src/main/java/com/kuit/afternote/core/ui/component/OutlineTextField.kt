package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun OutlineTextField(
    textFieldState: TextFieldState,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        state = textFieldState,
        lineLimits = TextFieldLineLimits.SingleLine,
        placeholder = {
            Text(
                text = label,
                fontSize = 16.sp,
                fontFamily = Sansneo,
                color = Gray4
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Gray4
        ),
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    )
}

@Composable
fun OutlineTextField(
    textFieldState: TextFieldState,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onAuthClick: () -> Unit
) {
    OutlinedTextField(
        state = textFieldState,
        lineLimits = TextFieldLineLimits.SingleLine,
        placeholder = {
            Text(
                text = label,
                fontSize = 16.sp,
                fontFamily = Sansneo,
                color = Gray4
            )
        },
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Gray4
        ),
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 25.dp)
            ) {
                Text(
                    text = "인증번호 받기",
                    modifier = Modifier
                        .clickable {
                            onAuthClick()
                        }
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    )
}

@Composable
fun OutlineTextField(
    textFieldState: TextFieldState,
    label: String,
    onFileAddClick: () -> Unit
) {
    OutlinedTextField(
        state = textFieldState,
        lineLimits = TextFieldLineLimits.SingleLine,
        placeholder = {
            Text(
                text = label,
                fontSize = 16.sp,
                fontFamily = Sansneo,
                color = Gray4
            )
        },
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Gray4
        ),
        readOnly = true,
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 25.dp)
            ) {
                IconButton(onClick = { onFileAddClick() }) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = null,
                        tint = B2,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun OutlineTextFieldPreview() {
    OutlineTextField(rememberTextFieldState(), "시작")
}
