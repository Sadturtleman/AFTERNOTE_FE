package com.kuit.afternote.data.provider

import com.kuit.afternote.core.dummy.afternote.AfternoteEditDummies
import com.kuit.afternote.core.dummy.album.AlbumDummies
import com.kuit.afternote.domain.provider.AfternoteEditDataProvider
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.MainPageEditReceiver
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song
import javax.inject.Inject

/**
 * Fake implementation. Only place that imports and uses *Dummies.
 */
class FakeAfternoteEditDataProvider @Inject constructor() : AfternoteEditDataProvider {
    override fun getSongs(): List<Song> =
        AfternoteEditDummies.defaultSongs()
    override fun getMainPageEditReceivers(): List<MainPageEditReceiver> =
        AfternoteEditDummies.defaultMainPageEditReceivers()
    override fun getMainPageItemsForDev(): List<Pair<String, String>> =
        AfternoteEditDummies.defaultMainPageItemsForDev()
    override fun getAlbumCovers(): List<com.kuit.afternote.core.ui.component.list.AlbumCover> =
        AlbumDummies.list
}
