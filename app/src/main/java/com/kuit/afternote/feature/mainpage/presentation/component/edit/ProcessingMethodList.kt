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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
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
@Suppress("LongParameterList", "ComplexMethod")
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
    initialShowTextField: Boolean = false,
    initialExpandedItemId: String? = null
) {
    var showTextField by remember { mutableStateOf(initialShowTextField) }
    val textFieldState = rememberTextFieldState()
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

    // 각 아이템의 expanded 상태를 추적하기 위한 맵
    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }
    // 각 아이템의 위치를 추적하기 위한 맵
    val itemPositions = remember { mutableStateMapOf<String, Offset>() }
    // 각 아이템의 크기를 추적하기 위한 맵
    val itemSizes = remember { mutableStateMapOf<String, IntSize>() }
    // 부모 Box의 루트 위치
    var boxPositionInRoot by remember { mutableStateOf(Offset.Zero) }
    val density = LocalDensity.current

    items.forEach { item ->
        if (!expandedStates.containsKey(item.id)) {
            expandedStates[item.id] = initialExpandedItemId == item.id
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                boxPositionInRoot = coordinates.positionInRoot()
            }
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
                val expanded = expandedStates[item.id] ?: false

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            // 각 아이템의 위치와 크기 저장
                            itemPositions[item.id] = coordinates.positionInRoot()
                            itemSizes[item.id] = coordinates.size
                        }
                ) {
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
                            expandedStates[item.id] = !expanded
                        }
                    )
                }
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

        // 드롭다운 메뉴를 최상위 Box에 배치 (레이아웃 영향 없음)
        items.forEach { item ->
            val expanded = expandedStates[item.id] ?: false
            val position = itemPositions[item.id]
            val size = itemSizes[item.id]

            if (expanded && position != null && size != null) {
                Box(
                    modifier = Modifier
                        .offset(
                            // 체크박스 아랫변 기준: 위로 2dp, 오른쪽으로 2dp
                            x = with(density) {
                                (position.x + size.width - boxPositionInRoot.x).toDp() - 91.dp + 2.dp
                            },
                            y = with(density) {
                                (position.y + size.height - boxPositionInRoot.y).toDp() - 2.dp
                            }
                        )
                ) {
                    EditDropdownMenu(
                        onEditClick = {
                            expandedStates[item.id] = false
                            onItemEditClick(item.id)
                        },
                        onDeleteClick = {
                            expandedStates[item.id] = false
                            onItemDeleteClick(item.id)
                        }
                    )
                }
            }
        }

        // 드롭다운 메뉴 외부 클릭 시 닫기 (전체 화면 오버레이 - 상세 화면과 동일한 방식)
        val hasExpandedDropdown = expandedStates.values.any { it }
        if (hasExpandedDropdown) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        // 모든 드롭다운 닫기
                        expandedStates.keys.forEach { key ->
                            expandedStates[key] = false
                        }
                    }
            )
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

@Preview(showBackground = true, name = "드롭다운 펼쳐진 상태")
@Composable
private fun ProcessingMethodListWithDropdownPreview() {
    AfternoteTheme {
        ProcessingMethodList(
            items = listOf(
                ProcessingMethodItem("1", "게시물 내리기"),
                ProcessingMethodItem("2", "댓글 비활성화"),
                ProcessingMethodItem("3", "추모 계정으로 전환하기")
            ),
            onAddClick = {},
            onItemMoreClick = {},
            onItemEditClick = {},
            onItemDeleteClick = {},
            onItemAdded = {},
            onTextFieldVisibilityChanged = {},
            initialShowTextField = false,
            initialExpandedItemId = "1"
        )
    }
}
