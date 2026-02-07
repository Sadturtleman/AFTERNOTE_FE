package com.kuit.afternote.feature.afternote.presentation.component.edit.processingmethod

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.SelectableRadioCard
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AccountProcessingMethod
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.ProcessingMethodOption
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 처리 방법 라디오 버튼 컴포넌트 (공통)
 *
 * 피그마 디자인 기반:
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
) {
    SelectableRadioCard(
        modifier = modifier
            .fillMaxWidth(),
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
