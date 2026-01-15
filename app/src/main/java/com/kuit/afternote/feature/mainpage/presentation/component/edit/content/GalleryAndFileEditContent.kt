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
import com.kuit.afternote.core.MessageTextField
import com.kuit.afternote.core.RequiredLabel
import com.kuit.afternote.feature.mainpage.presentation.component.edit.InformationProcessingRadioButton
import com.kuit.afternote.feature.mainpage.presentation.component.edit.ProcessingMethodList
import com.kuit.afternote.feature.mainpage.presentation.component.edit.ProcessingMethodListParams
import com.kuit.afternote.feature.mainpage.presentation.component.edit.RecipientList
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.InfoMethodSection
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.InformationProcessingMethod
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Recipient
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.RecipientSection
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Spacing

/**
 * 갤러리 및 파일 선택 시 표시되는 콘텐츠
 */
@Composable
fun GalleryAndFileEditContent(
    modifier: Modifier = Modifier,
    params: GalleryAndFileEditContentParams
) {
    // 정보 처리 방법 섹션
    RequiredLabel(text = "정보 처리 방법", offsetY = 4f)

    Spacer(modifier = Modifier.height(Spacing.m))

    InformationProcessingRadioButton(
        method = InformationProcessingMethod.TRANSFER_TO_RECIPIENT,
        selected = params.infoMethodSection.selectedMethod == InformationProcessingMethod.TRANSFER_TO_RECIPIENT,
        onClick = { params.infoMethodSection.onMethodSelected(InformationProcessingMethod.TRANSFER_TO_RECIPIENT) }
    )

    Spacer(modifier = Modifier.height(Spacing.s))

    InformationProcessingRadioButton(
        method = InformationProcessingMethod.TRANSFER_TO_ADDITIONAL_RECIPIENT,
        selected = params.infoMethodSection.selectedMethod == InformationProcessingMethod.TRANSFER_TO_ADDITIONAL_RECIPIENT,
        onClick = { params.infoMethodSection.onMethodSelected(InformationProcessingMethod.TRANSFER_TO_ADDITIONAL_RECIPIENT) }
    )

    // 추가 수신자에게 정보 전달 선택 시 수신자 추가 섹션 표시
    params.recipientSection?.let { recipientSection ->
        Spacer(modifier = Modifier.height(Spacing.xl))

        // 수신자 추가 섹션 제목
        RequiredLabel(text = "수신자 추가", offsetY = 3f)

        Spacer(modifier = Modifier.height(9.dp))

        RecipientList(
            recipients = recipientSection.recipients,
            events = recipientSection.callbacks
        )
    }
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
    MessageTextField(
        textFieldState = params.messageState
    )

    // 갤러리 및 파일 탭 하단 여백 (459dp)
    Spacer(modifier = Modifier.height(Spacing.galleryAndFileBottom))
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
                params = GalleryAndFileEditContentParams(
                    messageState = rememberTextFieldState(),
                    infoMethodSection = InfoMethodSection(
                        selectedMethod = InformationProcessingMethod.TRANSFER_TO_RECIPIENT,
                        onMethodSelected = {}
                    )
                )
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
                params = GalleryAndFileEditContentParams(
                    messageState = rememberTextFieldState(),
                    infoMethodSection = InfoMethodSection(
                        selectedMethod = InformationProcessingMethod.TRANSFER_TO_ADDITIONAL_RECIPIENT,
                        onMethodSelected = {}
                    ),
                    recipientSection = RecipientSection(
                        recipients = listOf(
                            Recipient("1", "김지은", "친구"),
                            Recipient("2", "박선호", "가족")
                        )
                    )
                )
            )
        }
    }
}
