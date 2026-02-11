package com.kuit.afternote.core.dummy.afternote

import com.kuit.afternote.core.uimodel.PlaylistSongDisplay
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiver
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.ProcessingMethodItem
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.Song

/**
 * Dummy data for 애프터노트 수정 화면 (플레이리스트, 수신자 등).
 * Replace with repository/API load when backend is ready.
 */
object AfternoteEditDummies {
    /**
     * Default processing method items for record-type services (애프터노트 수정 화면). Dummy data.
     */
    fun defaultRecordProcessingMethods(): List<ProcessingMethodItem> =
        listOf(
            ProcessingMethodItem("1", "게시물 내리기"),
            ProcessingMethodItem("2", "추모 게시물 올리기"),
            ProcessingMethodItem("3", "추모 계정으로 전환하기")
        )

    /**
     * Default processing method items for gallery/file-type services. Dummy data.
     */
    fun defaultGalleryProcessingMethods(): List<ProcessingMethodItem> =
        listOf(
            ProcessingMethodItem("1", "'엽사' 폴더 박선호에게 전송"),
            ProcessingMethodItem("2", "'흑역사' 폴더 삭제")
        )

    /**
     * Default afternote edit receivers for edit screen. Replace with API load when backend is ready.
     */
    fun defaultAfternoteEditReceivers(): List<AfternoteEditReceiver> =
        listOf(
            AfternoteEditReceiver(id = "1", name = "김지은", label = "친구"),
            AfternoteEditReceiver(id = "2", name = "박선호", label = "가족")
        )

    /**
     * Default search-result songs for Add Song screen. Replace with real search API when ready.
     */
    fun defaultAddSongSearchResults(): List<PlaylistSongDisplay> =
        listOf(
            PlaylistSongDisplay(id = "1", title = "보고싶다", artist = "김범수"),
            PlaylistSongDisplay(id = "2", title = "사랑했나봐", artist = "윤도현"),
            PlaylistSongDisplay(id = "3", title = "나의 옛날이야기", artist = "김광석"),
            PlaylistSongDisplay(id = "4", title = "그대와 영원히", artist = "이문세"),
            PlaylistSongDisplay(id = "5", title = "흩어진 꿈", artist = "넬"),
            PlaylistSongDisplay(id = "6", title = "안녕", artist = "폴킴"),
            PlaylistSongDisplay(id = "7", title = "첫눈처럼 너에게 가겠다", artist = "에일리"),
            PlaylistSongDisplay(id = "8", title = "너를 만나", artist = "폴킴"),
            PlaylistSongDisplay(id = "9", title = "겨울비", artist = "박효신")
        )

    /**
     * Default playlist songs for edit screen. Replace with API load when backend is ready.
     */
    fun defaultSongs(): List<Song> =
        listOf(
            Song(id = "1", title = "노래 제목", artist = "가수 이름"),
            Song(id = "2", title = "노래 제목", artist = "가수 이름"),
            Song(id = "3", title = "노래 제목", artist = "가수 이름"),
            Song(id = "4", title = "노래 제목", artist = "가수 이름"),
            Song(id = "5", title = "노래 제목", artist = "가수 이름"),
            Song(id = "6", title = "노래 제목", artist = "가수 이름"),
            Song(id = "7", title = "노래 제목", artist = "가수 이름"),
            Song(id = "8", title = "노래 제목", artist = "가수 이름"),
            Song(id = "9", title = "노래 제목", artist = "가수 이름"),
            Song(id = "10", title = "노래 제목", artist = "가수 이름"),
            Song(id = "11", title = "노래 제목", artist = "가수 이름")
        )
}
