package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kuit.afternote.ui.theme.AfternoteTheme

@Composable
fun CircleCheckBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    size: Dp = 20.dp
) {
    Surface(
        modifier = Modifier
            .size(size)
            .clickable { onCheckedChange(!checked) },
        shape = CircleShape,
        color = if (checked) Color(0xFFBDBDBD) else Color.Transparent,
        border = if (checked) {
            null
        } else {
            BorderStroke(
                1.5.dp,
                Color(0xFFBDBDBD)
            )
        }
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CircleCheckBoxPreview() {
    AfternoteTheme {
        Row {
            CircleCheckBox(
                checked = true,
                onCheckedChange = {}
            )
            CircleCheckBox(
                checked = false,
                onCheckedChange = {}
            )
        }
    }
}
