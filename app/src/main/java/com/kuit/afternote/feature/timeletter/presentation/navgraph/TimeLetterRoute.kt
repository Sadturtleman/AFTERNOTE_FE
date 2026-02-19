package com.kuit.afternote.feature.timeletter.presentation.navgraph

import kotlinx.serialization.Serializable

sealed interface TimeLetterRoute {
    @Serializable
    data object TimeLetterMainRoute : TimeLetterRoute

    @Serializable
    data class TimeLetterWriterRoute(val draftId: Long? = null) : TimeLetterRoute

    @Serializable
    data object DraftLetterRoute : TimeLetterRoute

    /**
     * 타임레터 상세 화면 라우트.
     * 리스트 아이템 클릭 시 해당 타임레터 데이터를 담아 이동한다.
     */
    @Serializable
    data class TimeLetterDetailRoute(
        val id: String,
        val receiverName: String,
        val sendDate: String,
        val title: String,
        val content: String,
        val createDate: String,
        val mediaUrls: List<String> = emptyList(),
        val audioUrls: List<String> = emptyList(),
        val linkUrls: List<String> = emptyList()
    ) : TimeLetterRoute
}
