package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.OutputTransformation
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.ErrorRed
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

private const val DEFAULT_PLACEHOLDER = "Text Field"
private const val PASSWORD_MASK_CHAR = '\u2022'

private val OutlineTextFieldShape = RoundedCornerShape(8.dp)
private val OutlineTextFieldHeightBasic = 70.dp
private val OutlineTextFieldHeightLabeled = 56.dp
private val OutlineTextFieldHeightMultiline = 160.dp

/**
 * Shared placeholder content for outline text fields (simple: 16.sp, Gray4).
 */
@Composable
private fun OutlineTextFieldPlaceholderSimple(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontFamily = Sansneo,
        color = Gray4
    )
}

/**
 * Shared placeholder content with line height (16.sp, lineHeight 20.sp, Gray4).
 */
@Composable
private fun OutlineTextFieldPlaceholderWithLineHeight(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontFamily = Sansneo,
        fontWeight = FontWeight.Normal,
        color = Gray4
    )
}

@Composable
private fun outlineTextFieldBasicColors() = OutlinedTextFieldDefaults.colors(
    unfocusedBorderColor = Gray4,
    disabledTextColor = Gray9,
    disabledPlaceholderColor = Gray4,
    disabledContainerColor = White
)

@Composable
private fun outlineTextFieldBasicColorsSimple() = OutlinedTextFieldDefaults.colors(
    unfocusedBorderColor = Gray4
)

@Composable
private fun outlineTextFieldFilledColors(containerColor: Color) =
    OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = Color.Transparent,
        focusedBorderColor = Color.Transparent,
        unfocusedContainerColor = containerColor,
        focusedContainerColor = containerColor
    )

@Composable
private fun outlineTextFieldFilledColorsAll(containerColor: Color) =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        disabledBorderColor = Color.Transparent,
        errorBorderColor = Color.Transparent,
        focusedContainerColor = containerColor,
        unfocusedContainerColor = containerColor,
        disabledContainerColor = containerColor,
        errorContainerColor = containerColor
    )

/**
 * Style configuration for labeled OutlineTextField.
 */
data class LabeledTextFieldStyle(
    val containerColor: Color = White,
    val labelSpacing: Dp = 6.dp,
    val labelFontSize: TextUnit = 12.sp,
    val labelLineHeight: TextUnit = 18.sp,
    val labelFontWeight: FontWeight = FontWeight.Normal,
    val labelColor: Color = Gray9
)

private val PasswordOutputTransformation = OutputTransformation {
    val originalLength = length
    replace(0, originalLength, PASSWORD_MASK_CHAR.toString().repeat(originalLength))
}

// ============================================================================
// Basic Variants (placeholder inside field, 70.dp height)
// ============================================================================

/**
 * Single-line outlined text field (basic variant).
 *
 * @param textFieldState Text field state holder.
 * @param label Placeholder text shown when empty.
 * @param keyboardType Keyboard type; if [KeyboardType.Password], input is visually masked.
 * @param enabled Whether the field is enabled.
 * @param focusRequester Optional focus controller for programmatic focus.
 * @param requestFocusOnEnabled If true, requests focus when the field is laid out and enabled.
 */
@Composable
fun OutlineTextField(
    textFieldState: TextFieldState,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    focusRequester: FocusRequester? = null,
    requestFocusOnEnabled: Boolean = false
) {
    OutlinedTextField(
        state = textFieldState,
        lineLimits = TextFieldLineLimits.SingleLine,
        placeholder = { OutlineTextFieldPlaceholderSimple(text = label) },
        colors = outlineTextFieldBasicColors(),
        shape = OutlineTextFieldShape,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        outputTransformation = if (keyboardType == KeyboardType.Password) {
            PasswordOutputTransformation
        } else {
            null
        },
        enabled = enabled,
        readOnly = false,
        modifier = Modifier
            .fillMaxWidth()
            .height(OutlineTextFieldHeightBasic)
            .then(
                if (focusRequester != null) {
                    Modifier
                        .focusRequester(focusRequester)
                        .onGloballyPositioned {
                            if (enabled && requestFocusOnEnabled) {
                                focusRequester.requestFocus()
                            }
                        }
                } else {
                    Modifier
                }
            )
    )
}

/**
 * Single-line outlined text field with a trailing "auth code request" action.
 */
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
        placeholder = { OutlineTextFieldPlaceholderSimple(text = label) },
        shape = OutlineTextFieldShape,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        outputTransformation = if (keyboardType == KeyboardType.Password) {
            PasswordOutputTransformation
        } else {
            null
        },
        colors = outlineTextFieldBasicColorsSimple(),
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 25.dp)
            ) {
                Text(
                    text = "인증번호 받기",
                    modifier = Modifier.clickable { onAuthClick() }
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(OutlineTextFieldHeightBasic)
    )
}

/**
 * Single-line outlined text field that behaves like a file picker.
 */
@Composable
fun OutlineTextField(
    textFieldState: TextFieldState,
    label: String,
    onFileAddClick: () -> Unit
) {
    OutlinedTextField(
        state = textFieldState,
        lineLimits = TextFieldLineLimits.SingleLine,
        placeholder = { OutlineTextFieldPlaceholderSimple(text = label) },
        shape = OutlineTextFieldShape,
        colors = outlineTextFieldBasicColorsSimple(),
        readOnly = true,
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 25.dp)
            ) {
                IconButton(onClick = { onFileAddClick() }) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "Add file",
                        tint = B2,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(OutlineTextFieldHeightBasic)
    )
}

// ============================================================================
// Labeled Variants (external label above field, filled style, 56.dp height)
// ============================================================================

/**
 * Labeled single-line text field with optional error state.
 *
 * **Use when**
 * - You need a label above the field (instead of a placeholder-only input).
 * - You want a filled look (border hidden) with a configurable container color.
 * - You optionally need error state with red border.
 *
 * @param modifier Modifier for the whole component.
 * @param label Label text shown above the field.
 * @param textFieldState Text field state holder.
 * @param placeholder Placeholder text shown when empty.
 * @param keyboardType Keyboard type; if [KeyboardType.Password], input is visually masked.
 * @param style Style configuration (container color, label spacing).
 * @param isError Whether to show the red error border.
 */
@Composable
fun OutlineTextField(
    modifier: Modifier = Modifier,
    label: String,
    textFieldState: TextFieldState,
    placeholder: String = DEFAULT_PLACEHOLDER,
    keyboardType: KeyboardType = KeyboardType.Text,
    style: LabeledTextFieldStyle = LabeledTextFieldStyle(),
    isError: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = style.labelSpacing)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = style.labelFontSize,
                lineHeight = style.labelLineHeight,
                fontFamily = Sansneo,
                fontWeight = style.labelFontWeight,
                color = Gray9
            )
        )

        OutlinedTextField(
            state = textFieldState,
            lineLimits = TextFieldLineLimits.SingleLine,
            contentPadding = PaddingValues(
                vertical = 16.dp,
                horizontal = 24.dp
            ),
            placeholder = { OutlineTextFieldPlaceholderWithLineHeight(text = placeholder) },
            colors = outlineTextFieldFilledColors(style.containerColor),
            shape = OutlineTextFieldShape,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            outputTransformation = if (keyboardType == KeyboardType.Password) {
                PasswordOutputTransformation
            } else {
                null
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(OutlineTextFieldHeightLabeled)
                .background(style.containerColor, OutlineTextFieldShape)
                .then(
                    if (isError) {
                        Modifier.border(
                            width = 1.dp,
                            color = ErrorRed,
                            shape = OutlineTextFieldShape
                        )
                    } else {
                        Modifier
                    }
                ),
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

// ============================================================================
// Multiline Variant (external label, larger container)
// ============================================================================

/**
 * Marker object for multiline OutlineTextField variant.
 */
object Multiline

/**
 * Multiline message-style text field.
 *
 * @param modifier Modifier for the whole component.
 * @param label Label text shown above the field.
 * @param textFieldState Text field state holder.
 * @param placeholder Placeholder text shown when empty.
 * @param multiline Marker to distinguish this as multiline variant.
 */
@Suppress("UNUSED_PARAMETER")
@Composable
fun OutlineTextField(
    modifier: Modifier = Modifier,
    label: String,
    textFieldState: TextFieldState,
    placeholder: String = DEFAULT_PLACEHOLDER,
    multiline: Multiline
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
            lineLimits = TextFieldLineLimits.MultiLine(),
            placeholder = { OutlineTextFieldPlaceholderWithLineHeight(text = placeholder) },
            colors = outlineTextFieldFilledColors(White),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions.Default,
            modifier = Modifier
                .fillMaxWidth()
                .height(OutlineTextFieldHeightMultiline)
                .background(White, RoundedCornerShape(16.dp)),
            contentPadding = PaddingValues(all = 16.dp),
            textStyle = TextStyle(
                fontSize = 16.sp,
                lineHeight = 20.dp.value.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Normal,
                color = Gray9
            )
        )
    }
}

// ============================================================================
// Filled box variant (no label, multiline-style container)
// ============================================================================

/**
 * Multiline-style outlined text field with no label (e.g. "기타 직접 입력" box).
 *
 * @param modifier Modifier for the field.
 * @param textFieldState Text field state holder.
 * @param placeholder Placeholder text when empty.
 * @param containerColor Background and border fill color.
 * @param height Height of the field.
 * @param shape Shape of the container.
 */
@Composable
fun OutlineTextField(
    modifier: Modifier = Modifier,
    textFieldState: TextFieldState,
    placeholder: String = DEFAULT_PLACEHOLDER,
    containerColor: Color = Gray1,
    height: Dp = OutlineTextFieldHeightMultiline,
    shape: RoundedCornerShape = RoundedCornerShape(16.dp)
) {
    OutlinedTextField(
        state = textFieldState,
        lineLimits = TextFieldLineLimits.MultiLine(),
        placeholder = { OutlineTextFieldPlaceholderWithLineHeight(text = placeholder) },
        colors = outlineTextFieldFilledColorsAll(containerColor),
        shape = shape,
        keyboardOptions = KeyboardOptions.Default,
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(containerColor, shape),
        contentPadding = PaddingValues(all = 16.dp),
        textStyle = TextStyle(
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Normal,
            color = Gray9
        )
    )
}

// ============================================================================
// Previews
// ============================================================================

@Preview(showBackground = true, name = "기본 플레이스홀더")
@Composable
private fun OutlineTextFieldBasicPreview() {
    AfternoteTheme {
        OutlineTextField(
            textFieldState = rememberTextFieldState(),
            label = "시작",
            keyboardType = KeyboardType.Text
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
            modifier = Modifier,
            label = "아이디",
            textFieldState = rememberTextFieldState()
        )
    }
}

@Preview(showBackground = true, name = "에러 상태")
@Composable
private fun OutlineTextFieldWithErrorPreview() {
    AfternoteTheme {
        OutlineTextField(
            modifier = Modifier,
            label = "비밀번호",
            textFieldState = rememberTextFieldState(),
            isError = true
        )
    }
}

@Preview(showBackground = true, name = "비활성화 상태")
@Composable
private fun OutlineTextFieldDisabledPreview() {
    AfternoteTheme {
        OutlineTextField(
            textFieldState = rememberTextFieldState(),
            label = "비활성화된 필드",
            enabled = false
        )
    }
}

@Preview(showBackground = true, name = "비밀번호 입력")
@Composable
private fun OutlineTextFieldPasswordPreview() {
    AfternoteTheme {
        OutlineTextField(
            textFieldState = rememberTextFieldState("password123"),
            label = "비밀번호",
            keyboardType = KeyboardType.Password
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
            multiline = Multiline
        )
    }
}
