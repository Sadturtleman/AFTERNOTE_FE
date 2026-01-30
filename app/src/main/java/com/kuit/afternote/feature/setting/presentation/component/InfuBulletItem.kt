package com.kuit.afternote.feature.setting.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun InfoBulletItem(text: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text("•", fontSize = 14.sp, color = Gray9)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = Gray9,
            lineHeight = 20.sp,
            fontFamily = Sansneo
        )
    }
}
