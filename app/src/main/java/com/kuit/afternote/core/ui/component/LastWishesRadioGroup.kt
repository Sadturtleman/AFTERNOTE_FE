package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.button.CustomRadioButton
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

/** Section label used for edit and view (receiver) so both share the same literal. */
const val LABEL_LAST_WISH = "남기고 싶은 당부"

/**
 * 남기고 싶은 당부 라디오 버튼 옵션
 *
 * @param text 옵션 텍스트
 * @param value 옵션 값 (선택 시 반환되는 값)
 */
data class LastWishOption(
    val text: String,
    val value: String
)

/**
 * 기타(직접 입력) 선택 시 표시할 텍스트 필드 상태.
 *
 * @param text 현재 입력값
 * @param onTextChange 입력 변경 콜백
 */
data class LastWishOtherState(
    val text: String,
    val onTextChange: (String) -> Unit
)

/**
 * 남기고 싶은 당부 컴포넌트 (edit·view 공통).
 *
 * - **Edit mode**: [displayTextOnly]가 null이면 [options]로 라디오 그룹 표시.
 * - **View mode**: [displayTextOnly]가 non-null이면 라벨 + 테두리 Surface에 해당 텍스트만 표시 (수신자 화면).
 *
 * @param displayTextOnly View 모드일 때 표시할 단일 텍스트; null이면 edit 모드(라디오 옵션).
 * @param otherState 기타(직접 입력) 선택 시 텍스트 필드 상태; null이면 해당 옵션에 텍스트 필드 미표시.
 */
@Composable
fun LastWishesRadioGroup(
    modifier: Modifier = Modifier,
    label: String = LABEL_LAST_WISH,
    options: List<LastWishOption> = emptyList(),
    selectedValue: String? = null,
    onOptionSelected: (String) -> Unit = {},
    displayTextOnly: String? = null,
    otherState: LastWishOtherState? = null
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

        if (displayTextOnly != null) {
            LastWishViewModeContent(displayTextOnly = displayTextOnly)
        } else {
            LastWishEditModeContent(
                options = options,
                selectedValue = selectedValue,
                onOptionSelected = onOptionSelected,
                otherState = otherState
            )
        }
    }
}

@Composable
private fun LastWishViewModeContent(displayTextOnly: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, B1),
        color = White
    ) {
        Text(
            text = displayTextOnly,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            color = B1,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
private fun LastWishEditModeContent(
    options: List<LastWishOption>,
    selectedValue: String?,
    onOptionSelected: (String) -> Unit,
    otherState: LastWishOtherState?
) {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        options.forEach { option ->
            val selected = selectedValue == option.value
            if (option.value == "other" && otherState != null) {
                LastWishOtherCard(
                    option = option,
                    selected = selected,
                    otherState = otherState,
                    onOptionSelected = { onOptionSelected(option.value) }
                )
            } else {
                SelectableRadioCard(
                    modifier = Modifier.fillMaxWidth(),
                    selected = selected,
                    onClick = { onOptionSelected(option.value) }
                ) {
                    Text(
                        text = option.text,
                        style = optionLabelStyle(selected = selected),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun LastWishOtherCard(
    option: LastWishOption,
    selected: Boolean,
    otherState: LastWishOtherState,
    onOptionSelected: () -> Unit
) {
    val borderColor = if (selected) B2 else Color.Transparent
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onOptionSelected,
                role = Role.RadioButton,
                interactionSource = interactionSource,
                indication = null
            ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        color = White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomRadioButton(
                    selected = selected,
                    onClick = null,
                    buttonSize = 24.dp,
                    selectedColor = B2,
                    unselectedColor = Gray4
                )
                Text(
                    text = option.text,
                    style = optionLabelStyle(selected = selected),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (selected) {
                LastWishOtherTextField(
                    value = otherState.text,
                    onValueChange = otherState.onTextChange,
                )
            }
        }
    }
}

private fun optionLabelStyle(selected: Boolean): TextStyle = TextStyle(
    fontSize = 16.sp,
    lineHeight = 22.sp,
    fontFamily = Sansneo,
    fontWeight = FontWeight.Medium,
    color = if (selected) B1 else Gray9
)

@Composable
private fun LastWishOtherTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberTextFieldState(initialText = value)
    LaunchedEffect(value) {
        if (state.text.toString() != value) {
            state.edit { replace(0, length, value) }
        }
    }
    LaunchedEffect(state) {
        snapshotFlow { state.text.toString() }.collect { onValueChange(it) }
    }
    OutlineTextField(
        modifier = modifier,
        textFieldState = state,
        placeholder = "Text Field",
        containerColor = Gray1,
        height = 160.dp,
        shape = RoundedCornerShape(16.dp)
    )
}

@Preview(showBackground = true, name = "Edit mode")
@Composable
private fun LastWishesRadioGroupEditPreview() {
    AfternoteTheme {
        val options = listOf(
            LastWishOption(text = "차분하고 조용하게 보내주세요.", value = "calm"),
            LastWishOption(text = "슬퍼 하지 말고 밝고 따뜻하게 보내주세요.", value = "bright"),
            LastWishOption(text = "기타(직접 입력)", value = "other")
        )
        Column(modifier = Modifier.padding(20.dp)) {
            LastWishesRadioGroup(
                label = LABEL_LAST_WISH,
                options = options,
                selectedValue = "calm",
                onOptionSelected = {},
                otherState = LastWishOtherState(text = "", onTextChange = {})
            )
        }
    }
}

@Preview(showBackground = true, name = "Edit mode - 기타(직접 입력) selected")
@Composable
private fun LastWishesRadioGroupOtherSelectedPreview() {
    AfternoteTheme {
        val options = listOf(
            LastWishOption(text = "차분하고 조용하게 보내주세요.", value = "calm"),
            LastWishOption(text = "슬퍼 하지 말고 밝고 따뜻하게 보내주세요.", value = "bright"),
            LastWishOption(text = "기타(직접 입력)", value = "other")
        )
        Column(modifier = Modifier.padding(20.dp)) {
            LastWishesRadioGroup(
                label = LABEL_LAST_WISH,
                options = options,
                selectedValue = "other",
                onOptionSelected = {},
                otherState = LastWishOtherState(
                    text = "끼니 거르지 말고 건강 챙기고 지내.",
                    onTextChange = {}
                )
            )
        }
    }
}

@Preview(showBackground = true, name = "View mode")
@Composable
private fun LastWishesRadioGroupViewPreview() {
    AfternoteTheme {
        Column(modifier = Modifier.padding(20.dp)) {
            LastWishesRadioGroup(
                label = LABEL_LAST_WISH,
                displayTextOnly = "끼니 거르지 말고 건강 챙기고 지내."
            )
        }
    }
}
