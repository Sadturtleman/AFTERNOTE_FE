package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.kuit.afternote.ui.expand.bottomBorder
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray3
import com.kuit.afternote.ui.theme.Gray8
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

/**
 * 선택 드롭다운 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 라벨: 12sp, Regular, Gray9
 * - 드롭다운 필드: 흰색 배경, 하단 보더, 36dp 높이
 * - 선택된 값: 16sp, Regular, Gray8
 * - 드롭다운 아이콘: 오른쪽 정렬
 * - 드롭다운 메뉴 offset: 기본 4.dp
 */
@Composable
fun SelectionDropdown(
    modifier: Modifier = Modifier,
    label: String,
    selectedValue: String,
    options: List<String>,
    onValueSelected: (String) -> Unit
) {
    SelectionDropdown(
        modifier = modifier,
        label = label,
        selectedValue = selectedValue,
        options = options,
        onValueSelected = onValueSelected,
        menuOffset = 4.dp
    )
}

/**
 * 선택 드롭다운 컴포넌트 (메뉴 offset 지정 가능)
 *
 * @param menuOffset 드롭다운 메뉴가 필드 아래 나타나는 간격 (기본: 4.dp)
 * @param menuBackgroundColor 드롭다운 메뉴 배경색 (기본: White)
 * DropdownMenu를 사용하여 정확한 offset 조정이 가능합니다.
 */
@Composable
fun SelectionDropdown(
    modifier: Modifier = Modifier,
    label: String,
    selectedValue: String,
    options: List<String>,
    onValueSelected: (String) -> Unit,
    menuOffset: Dp = 4.dp,
    menuBackgroundColor: Color = White
) {
    var expanded by remember { mutableStateOf(false) }
    var boxWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp
        )
    ) {
        // 라벨
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

        // 드롭다운 필드
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .onGloballyPositioned { coordinates ->
                        boxWidth = with(density) { coordinates.size.width.toDp() }
                    }.clickable { expanded = !expanded }
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
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(x = 0.dp, y = menuOffset),
                modifier = Modifier
                    .then(if (boxWidth > 0.dp) Modifier.width(boxWidth) else Modifier.fillMaxWidth())
                    .shadow(elevation = 0.dp)
                    .background(menuBackgroundColor)
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
                                    fontWeight = FontWeight.Normal,
                                    color = Gray9,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        onClick = {
                            onValueSelected(option)
                            expanded = false
                        }
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
            label = "종류",
            selectedValue = "소셜네트워크",
            options = listOf("소셜네트워크", "비즈니스", "갤러리 및 파일"),
            onValueSelected = {}
        )
    }
}
