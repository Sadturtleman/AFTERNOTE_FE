package com.kuit.afternote.feature.mainpage.presentation.component.edit.memorial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.SelectableRadioCard
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.Spacing

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
 * 남기고 싶은 당부 라디오 버튼 그룹 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 라벨: 12sp, Regular, Gray9
 * - 라벨과 옵션 간 간격: 6dp
 * - 옵션 간 간격: 8dp
 * - 카드 너비: fillMaxWidth
 * - 카드 높이: 자동 (내용에 따라)
 * - 보더: 1dp, 선택됨: B2 (#89C2FF), 선택 안 됨: Gray4 (#BDBDBD)
 * - 배경: 흰색
 * - 패딩: 16dp (모든 방향)
 * - Row: horizontalArrangement = spacedBy(16.dp), verticalAlignment = CenterVertically
 * - 라디오 버튼: 24dp
 * - 텍스트: 16sp, Regular, 선택됨: B1 (#328BFF), 선택 안 됨: Gray9 (#212121)
 */
@Composable
fun LastWishesRadioGroup(
    modifier: Modifier = Modifier,
    label: String = "남기고 싶은 당부",
    options: List<LastWishOption>,
    selectedValue: String?,
    onOptionSelected: (String) -> Unit
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

        Column(
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            options.forEach { option ->
                LastWishRadioButton(
                    option = option,
                    selected = selectedValue == option.value,
                    onClick = { onOptionSelected(option.value) }
                )
            }
        }
    }
}

/**
 * 남기고 싶은 당부 개별 라디오 버튼 컴포넌트
 */
@Composable
private fun LastWishRadioButton(
    modifier: Modifier = Modifier,
    option: LastWishOption,
    selected: Boolean,
    onClick: () -> Unit
) {
    SelectableRadioCard(
        modifier = modifier.fillMaxWidth(),
        selected = selected,
        onClick = onClick,
        borderWhenUnselected = true
    ) {
        Text(
            text = option.text,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = if (selected) B1 else Gray9
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LastWishesRadioGroupPreview() {
    AfternoteTheme {
        val options = listOf(
            LastWishOption(
                text = "차분하고 조용하게 보내주세요.",
                value = "calm"
            ),
            LastWishOption(
                text = "슬퍼 하지 말고 밝고 따뜻하게 보내주세요.",
                value = "bright"
            ),
            LastWishOption(
                text = "기타(직접 입력)",
                value = "other"
            )
        )

        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // 선택된 상태 (첫 번째 옵션)
            LastWishesRadioGroup(
                label = "남기고 싶은 당부",
                options = options,
                selectedValue = "calm",
                onOptionSelected = {}
            )

            Spacer(modifier = Modifier.height(Spacing.l))

            // 선택 안 된 상태
            LastWishesRadioGroup(
                label = "남기고 싶은 당부",
                options = options,
                selectedValue = null,
                onOptionSelected = {}
            )

            Spacer(modifier = Modifier.height(Spacing.l))

            // 두 번째 옵션 선택
            LastWishesRadioGroup(
                label = "남기고 싶은 당부",
                options = options,
                selectedValue = "bright",
                onOptionSelected = {}
            )
        }
    }
}
