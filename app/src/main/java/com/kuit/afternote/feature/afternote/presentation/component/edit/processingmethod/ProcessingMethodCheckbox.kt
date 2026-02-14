package com.kuit.afternote.feature.afternote.presentation.component.edit.processingmethod

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.detail.EditDropdownMenu
import com.kuit.afternote.core.ui.component.icon.CheckCircleIcon
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.ProcessingMethodItem
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * Callbacks for processing method checkbox dropdown (more, dismiss, edit, delete).
 */
data class ProcessingMethodCheckboxCallbacks(
    val onMoreClick: () -> Unit = {},
    val onDismissDropdown: () -> Unit = {},
    val onEditClick: () -> Unit = {},
    val onDeleteClick: () -> Unit = {},
    val onEditConfirmed: (String) -> Unit = {}
)

private val processingMethodTextStyle = TextStyle(
    fontSize = 14.sp,
    lineHeight = 20.sp,
    fontFamily = Sansneo,
    fontWeight = FontWeight.Normal,
    color = Gray9
)

/**
 * 처리 방법 체크박스 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 체크박스: 16dp
 * - 텍스트: 14sp, Regular, Gray9
 * - 더보기 아이콘: 오른쪽 정렬
 *
 * [isEditing]이 true이면 텍스트 영역이 BasicTextField로 전환되어 인라인 편집을 지원합니다.
 */
@Composable
fun ProcessingMethodCheckbox(
    modifier: Modifier = Modifier,
    item: ProcessingMethodItem,
    expanded: Boolean = false,
    isEditing: Boolean = false,
    callbacks: ProcessingMethodCheckboxCallbacks = ProcessingMethodCheckboxCallbacks()
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CheckCircleIcon()

        if (isEditing) {
            InlineEditTextField(
                initialText = item.text,
                onConfirm = callbacks.onEditConfirmed,
                modifier = Modifier.weight(1f)
            )
        } else {
            Text(
                text = item.text,
                style = processingMethodTextStyle,
                modifier = Modifier.weight(1f)
            )
        }

        Box {
            Image(
                painter = painterResource(R.drawable.ic_more_horizontal_1),
                contentDescription = "더보기",
                modifier = Modifier
                    .clickable(onClick = callbacks.onMoreClick)
            )
            EditDropdownMenu(
                expanded = expanded,
                onDismissRequest = callbacks.onDismissDropdown,
                onEditClick = callbacks.onEditClick,
                onDeleteClick = callbacks.onDeleteClick
            )
        }
    }
}

/**
 * 인라인 편집용 텍스트 필드.
 *
 * 기존 Text와 동일한 스타일(14sp, Regular, Gray9)을 유지하여
 * 편집 모드 전환 시 레이아웃이 자연스럽게 유지됩니다.
 * Enter 키 또는 포커스 해제 시 편집을 확정합니다.
 *
 * 포커스를 한 번도 획득하지 않은 상태에서의 focus loss는 무시합니다.
 * (DropdownMenu dismiss 과정에서 발생하는 포커스 흔들림 방지)
 */
@Composable
private fun InlineEditTextField(
    initialText: String,
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = initialText,
                selection = TextRange(initialText.length)
            )
        )
    }
    var hasConfirmed by remember { mutableStateOf(false) }
    var hasGainedFocus by remember { mutableStateOf(false) }

    fun confirmEdit() {
        if (hasConfirmed) return
        hasConfirmed = true
        val trimmed = textFieldValue.text.trim()
        if (trimmed.isNotEmpty()) {
            onConfirm(trimmed)
        }
    }

    BasicTextField(
        value = textFieldValue,
        onValueChange = { textFieldValue = it },
        textStyle = processingMethodTextStyle,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                confirmEdit()
                focusManager.clearFocus()
            }
        ),
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    hasGainedFocus = true
                } else if (hasGainedFocus && !hasConfirmed) {
                    confirmEdit()
                }
            }
    )

    LaunchedEffect(Unit) {
        // Wait one frame for DropdownMenu dismiss to settle before requesting focus
        @Suppress("UNUSED_VARIABLE")
        val frame = withFrameNanos { it }
        focusRequester.requestFocus()
    }
}

@Preview(showBackground = true)
@Composable
private fun ProcessingMethodCheckboxPreview() {
    AfternoteTheme {
        Column {
            ProcessingMethodCheckbox(
                item = ProcessingMethodItem("1", "게시물 내리기"),
            )
        }
    }
}

@Preview(showBackground = true, name = "편집 모드")
@Composable
private fun ProcessingMethodCheckboxEditingPreview() {
    AfternoteTheme {
        Column {
            ProcessingMethodCheckbox(
                item = ProcessingMethodItem("1", "게시물 내리기"),
                isEditing = true,
                callbacks = ProcessingMethodCheckboxCallbacks(
                    onEditConfirmed = {}
                )
            )
        }
    }
}
