package com.kuit.afternote.feature.setting.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.feature.setting.presentation.component.KeyPad
import com.kuit.afternote.feature.setting.presentation.component.PasscodeIndicator
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Sansneo
import org.checkerframework.checker.units.qual.K

@Composable
fun PasswordModifyScreen(){
    Scaffold(
        topBar = {
            TopBar(title = "앱 잠금 비밀번호 설정") {}
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Image(
                painter = painterResource(R.drawable.lock),
                contentDescription = null,
                modifier = Modifier.size(56.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "비밀번호를 입력해 주세요",
                fontFamily = Sansneo,
                fontSize = 18.sp,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            PasscodeIndicator(totalDots = 4, filledDots = 3)

            KeyPad(modifier = Modifier.padding(horizontal = 20.dp))
        }
    }
}

@Preview
@Composable
private fun PasswordModifyScreenPreview(){
    PasswordModifyScreen()
}
