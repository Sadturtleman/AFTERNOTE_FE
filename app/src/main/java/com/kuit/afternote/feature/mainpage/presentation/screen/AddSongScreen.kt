package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kuit.afternote.core.ui.component.SongPlaylistScreen
import com.kuit.afternote.core.uimodel.PlaylistSongDisplay
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song
import com.kuit.afternote.feature.mainpage.presentation.navgraph.MainPageLightTheme

/**
 * 노래 추가 화면의 콜백
 */
@Immutable
data class AddSongCallbacks(
    val onBackClick: () -> Unit,
    val onSongsAdded: (List<Song>) -> Unit
)

/**
 * 노래 추가하기 화면.
 * 공통 SongPlaylistScreen(selectable)을 사용.
 *
 * @param songs 표시할 노래 목록
 * @param initialSelectedSongIds Preview용. 넣으면 해당 ID가 선택된 상태로 시작
 */
@Composable
fun AddSongScreen(
    modifier: Modifier = Modifier,
    songs: List<PlaylistSongDisplay>,
    callbacks: AddSongCallbacks,
    initialSelectedSongIds: Set<String>? = null
) {
    SongPlaylistScreen(
        modifier = modifier,
        title = "추모 플레이리스트 추가",
        onBackClick = callbacks.onBackClick,
        songs = songs,
        onSongsSelected = { selected ->
            callbacks.onSongsAdded(selected.map { Song(id = it.id, title = it.title, artist = it.artist) })
        },
        initialSelectedSongIds = initialSelectedSongIds
    )
}

@Preview(showBackground = true)
@Composable
private fun AddSongScreenPreview() {
    MainPageLightTheme {
        AddSongScreen(
            songs = (1..9).map { i ->
                PlaylistSongDisplay(id = "s$i", title = "노래 제목 $i", artist = "가수 이름")
            },
            callbacks = AddSongCallbacks(
                onBackClick = {},
                onSongsAdded = {}
            )
        )
    }
}

@Preview(showBackground = true, name = "추가하기 버튼 노출")
@Composable
private fun AddSongScreenAddButtonPreview() {
    MainPageLightTheme {
        AddSongScreen(
            songs = (1..9).map { i ->
                PlaylistSongDisplay(id = "s$i", title = "노래 제목 $i", artist = "가수 이름")
            },
            callbacks = AddSongCallbacks(
                onBackClick = {},
                onSongsAdded = {}
            ),
            initialSelectedSongIds = setOf("s1", "s3")
        )
    }
}
