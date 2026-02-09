package com.kuit.afternote.feature.timeletter.presentation.navgraph

import kotlinx.serialization.Serializable

sealed interface TimeLetterRoute {
    @Serializable
    data object TimeLetterMainRoute : TimeLetterRoute

    @Serializable
    data class TimeLetterWriterRoute(val draftId: Long? = null) : TimeLetterRoute

    @Serializable
    data object DraftLetterRoute : TimeLetterRoute

    @Serializable
    data object ReceiveListRoute : TimeLetterRoute
}
