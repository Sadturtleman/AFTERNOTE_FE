package com.kuit.afternote.domain.provider

import com.kuit.afternote.core.component.list.AlbumCover
import com.kuit.afternote.presentation.uimodel.PlaylistSongDisplay
import com.kuit.afternote.feature.afternote.presentation.edit.model.AfternoteEditReceiver
import com.kuit.afternote.feature.afternote.presentation.edit.model.Song
import javax.inject.Inject

/**
 * Real implementation. Returns empty/placeholder until API is available.
 */
class RealAfternoteEditDataProvider
    @Inject
    constructor() : AfternoteEditDataProvider {
        override fun getSongs(): List<com.kuit.afternote.feature.afternote.presentation.edit.model.Song> = emptyList()

        override fun getAfternoteEditReceivers(): List<com.kuit.afternote.feature.afternote.presentation.edit.model.AfternoteEditReceiver> =
            emptyList()

        override fun getDefaultAfternoteItems(): List<Pair<String, String>> = emptyList()

        override fun getAlbumCovers(): List<AlbumCover> = emptyList()

        override fun getAddSongSearchResults(): List<PlaylistSongDisplay> = emptyList()
    }
