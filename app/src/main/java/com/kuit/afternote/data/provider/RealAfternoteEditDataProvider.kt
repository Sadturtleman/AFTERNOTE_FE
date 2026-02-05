package com.kuit.afternote.data.provider

import com.kuit.afternote.core.ui.component.list.AlbumCover
import com.kuit.afternote.domain.provider.AfternoteEditDataProvider
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.MainPageEditReceiver
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song
import javax.inject.Inject

/**
 * Real implementation. Returns empty/placeholder until API is available.
 */
class RealAfternoteEditDataProvider @Inject constructor() : AfternoteEditDataProvider {
    override fun getSongs(): List<Song> = emptyList()
    override fun getMainPageEditReceivers(): List<MainPageEditReceiver> = emptyList()
    override fun getMainPageItemsForDev(): List<Pair<String, String>> = emptyList()
    override fun getAlbumCovers(): List<AlbumCover> = emptyList()
}
