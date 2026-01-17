package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song

/**
 * 추모 플레이리스트 화면의 상태를 관리하는 State Holder
 */
@Stable
class MemorialPlaylistStateHolder {
    var songs by mutableStateOf<List<Song>>(emptyList())
        private set

    var selectedSongIds by mutableStateOf<Set<String>>(emptySet())
        private set

    var isSelectionMode by mutableStateOf(false)
        private set
    
    /**
     * 노래 개수 변경 콜백 (AfternoteEditState와 동기화용)
     */
    var onSongCountChanged: (() -> Unit)? = null

    /**
     * 노래 선택/해제
     */
    fun toggleSongSelection(songId: String) {
        selectedSongIds = if (selectedSongIds.contains(songId)) {
            selectedSongIds - songId
        } else {
            selectedSongIds + songId
        }
        // 선택된 노래가 있으면 선택 모드 활성화
        isSelectionMode = selectedSongIds.isNotEmpty()
    }

    /**
     * 전체 삭제
     */
    fun deleteAll() {
        songs = emptyList()
        selectedSongIds = emptySet()
        isSelectionMode = false
        onSongCountChanged?.invoke()
    }

    /**
     * 선택된 노래 삭제
     */
    fun deleteSelected() {
        songs = songs.filter { it.id !in selectedSongIds }
        selectedSongIds = emptySet()
        isSelectionMode = false
        onSongCountChanged?.invoke()
    }

    /**
     * 초기 데이터 설정
     */
    fun initializeSongs(songs: List<Song>) {
        this.songs = songs
        onSongCountChanged?.invoke()
    }
    
    /**
     * 노래 추가 (AddSongScreen에서 사용)
     */
    fun addSongs(newSongs: List<Song>) {
        songs = songs + newSongs
        onSongCountChanged?.invoke()
    }
}

/**
 * MemorialPlaylistStateHolder를 생성하는 Composable 함수
 */
@Stable
@Composable
fun rememberMemorialPlaylistStateHolder(): MemorialPlaylistStateHolder {
    val stateHolder = remember { MemorialPlaylistStateHolder() }
    
    // 초기 데이터 설정
    if (stateHolder.songs.isEmpty()) {
        stateHolder.initializeSongs(
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
        )
    }
    
    return stateHolder
}
