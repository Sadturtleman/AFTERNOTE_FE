package com.kuit.afternote.feature.receiver.presentation.component

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
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun MasterKeyInputContent(masterKey: TextFieldState) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "마스터 키 입력",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Sansneo
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "고인에게 전달받은 마스터 키를 입력해주세요.",
            fontFamily = Sansneo,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Gray6
        )

        Spacer(modifier = Modifier.height(44.dp))

        OutlineTextField(
            textFieldState = masterKey,
            label = "마스터 키 입력",
            keyboardType = KeyboardType.Number
        )
    }
}
