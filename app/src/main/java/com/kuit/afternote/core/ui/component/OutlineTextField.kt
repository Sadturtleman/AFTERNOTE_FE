package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.model.RrnVisualTransformation
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

@Composable
fun OutlineTextField(
    textFieldState: TextFieldState,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true
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
            unfocusedBorderColor = Gray4,
            disabledTextColor = Gray9,
            disabledPlaceholderColor = Gray4,
            disabledContainerColor = White
        ),
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        enabled = true,
        readOnly = !enabled,
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

@Composable
fun OutlineTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            // 1. 보안 및 유효성 검사: 숫자만 허용하며 최대 7자 제한
            if (input.length <= 7 && input.all { it.isDigit() }) {
                onValueChange(input)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        // 2. 이미지의 좌측 레이아웃 재현 (Prefix 활용)
        prefix = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("주민번호", color = Color.Gray, fontSize = 14.sp)
                Text(" — ", color = Color.Black)
                Text("T ", color = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
            }
        },
        visualTransformation = RrnVisualTransformation(), // 3. 마스킹 및 하이픈 로직 분리
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.LightGray
        )
    )
}
/**
 * 라벨이 있는 텍스트 필드 컴포넌트 (LabeledTextField 호환)
 *
 * @param modifier Modifier (기본: Modifier)
 * @param label 라벨 텍스트
 * @param textFieldState 텍스트 필드 상태
 * @param placeholder 플레이스홀더 텍스트 (기본: "Text Field")
 * @param keyboardType 키보드 타입 (기본: KeyboardType.Text)
 * @param containerColor 텍스트 필드 배경색 (기본: White)
 * @param labelSpacing 라벨과 필드 간 간격 (기본: 6.dp)
 */
@Composable
fun OutlineTextField(
    modifier: Modifier = Modifier,
    label: String,
    textFieldState: TextFieldState,
    placeholder: String = "Text Field",
    keyboardType: KeyboardType = KeyboardType.Text,
    containerColor: Color = White,
    labelSpacing: Dp = 6.dp
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = labelSpacing)
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
                    lineHeight = 20.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray4
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedContainerColor = containerColor,
                focusedContainerColor = containerColor
            ),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(containerColor, RoundedCornerShape(8.dp)),
            textStyle = TextStyle(
                color = Gray9
            )
        )
    }
}

/**
 * 멀티라인 텍스트 필드 컴포넌트 (MessageTextField 호환)
 *
 * @param modifier Modifier (기본: Modifier)
 * @param label 라벨 텍스트 (기본: "남기실 말씀")
 * @param textFieldState 텍스트 필드 상태
 * @param placeholder 플레이스홀더 텍스트 (기본: "Text Field")
 */
@Composable
fun OutlineTextField(
    modifier: Modifier = Modifier,
    label: String = "남기실 말씀",
    textFieldState: TextFieldState,
    placeholder: String = "Text Field",
    isMultiline: Boolean = true
) {
    if (isMultiline) {
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
}

@Preview(showBackground = true, name = "기본 플레이스홀더")
@Composable
private fun OutlineTextFieldBasicPreview() {
    AfternoteTheme {
        OutlineTextField(
            textFieldState = rememberTextFieldState(),
            label = "시작"
        )
    }
}

@Preview(showBackground = true, name = "인증번호 받기 버튼")
@Composable
private fun OutlineTextFieldWithAuthPreview() {
    AfternoteTheme {
        OutlineTextField(
            textFieldState = rememberTextFieldState(),
            label = "전화번호",
            onAuthClick = {}
        )
    }
}

@Preview(showBackground = true, name = "파일 추가 버튼")
@Composable
private fun OutlineTextFieldWithFileAddPreview() {
    AfternoteTheme {
        OutlineTextField(
            textFieldState = rememberTextFieldState(),
            label = "파일 선택",
            onFileAddClick = {}
        )
    }
}

@Preview(showBackground = true, name = "라벨이 있는 버전")
@Composable
private fun OutlineTextFieldWithLabelPreview() {
    AfternoteTheme {
        OutlineTextField(
            label = "아이디",
            textFieldState = rememberTextFieldState()
        )
    }
}

@Preview(showBackground = true, name = "멀티라인 버전")
@Composable
private fun OutlineTextFieldMultilinePreview() {
    AfternoteTheme {
        OutlineTextField(
            label = "남기실 말씀",
            textFieldState = rememberTextFieldState(),
            isMultiline = true
        )
    }
}
