package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.feature.mainpage.presentation.component.detail.EditDropdownMenu
import com.kuit.afternote.feature.mainpage.presentation.model.ProcessingMethodItem
import com.kuit.afternote.ui.expand.bottomBorder
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

/**
 * 처리 방법 리스트 컴포넌트
 */
@Composable
fun ProcessingMethodList(
    modifier: Modifier = Modifier,
    items: List<ProcessingMethodItem>,
    onAddClick: () -> Unit = {},
    onItemMoreClick: (String) -> Unit = {},
    onItemEditClick: (String) -> Unit = {},
    onItemDeleteClick: (String) -> Unit = {},
    onItemAdded: (String) -> Unit = {},
    onTextFieldVisibilityChanged: (Boolean) -> Unit = {},
    initialShowTextField: Boolean = false
) {
    var showTextField by remember { mutableStateOf(initialShowTextField) }
    val textFieldState = rememberTextFieldState()
    var expandedItemId by remember { mutableStateOf<String?>(null) }
    var dropdownPosition by remember { mutableStateOf<Offset?>(null) }
    val density = LocalDensity.current
    val focusManager = LocalFocusManager.current

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val onItemAddedState = rememberUpdatedState(onItemAdded)
    val onTextFieldVisibilityChangedState = rememberUpdatedState(onTextFieldVisibilityChanged)

    // 포커스 해제 시 텍스트 필드 내용을 리스트에 추가
    var previousFocusedState by remember { mutableStateOf(false) }

    fun addItemIfNotEmpty() {
        val text = textFieldState.text.toString().trim()
        if (text.isNotEmpty()) {
            onItemAddedState.value(text)
            textFieldState.edit { replace(0, length, "") }
        }
        showTextField = false
        onTextFieldVisibilityChangedState.value?.invoke(false)
        focusManager.clearFocus()
    }

    DisposableEffect(isFocused, showTextField) {
        if (showTextField && previousFocusedState && !isFocused) {
            addItemIfNotEmpty()
        }
        previousFocusedState = isFocused
        onDispose { }
    }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = White,
                    shape = RoundedCornerShape(16.dp)
                ).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items.forEachIndexed { index, item ->
                ProcessingMethodCheckbox(
                    item = item,
                    onClick = {
                        focusManager.clearFocus()
                        if (showTextField) {
                            addItemIfNotEmpty()
                        }
                    },
                    onMoreClick = {
                        focusManager.clearFocus()
                        if (expandedItemId == item.id) {
                            expandedItemId = null
                            dropdownPosition = null
                        } else {
                            expandedItemId = item.id
                        }
                    },
                    onMoreButtonPositioned = { position ->
                        if (expandedItemId == item.id) {
                            dropdownPosition = position
                        }
                    }
                )
                Spacer(
                    modifier = Modifier.height(6.dp)
                )
            }

            // 텍스트 필드 (버튼 클릭 시 표시)
            if (showTextField) {
                Spacer(
                    modifier = Modifier.height(7.dp)
                )
                // 텍스트 필드 컨테이너 (하단 보더 포함)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bottomBorder(color = Gray2, width = 1.dp)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    OutlinedTextField(
                        state = textFieldState,
                        lineLimits = TextFieldLineLimits.SingleLine,
                        placeholder = {
                            Text(
                                text = "Text Field",
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

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // 추가 버튼
            Image(
                painter = painterResource(R.drawable.ic_add_circle),
                contentDescription = "추가",
                modifier = Modifier
                    .clickable(onClick = {
                        showTextField = !showTextField
                        onAddClick()
                    })
            )
        }

        // 드롭다운 메뉴 오버레이
        if (expandedItemId != null && dropdownPosition != null) {
            // 외부 클릭 시 닫기
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        expandedItemId = null
                        dropdownPosition = null
                    }
            )

            // 드롭다운 메뉴 표시
            val currentItemId = expandedItemId
            if (currentItemId != null && dropdownPosition != null) {
                // 더보기 버튼의 오른쪽 끝에 드롭다운 메뉴의 오른쪽 끝을 맞춤
                // 더보기 버튼은 약 16dp 크기
                Box(
                    modifier = Modifier
                        .offset(
                            x = with(density) {
                                dropdownPosition!!.x.toDp() + 16.dp - 91.dp
                            },
                            y = with(density) {
                                dropdownPosition!!.y.toDp() + 24.dp
                            }
                        )
                ) {
                    EditDropdownMenu(
                        onEditClick = {
                            val itemId = currentItemId
                            expandedItemId = null
                            dropdownPosition = null
                            onItemEditClick(itemId)
                        },
                        onDeleteClick = {
                            val itemId = currentItemId
                            expandedItemId = null
                            dropdownPosition = null
                            onItemDeleteClick(itemId)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProcessingMethodListPreview() {
    AfternoteTheme {
        ProcessingMethodList(
            items = listOf(
                ProcessingMethodItem("1", "게시물 내리기"),
                ProcessingMethodItem("2", "댓글 비활성화")
            ),
            onAddClick = {},
            onItemMoreClick = {},
            onItemEditClick = {},
            onItemDeleteClick = {},
            onItemAdded = {},
            onTextFieldVisibilityChanged = {},
            initialShowTextField = true
        )
    }
}
