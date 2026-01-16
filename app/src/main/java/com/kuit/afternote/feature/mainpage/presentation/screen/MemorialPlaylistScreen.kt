package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ArrowIconSpec
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.core.CircleArrowIcon
import com.kuit.afternote.core.CustomRadioButton
import com.kuit.afternote.core.Header
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 추모 플레이리스트 화면의 콜백
 */
@Immutable
data class MemorialPlaylistCallbacks(
    val onBackClick: () -> Unit,
    val onAddSongClick: () -> Unit,
    val onDeleteAllClick: () -> Unit = {},
    val onDeleteSelectedClick: () -> Unit = {}
)

/**
 * 추모 플레이리스트 화면
 *
 * 피그마 디자인 기반:
 * - 헤더 (뒤로가기, 타이틀, 노래 추가하기 버튼)
 * - 총 노래 개수 표시
 * - 노래 목록 (앨범 커버, 제목, 가수, 체크박스)
 * - 하단 네비게이션 바
 * - 선택 모드 시 하단 액션 바 (전체 삭제, 선택 삭제)
 */
@Composable
fun MemorialPlaylistScreen(
    modifier: Modifier = Modifier,
    stateHolder: MemorialPlaylistStateHolder = rememberMemorialPlaylistStateHolder(),
    callbacks: MemorialPlaylistCallbacks
) {
    MemorialPlaylistScaffold(
        modifier = modifier,
        stateHolder = stateHolder,
        callbacks = callbacks
    )
}

@Composable
private fun MemorialPlaylistScaffold(
    modifier: Modifier,
    stateHolder: MemorialPlaylistStateHolder,
    callbacks: MemorialPlaylistCallbacks
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                selectedItem = BottomNavItem.HOME,
                onItemSelected = { /* 네비게이션은 상위에서 처리 */ }
            )
        }
    ) { paddingValues ->
        MemorialPlaylistContent(
            modifier = Modifier.padding(paddingValues),
            stateHolder = stateHolder,
            callbacks = callbacks
        )
    }
}

@Composable
private fun MemorialPlaylistContent(
    modifier: Modifier,
    stateHolder: MemorialPlaylistStateHolder,
    callbacks: MemorialPlaylistCallbacks
) {
    val songs = stateHolder.songs
    val selectedSongIds = stateHolder.selectedSongIds
    val isSelectionMode = stateHolder.isSelectionMode

    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            MemorialPlaylistHeader(
                onBackClick = callbacks.onBackClick,
                onAddSongClick = callbacks.onAddSongClick
            )

            MemorialPlaylistList(
                songs = songs,
                selectedSongIds = selectedSongIds,
                isSelectionMode = isSelectionMode,
                onSongClick = { songId ->
                    stateHolder.toggleSongSelection(songId)
                }
            )
        }

        if (isSelectionMode && selectedSongIds.isNotEmpty()) {
            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                MemorialPlaylistActionBar(
                    onDeleteAllClick = {
                        stateHolder.deleteAll()
                        callbacks.onDeleteAllClick()
                    },
                    onDeleteSelectedClick = {
                        stateHolder.deleteSelected()
                        callbacks.onDeleteSelectedClick()
                    }
                )
            }
        }
    }
}

@Composable
private fun MemorialPlaylistHeader(
    onBackClick: () -> Unit,
    onAddSongClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
//        Header(
//            title = "추모 플레이리스트",
//            onBackClick = onBackClick
//        )

        // 노래 추가하기 버튼 (헤더 오른쪽)
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 20.dp)
                .background(
                    color = B3,
                    shape = RoundedCornerShape(20.dp)
                )
                .clickable(onClick = onAddSongClick)
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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

            CircleArrowIcon(
                iconSpec = ArrowIconSpec(
                    iconRes = R.drawable.ic_arrow_right_playlist,
                    contentDescription = "추가"
                ),
                backgroundColor = B1,
                size = 12.dp
            )
        }
    }
}

@Composable
private fun MemorialPlaylistList(
    songs: List<Song>,
    selectedSongIds: Set<String>,
    isSelectionMode: Boolean,
    onSongClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 상단 라벨 (선택 모드 여부에 따라 다르게 표시)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isSelectionMode) {
                    // 선택 모드: "현재 플레이리스트" (왼쪽), "총 N곡" (오른쪽)
                    Text(
                        text = "현재 플레이리스트",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Normal,
                            color = Gray9
                        )
                    )
                    Text(
                        text = "총 ${songs.size}곡",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Normal,
                            color = Gray9
                        )
                    )
                } else {
                    // 비선택 모드: "총 N곡" (왼쪽)
                    Text(
                        text = "총 ${songs.size}곡",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Normal,
                            color = Gray9
                        )
                    )
                }
            }
        }

        // 노래 목록
        lazyItems(songs) { song ->
            SongListItem(
                song = song,
                isSelected = selectedSongIds.contains(song.id),
                onClick = { onSongClick(song.id) }
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
        // 앨범 커버 (Placeholder - 이미지 로딩 라이브러리 추가 후 구현)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = song.title.take(1),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
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
}

@Composable
private fun MemorialPlaylistActionBar(
    onDeleteAllClick: () -> Unit,
    onDeleteSelectedClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 전체 삭제
            Text(
                text = "전체 삭제",
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onDeleteAllClick)
                    .padding(vertical = 12.dp),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    color = Gray9,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            )

            // 구분선
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(20.dp)
                    .background(Color(0xFFE0E0E0))
            )

            // 선택 삭제
            Text(
                text = "선택 삭제",
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onDeleteSelectedClick)
                    .padding(vertical = 12.dp),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    color = Gray9,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MemorialPlaylistScreenPreview() {
    AfternoteTheme {
        MemorialPlaylistScreen(
            callbacks = MemorialPlaylistCallbacks(
                onBackClick = {},
                onAddSongClick = {}
            )
        )
    }
}
