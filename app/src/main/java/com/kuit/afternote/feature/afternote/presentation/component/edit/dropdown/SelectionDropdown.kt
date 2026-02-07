package com.kuit.afternote.feature.afternote.presentation.component.edit.dropdown

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.Label
import com.kuit.afternote.core.ui.component.LabelStyle
import com.kuit.afternote.ui.expand.bottomBorder
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray3
import com.kuit.afternote.ui.theme.Gray8
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

private const val CATEGORY_SOCIAL_NETWORK = "소셜네트워크"
private const val CATEGORY_BUSINESS = "비즈니스"
private const val CATEGORY_GALLERY_AND_FILE_PREVIEW = "갤러리 및 파일"

/**
 * Label configuration for [SelectionDropdown].
 */
data class SelectionDropdownLabelParams(
    val label: String,
    val isRequired: Boolean = false,
    val labelStyle: LabelStyle = LabelStyle(
        fontSize = 12.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.Normal,
        requiredDotOffsetY = 2.dp
    )
)

/**
 * 드롭다운 메뉴 스타일 설정
 *
 * @param menuOffset 드롭다운 메뉴가 필드 아래 나타나는 간격 (기본: 4.dp)
 * @param menuBackgroundColor 드롭다운 메뉴 배경색 (기본: White)
 * @param shadowElevation 드롭다운 메뉴 그림자 elevation (기본: 0.dp)
 * @param tonalElevation 드롭다운 메뉴 톤 elevation (기본: 0.dp)
 */
data class DropdownMenuStyle(
    val menuOffset: Dp = 4.dp,
    val menuBackgroundColor: Color = White,
    val shadowElevation: Dp = 0.dp,
    val tonalElevation: Dp = 0.dp
)

/**
 * 선택 드롭다운 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 라벨: 기본 12sp, Regular, Gray9 (LabelStyle로 커스터마이징 가능)
 * - 드롭다운 필드: 흰색 배경, 하단 보더
 * - 선택된 값: 16sp, Regular, Gray8
 * - 드롭다운 아이콘: 오른쪽 정렬
 * - 드롭다운 메뉴 offset: 기본 4.dp
 *
 * @param modifier Modifier for the component
 * @param labelParams Label text, required indicator, and style
 * @param selectedValue Currently selected value
 * @param options List of selectable options
 * @param onValueSelected Callback when an option is selected
 * @param menuStyle Style configuration for the dropdown menu
 * @param state State holder for the dropdown
 */
@Composable
fun SelectionDropdown(
    modifier: Modifier = Modifier,
    labelParams: SelectionDropdownLabelParams,
    selectedValue: String,
    options: List<String>,
    onValueSelected: (String) -> Unit,
    menuStyle: DropdownMenuStyle = DropdownMenuStyle(),
    state: SelectionDropdownState = rememberSelectionDropdownState()
) {
    val density = LocalDensity.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp
        )
    ) {
        // 라벨
        Label(
            text = labelParams.label,
            isRequired = labelParams.isRequired,
            style = labelParams.labelStyle
        )

        // 드롭다운 필드
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
//                    .height(36.dp)
                    .onGloballyPositioned { coordinates ->
                        val newWidth = with(density) { coordinates.size.width.toDp() }
                        if (newWidth != state.boxWidth) {
                            state.boxWidth = newWidth
                        }
                    }.clickable { state.expanded = !state.expanded }
                    .bottomBorder(color = Gray3, width = 0.5.dp)
                    .padding(all = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selectedValue,
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Normal,
                            color = Gray8
                        )
                    )

                    Image(
                        painter = painterResource(R.drawable.ic_dropdown_vector),
                        contentDescription = "드롭다운"
                    )
                }
            }

            // 드롭다운 메뉴
            DropdownMenu(
                expanded = state.expanded,
                onDismissRequest = { state.expanded = false },
                offset = DpOffset(x = 0.dp, y = menuStyle.menuOffset),
                containerColor = menuStyle.menuBackgroundColor,
                shadowElevation = menuStyle.shadowElevation,
                tonalElevation = menuStyle.tonalElevation,
                modifier = Modifier
                    .then(if (state.boxWidth > 0.dp) Modifier.width(state.boxWidth) else Modifier.fillMaxWidth())
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 22.sp,
                                    fontFamily = Sansneo,
                                    fontWeight = FontWeight.Medium,
                                    color = Gray9,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        onClick = {
                            onValueSelected(option)
                            state.expanded = false
                        },
                        contentPadding = PaddingValues(vertical = 16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectionDropdownPreview() {
    AfternoteTheme {
        SelectionDropdown(
            labelParams = SelectionDropdownLabelParams(label = "종류"),
            selectedValue = CATEGORY_SOCIAL_NETWORK,
            options = listOf(CATEGORY_SOCIAL_NETWORK, CATEGORY_BUSINESS, CATEGORY_GALLERY_AND_FILE_PREVIEW),
            onValueSelected = {}
        )
    }
}

@Preview(showBackground = true, name = "Required label")
@Composable
private fun SelectionDropdownRequiredLabelPreview() {
    AfternoteTheme {
        SelectionDropdown(
            labelParams = SelectionDropdownLabelParams(
                label = "관계",
                isRequired = true,
                labelStyle = LabelStyle(
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Medium
                )
            ),
            selectedValue = "딸",
            options = listOf("딸", "아들", "친구", "가족"),
            onValueSelected = {}
        )
    }
}

@Preview(
    showBackground = true,
    name = "펼쳐진 드롭다운 메뉴 (기본)"
)
@Composable
private fun ExpandedDropdownMenuPreview() {
    AfternoteTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = {},
                offset = DpOffset(x = 0.dp, y = 4.dp),
                containerColor = White,
                shadowElevation = 0.dp,
                tonalElevation = 0.dp,
                modifier = Modifier.width(200.dp)
            ) {
                listOf(CATEGORY_SOCIAL_NETWORK, CATEGORY_BUSINESS, CATEGORY_GALLERY_AND_FILE_PREVIEW).forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 22.sp,
                                    fontFamily = Sansneo,
                                    fontWeight = FontWeight.Normal,
                                    color = Gray9,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "펼쳐진 드롭다운 메뉴 (높은 Elevation)"
)
@Composable
private fun ExpandedDropdownMenuWithElevationPreview() {
    AfternoteTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = {},
                offset = DpOffset(x = 0.dp, y = 4.dp),
                containerColor = White,
                shadowElevation = 10.dp,
                tonalElevation = 10.dp,
                modifier = Modifier.width(200.dp)
            ) {
                listOf("인스타그램", "페이스북", "트위터", "링크드인").forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 22.sp,
                                    fontFamily = Sansneo,
                                    fontWeight = FontWeight.Normal,
                                    color = Gray9,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "펼쳐진 드롭다운 메뉴 (다이얼로그 스타일)"
)
@Composable
private fun ExpandedDropdownMenuInDialogPreview() {
    AfternoteTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = {},
                offset = DpOffset(x = 0.dp, y = 5.2.dp),
                containerColor = Gray1,
                shadowElevation = 0.dp,
                tonalElevation = 0.dp,
                modifier = Modifier.width(200.dp)
            ) {
                listOf("친구", "가족", "연인", "동료").forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 22.sp,
                                    fontFamily = Sansneo,
                                    fontWeight = FontWeight.Normal,
                                    color = Gray9,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "펼쳐진 드롭다운 메뉴 (긴 리스트)"
)
@Composable
private fun ExpandedDropdownMenuLongListPreview() {
    AfternoteTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = {},
                offset = DpOffset(x = 0.dp, y = 4.dp),
                containerColor = White,
                shadowElevation = 0.dp,
                tonalElevation = 0.dp,
                modifier = Modifier.width(200.dp)
            ) {
                listOf(
                    "카카오톡",
                    "인스타그램",
                    "페이스북",
                    "트위터",
                    "링크드인",
                    "텔레그램",
                    "디스코드",
                    "슬랙"
                ).forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    lineHeight = 22.sp,
                                    fontFamily = Sansneo,
                                    fontWeight = FontWeight.Normal,
                                    color = Gray9,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        onClick = {}
                    )
                }
            }
        }
    }
}
