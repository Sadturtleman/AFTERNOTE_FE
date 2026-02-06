package com.kuit.afternote.feature.afternote.presentation.component.edit.processingmethod

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AccountProcessingMethod
import com.kuit.afternote.ui.theme.AfternoteTheme

/**
 * 계정 처리 방법 라디오 버튼 컴포넌트
 */
@Composable
fun AccountProcessingRadioButton(
    modifier: Modifier = Modifier,
    method: AccountProcessingMethod,
    selected: Boolean,
    onClick: () -> Unit
) {
    ProcessingMethodRadioButton(
        modifier = modifier,
        option = method,
        selected = selected,
        onClick = onClick,
//        height = 102.dp
    )
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
            Spacer(modifier = Modifier.height(8.dp))
            AccountProcessingRadioButton(
                method = AccountProcessingMethod.PERMANENT_DELETE,
                selected = false,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(8.dp))
            AccountProcessingRadioButton(
                method = AccountProcessingMethod.TRANSFER_TO_RECEIVER,
                selected = false,
                onClick = {}
            )
        }
    }
}
