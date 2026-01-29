package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.SongPlaylistScreen
import com.kuit.afternote.core.uimodel.PlaylistSongDisplay

@Composable
fun MemorialPlaylistScreen(
    modifier: Modifier = Modifier,
    songs: List<PlaylistSongDisplay>,
    onBackClick: () -> Unit
) {
    SongPlaylistScreen(
        modifier = modifier,
        title = "추모 플레이리스트",
        onBackClick = onBackClick,
        songs = songs,
        defaultBottomNavItem = BottomNavItem.AFTERNOTE
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewMemorialPlaylist() {
    MaterialTheme {
        MemorialPlaylistScreen(
            songs = (0..9).map { i ->
                PlaylistSongDisplay(id = "$i", title = "노래 제목", artist = "가수 이름")
            },
            onBackClick = {}
        )
    }
}
