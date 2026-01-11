package com.kuit.afternote.feature.mainpage.presentation.component.edit.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.feature.mainpage.presentation.component.common.RequiredLabel
import com.kuit.afternote.feature.mainpage.presentation.component.edit.InformationProcessingRadioButton
import com.kuit.afternote.feature.mainpage.presentation.component.edit.RecipientList
import com.kuit.afternote.feature.mainpage.presentation.model.InformationProcessingMethod
import com.kuit.afternote.feature.mainpage.presentation.model.Recipient
import com.kuit.afternote.ui.theme.AfternoteTheme

/**
 * 갤러리 및 파일 선택 시 표시되는 콘텐츠
 */
@Composable
fun GalleryAndFileEditContent(
    modifier: Modifier = Modifier,
    selectedInformationProcessingMethod: InformationProcessingMethod,
    onInformationProcessingMethodSelected: (InformationProcessingMethod) -> Unit,
    recipients: List<Recipient> = emptyList(),
    onRecipientAddClick: () -> Unit = {},
    onRecipientItemEditClick: (String) -> Unit = {},
    onRecipientItemDeleteClick: (String) -> Unit = {},
    onRecipientItemAdded: (String) -> Unit = {},
    onRecipientTextFieldVisibilityChanged: (Boolean) -> Unit = {}
) {
    // 정보 처리 방법 섹션
    RequiredLabel(text = "정보 처리 방법", offsetY = 4f)

    Spacer(modifier = Modifier.height(16.dp))

    InformationProcessingRadioButton(
        method = InformationProcessingMethod.TRANSFER_TO_RECIPIENT,
        selected = selectedInformationProcessingMethod == InformationProcessingMethod.TRANSFER_TO_RECIPIENT,
        onClick = { onInformationProcessingMethodSelected(InformationProcessingMethod.TRANSFER_TO_RECIPIENT) }
    )

    Spacer(modifier = Modifier.height(8.dp))

    InformationProcessingRadioButton(
        method = InformationProcessingMethod.TRANSFER_TO_ADDITIONAL_RECIPIENT,
        selected = selectedInformationProcessingMethod == InformationProcessingMethod.TRANSFER_TO_ADDITIONAL_RECIPIENT,
        onClick = { onInformationProcessingMethodSelected(InformationProcessingMethod.TRANSFER_TO_ADDITIONAL_RECIPIENT) }
    )

    // 추가 수신자에게 정보 전달 선택 시 수신자 추가 섹션 표시
    if (selectedInformationProcessingMethod == InformationProcessingMethod.TRANSFER_TO_ADDITIONAL_RECIPIENT) {
        Spacer(modifier = Modifier.height(32.dp))

        // 수신자 추가 섹션 제목
        RequiredLabel(text = "수신자 추가", offsetY = 3f)

        Spacer(modifier = Modifier.height(9.dp))

        RecipientList(
            recipients = recipients,
            onAddClick = onRecipientAddClick,
            onItemEditClick = onRecipientItemEditClick,
            onItemDeleteClick = onRecipientItemDeleteClick,
            onItemAdded = onRecipientItemAdded,
            onTextFieldVisibilityChanged = onRecipientTextFieldVisibilityChanged
        )
    }

    Spacer(modifier = Modifier.height(32.dp))
}

@Preview(showBackground = true)
@Composable
private fun GalleryAndFileEditContentPreview() {
    AfternoteTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // 첫 번째 옵션 선택됨 (파란 테두리), 두 번째는 선택 안 됨 (테두리 없음) 상태를 한 화면에 표시
            GalleryAndFileEditContent(
                selectedInformationProcessingMethod = InformationProcessingMethod.TRANSFER_TO_RECIPIENT,
                onInformationProcessingMethodSelected = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "추가 수신자 선택됨")
@Composable
private fun GalleryAndFileEditContentWithRecipientsPreview() {
    AfternoteTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            GalleryAndFileEditContent(
                selectedInformationProcessingMethod = InformationProcessingMethod.TRANSFER_TO_ADDITIONAL_RECIPIENT,
                onInformationProcessingMethodSelected = {},
                recipients = listOf(
                    Recipient("1", "김지은", "친구"),
                    Recipient("2", "박선호", "가족")
                )
            )
        }
    }
}
