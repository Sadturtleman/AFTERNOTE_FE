package com.kuit.afternote.feature.dailyrecord.presentation.navigiation

import kotlinx.serialization.Serializable

sealed interface RecordRoute {
    @Serializable
    data object MainRoute : RecordRoute

    @Serializable
    data object DiaryRoute : RecordRoute

    @Serializable
    data object QuestionRoute : RecordRoute

    @Serializable
    data object DeepMindRoute : RecordRoute

    @Serializable
    data object QuestionRouteList : RecordRoute

    @Serializable
    data class ListRoute(val listMode: String = "DIARY") : RecordRoute

    @Serializable
    data object WeekendReportRoute : RecordRoute

    @Serializable
    data class EditRoute(val recordId: Long) : RecordRoute

}
