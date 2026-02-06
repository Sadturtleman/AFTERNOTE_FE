package com.kuit.afternote.feature.mainpage.presentation.component.edit.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.Label
import com.kuit.afternote.core.ui.component.LabelStyle
import com.kuit.afternote.core.ui.component.Multiline
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.feature.mainpage.presentation.component.edit.mainpageeditreceiver.MainPageEditReceiverList
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.InfoMethodSection
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.InformationProcessingMethod
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.MainPageEditReceiverSection
import com.kuit.afternote.feature.mainpage.presentation.component.edit.processingmethod.ProcessingMethodList
import com.kuit.afternote.feature.mainpage.presentation.component.edit.processingmethod.ProcessingMethodListParams
import com.kuit.afternote.feature.mainpage.presentation.component.edit.processingmethod.ProcessingMethodRadioButton
import com.kuit.afternote.app.compositionlocal.DataProviderLocals
import com.kuit.afternote.data.provider.FakeAfternoteEditDataProvider
import com.kuit.afternote.ui.theme.AfternoteTheme

/**
 * 갤러리 및 파일 선택 시 표시되는 콘텐츠
 */
@Composable
fun GalleryAndFileEditContent(
    modifier: Modifier = Modifier,
    bottomPadding: PaddingValues,
    params: GalleryAndFileEditContentParams
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

    GalleryAndFileEditContentContent(
        modifier = modifier,
        params = params,
        spacerHeight = spacerHeight
    )
}

@Composable
private fun GalleryAndFileEditContentContent(
    modifier: Modifier = Modifier,
    params: GalleryAndFileEditContentParams,
    spacerHeight: Dp
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // 정보 처리 방법 섹션
        Label(
            text = "정보 처리 방법",
            isRequired = true,
            style = LabelStyle(requiredDotOffsetY = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ProcessingMethodRadioButton(
            option = InformationProcessingMethod.TRANSFER_TO_MAINPAGE_EDIT_RECEIVER,
            selected = params.infoMethodSection.selectedMethod == InformationProcessingMethod.TRANSFER_TO_MAINPAGE_EDIT_RECEIVER,
            onClick = { params.infoMethodSection.onMethodSelected(InformationProcessingMethod.TRANSFER_TO_MAINPAGE_EDIT_RECEIVER) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        ProcessingMethodRadioButton(
            option = InformationProcessingMethod.TRANSFER_TO_ADDITIONAL_MAINPAGE_EDIT_RECEIVER,
            selected = params.infoMethodSection.selectedMethod == InformationProcessingMethod.TRANSFER_TO_ADDITIONAL_MAINPAGE_EDIT_RECEIVER,
            onClick = {
                params.infoMethodSection.onMethodSelected(
                    InformationProcessingMethod.TRANSFER_TO_ADDITIONAL_MAINPAGE_EDIT_RECEIVER
                )
            }
        )

        // 추가 수신자에게 정보 전달 선택 시 수신자 추가 섹션 표시
        params.mainPageEditReceiverSection?.let { mainPageEditReceiverSection ->
            Spacer(modifier = Modifier.height(32.dp))

            // 수신자 추가 섹션 제목
            Label(
                text = "수신자 추가",
                isRequired = true,
                style = LabelStyle(requiredDotOffsetY = 3.dp)
            )

            Spacer(modifier = Modifier.height(9.dp))

            MainPageEditReceiverList(
                mainPageEditReceivers = mainPageEditReceiverSection.mainPageEditReceivers,
                events = mainPageEditReceiverSection.callbacks
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 처리 방법 리스트 섹션
        Label(
            text = "처리 방법 리스트",
            isRequired = true,
            style = LabelStyle(requiredDotOffsetY = 2.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

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

        Spacer(modifier = Modifier.height(32.dp))

        // 남기실 말씀
        OutlineTextField(
            label = "남기실 말씀",
            textFieldState = params.messageState,
            multiline = Multiline
        )

        // 갤러리 및 파일 탭 하단 여백 (Viewport 높이의 10%, 800dp 기준 약 80dp)
        // LocalWindowInfo를 사용하여 창 높이를 기준으로 계산
        Spacer(modifier = Modifier.height(spacerHeight))
    }
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
                bottomPadding = PaddingValues(bottom = 88.dp),
                params = GalleryAndFileEditContentParams(
                    messageState = rememberTextFieldState(),
                    infoMethodSection = InfoMethodSection(
                        selectedMethod = InformationProcessingMethod.TRANSFER_TO_MAINPAGE_EDIT_RECEIVER,
                        onMethodSelected = {}
                    )
                )
            )
        }
    }
}

@Preview(showBackground = true, name = "추가 수신자 선택됨")
@Composable
private fun GalleryAndFileEditContentWithMainPageEditReceiversPreview() {
    AfternoteTheme {
        CompositionLocalProvider(
            DataProviderLocals.LocalAfternoteEditDataProvider provides FakeAfternoteEditDataProvider()
        ) {
            val provider = DataProviderLocals.LocalAfternoteEditDataProvider.current
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                GalleryAndFileEditContent(
                    bottomPadding = PaddingValues(bottom = 88.dp),
                    params = GalleryAndFileEditContentParams(
                        messageState = rememberTextFieldState(),
                        infoMethodSection = InfoMethodSection(
                            selectedMethod = InformationProcessingMethod.TRANSFER_TO_ADDITIONAL_MAINPAGE_EDIT_RECEIVER,
                            onMethodSelected = {}
                        ),
                        mainPageEditReceiverSection = MainPageEditReceiverSection(
                            mainPageEditReceivers = provider.getMainPageEditReceivers()
                        )
                    )
                )
            }
        }
    }
}
