package com.kuit.afternote.feature.mainpage.presentation.component.edit.processingmethod

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.expand.bottomBorder
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 아이템 추가용 텍스트 필드 컴포넌트
 *
 * 포커스 해제 시 자동으로 아이템을 추가하고 텍스트 필드를 숨깁니다.
 */
@Composable
fun AddItemTextField(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onItemAdded: (String) -> Unit,
    onVisibilityChanged: (Boolean) -> Unit,
    placeholder: String = "Text Field",
    previousFocusedState: Boolean = false,
    onPreviousFocusedStateChange: (Boolean) -> Unit = {}
) {
    val textFieldState = rememberTextFieldState()
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val onItemAddedState = rememberUpdatedState(onItemAdded)
    val onVisibilityChangedState = rememberUpdatedState(onVisibilityChanged)

    fun addItemIfNotEmpty() {
        val text = textFieldState.text.toString().trim()
        if (text.isNotEmpty()) {
            onItemAddedState.value(text)
            textFieldState.edit { replace(0, length, "") }
        }
        onVisibilityChangedState.value(false)
        focusManager.clearFocus()
    }

    DisposableEffect(isFocused, visible, previousFocusedState) {
        if (visible && previousFocusedState && !isFocused) {
            addItemIfNotEmpty()
        }
        onPreviousFocusedStateChange(isFocused)
        onDispose { }
    }

    if (visible) {
        Spacer(modifier = Modifier.height(7.dp))
        // 텍스트 필드 컨테이너 (하단 보더 포함)
        Box(
            modifier = modifier
                .fillMaxWidth()
                .bottomBorder(color = Gray2, width = 1.dp)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            OutlinedTextField(
                state = textFieldState,
                lineLimits = TextFieldLineLimits.SingleLine,
                placeholder = {
                    Text(
                        text = placeholder,
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Normal,
                            color = Gray4
                        )
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                interactionSource = interactionSource,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray9
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddItemTextFieldPreview() {
    AfternoteTheme {
        AddItemTextField(
            visible = true,
            onItemAdded = {},
            onVisibilityChanged = {}
        )
    }
}
