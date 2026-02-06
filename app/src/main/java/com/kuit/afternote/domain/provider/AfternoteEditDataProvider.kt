package com.kuit.afternote.domain.provider

import com.kuit.afternote.core.ui.component.list.AlbumCover
import com.kuit.afternote.core.uimodel.PlaylistSongDisplay
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.MainPageEditReceiver
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song

/**
 * Provides data for afternote edit flows (songs, receivers, album covers, main page items).
 * Implementation (real vs dummy-backed) is decided at DI; consumers use this interface only.
 */
interface AfternoteEditDataProvider {
    fun getSongs(): List<Song>
    fun getMainPageEditReceivers(): List<MainPageEditReceiver>
    fun getMainPageItemsForDev(): List<Pair<String, String>>
    fun getAlbumCovers(): List<AlbumCover>

    /**
     * Search results for Add Song screen. Real impl uses API; fake returns dummies until API is ready.
     */
    fun getAddSongSearchResults(): List<PlaylistSongDisplay>
}
