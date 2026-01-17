package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.InformationProcessingMethod
import com.kuit.afternote.ui.theme.AfternoteTheme

/**
 * 정보 처리 방법 라디오 버튼 컴포넌트 (갤러리 및 파일용)
 */
@Composable
fun InformationProcessingRadioButton(
    modifier: Modifier = Modifier,
    method: InformationProcessingMethod,
    selected: Boolean,
    onClick: () -> Unit
) {
    ProcessingMethodRadioButton(
        modifier = modifier,
        option = method,
        selected = selected,
        onClick = onClick,
        height = 110.dp
    )
}

@Preview(showBackground = true)
@Composable
private fun InformationProcessingRadioButtonPreview() {
    AfternoteTheme {
        Column {
            InformationProcessingRadioButton(
                method = InformationProcessingMethod.TRANSFER_TO_RECIPIENT,
                selected = true,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(8.dp))
            InformationProcessingRadioButton(
                method = InformationProcessingMethod.TRANSFER_TO_ADDITIONAL_RECIPIENT,
                selected = false,
                onClick = {}
            )
        }
    }
}
