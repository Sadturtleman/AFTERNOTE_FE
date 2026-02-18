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
    data object ListRoute : RecordRoute

    /** 깊은 생각 전용 리스트. DEEP_THOUGHT 기록만 표시, FAB → RecordDeepMindScreen */
    @Serializable
    data object DeepMindListRoute : RecordRoute

    @Serializable
    data object WeekendReportRoute : RecordRoute

    @Serializable
    data class EditRoute(val recordId: Long) : RecordRoute

    /** 일기(DIARY) 수정 화면. RecordDiaryScreen을 기존 내용으로 재구성합니다. */
    @Serializable
    data class EditDiaryRoute(val recordId: Long) : RecordRoute

    /** 깊은 생각(DEEP_THOUGHT) 수정 화면. RecordDeepMindScreen을 기존 내용으로 재구성합니다. */
    @Serializable
    data class EditDeepMindRoute(val recordId: Long) : RecordRoute
}
