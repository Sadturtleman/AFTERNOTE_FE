package com.kuit.afternote.feature.receiverauth.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
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
            keyboardType = KeyboardType.Text
        )
    }
}

/**
 * ViewModel 단일 소스 연동용 마스터 키 입력 콘텐츠.
 *
 * @param value 마스터 키 문자열 (ViewModel uiState.masterKeyInput)
 * @param onValueChange 입력 변경 시 호출 (ViewModel.updateMasterKey)
 * @param isError 에러 표시 여부 (예: errorMessage non-null)
 */
@Composable
fun MasterKeyInputContent(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false
) {
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

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("마스터 키 입력") },
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors()
        )
    }
}
