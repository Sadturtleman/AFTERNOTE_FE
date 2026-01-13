package com.kuit.afternote.feature.receiver.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.CircleCheckBox
import com.kuit.afternote.ui.theme.Gray9

@Composable
fun CheckItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CircleCheckBox(
            checked = true,
            onCheckedChange = {}
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            fontSize = 14.sp,
            color = Gray9
        )
    }
}
