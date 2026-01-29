package com.kuit.afternote.feature.setting.presentation.navgraph

import kotlinx.serialization.Serializable

sealed interface SettingRoute {
    @Serializable
    data object SettingMainRoute : SettingRoute

    @Serializable
    data class ReceiverDetailRoute(
        val receiverId: String = ""
    ) : SettingRoute

    @Serializable
    data object PasswordChangeRoute : SettingRoute

    @Serializable
    data object ReceiverListRoute : SettingRoute

    @Serializable
    data object ReceiverRegisterRoute : SettingRoute

    @Serializable
    data object PostDeliveryConditionRoute : SettingRoute

    @Serializable
    data class DailyAnswerRoute(
        val receiverId: String = ""
    ) : SettingRoute
}
