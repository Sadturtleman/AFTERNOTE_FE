package com.kuit.afternote.feature.home.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun RecipientBadge(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.home_recipient_complete),
    showCheckIcon: Boolean = true,
    onCLick: () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = B3,
        onClick = onCLick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showCheckIcon) {
                Icon(
                    painter = painterResource(R.drawable.ic_check_small),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = androidx.compose.ui.graphics.Color.Unspecified
                )
            }
            Text(
                text = text,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = Gray9
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipientBadgePreview() {
    AfternoteTheme {
        RecipientBadge()
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipientBadgeNoIconPreview() {
    AfternoteTheme {
        RecipientBadge(showCheckIcon = false)
    }
}
