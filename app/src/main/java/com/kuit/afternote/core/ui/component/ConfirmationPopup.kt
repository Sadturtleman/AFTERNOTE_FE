package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kuit.afternote.ui.expand.dropShadow
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun ConfirmationPopup(
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    dismissText: String = "아니요",
    confirmText: String = "예"
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        ConfirmationPopupContent(
            message = message,
            onDismiss = onDismiss,
            onConfirm = onConfirm,
            modifier = modifier,
            dismissText = dismissText,
            confirmText = confirmText
        )
    }
}

@Composable
fun ConfirmationPopupContent(
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    dismissText: String = "아니요",
    confirmText: String = "예"
) {
    val containerShape = RoundedCornerShape(16.dp)
    val buttonShape = RoundedCornerShape(8.dp)
    val buttonTextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontFamily = Sansneo,
        fontWeight = FontWeight.Medium,
        color = Gray9,
        textAlign = TextAlign.Center
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .dropShadow(
                    shape = containerShape,
                    color = Color.Black.copy(alpha = 0.15f),
                    blur = 10.dp,
                    offsetX = 0.dp,
                    offsetY = 2.dp,
                    spread = 0.dp
                )
                .clip(containerShape)
                .background(Color.White)
                .padding(
                    horizontal = 24.dp,
                    vertical = 32.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        Text(
            text = message,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray9,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(7.5.dp))
            Text(
                text = dismissText,
                style = buttonTextStyle,
                modifier = Modifier
                    .weight(1f)
                    .dropShadow(
                        shape = buttonShape,
                        color = Color.Black.copy(alpha = 0.05f),
                        blur = 5.dp,
                        offsetX = 0.dp,
                        offsetY = 2.dp,
                        spread = 0.dp
                    )
                    .clip(buttonShape)
                    .background(Gray3)
                    .clickable(onClick = onDismiss)
                    .padding(
                        horizontal = 24.dp,
                        vertical = 16.dp
                    )
            )

            Text(
                text = confirmText,
                style = buttonTextStyle,
                modifier = Modifier
                    .weight(1f)
                    .dropShadow(
                        shape = buttonShape,
                        color = Color.Black.copy(alpha = 0.05f),
                        blur = 5.dp,
                        offsetX = 0.dp,
                        offsetY = 2.dp,
                        spread = 0.dp
                    )
                    .clip(buttonShape)
                    .background(B3)
                    .clickable(onClick = onConfirm)
                    .padding(
                        horizontal = 24.dp,
                        vertical = 16.dp
                    )
            )
            Spacer(modifier = Modifier.height(7.5.dp))
        }
    }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmationPopupPreview() {
    AfternoteTheme {
        ConfirmationPopupContent(
            message = "인스타그램에 대한 기록을 삭제하시겠습니까?\n삭제 시, 되돌릴 수 없습니다.",
            onDismiss = {},
            onConfirm = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmationPopupCustomButtonsPreview() {
    AfternoteTheme {
        ConfirmationPopupContent(
            message = "사망 프로토콜이 아직 실행되지 않았습니다.\n프로토콜을 실행하시겠습니까?",
            onDismiss = {},
            onConfirm = {},
            dismissText = "취소",
            confirmText = "실행"
        )
    }
}
