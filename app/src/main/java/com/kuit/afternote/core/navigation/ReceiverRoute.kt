package com.kuit.afternote.core.navigation

import kotlinx.serialization.Serializable

/**
 * 공통 수신자 목록 화면 라우트.
 * 타임레터 작성·애프터노트 수신자 지정 등에서 수신자 선택 시 사용합니다.
 */
sealed interface ReceiverRoute {

    /** 수신자 목록 화면 (설정의 수신자 목록과 동일 소스). */
    @Serializable
    data object ReceiverListRoute : ReceiverRoute
}

/** 수신자 목록에서 선택한 수신자 ID를 이전 화면으로 돌려줄 때 사용하는 SavedStateHandle 키. */
const val SELECTED_RECEIVER_ID_KEY = "selected_receiver_id"
