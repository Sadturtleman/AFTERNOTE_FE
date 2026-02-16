package com.kuit.afternote.data.provider

import com.kuit.afternote.core.dummy.afternote.AfternoteEditDummies
import com.kuit.afternote.core.dummy.afternote.AfternoteListDummies
import com.kuit.afternote.core.dummy.album.AlbumDummies
import com.kuit.afternote.core.uimodel.PlaylistSongDisplay
import com.kuit.afternote.domain.provider.AfternoteEditDataProvider
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiver
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.Song
import javax.inject.Inject

/**
 * Fake implementation. Only place that imports and uses *Dummies.
 * Initial receivers are empty so "수신자 추가" does not show dummy names (김지은, 박선호)
 * when the user selects "추가 수신자에게 정보 전달"; matches [RealAfternoteEditDataProvider].
 */
class FakeAfternoteEditDataProvider @Inject constructor() : AfternoteEditDataProvider {
    override fun getSongs(): List<Song> =
        AfternoteEditDummies.defaultSongs()
    override fun getAfternoteEditReceivers(): List<AfternoteEditReceiver> =
        emptyList()
    override fun getDefaultAfternoteItems(): List<Pair<String, String>> =
        AfternoteListDummies.defaultAfternoteList()
    override fun getAlbumCovers(): List<com.kuit.afternote.core.ui.component.list.AlbumCover> =
        AlbumDummies.list
    override fun getAddSongSearchResults(): List<PlaylistSongDisplay> =
        AfternoteEditDummies.defaultAddSongSearchResults()
}
