package com.kuit.afternote.feature.afternote.presentation.dummy.afternote

import com.kuit.afternote.feature.afternote.presentation.uimodel.PlaylistSongDisplay

/**
 * Dummy data for 애프터노트 수정 화면 (플레이리스트, 수신자 등).
 * Replace with repository/API load when backend is ready.
 */
object AfternoteEditDummies {
    /**
     * Default processing method items for record-type services (애프터노트 수정 화면). Dummy data.
     */
    fun defaultRecordProcessingMethods(): List<com.kuit.afternote.feature.afternote.presentation.edit.model.ProcessingMethodItem> =
        listOf(
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.ProcessingMethodItem(
                "1",
                "게시물 내리기",
            ),
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.ProcessingMethodItem(
                "2",
                "추모 게시물 올리기",
            ),
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.ProcessingMethodItem(
                "3",
                "추모 계정으로 전환하기",
            ),
        )

    /**
     * Default processing method items for gallery/file-type services. Dummy data.
     */
    fun defaultGalleryProcessingMethods(): List<com.kuit.afternote.feature.afternote.presentation.edit.model.ProcessingMethodItem> =
        listOf(
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.ProcessingMethodItem(
                "1",
                "'엽사' 폴더 박선호에게 전송",
            ),
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.ProcessingMethodItem(
                "2",
                "'흑역사' 폴더 삭제",
            ),
        )

    /**
     * Default afternote edit receivers for edit screen. Replace with API load when backend is ready.
     */
    fun defaultAfternoteEditReceivers(): List<com.kuit.afternote.feature.afternote.presentation.edit.model.AfternoteEditReceiver> =
        listOf(
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.AfternoteEditReceiver(
                id = "1",
                name = "김지은",
                label = "친구",
            ),
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.AfternoteEditReceiver(
                id = "2",
                name = "박선호",
                label = "가족",
            ),
        )

    /**
     * Default search-result songs for Add Song screen. Replace with real search API when ready.
     */
    fun defaultAddSongSearchResults(): List<com.kuit.afternote.feature.afternote.presentation.uimodel.PlaylistSongDisplay> =
        listOf(
            PlaylistSongDisplay(
                id = "1",
                title = "보고싶다",
                artist = "김범수",
            ),
            PlaylistSongDisplay(
                id = "2",
                title = "사랑했나봐",
                artist = "윤도현",
            ),
            PlaylistSongDisplay(
                id = "3",
                title = "나의 옛날이야기",
                artist = "김광석",
            ),
            PlaylistSongDisplay(
                id = "4",
                title = "그대와 영원히",
                artist = "이문세",
            ),
            PlaylistSongDisplay(
                id = "5",
                title = "흩어진 꿈",
                artist = "넬",
            ),
            PlaylistSongDisplay(
                id = "6",
                title = "안녕",
                artist = "폴킴",
            ),
            PlaylistSongDisplay(
                id = "7",
                title = "첫눈처럼 너에게 가겠다",
                artist = "에일리",
            ),
            PlaylistSongDisplay(
                id = "8",
                title = "너를 만나",
                artist = "폴킴",
            ),
            PlaylistSongDisplay(
                id = "9",
                title = "겨울비",
                artist = "박효신",
            ),
        )

    /**
     * Default playlist songs for edit screen. Replace with API load when backend is ready.
     */
    fun defaultSongs(): List<com.kuit.afternote.feature.afternote.presentation.edit.model.Song> =
        listOf(
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.Song(
                id = "1",
                title = "노래 제목",
                artist = "가수 이름",
            ),
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.Song(
                id = "2",
                title = "노래 제목",
                artist = "가수 이름",
            ),
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.Song(
                id = "3",
                title = "노래 제목",
                artist = "가수 이름",
            ),
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.Song(
                id = "4",
                title = "노래 제목",
                artist = "가수 이름",
            ),
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.Song(
                id = "5",
                title = "노래 제목",
                artist = "가수 이름",
            ),
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.Song(
                id = "6",
                title = "노래 제목",
                artist = "가수 이름",
            ),
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.Song(
                id = "7",
                title = "노래 제목",
                artist = "가수 이름",
            ),
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.Song(
                id = "8",
                title = "노래 제목",
                artist = "가수 이름",
            ),
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.Song(
                id = "9",
                title = "노래 제목",
                artist = "가수 이름",
            ),
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.Song(
                id = "10",
                title = "노래 제목",
                artist = "가수 이름",
            ),
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.Song(
                id = "11",
                title = "노래 제목",
                artist = "가수 이름",
            ),
        )
}
