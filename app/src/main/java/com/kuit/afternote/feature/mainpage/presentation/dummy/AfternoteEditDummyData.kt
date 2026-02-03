package com.kuit.afternote.feature.mainpage.presentation.dummy

import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song

/**
 * Dummy data for 애프터노트 수정 화면 (플레이리스트 등).
 * Replace with repository/API load when backend is ready.
 */
internal object AfternoteEditDummyData {
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
