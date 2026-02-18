package com.kuit.afternote.feature.setting.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.LabeledTextFieldStyle
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Red
import com.kuit.afternote.ui.theme.Sansneo

/**
 * Callbacks for the withdrawal confirmation screen.
 */
data class WithdrawalPasswordCallbacks(
    val onBackClick: () -> Unit = {},
    val onWithdrawClick: (confirmationText: String) -> Unit = {},
    val onGoBackClick: () -> Unit = {},
    val onInputChanged: () -> Unit = {},
)

/**
 * 회원 탈퇴 안내 - 문장 확인 화면.
 *
 * Figma node: 4688:30519, 4688:30596
 * - 헤더: "회원 탈퇴 안내"
 * - 안내 문구: "안전한 탈퇴 진행을 위해 아래 문장을 입력해 주세요."
 * - 탈퇴할 계정 카드 (Gray2, 16dp radius)
 * - 확인 문장 입력 필드 (placeholder: "탈퇴하겠습니다.")
 * - 에러 메시지 (문장 불일치 시 빨간 텍스트)
 * - 탈퇴하기 (B3) / 이전으로 (Gray4) 버튼
 *
 * @param accountDisplay 탈퇴할 계정 정보 (예: "박서연(example@mail.com)")
 * @param confirmationState 확인 문장 입력 상태
 * @param sentenceError 문장 불일치 시 표시할 에러 메시지 (null이면 숨김)
 * @param callbacks 화면 콜백
 */
@Composable
fun WithdrawalPasswordScreen(
    modifier: Modifier = Modifier,
    accountDisplay: String = "",
    confirmationState: TextFieldState = rememberTextFieldState(),
    sentenceError: String? = null,
    callbacks: WithdrawalPasswordCallbacks = WithdrawalPasswordCallbacks(),
) {
    LaunchedEffect(confirmationState.text) {
        callbacks.onInputChanged()
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Gray1,
        topBar = {
            TopBar(
                title = "회원 탈퇴 안내",
                onBackClick = callbacks.onBackClick,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 안내 문구
            Text(
                text = "안전한 탈퇴 진행을 위해 아래 문장을 입력해 주세요.",
                fontSize = 16.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray9,
                lineHeight = 22.sp,
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 탈퇴할 계정 카드
            AccountCard(accountDisplay = accountDisplay)

            Spacer(modifier = Modifier.height(16.dp))

            // 확인 문장 입력 필드 (labeled 스타일 없이, 플레이스홀더만 있는 filled 필드)
            OutlineTextField(
                label = stringResource(R.string.withdrawal_confirmation_sentence),
                textFieldState = confirmationState,
                style = LabeledTextFieldStyle(
                    labelFontSize = 0.sp,
                    labelLineHeight = 0.sp,
                    labelSpacing = 0.dp,
                    labelColor = Gray1,
                ),
                placeholder = stringResource(R.string.withdrawal_confirmation_sentence),
            )

            if (sentenceError != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = sentenceError,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 12.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    color = Red,
                    lineHeight = 18.sp,
                )
            }

            Spacer(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .height(64.dp),
            )

            // 탈퇴하기 버튼
            ClickButton(
                color = B3,
                onButtonClick = {
                    callbacks.onWithdrawClick(confirmationState.text.toString())
                },
                title = "탈퇴하기",
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 이전으로 버튼
            ClickButton(
                color = Gray4,
                onButtonClick = callbacks.onGoBackClick,
                title = "이전으로",
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
            .padding(16.dp),
    ) {
        Text(
            text = "탈퇴할 계정",
            fontSize = 16.sp,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Medium,
            color = Gray9,
            lineHeight = 22.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = accountDisplay,
            fontSize = 14.sp,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Normal,
            color = Gray9,
            lineHeight = 20.sp,
        )
    }
}

// -- Previews --

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false",
)
@Composable
private fun WithdrawalPasswordScreenPreview() {
    AfternoteTheme(darkTheme = false) {
        WithdrawalPasswordScreen(
            accountDisplay = "박서연(example@mail.com)",
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false",
    name = "문장 불일치 에러",
)
@Composable
private fun WithdrawalPasswordScreenErrorPreview() {
    AfternoteTheme(darkTheme = false) {
        WithdrawalPasswordScreen(
            accountDisplay = "박서연(example@mail.com)",
            sentenceError = stringResource(R.string.withdrawal_sentence_mismatch),
        )
    }
}
