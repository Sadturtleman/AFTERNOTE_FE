package com.kuit.afternote.feature.mainpage.presentation.component.edit.model

/**
 * 계정 처리 방법 라디오 버튼 옵션
 */
enum class AccountProcessingMethod(
    override val title: String,
    override val description: String
) : ProcessingMethodOption {
    MEMORIAL_ACCOUNT(
        "추모 계정으로 전환",
        "가족과 친구들이 당신을 추억할 수 있도록 계정을 유지하고, 추모 라벨을 붙여 계정을 보존합니다."
    ),
    PERMANENT_DELETE(
        "계정 영구 삭제",
        "모든 사진과 게시물을 완전히 삭제하고 계정을 영구히 폐쇄합니다."
    ),
    TRANSFER_TO_RECIPIENT(
        "수신자에게 정보 전달",
        "수신자가 직접 로그인하여 계정을 관리할 수 있도록 계정 정보를 전달합니다."
    )
}
