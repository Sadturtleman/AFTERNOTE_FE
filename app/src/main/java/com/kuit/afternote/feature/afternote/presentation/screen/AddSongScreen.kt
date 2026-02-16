package com.kuit.afternote.feature.afternote.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.core.ui.component.list.SongPlaylistScreen
import com.kuit.afternote.core.ui.component.list.SongPlaylistScreenSelectableOptions
import com.kuit.afternote.core.uimodel.PlaylistSongDisplay
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.Song
import com.kuit.afternote.feature.afternote.presentation.navgraph.AfternoteLightTheme

/**
 * 노래 추가 화면의 콜백
 */
@Immutable
data class AddSongCallbacks(
    val onBackClick: () -> Unit,
    val onSongsAdded: (List<Song>) -> Unit
)

/**
 * 노래 추가하기 화면 (API 검색 연동).
 * [viewModel]의 검색어로 GET /api/music/search 호출 결과를 표시.
 */
@Composable
fun AddSongScreen(
    modifier: Modifier = Modifier,
    viewModel: AddSongViewModelContract,
    callbacks: AddSongCallbacks
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SongPlaylistScreen(
        modifier = modifier,
        title = "추모 플레이리스트 추가",
        onBackClick = callbacks.onBackClick,
        songs = uiState.songs,
        onSongsSelected = { selected ->
            callbacks.onSongsAdded(
                selected.map {
                    Song(
                        id = it.id,
                        title = it.title,
                        artist = it.artist,
                        albumCoverUrl = it.albumImageUrl
                    )
                }
            )
        },
        options = SongPlaylistScreenSelectableOptions(
            searchQuery = uiState.searchQuery,
            onSearchQueryChange = viewModel::onSearchQueryChange
        )
    )
}

/**
 * 노래 추가하기 화면 (Preview·더미용).
 * 정적 목록만 표시하며 검색은 클라이언트 필터만 적용.
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
            callbacks.onSongsAdded(
                selected.map {
                    Song(
                        id = it.id,
                        title = it.title,
                        artist = it.artist,
                        albumCoverUrl = it.albumImageUrl
                    )
                }
            )
        },
        options = SongPlaylistScreenSelectableOptions(initialSelectedSongIds = initialSelectedSongIds)
    )
}

@Preview(showBackground = true)
@Composable
private fun AddSongScreenPreview() {
    AfternoteLightTheme {
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
    AfternoteLightTheme {
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

/**
 * Fake [AddSongViewModelContract] for Preview (no Hilt).
 */
private class FakeAddSongViewModel : AddSongViewModelContract {
    override val uiState =
        MutableStateFlow(
            AddSongUiState(
                songs = (1..5).map { i ->
                    PlaylistSongDisplay(
                        id = "f$i",
                        title = "노래 $i",
                        artist = "가수"
                    )
                },
                searchQuery = "아이유"
            )
        ).asStateFlow()

    override fun onSearchQueryChange(query: String) {
        // No-op for Preview; real ViewModel triggers API search.
    }
}

@Preview(showBackground = true, name = "API 검색 연동 (Fake VM)")
@Composable
private fun AddSongScreenWithViewModelPreview() {
    AfternoteLightTheme {
        AddSongScreen(
            viewModel = FakeAddSongViewModel(),
            callbacks = AddSongCallbacks(onBackClick = {}, onSongsAdded = {})
        )
    }
}
