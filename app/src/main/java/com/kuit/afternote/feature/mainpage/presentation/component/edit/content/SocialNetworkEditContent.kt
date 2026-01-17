package com.kuit.afternote.feature.mainpage.presentation.component.edit.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.core.ui.component.RequiredLabel
import com.kuit.afternote.feature.mainpage.presentation.component.edit.AccountProcessingRadioButton
import com.kuit.afternote.feature.mainpage.presentation.component.edit.ProcessingMethodList
import com.kuit.afternote.feature.mainpage.presentation.component.edit.ProcessingMethodListParams
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.AccountProcessingMethod
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.AccountSection
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Spacing

/**
 * 소셜네트워크 등 일반적인 종류 선택 시 표시되는 콘텐츠
 */
@Composable
fun SocialNetworkEditContent(
    modifier: Modifier = Modifier,
    params: SocialNetworkEditContentParams
) {
    // 계정 정보 섹션
    RequiredLabel(text = "계정 정보", offsetY = 4f)

    Spacer(modifier = Modifier.height(Spacing.m))

    OutlineTextField(
        label = "아이디",
        textFieldState = params.accountSection.idState
    )

    Spacer(modifier = Modifier.height(10.dp))

    OutlineTextField(
        label = "비밀번호",
        textFieldState = params.accountSection.passwordState,
        keyboardType = androidx.compose.ui.text.input.KeyboardType.Password
    )

    Spacer(modifier = Modifier.height(Spacing.xl))

    // 계정 처리 방법 섹션
    RequiredLabel(text = "계정 처리 방법", offsetY = 2f)

    Spacer(modifier = Modifier.height(Spacing.m))

    AccountProcessingRadioButton(
        method = AccountProcessingMethod.MEMORIAL_ACCOUNT,
        selected = params.accountSection.selectedMethod == AccountProcessingMethod.MEMORIAL_ACCOUNT,
        onClick = { params.accountSection.onMethodSelected(AccountProcessingMethod.MEMORIAL_ACCOUNT) }
    )

    Spacer(modifier = Modifier.height(Spacing.s))

    AccountProcessingRadioButton(
        method = AccountProcessingMethod.PERMANENT_DELETE,
        selected = params.accountSection.selectedMethod == AccountProcessingMethod.PERMANENT_DELETE,
        onClick = { params.accountSection.onMethodSelected(AccountProcessingMethod.PERMANENT_DELETE) }
    )

    Spacer(modifier = Modifier.height(Spacing.s))

    AccountProcessingRadioButton(
        method = AccountProcessingMethod.TRANSFER_TO_RECIPIENT,
        selected = params.accountSection.selectedMethod == AccountProcessingMethod.TRANSFER_TO_RECIPIENT,
        onClick = { params.accountSection.onMethodSelected(AccountProcessingMethod.TRANSFER_TO_RECIPIENT) }
    )

    Spacer(modifier = Modifier.height(Spacing.xl))
    // 처리 방법 리스트 섹션
    RequiredLabel(text = "처리 방법 리스트", offsetY = 2f)

    Spacer(modifier = Modifier.height(Spacing.m))

    ProcessingMethodList(
        params = ProcessingMethodListParams(
            items = params.processingMethodSection.items,
            onAddClick = params.processingMethodSection.callbacks.onAddClick,
            onItemMoreClick = params.processingMethodSection.callbacks.onItemMoreClick,
            onItemEditClick = params.processingMethodSection.callbacks.onItemEditClick,
            onItemDeleteClick = params.processingMethodSection.callbacks.onItemDeleteClick,
            onItemAdded = params.processingMethodSection.callbacks.onItemAdded,
            onTextFieldVisibilityChanged = params.processingMethodSection.callbacks.onTextFieldVisibilityChanged
        )
    )

    Spacer(modifier = Modifier.height(Spacing.xl))

    // 남기실 말씀
    OutlineTextField(
        label = "남기실 말씀",
        textFieldState = params.messageState,
        isMultiline = true
    )

    // 소셜네트워크 탭 하단 여백 (81dp)
    Spacer(modifier = Modifier.height(Spacing.socialNetworkBottom))
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
