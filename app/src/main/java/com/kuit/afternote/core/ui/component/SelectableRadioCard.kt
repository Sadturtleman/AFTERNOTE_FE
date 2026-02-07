package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.button.CustomRadioButton
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

/**
 * 선택 가능한 라디오 버튼 카드 컴포넌트 (Slot API 패턴)
 *
 * 공통 껍데기를 제공하고, 내용물(content)은 호출부에서 주입받는 방식입니다.
 * Material Design의 Slot API 패턴을 따릅니다.
 *
 * @param selected 선택 여부
 * @param onClick 클릭 이벤트
 * @param modifier Modifier
 * @param borderWhenUnselected 선택 안 됨일 때 보더 표시 여부 (기본: false)
 * @param radioButtonSpacing 라디오 버튼과 콘텐츠 간 간격 (기본: 16.dp)
 * @param content 콘텐츠 Slot (ColumnScope를 받아 세로 레이아웃 구성 가능)
 */
@Composable
fun SelectableRadioCard(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderWhenUnselected: Boolean = false,
    radioButtonSpacing: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val borderColor = when {
        selected -> B2
        borderWhenUnselected -> Gray4
        else -> Color.Transparent
    }

    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = modifier.selectable(
            selected = selected,
            onClick = onClick,
            role = Role.RadioButton,
            interactionSource = interactionSource,
            indication = null
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        color = White
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 내부 라디오 버튼은 클릭 이벤트 null 처리하여 부모 Surface 클릭과 충돌 방지
            CustomRadioButton(
                selected = selected,
                onClick = null,
                buttonSize = 24.dp,
                selectedColor = B2,
                unselectedColor = Gray4
            )

            Spacer(modifier = Modifier.width(radioButtonSpacing))

            Column {
                content()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectableRadioCardPreview() {
    AfternoteTheme {
        Column {
            // 선택됨 - 단순 텍스트
            SelectableRadioCard(
                selected = true,
                onClick = {},
                content = {
                    androidx.compose.material3.Text(
                        text = "선택된 옵션",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Medium,
                            color = Gray9
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 선택 안 됨 - 단순 텍스트
            SelectableRadioCard(
                selected = false,
                onClick = {},
                content = {
                    androidx.compose.material3.Text(
                        text = "선택 안 된 옵션",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Medium,
                            color = Gray9
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 선택됨 - 제목 + 설명
            SelectableRadioCard(
                selected = true,
                onClick = {},
                content = {
                    androidx.compose.material3.Text(
                        text = "제목 텍스트",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Medium,
                            color = Gray9
                        )
                    )
                    androidx.compose.material3.Text(
                        text = "설명 텍스트",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Normal,
                            color = Gray9
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 선택 안 됨 - borderWhenUnselected = true
            SelectableRadioCard(
                selected = false,
                onClick = {},
                borderWhenUnselected = true,
                content = {
                    androidx.compose.material3.Text(
                        text = "보더가 있는 선택 안 된 옵션",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Medium,
                            color = Gray9
                        )
                    )
                }
            )
        }
    }
}
