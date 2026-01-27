package com.kuit.afternote.feature.mainpage.presentation.component.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kuit.afternote.core.ui.component.ConfirmationPopup
import com.kuit.afternote.core.ui.component.ConfirmationPopupContent
import com.kuit.afternote.ui.theme.AfternoteTheme

@Composable
fun DeleteConfirmDialog(
    serviceName: String = "인스타그램",
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    ConfirmationPopup(
        message = "${serviceName}에 대한 기록을 삭제하시겠습니까?" +
            "\n삭제 시, 되돌릴 수 없습니다.",
        onDismiss = onDismiss,
        onConfirm = onConfirm
    )
}

@Composable
fun DeleteConfirmDialogContent(
    serviceName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConfirmationPopupContent(
        message = "${serviceName}에 대한 기록을 삭제하시겠습니까?" +
            "\n삭제 시, 되돌릴 수 없습니다.",
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun DeleteConfirmDialogPreview() {
    AfternoteTheme {
        DeleteConfirmDialog()
    }
}
