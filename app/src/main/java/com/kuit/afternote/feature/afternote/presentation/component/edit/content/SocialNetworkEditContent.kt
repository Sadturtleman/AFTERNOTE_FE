package com.kuit.afternote.feature.afternote.presentation.component.edit.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.Label
import com.kuit.afternote.core.ui.component.LabelStyle
import com.kuit.afternote.core.ui.component.SelectableRadioCard
import com.kuit.afternote.core.ui.component.Multiline
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AccountProcessingMethod
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AccountSection
import com.kuit.afternote.feature.afternote.presentation.component.edit.processingmethod.OptionRadioCardContent
import com.kuit.afternote.feature.afternote.presentation.component.edit.processingmethod.ProcessingMethodList
import com.kuit.afternote.feature.afternote.presentation.component.edit.processingmethod.ProcessingMethodListParams
import com.kuit.afternote.ui.theme.AfternoteTheme

/**
 * 소셜네트워크 등 일반적인 종류 선택 시 표시되는 콘텐츠
 */
@Composable
fun SocialNetworkEditContent(
    modifier: Modifier = Modifier,
    bottomPadding: PaddingValues,
    params: SocialNetworkEditContentParams
) {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    // Scaffold가 제공하는 bottomPadding을 사용 (네비게이션 바 높이 + 시스템 바 높이 자동 계산)
    val bottomPaddingDp = bottomPadding.calculateBottomPadding()
    // Viewport 높이 = 창 높이 - bottomPadding (네비게이션 바 상단까지의 높이)
    // 하단 여백은 네비게이션 바 상단까지의 Viewport 높이의 10%로 계산
    val viewportHeight = with(density) {
        windowInfo.containerSize.height.toDp() - bottomPaddingDp
    }
    val spacerHeight = viewportHeight * 0.1f

    SocialNetworkEditContentContent(
        modifier = modifier,
        params = params,
        spacerHeight = spacerHeight
    )
}

@Composable
private fun SocialNetworkEditContentContent(
    modifier: Modifier = Modifier,
    params: SocialNetworkEditContentParams,
    spacerHeight: Dp
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // 계정 정보 섹션
        Label(
            text = "계정 정보",
            isRequired = true,
            style = LabelStyle(requiredDotOffsetY = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlineTextField(
            modifier = Modifier,
            label = "아이디",
            textFieldState = params.accountSection.idState,
            keyboardType = KeyboardType.Text
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlineTextField(
            modifier = Modifier,
            label = "비밀번호",
            textFieldState = params.accountSection.passwordState,
            keyboardType = KeyboardType.Password
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 계정 처리 방법 섹션
        Label(
            text = "계정 처리 방법",
            isRequired = true,
            style = LabelStyle(requiredDotOffsetY = 2.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        SelectableRadioCard(
            selected = params.accountSection.selectedMethod == AccountProcessingMethod.MEMORIAL_ACCOUNT,
            onClick = { params.accountSection.onMethodSelected(AccountProcessingMethod.MEMORIAL_ACCOUNT) },
            modifier = Modifier.fillMaxWidth(),
            content = {
                OptionRadioCardContent(
                    option = AccountProcessingMethod.MEMORIAL_ACCOUNT,
                    selected = params.accountSection.selectedMethod == AccountProcessingMethod.MEMORIAL_ACCOUNT
                )
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SelectableRadioCard(
            selected = params.accountSection.selectedMethod == AccountProcessingMethod.PERMANENT_DELETE,
            onClick = { params.accountSection.onMethodSelected(AccountProcessingMethod.PERMANENT_DELETE) },
            modifier = Modifier.fillMaxWidth(),
            content = {
                OptionRadioCardContent(
                    option = AccountProcessingMethod.PERMANENT_DELETE,
                    selected = params.accountSection.selectedMethod == AccountProcessingMethod.PERMANENT_DELETE
                )
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SelectableRadioCard(
            selected = params.accountSection.selectedMethod == AccountProcessingMethod.TRANSFER_TO_RECEIVER,
            onClick = { params.accountSection.onMethodSelected(AccountProcessingMethod.TRANSFER_TO_RECEIVER) },
            modifier = Modifier.fillMaxWidth(),
            content = {
                OptionRadioCardContent(
                    option = AccountProcessingMethod.TRANSFER_TO_RECEIVER,
                    selected = params.accountSection.selectedMethod == AccountProcessingMethod.TRANSFER_TO_RECEIVER
                )
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        params.recipientSection?.let { section ->
            RecipientDesignationSection(section = section)
            Spacer(modifier = Modifier.height(32.dp))
        }

        // 처리 방법 리스트 섹션
        Label(
            text = "처리 방법 리스트",
            isRequired = true,
            style = LabelStyle(requiredDotOffsetY = 2.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProcessingMethodList(
            params = ProcessingMethodListParams(
                items = params.processingMethodSection.items,
                onItemDeleteClick = params.processingMethodSection.callbacks.onItemDeleteClick,
                onItemAdded = params.processingMethodSection.callbacks.onItemAdded,
                onTextFieldVisibilityChanged = params.processingMethodSection.callbacks.onTextFieldVisibilityChanged,
                onItemEdited = params.processingMethodSection.callbacks.onItemEdited
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 남기실 말씀
        OutlineTextField(
            label = "남기실 말씀",
            textFieldState = params.messageState,
            multiline = Multiline
        )

        // 소셜네트워크 탭 하단 여백 (Viewport 높이의 10%, 800dp 기준 약 80dp)
        // LocalWindowInfo를 사용하여 창 높이를 기준으로 계산
        Spacer(modifier = Modifier.height(spacerHeight))
    }
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
                bottomPadding = PaddingValues(bottom = 88.dp),
                params = SocialNetworkEditContentParams(
                    messageState = rememberTextFieldState(),
                    accountSection = AccountSection(
                        idState = rememberTextFieldState(),
                        passwordState = rememberTextFieldState(),
                        selectedMethod = AccountProcessingMethod.MEMORIAL_ACCOUNT,
                        onMethodSelected = {}
                    )
                )
            )
        }
    }
}
