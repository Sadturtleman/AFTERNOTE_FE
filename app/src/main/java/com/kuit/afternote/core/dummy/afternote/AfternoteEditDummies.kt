package com.kuit.afternote.core.dummy.afternote

import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.MainPageEditReceiver
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song

/**
 * Dummy data for 애프터노트 수정 화면 (플레이리스트, 수신자 등).
 * Replace with repository/API load when backend is ready.
 */
object AfternoteEditDummies {
    /**
     * Default main page items (service name to date) for dev "main with items" screen.
     * Replace with API load when backend is ready.
     */
    fun defaultMainPageItemsForDev(): List<Pair<String, String>> =
        listOf(
            "갤러리" to "2025.01.28",
            "추모 가이드라인" to "2025.01.28"
        )

    /**
     * Default main page edit receivers for edit screen. Replace with API load when backend is ready.
     */
    fun defaultMainPageEditReceivers(): List<MainPageEditReceiver> =
        listOf(
            MainPageEditReceiver(id = "1", name = "김지은", label = "친구"),
            MainPageEditReceiver(id = "2", name = "박선호", label = "가족")
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
