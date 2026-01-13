package com.kuit.afternote.feature.receiver.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun RecordListItem(
    date: String,
    content: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp) // 살짝 그림자
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = date,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Gray9,
                fontFamily = Sansneo
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = content,
                fontSize = 16.sp,
                color = Gray6,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
