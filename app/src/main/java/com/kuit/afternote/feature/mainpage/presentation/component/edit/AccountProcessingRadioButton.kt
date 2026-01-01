package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.CustomRadioButton
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

/**
 * 계정 처리 방법 라디오 버튼 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 너비: 350.dp
 * - 높이: 102.dp
 * - 보더: 1.dp, B2 색상 (#89C2FF)
 * - 배경: 흰색
 * - 패딩: 16.dp (모든 방향)
 * - Row: horizontalArrangement = spacedBy(16.dp), verticalAlignment = CenterVertically
 * - 라디오 버튼: 24dp
 * - 제목: 16sp, Medium, 선택됨: B2, 선택 안 됨: Gray9
 * - 설명: 14sp, Regular, Gray6
 */
@Composable
fun AccountProcessingRadioButton(
    modifier: Modifier = Modifier,
    method: AccountProcessingMethod,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = B2,
                shape = RoundedCornerShape(16.dp)
            )
            .width(350.dp)
            .height(102.dp)
            .background(
                color = White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp)
        ,
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            CustomRadioButton(
                selected = selected,
                onClick = onClick,
                buttonSize = 24.dp,
                selectedColor = B2
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = method.title,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Medium,
                        color = if (selected) B1 else Gray9
                    )
                )

                Text(
                    text = method.description,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Normal,
                        color = Gray6
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AccountProcessingRadioButtonPreview() {
    AfternoteTheme {
        Column {
            AccountProcessingRadioButton(
                method = AccountProcessingMethod.MEMORIAL_ACCOUNT,
                selected = true,
                onClick = {}
            )
        }
    }
}
