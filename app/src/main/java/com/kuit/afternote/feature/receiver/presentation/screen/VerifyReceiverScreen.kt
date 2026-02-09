package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.core.uimodel.PhoneNumberVisualTransformation
import com.kuit.afternote.feature.receiver.presentation.component.OtpInputField
import com.kuit.afternote.feature.receiver.presentation.uimodel.VerifySelfStep
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray3
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun VerifyReceiverScreen(onBackClick: () -> Unit) {
    var step by remember { mutableStateOf(VerifySelfStep.START) }
    val phoneNumber = rememberTextFieldState()
    var masterCode by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopBar(
                title = "수신자 인증",
                onBackClick = {
                    step.previous()?.let { step = it } ?: onBackClick()
                },
                step = step
            )
        }
    ) { paddingValues ->
        // 공통 레이아웃 템플릿 호출
        AuthBaseScreen(
            paddingValues = paddingValues,
            buttonContent = {
                val (title, action, enabled) = when (step) {
                    VerifySelfStep.START -> Triple("인증 시작하기", { step = VerifySelfStep.PHONE_AUTH }, true)
                    VerifySelfStep.PHONE_AUTH -> Triple("다음", { step = VerifySelfStep.MASTER_CODE }, phoneNumber.text.length == 11)
                    VerifySelfStep.MASTER_CODE -> Triple("다음", { /* 완료 로직 */ }, masterCode.length == 6)
                }

                ClickButton(
                    title = title,
                    onButtonClick = action,
                    isTrue = enabled
                )
            }
        ) {
            // 상단 콘텐츠 영역 (Step에 따라 변경)
            when (step) {
                VerifySelfStep.START -> {
                    Spacer(Modifier.height(40.dp))
                    Text("수신자 본인 확인", fontSize = 24.sp, fontFamily = Sansneo, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(15.dp))
                    Text("고객의 소중한 정보를 보호하기 위해\n가족관계 및 사망 사실 확인이 필요합니다.", fontSize = 16.sp, color = Gray3, fontFamily = Sansneo)
                }
                VerifySelfStep.PHONE_AUTH -> {
                    Spacer(Modifier.height(40.dp))
                    Text("수신자 본인 확인", fontSize = 24.sp, fontFamily = Sansneo, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(15.dp))
                    Text("수신자님 본인 확인을 위해\n작성하신 휴대폰 번호로 인증번호를 보내드립니다.", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = Gray6, fontFamily = Sansneo)
                    Spacer(Modifier.height(25.dp))
                    OutlineTextField(textFieldState = phoneNumber, outputTransformation = PhoneNumberVisualTransformation(), label = "000-0000-0000")
                }
                VerifySelfStep.MASTER_CODE -> {
                    Spacer(Modifier.height(40.dp))
                    Text("수신자 본인 확인", fontSize = 24.sp, fontFamily = Sansneo, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(15.dp))
                    Text("입력하신 휴대폰 번호로 인증번호를 보냈습니다.\n수신된 문자에서 인증번호를 확인하고 입력해주세요.", fontWeight = FontWeight.Medium, fontSize = 16.sp, color = Gray6, fontFamily = Sansneo)
                    Spacer(Modifier.height(25.dp))
                    OtpInputField(otpText = masterCode, onOtpTextChange = { newValue, _ -> masterCode = newValue })
                }
            }
        }
    }
}

/**
 * 재사용 가능한 인증 화면 템플릿
 * - content: 상단에 배치될 텍스트나 입력 필드
 * - buttonContent: 화면 정중앙에 배치될 버튼
 */
@Composable
private fun AuthBaseScreen(
    paddingValues: PaddingValues,
    buttonContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 20.dp) // 공통 여백
    ) {
        // 1. 상단 콘텐츠 (항상 위쪽에 위치)
        Column(
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            content()
        }

        // 2. 버튼 (항상 화면 정중앙에 위치)
        // 콘텐츠 양이 많아도 버튼 위치는 변하지 않습니다.
        Box(
            modifier = Modifier.align(Alignment.Center)
        ) {
            buttonContent()
        }
    }
}
@Preview
@Composable
private fun VerifyReceiverScreenPreview(){
    VerifyReceiverScreen {  }
}
