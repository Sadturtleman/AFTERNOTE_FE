package com.kuit.afternote.feature.receiver.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun ReceiveEndContent() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "열람 요청 완료",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Sansneo
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "현재 서류 확인중입니다.\n빠르게 처리하여 열람하실 수 있도록 하겠습니다.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = Sansneo
        )
    }
}
