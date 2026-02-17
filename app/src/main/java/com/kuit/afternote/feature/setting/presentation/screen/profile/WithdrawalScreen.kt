package com.kuit.afternote.feature.setting.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Red
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * Callbacks for the withdrawal screen.
 */
data class WithdrawalScreenCallbacks(
    val onBackClick: () -> Unit = {},
    val onWithdrawClick: () -> Unit = {},
    val onCancelClick: () -> Unit = {}
)

/**
 * 회원 탈퇴 안내 화면.
 *
 * Figma 기반:
 * - 헤더: "회원 탈퇴 안내"
 * - 4개 정보 카드: 탈퇴할 계정, 탈퇴 시 삭제되는 내용, 탈퇴 전 확인 사항, 개인정보 처리 안내
 * - 동의 체크박스
 * - 탈퇴하기 / 취소하기 버튼
 *
 * @param accountDisplay 탈퇴할 계정 정보 (예: "박서연(example@mail.com)")
 * @param callbacks 화면 콜백
 */
@Composable
fun WithdrawalScreen(
    modifier: Modifier = Modifier,
    accountDisplay: String = "",
    callbacks: WithdrawalScreenCallbacks = WithdrawalScreenCallbacks()
) {
    var isAgreed by remember { mutableStateOf(false) }

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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 안내 문구
            WithdrawalDescription()

            Spacer(modifier = Modifier.height(16.dp))

            // 탈퇴할 계정 카드
            AccountInfoCard(accountDisplay = accountDisplay)

            Spacer(modifier = Modifier.height(12.dp))

            // 탈퇴 시 삭제되는 내용 카드
            DeletionInfoCard()

            Spacer(modifier = Modifier.height(12.dp))

            // 탈퇴 전 확인 사항 카드
            PreWithdrawalCheckCard()

            Spacer(modifier = Modifier.height(12.dp))

            // 개인정보 처리 안내 카드
            PrivacyInfoCard()

            Spacer(modifier = Modifier.height(20.dp))

            // 동의 체크박스
            AgreementCheckbox(
                isChecked = isAgreed,
                onCheckedChange = { isAgreed = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 탈퇴하기 버튼 (동의 체크 시에만 활성)
            ClickButton(
                onButtonClick = callbacks.onWithdrawClick,
                title = "탈퇴하기",
                isTrue = isAgreed,
                activeColor = B2
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 취소하기 버튼
            ClickButton(
                color = Gray4,
                onButtonClick = callbacks.onCancelClick,
                title = "취소하기"
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// -- Section components --

@Composable
private fun WithdrawalDescription() {
    Text(
        text = "애프터노트를 정말 탈퇴하시겠습니까?\n서비스 탈퇴 전 아래 내용을 꼭 확인해 주세요.",
        fontSize = 16.sp,
        fontFamily = Sansneo,
        fontWeight = FontWeight.Medium,
        color = Gray9,
        lineHeight = 22.sp
    )
}

@Composable
private fun AccountInfoCard(accountDisplay: String) {
    InfoCard {
        CardTitle(text = "탈퇴할 계정")
        Spacer(modifier = Modifier.height(8.dp))
        CardBody(text = accountDisplay)
    }
}

@Composable
private fun DeletionInfoCard() {
    InfoCard {
        CardTitle(text = "탈퇴 시 삭제되는 내용")
        Spacer(modifier = Modifier.height(8.dp))
        CardBody(text = "다음 정보는 탈퇴와 동시에 영구적으로 삭제됩니다.")
        Spacer(modifier = Modifier.height(8.dp))
        BulletItem(text = "작성한 모든 기록 및 메세지")
        BulletItem(text = "예약된 데이터 전달 설정")
        BulletItem(text = "업로드한 파일 및 첨부 데이터")
        BulletItem(text = "계정 정보 및 프로필 정보")
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = Gray9)) {
                    append("삭제된 데이터는 ")
                }
                withStyle(SpanStyle(color = Red)) {
                    append("어떠한 경우에도 복구할 수 없습니다.")
                }
            },
            fontSize = 14.sp,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Normal,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun PreWithdrawalCheckCard() {
    InfoCard {
        CardTitle(text = "탈퇴 전 확인 사항")
        Spacer(modifier = Modifier.height(8.dp))
        BulletItem(text = "전달이 예정된 메세지는 모두 취소됩니다.")
        BulletItem(text = "삭제된 기록은 다시 확인할 수 없습니다.")
        BulletItem(text = "탈퇴일로부터 30일 이후, 해당 계정의 이메일 주소로\n새로 가입할 수 있습니다.")
        BulletItem(text = "단 기존 정보는 파기되어 복구할 수 없습니다.")
    }
}

@Composable
private fun PrivacyInfoCard() {
    InfoCard {
        CardTitle(text = "개인정보 처리 안내")
        Spacer(modifier = Modifier.height(8.dp))
        CardBody(
            text = "회원 탈퇴 시 개인정보는 즉시 삭제됩니다.\n" +
                "단, 관련 법령에 따라 일정 기간 보관이 필요한 경우\n" +
                "해당 정보는 법적 기준에 따라 보관 후 삭제됩니다."
        )
    }
}

// -- Reusable building blocks --

@Composable
private fun InfoCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Gray2)
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
private fun CardTitle(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontFamily = Sansneo,
        fontWeight = FontWeight.Medium,
        color = Gray9,
        lineHeight = 22.sp
    )
}

@Composable
private fun CardBody(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontFamily = Sansneo,
        fontWeight = FontWeight.Normal,
        color = Gray9,
        lineHeight = 20.sp
    )
}

@Composable
private fun BulletItem(text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Bullet dot (4dp circle, vertically centered with first line: (20dp - 4dp) / 2 = 8dp top)
        Spacer(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(4.dp)
                .background(Gray9, CircleShape)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Normal,
            color = Gray9,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun AgreementCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Custom checkbox circle (12dp)
        Spacer(
            modifier = Modifier
                .size(12.dp)
                .background(
                    color = if (isChecked) B3 else Gray4,
                    shape = CircleShape
                )
        )
        Text(
            text = "탈퇴 주의사항을 모두 확인했으며, 이에 동의합니다.",
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
private fun WithdrawalScreenPreview() {
    AfternoteTheme(darkTheme = false) {
        WithdrawalScreen(
            accountDisplay = "박서연(example@mail.com)"
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false",
    name = "동의 체크 상태"
)
@Composable
private fun WithdrawalScreenAgreedPreview() {
    AfternoteTheme(darkTheme = false) {
        WithdrawalScreen(
            accountDisplay = "박서연(example@mail.com)"
        )
    }
}
