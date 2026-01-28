package com.kuit.afternote.feature.mainpage.presentation.navgraph

import kotlinx.serialization.Serializable

sealed interface MainPageRoute {
    @Serializable
    data object MainRoute : MainPageRoute

    @Serializable
    data object DetailRoute : MainPageRoute

    @Serializable
    data object GalleryDetailRoute : MainPageRoute

    @Serializable
    data object EditRoute : MainPageRoute

    @Serializable
    data object FingerprintLoginRoute : MainPageRoute

    @Serializable
    data object AddSongRoute : MainPageRoute

    @Serializable
    data object MemorialPlaylistRoute : MainPageRoute
}
