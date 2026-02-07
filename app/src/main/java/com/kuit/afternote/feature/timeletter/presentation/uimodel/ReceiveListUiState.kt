package com.kuit.afternote.feature.timeletter.presentation.uimodel

import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterReceiver

/**
 * 타임레터 수신자 목록 화면 UI 상태.
 * 수신자 데이터는 GET /users/receivers(설정 수신자 목록과 동일)를 사용합니다.
 *
 * @property receivers 수신자 목록
 * @property isLoading 로딩 여부
 */
data class ReceiveListUiState(
    val receivers: List<TimeLetterReceiver> = emptyList(),
    val isLoading: Boolean = false
)
