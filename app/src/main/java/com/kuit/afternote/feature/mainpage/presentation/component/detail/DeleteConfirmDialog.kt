package com.kuit.afternote.feature.mainpage.presentation.component.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.kuit.afternote.ui.theme.TextPrimary

@Composable
fun DeleteConfirmDialog(
    serviceName: String = "인스타그램",
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        DeleteConfirmDialogContent(
            serviceName = serviceName,
            onDismiss = onDismiss,
            onConfirm = onConfirm
        )
    }
}

@Composable
fun DeleteConfirmDialogContent(
    serviceName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .dropShadow(
                shape = RoundedCornerShape(16.dp),
                color = Color.Black.copy(alpha = 0.15f),
                blur = 10.dp,
                offsetX = 0.dp,
                offsetY = 2.dp,
                spread = 0.dp
            ).clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(horizontal = 33.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${serviceName}에 대한 기록을 삭제하시겠습니까?" +
                "\n삭제 시, 되돌릴 수 없습니다.",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .padding(
                    bottom = 20.dp
                )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "아니요",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    color = Gray9,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .width(136.dp)
                    .dropShadow(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.05f),
                        blur = 5.dp,
                        offsetX = 0.dp,
                        offsetY = 2.dp,
                        spread = 0.dp
                    ).clip(RoundedCornerShape(8.dp))
                    .background(Gray3)
                    .clickable(onClick = onDismiss)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .weight(1f)
            )

            Text(
                text = "예",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    color = Gray9,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .width(136.dp)
                    .dropShadow(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.05f),
                        blur = 5.dp,
                        offsetX = 0.dp,
                        offsetY = 2.dp,
                        spread = 0.dp
                    ).clip(RoundedCornerShape(8.dp))
                    .background(B3)
                    .clickable(onClick = onConfirm)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteConfirmDialogPreview() {
    AfternoteTheme {
        DeleteConfirmDialog()
    }
}
