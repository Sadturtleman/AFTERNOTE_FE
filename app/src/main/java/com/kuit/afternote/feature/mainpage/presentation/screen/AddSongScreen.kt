package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.ArrowIconSpec
import com.kuit.afternote.core.ui.component.BottomNavItem
import com.kuit.afternote.core.ui.component.BottomNavigationBar
import com.kuit.afternote.core.ui.component.CustomRadioButton
import com.kuit.afternote.core.ui.component.RightArrowIcon
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song
import com.kuit.afternote.feature.mainpage.presentation.navgraph.MainPageLightTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 노래 추가 화면의 콜백
 */
@Immutable
data class AddSongCallbacks(
    val onBackClick: () -> Unit,
    val onSongsAdded: (List<Song>) -> Unit
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
    currentPlaylistCount: Int? = null,
    callbacks: AddSongCallbacks
) {
    var selectedSongIds by remember { mutableStateOf<Set<String>>(emptySet()) }

    AddSongScaffold(
        modifier = modifier,
        availableSongs = availableSongs,
        currentPlaylistCount = currentPlaylistCount,
        selectedSongIds = selectedSongIds,
        onSongToggle = { songId ->
            selectedSongIds = if (selectedSongIds.contains(songId)) {
                selectedSongIds - songId
            } else {
                selectedSongIds + songId
            }
        },
        onAddClick = {
            val selectedSongs = availableSongs.filter { it.id in selectedSongIds }
            callbacks.onSongsAdded(selectedSongs)
        },
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
    currentPlaylistCount: Int?,
    selectedSongIds: Set<String>,
    onSongToggle: (String) -> Unit,
    onAddClick: () -> Unit,
    callbacks: AddSongCallbacks
) {
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.HOME) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = "추모 플레이리스트 추가",
                onBackClick = callbacks.onBackClick
            )
        },
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
            currentPlaylistCount = currentPlaylistCount,
            selectedSongIds = selectedSongIds,
            onSongToggle = onSongToggle,
            onAddClick = onAddClick
        )
    }
}

@Composable
private fun AddSongContent(
    modifier: Modifier,
    availableSongs: List<Song>,
    currentPlaylistCount: Int?,
    selectedSongIds: Set<String>,
    onSongToggle: (String) -> Unit,
    onAddClick: () -> Unit
) {
    AddSongList(
        modifier = modifier,
        songs = availableSongs,
        currentPlaylistCount = currentPlaylistCount,
        selectedSongIds = selectedSongIds,
        onSongClick = { onSongToggle(it) },
        onAddClick = onAddClick
    )
}

@Composable
private fun AddSongList(
    modifier: Modifier = Modifier,
    songs: List<Song>,
    currentPlaylistCount: Int?,
    selectedSongIds: Set<String>,
    onSongClick: (String) -> Unit,
    onAddClick: () -> Unit
) {
    val displayCount = currentPlaylistCount ?: songs.size

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        // 상단: 총 곡 수와 노래 추가하기 버튼 (작성 화면 플레이리스트 개수와 일치)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 왼쪽: 총 곡 수 (currentPlaylistCount 있으면 플레이리스트 기준, 없으면 추가 가능 목록 기준)
                Column {
                    Spacer(modifier = Modifier.height(25.dp))
                    Text(
                        text = "총 ${displayCount}곡",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Normal,
                            color = Color(color = 0xFF000000)
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // 오른쪽: 노래 추가하기 버튼
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .background(
                                color = B3,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable(onClick = onAddClick),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                Text(
                                    text = "노래 추가하기",
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp,
                                        fontFamily = Sansneo,
                                        fontWeight = FontWeight.Medium,
                                        color = Gray9
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                RightArrowIcon(
                                    iconSpec = ArrowIconSpec(
                                        iconRes = R.drawable.ic_arrow_right_playlist,
                                        contentDescription = "추가"
                                    ),
                                    backgroundColor = B1,
                                    size = 12.dp
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    Spacer(modifier = Modifier.height(11.dp))
                }
            }
        }

        // 노래 목록 (1부터 차례대로 숫자 표시)
        itemsIndexed(songs) { index, song ->
            SongListItem(
                song = song,
                displayIndex = index + 1,
                isSelected = selectedSongIds.contains(song.id),
                onClick = { onSongClick(song.id) }
            )
        }
    }
}

@Composable
private fun SongListItem(
    song: Song,
    displayIndex: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 앨범 커버 (개발 편의: 1부터 차례대로 숫자 적힌 placeholder 이미지)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                val placeholderResId = when (((displayIndex - 1) % 12) + 1) {
                    1 -> R.drawable.img_placeholder_1
                    2 -> R.drawable.img_placeholder_2
                    3 -> R.drawable.img_placeholder_3
                    4 -> R.drawable.img_placeholder_4
                    5 -> R.drawable.img_placeholder_5
                    6 -> R.drawable.img_placeholder_6
                    7 -> R.drawable.img_placeholder_7
                    8 -> R.drawable.img_placeholder_8
                    9 -> R.drawable.img_placeholder_9
                    10 -> R.drawable.img_placeholder_10
                    11 -> R.drawable.img_placeholder_11
                    else -> R.drawable.img_placeholder_12
                }
                Image(
                    painter = painterResource(id = placeholderResId),
                    contentDescription = "$displayIndex",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
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
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun AddSongScreenPreview() {
    MainPageLightTheme {
        AddSongScreen(
            callbacks = AddSongCallbacks(
                onBackClick = {},
                onSongsAdded = {}
            )
        )
    }
}
