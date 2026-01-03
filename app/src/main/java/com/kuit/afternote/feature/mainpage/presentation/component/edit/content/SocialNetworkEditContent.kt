package com.kuit.afternote.feature.mainpage.presentation.component.edit.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.feature.mainpage.presentation.component.common.textfield.AccountInfoTextField
import com.kuit.afternote.feature.mainpage.presentation.component.edit.AccountProcessingRadioButton
import com.kuit.afternote.feature.mainpage.presentation.model.AccountProcessingMethod
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 소셜네트워크 등 일반적인 종류 선택 시 표시되는 콘텐츠
 */
@Composable
fun SocialNetworkEditContent(
    modifier: Modifier = Modifier,
    idState: TextFieldState,
    passwordState: TextFieldState,
    selectedProcessingMethod: AccountProcessingMethod,
    onProcessingMethodSelected: (AccountProcessingMethod) -> Unit
) {
    // 계정 정보 섹션
    Box {
        var textWidth by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        Text(
            text = "계정 정보",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight(500),
                color = Gray9
            ),
            modifier = Modifier.onGloballyPositioned { coordinates ->
                textWidth = with(density) { coordinates.size.width.toDp() }
            }
        )
        // 파란 점: 오른쪽 위 꼭짓점으로부터 오른쪽 8.dp, 아래 4.dp
        Box(
            modifier = Modifier
                .offset(x = textWidth + 8.dp, y = 4.dp)
                .size(4.dp)
                .background(color = B2, shape = CircleShape)
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    AccountInfoTextField(
        label = "아이디",
        textFieldState = idState
    )

    Spacer(modifier = Modifier.height(10.dp))

    AccountInfoTextField(
        label = "비밀번호",
        textFieldState = passwordState,
        keyboardType = androidx.compose.ui.text.input.KeyboardType.Password
    )

    Spacer(modifier = Modifier.height(32.dp))

    // 계정 처리 방법 섹션
    Box {
        var textWidth by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        Text(
            text = "계정 처리 방법",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight(500),
                color = Gray9
            ),
            modifier = Modifier.onGloballyPositioned { coordinates ->
                textWidth = with(density) { coordinates.size.width.toDp() }
            }
        )
        // 파란 점: 오른쪽 위 꼭짓점으로부터 오른쪽 8.dp, 아래 2.dp
        Box(
            modifier = Modifier
                .offset(x = textWidth + 8.dp, y = 2.dp)
                .size(4.dp)
                .background(color = B2, shape = CircleShape)
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    AccountProcessingRadioButton(
        method = AccountProcessingMethod.MEMORIAL_ACCOUNT,
        selected = selectedProcessingMethod == AccountProcessingMethod.MEMORIAL_ACCOUNT,
        onClick = { onProcessingMethodSelected(AccountProcessingMethod.MEMORIAL_ACCOUNT) }
    )

    Spacer(modifier = Modifier.height(8.dp))

    AccountProcessingRadioButton(
        method = AccountProcessingMethod.PERMANENT_DELETE,
        selected = selectedProcessingMethod == AccountProcessingMethod.PERMANENT_DELETE,
        onClick = { onProcessingMethodSelected(AccountProcessingMethod.PERMANENT_DELETE) }
    )

    Spacer(modifier = Modifier.height(8.dp))

    AccountProcessingRadioButton(
        method = AccountProcessingMethod.TRANSFER_TO_RECIPIENT,
        selected = selectedProcessingMethod == AccountProcessingMethod.TRANSFER_TO_RECIPIENT,
        onClick = { onProcessingMethodSelected(AccountProcessingMethod.TRANSFER_TO_RECIPIENT) }
    )

    Spacer(modifier = Modifier.height(32.dp))
}

@Preview(showBackground = true)
@Composable
private fun SocialNetworkEditContentPreview() {
    AfternoteTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // 첫 번째 옵션 선택됨 (파란 테두리), 나머지는 선택 안 됨 (테두리 없음) 상태를 한 화면에 표시
            SocialNetworkEditContent(
                idState = rememberTextFieldState(),
                passwordState = rememberTextFieldState(),
                selectedProcessingMethod = AccountProcessingMethod.MEMORIAL_ACCOUNT,
                onProcessingMethodSelected = {}
            )
        }
    }
}
