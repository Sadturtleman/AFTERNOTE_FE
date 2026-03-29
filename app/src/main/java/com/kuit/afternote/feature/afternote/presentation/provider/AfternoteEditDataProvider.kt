package com.kuit.afternote.feature.afternote.presentation.provider

import com.kuit.afternote.feature.afternote.presentation.component.list.AlbumCover
import com.kuit.afternote.feature.afternote.presentation.edit.model.AfternoteEditReceiver
import com.kuit.afternote.feature.afternote.presentation.edit.model.Song
import com.kuit.afternote.feature.afternote.presentation.uimodel.PlaylistSongDisplay

/**
 * Provides data for afternote edit flows (songs, receivers, album covers, afternote items).
 * Implementation (real vs dummy-backed) is decided at DI; consumers use this interface only.
 */
interface AfternoteEditDataProvider {
    fun getSongs(): List<Song>

    fun getAfternoteEditReceivers(): List<AfternoteEditReceiver>

    fun getDefaultAfternoteItems(): List<Pair<String, String>>

    fun getAlbumCovers(): List<AlbumCover>

    /**
     * Search results for Add Song screen. Real impl uses API; fake returns dummies until API is ready.
     */
    fun getAddSongSearchResults(): List<PlaylistSongDisplay>
}
