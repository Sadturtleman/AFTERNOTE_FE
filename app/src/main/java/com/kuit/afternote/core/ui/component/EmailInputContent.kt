package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Sansneo
import org.w3c.dom.Text

@Composable
fun EmailInputContent(email: TextFieldState, authCode: TextFieldState, onAuthClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "수신자 본인 확인",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Sansneo
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "고인의 소중한 정보를 보호하기 위해\n가족관계 및 사망 사실 확인이 필요합니다.",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            fontFamily = Sansneo,
            color = Gray6
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlineTextField(
            textFieldState = email,
            label = "이메일 주소",
            onAuthClick = { onAuthClick() },
            keyboardType = KeyboardType.Uri
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlineTextField(
            textFieldState = authCode,
            label = "인증번호",
            keyboardType = KeyboardType.Number
        )
    }
}
