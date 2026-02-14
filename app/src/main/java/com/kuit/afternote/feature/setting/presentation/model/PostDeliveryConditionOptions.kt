package com.kuit.afternote.feature.setting.presentation.model

/**
 * 정보 전달 방법 옵션 (첫 번째 섹션)
 */
sealed interface DeliveryMethodOption {
    val title: String
    val description: String

    data object AutomaticTransfer : DeliveryMethodOption {
        override val title: String = "자동 전달"
        override val description: String =
            "설정된 조건 충족 시, 별도의 요청 없이 지정한 수신자에게 자동으로 전달됩니다."
    }

    data object ReceiverApprovalTransfer : DeliveryMethodOption {
        override val title: String = "수신자 승인 후 전달"
        override val description: String =
            "수신자의 요청과 확인 절차를 거친 후, 안전하게 정보가 전달됩니다."
    }
}

/**
 * 정보 처리 방법 옵션 (두 번째 섹션 - 트리거 조건)
 */
sealed interface TriggerConditionOption {
    val title: String
    val description: String

    data object AppInactivity : TriggerConditionOption {
        override val title: String = "일정 기간 앱 미사용 시"
        override val description: String =
            "1년 이상의 기간 동안 앱 사용이 없을 경우 전달 조건이 자동으로 충족됩니다."
    }

    data object SpecificDate : TriggerConditionOption {
        override val title: String = "특정 날짜에 전달"
        override val description: String =
            "미리 정한 날짜에 맞추어 정보가 자동으로 전달됩니다."
    }

    data object ReceiverRequest : TriggerConditionOption {
        override val title: String = "수신자 요청 이후"
        override val description: String =
            "수신자의 요청이 접수된 후, 확인 절차를 거쳐 전달이 진행됩니다."
    }
}
