package com.kuit.afternote.feature.setting.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.ui.expand.dropShadow
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * Post-withdrawal completion dialog. Figma node: 4688-30712.
 *
 * White card with two lines of text and a single "확인하기" button.
 * Shown after account deletion succeeds; on confirm dismisses and caller should navigate (e.g. to splash).
 */
@Composable
fun WithdrawalCompleteDialog(
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onConfirm,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        val shape = RoundedCornerShape(16.dp)
        Column(
            modifier = modifier
                .fillMaxWidth()
                .dropShadow(
                    shape = shape,
                    color = Color.Black.copy(alpha = 0.15f),
                    blur = 10.dp,
                    offsetX = 0.dp,
                    offsetY = 2.dp,
                    spread = 0.dp
                )
                .clip(shape)
                .background(Color.White)
                .padding(
                    horizontal = 24.dp,
                    vertical = 40.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.withdrawal_complete_line1),
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray9,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.withdrawal_complete_line2),
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray9,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            ClickButton(
                color = B3,
                onButtonClick = onConfirm,
                title = stringResource(R.string.withdrawal_complete_confirm)
            )
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun WithdrawalCompleteDialogPreview() {
    AfternoteTheme(darkTheme = false) {
        WithdrawalCompleteDialog(onConfirm = {})
    }
}
