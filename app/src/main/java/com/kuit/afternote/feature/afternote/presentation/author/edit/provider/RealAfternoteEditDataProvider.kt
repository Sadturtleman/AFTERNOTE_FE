package com.kuit.afternote.feature.afternote.presentation.author.edit.provider
import com.kuit.afternote.feature.afternote.presentation.shared.component.list.AlbumCover
import com.kuit.afternote.feature.afternote.presentation.author.edit.model.AfternoteEditReceiver
import com.kuit.afternote.feature.afternote.presentation.author.edit.model.Song
import com.kuit.afternote.feature.afternote.presentation.shared.uimodel.PlaylistSongDisplay
import javax.inject.Inject

/**
 * Real implementation. Returns empty/placeholder until API is available.
 */
class RealAfternoteEditDataProvider
    @Inject
    constructor() : AfternoteEditDataProvider {
        override fun getSongs(): List<Song> = emptyList()

        override fun getAfternoteEditReceivers(): List<AfternoteEditReceiver> = emptyList()

        override fun getDefaultAfternoteItems(): List<Pair<String, String>> = emptyList()

        override fun getAlbumCovers(): List<AlbumCover> = emptyList()

        override fun getAddSongSearchResults(): List<PlaylistSongDisplay> = emptyList()
    }
