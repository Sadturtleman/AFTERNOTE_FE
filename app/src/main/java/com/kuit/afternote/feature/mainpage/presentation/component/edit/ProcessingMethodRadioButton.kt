package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.SelectableRadioCard
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.AccountProcessingMethod
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.ProcessingMethodOption
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 처리 방법 라디오 버튼 컴포넌트 (공통)
 *
 * 피그마 디자인 기반:
 * - 너비: 350.dp
 * - 높이: 기본 102.dp (설명이 2줄인 경우 110.dp)
 * - 보더: 1.dp, 선택됨: B2 (#89C2FF), 선택 안 됨: 없음
 * - 배경: 흰색
 * - 패딩: 16.dp (모든 방향)
 * - Row: horizontalArrangement = spacedBy(16.dp), verticalAlignment = CenterVertically
 * - 라디오 버튼: 24dp
 * - 제목: 16sp, Medium, 선택됨: B1, 선택 안 됨: Gray9
 * - 설명: 14sp, Regular, Gray6
 */
@Composable
fun ProcessingMethodRadioButton(
    modifier: Modifier = Modifier,
    option: ProcessingMethodOption,
    selected: Boolean,
    onClick: () -> Unit,
    height: Dp = 102.dp
) {
    SelectableRadioCard(
        modifier = modifier
            .width(350.dp)
            .height(height),
        selected = selected,
        onClick = onClick,
        borderWhenUnselected = false
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = option.title,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    color = if (selected) B1 else Gray9
                )
            )

            Text(
                text = option.description,
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

@Preview(showBackground = true)
@Composable
private fun ProcessingMethodRadioButtonPreview() {
    AfternoteTheme {
        Column {
            // 선택된 상태
            ProcessingMethodRadioButton(
                option = AccountProcessingMethod.MEMORIAL_ACCOUNT,
                selected = true,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(8.dp))
            // 선택 안 된 상태
            ProcessingMethodRadioButton(
                option = AccountProcessingMethod.PERMANENT_DELETE,
                selected = false,
                onClick = {}
            )
        }
    }
}
