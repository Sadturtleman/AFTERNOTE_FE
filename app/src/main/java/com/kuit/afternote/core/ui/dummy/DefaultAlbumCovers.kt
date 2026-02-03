package com.kuit.afternote.core.ui.dummy

import com.kuit.afternote.core.ui.component.list.AlbumCover

/**
 * Shared default album cover list for playlists (Previews and dummy state).
 * Replace with real data when loaded from API.
 */
object DefaultAlbumCovers {
    val list: List<AlbumCover> =
        listOf(
            AlbumCover("1"),
            AlbumCover("2"),
            AlbumCover("3"),
            AlbumCover("4")
        )
}
