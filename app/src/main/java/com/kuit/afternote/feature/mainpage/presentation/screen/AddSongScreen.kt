package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.core.CustomRadioButton
import com.kuit.afternote.core.Header
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 노래 추가 화면의 콜백
 */
@Immutable
data class AddSongCallbacks(
    val onBackClick: () -> Unit,
    val onSongSelected: (Song) -> Unit
)

/**
 * 노래 추가하기 화면
 *
 * 피그마 디자인 기반:
 * - 헤더 (뒤로가기, "추모 플레이리스트 추가" 타이틀)
 * - 노래 검색/선택 목록
 * - 하단 네비게이션 바
 */
@Composable
fun AddSongScreen(
    modifier: Modifier = Modifier,
    availableSongs: List<Song> = rememberAvailableSongs(),
    selectedSongIds: Set<String> = emptySet(),
    callbacks: AddSongCallbacks
) {
    AddSongScaffold(
        modifier = modifier,
        availableSongs = availableSongs,
        selectedSongIds = selectedSongIds,
        callbacks = callbacks
    )
}

@Composable
private fun rememberAvailableSongs(): List<Song> {
    return remember {
        listOf(
            Song(id = "10", title = "노래 제목", artist = "가수 이름"),
            Song(id = "11", title = "노래 제목", artist = "가수 이름"),
            Song(id = "12", title = "노래 제목", artist = "가수 이름"),
            Song(id = "13", title = "노래 제목", artist = "가수 이름"),
            Song(id = "14", title = "노래 제목", artist = "가수 이름"),
            Song(id = "15", title = "노래 제목", artist = "가수 이름"),
            Song(id = "16", title = "노래 제목", artist = "가수 이름"),
            Song(id = "17", title = "노래 제목", artist = "가수 이름"),
            Song(id = "18", title = "노래 제목", artist = "가수 이름")
        )
    }
}

@Composable
private fun AddSongScaffold(
    modifier: Modifier,
    availableSongs: List<Song>,
    selectedSongIds: Set<String>,
    callbacks: AddSongCallbacks
) {
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.HOME) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { selectedBottomNavItem = it }
            )
        }
    ) { paddingValues ->
        AddSongContent(
            modifier = Modifier.padding(paddingValues),
            availableSongs = availableSongs,
            selectedSongIds = selectedSongIds,
            callbacks = callbacks
        )
    }
}

@Composable
private fun AddSongContent(
    modifier: Modifier,
    availableSongs: List<Song>,
    selectedSongIds: Set<String>,
    callbacks: AddSongCallbacks
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
//            Header(
//                title = "추모 플레이리스트 추가",
//                onBackClick = callbacks.onBackClick,
//            )

            AddSongList(
                songs = availableSongs,
                selectedSongIds = selectedSongIds,
                onSongClick = { song ->
                    callbacks.onSongSelected(song)
                }
            )
        }
    }
}

@Composable
private fun AddSongList(
    songs: List<Song>,
    selectedSongIds: Set<String>,
    onSongClick: (Song) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(songs) { song ->
            SongListItem(
                song = song,
                isSelected = selectedSongIds.contains(song.id),
                onClick = { onSongClick(song) }
            )
        }
    }
}

@Composable
private fun SongListItem(
    song: Song,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 앨범 커버
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Black)
        ) {
            // TODO: 실제 이미지 로드
        }

        // 노래 정보
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = song.title,
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    color = Gray9
                )
            )
            Text(
                text = song.artist,
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray9
                )
            )
        }

        // 체크박스
        CustomRadioButton(
            selected = isSelected,
            onClick = null,
            buttonSize = 24.dp,
            selectedColor = B2,
            unselectedColor = Gray4
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddSongScreenPreview() {
    AfternoteTheme {
        AddSongScreen(
            callbacks = AddSongCallbacks(
                onBackClick = {},
                onSongSelected = {}
            )
        )
    }
}
