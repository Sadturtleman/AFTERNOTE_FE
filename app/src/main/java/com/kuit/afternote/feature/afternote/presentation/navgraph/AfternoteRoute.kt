package com.kuit.afternote.feature.afternote.presentation.navgraph

import kotlinx.serialization.Serializable

sealed interface AfternoteRoute {
    @Serializable
    data object AfternoteListRoute : AfternoteRoute

    @Serializable
    data class DetailRoute(val itemId: String = "") : AfternoteRoute

    @Serializable
    data class GalleryDetailRoute(val itemId: String = "") : AfternoteRoute

    @Serializable
    data class EditRoute(
        val itemId: String? = null,
        val initialCategory: String? = null
    ) : AfternoteRoute

    @Serializable
    data object FingerprintLoginRoute : AfternoteRoute

    @Serializable
    data object AddSongRoute : AfternoteRoute

    @Serializable
    data object MemorialPlaylistRoute : AfternoteRoute

    @Serializable
    data class MemorialGuidelineDetailRoute(val itemId: String = "") : AfternoteRoute
}
