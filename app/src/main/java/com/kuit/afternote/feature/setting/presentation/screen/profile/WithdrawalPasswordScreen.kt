package com.kuit.afternote.feature.setting.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.LabeledTextFieldStyle
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState

/**
 * Callbacks for the withdrawal password confirmation screen.
 */
data class WithdrawalPasswordCallbacks(
    val onBackClick: () -> Unit = {},
    val onWithdrawClick: () -> Unit = {},
    val onGoBackClick: () -> Unit = {}
)

/**
 * 회원 탈퇴 안내 - 비밀번호 확인 화면.
 *
 * Figma node: 4688:30519
 * - 헤더: "회원 탈퇴 안내"
 * - 안내 문구: "안전한 탈퇴 진행을 위해 비밀번호를 입력해 주세요."
 * - 탈퇴할 계정 카드
 * - 비밀번호 확인 입력 필드
 * - 탈퇴하기 / 이전으로 버튼
 *
 * @param accountDisplay 탈퇴할 계정 정보 (예: "박서연(example@mail.com)")
 * @param passwordState 비밀번호 입력 상태
 * @param callbacks 화면 콜백
 */
@Composable
fun WithdrawalPasswordScreen(
    modifier: Modifier = Modifier,
    accountDisplay: String = "",
    passwordState: TextFieldState = rememberTextFieldState(),
    callbacks: WithdrawalPasswordCallbacks = WithdrawalPasswordCallbacks()
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Gray1,
        topBar = {
            TopBar(
                title = "회원 탈퇴 안내",
                onBackClick = callbacks.onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 안내 문구
            Text(
                text = "안전한 탈퇴 진행을 위해 비밀번호를 입력해 주세요.",
                fontSize = 16.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray9,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 탈퇴할 계정 카드
            AccountCard(accountDisplay = accountDisplay)

            Spacer(modifier = Modifier.height(32.dp))

            // 비밀번호 확인 입력 필드
            OutlineTextField(
                label = "비밀번호 확인",
                textFieldState = passwordState,
                keyboardType = KeyboardType.Password,
                style = LabeledTextFieldStyle(
                    labelFontSize = 14.sp,
                    labelLineHeight = 20.sp,
                    labelSpacing = 4.dp
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            // 탈퇴하기 버튼
            ClickButton(
                color = B2,
                onButtonClick = callbacks.onWithdrawClick,
                title = "탈퇴하기"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 이전으로 버튼
            ClickButton(
                color = Gray4,
                onButtonClick = callbacks.onGoBackClick,
                title = "이전으로"
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun AccountCard(accountDisplay: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Gray2)
            .padding(16.dp)
    ) {
        Text(
            text = "탈퇴할 계정",
            fontSize = 16.sp,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Medium,
            color = Gray9,
            lineHeight = 22.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = accountDisplay,
            fontSize = 14.sp,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Normal,
            color = Gray9,
            lineHeight = 20.sp
        )
    }
}

// -- Previews --

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun WithdrawalPasswordScreenPreview() {
    AfternoteTheme(darkTheme = false) {
        WithdrawalPasswordScreen(
            accountDisplay = "박서연(example@mail.com)"
        )
    }
}
