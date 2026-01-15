package com.kuit.afternote.feature.mainpage.presentation.component.edit.model

/**
 * 정보 처리 방법 라디오 버튼 옵션 (갤러리 및 파일용)
 */
enum class InformationProcessingMethod(
    override val title: String,
    override val description: String
) : ProcessingMethodOption {
    TRANSFER_TO_RECIPIENT(
        "수신자에게 정보 전달",
        "수신자가 직접 로그인하여 정보를 관리할 수 있도록 계정 정보를 전달합니다."
    ),
    TRANSFER_TO_ADDITIONAL_RECIPIENT(
        "추가 수신자에게 정보 전달",
        "설정된 수신자 이외의 추가적인 수신자를 지정해여 해당 정보만 전달합니다."
    )
}
