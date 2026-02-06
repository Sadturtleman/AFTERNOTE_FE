package com.kuit.afternote.feature.afternote.presentation.navgraph

import kotlinx.serialization.Serializable

sealed interface AfternoteRoute {
    @Serializable
    data object AfternoteListRoute : AfternoteRoute

    @Serializable
    data object DetailRoute : AfternoteRoute

    @Serializable
    data object GalleryDetailRoute : AfternoteRoute

    @Serializable
    data object EditRoute : AfternoteRoute

    @Serializable
    data object FingerprintLoginRoute : AfternoteRoute

    @Serializable
    data object AddSongRoute : AfternoteRoute

    @Serializable
    data object MemorialPlaylistRoute : AfternoteRoute
}
