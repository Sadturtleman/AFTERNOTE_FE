package com.kuit.afternote.data.provider

import com.kuit.afternote.core.ui.component.list.AlbumCover
import com.kuit.afternote.core.uimodel.PlaylistSongDisplay
import com.kuit.afternote.domain.provider.AfternoteEditDataProvider
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiver
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.Song
import javax.inject.Inject

/**
 * Real implementation. Returns empty/placeholder until API is available.
 */
class RealAfternoteEditDataProvider @Inject constructor() : AfternoteEditDataProvider {
    override fun getSongs(): List<Song> = emptyList()
    override fun getAfternoteEditReceivers(): List<AfternoteEditReceiver> = emptyList()
    override fun getDefaultAfternoteItems(): List<Pair<String, String>> = emptyList()
    override fun getAlbumCovers(): List<AlbumCover> = emptyList()
    override fun getAddSongSearchResults(): List<PlaylistSongDisplay> = emptyList()
}
