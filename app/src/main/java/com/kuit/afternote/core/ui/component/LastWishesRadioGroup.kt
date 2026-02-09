package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
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
 * 남기고 싶은 당부 컴포넌트 (edit·view 공통).
 *
 * - **Edit mode**: [displayTextOnly]가 null이면 [options]로 라디오 그룹 표시.
 * - **View mode**: [displayTextOnly]가 non-null이면 라벨 + 테두리 Surface에 해당 텍스트만 표시 (수신자 화면).
 *
 * @param displayTextOnly View 모드일 때 표시할 단일 텍스트; null이면 edit 모드(라디오 옵션).
 */
@Composable
fun LastWishesRadioGroup(
    modifier: Modifier = Modifier,
    label: String = LABEL_LAST_WISH,
    options: List<LastWishOption> = emptyList(),
    selectedValue: String? = null,
    onOptionSelected: (String) -> Unit = {},
    displayTextOnly: String? = null
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
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                options.forEach { option ->
                    val selected = selectedValue == option.value
                    SelectableRadioCard(
                        modifier = Modifier.fillMaxWidth(),
                        selected = selected,
                        onClick = { onOptionSelected(option.value) },
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
            }
        }
    }
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
                onOptionSelected = {}
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
