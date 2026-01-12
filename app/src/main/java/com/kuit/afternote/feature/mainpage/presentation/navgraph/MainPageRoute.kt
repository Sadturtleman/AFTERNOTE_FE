package com.kuit.afternote.feature.mainpage.presentation.navgraph

import kotlinx.serialization.Serializable

sealed interface MainPageRoute {
    @Serializable
    data object MainEmptyRoute : MainPageRoute

    @Serializable
    data object MainWithItemsRoute : MainPageRoute

    @Serializable
    data object DetailRoute : MainPageRoute

    @Serializable
    data object EditRoute : MainPageRoute

    @Serializable
    data object FingerprintLoginRoute : MainPageRoute
}
