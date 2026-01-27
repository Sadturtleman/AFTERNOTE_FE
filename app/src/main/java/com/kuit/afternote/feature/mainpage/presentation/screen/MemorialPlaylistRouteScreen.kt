package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.ArrowIconSpec
import com.kuit.afternote.core.ui.component.BottomNavItem
import com.kuit.afternote.core.ui.component.BottomNavigationBar
import com.kuit.afternote.core.ui.component.CustomRadioButton
import com.kuit.afternote.core.ui.component.PlaylistSongItem
import com.kuit.afternote.core.ui.component.RightArrowIcon
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.core.uimodel.PlaylistSongDisplay
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song
import com.kuit.afternote.feature.mainpage.presentation.navgraph.MainPageLightTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 추모 플레이리스트 화면.
 * - 헤더: "추모 플레이리스트"
 * - 총 N곡, "노래 추가하기" 버튼(→ 추모 플레이리스트 추가 화면으로 이동), 담은 곡 목록
 *
 * @param initialSelectedSongIds Preview용. 넣으면 해당 ID가 선택된 상태로 시작 (기본 null)
 */
@Composable
fun MemorialPlaylistRouteScreen(
    modifier: Modifier = Modifier,
    playlistStateHolder: MemorialPlaylistStateHolder,
    onBackClick: () -> Unit,
    onNavigateToAddSong: () -> Unit,
    initialSelectedSongIds: Set<String>? = null
) {
    val selectedBottomNavItemState = remember { mutableStateOf(BottomNavItem.HOME) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = "추모 플레이리스트",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItemState.value,
                onItemSelected = { selectedBottomNavItemState.value = it }
            )
        }
    ) { paddingValues ->
        var selectedSongIds by remember {
            mutableStateOf(initialSelectedSongIds ?: emptySet())
        }
        val actionBarHeight = 80.dp
        val gapAboveNavBar = 24.dp
        val extraBottomPadding by animateDpAsState(
            targetValue = if (selectedSongIds.isNotEmpty()) actionBarHeight + gapAboveNavBar else 0.dp,
            label = "ListPaddingAnimation"
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MemorialPlaylistList(
                modifier = Modifier.fillMaxSize(),
                songs = playlistStateHolder.songs,
                selectedSongIds = selectedSongIds,
                onSongToggle = { id ->
                    selectedSongIds = if (id in selectedSongIds) selectedSongIds - id else selectedSongIds + id
                },
                onAddSongClick = onNavigateToAddSong,
                extraBottomPadding = extraBottomPadding
            )
            AnimatedVisibility(
                visible = selectedSongIds.isNotEmpty(),
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = gapAboveNavBar)
            ) {
                MemorialPlaylistActionBar(
                    onDeleteAllClick = {
                        playlistStateHolder.clearAllSongs()
                        selectedSongIds = emptySet()
                    },
                    onDeleteSelectedClick = {
                        playlistStateHolder.removeSongs(selectedSongIds)
                        selectedSongIds = emptySet()
                    }
                )
            }
        }
    }
}

@Composable
private fun MemorialPlaylistList(
    modifier: Modifier = Modifier,
    songs: List<Song>,
    selectedSongIds: Set<String>,
    onSongToggle: (String) -> Unit,
    onAddSongClick: () -> Unit,
    extraBottomPadding: Dp = 0.dp
) {
    val isSelectionMode = selectedSongIds.isNotEmpty()
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 0.dp, bottom = extraBottomPadding)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (isSelectionMode) {
                    Column {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "현재 플레이리스트",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 22.sp,
                                fontFamily = Sansneo,
                                fontWeight = FontWeight.Medium,
                                color = Gray9
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Column {
                        Spacer(modifier = Modifier.height(25.dp))
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
                        Spacer(modifier = Modifier.height(17.dp))
                    }
                } else {
                    Column {
                        Spacer(modifier = Modifier.height(25.dp))
                        Text(
                            text = "총 ${songs.size}곡",
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
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .background(
                                    color = B3,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable(onClick = onAddSongClick),
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
        }
        itemsIndexed(songs) { _, song ->
            val display = PlaylistSongDisplay(id = song.id, title = song.title, artist = song.artist)
            PlaylistSongItem(
                song = display,
//                displayIndex = index + 1,
                onClick = { onSongToggle(song.id) },
                trailingContent = {
                    CustomRadioButton(
                        selected = song.id in selectedSongIds,
                        onClick = null,
                        buttonSize = 24.dp,
                        selectedColor = B2,
                        unselectedColor = Gray4
                    )
                }
            )
        }
    }
}

@Composable
private fun MemorialPlaylistActionBar(
    modifier: Modifier = Modifier,
    onDeleteAllClick: () -> Unit,
    onDeleteSelectedClick: () -> Unit
) {
    val actionBarShape = RoundedCornerShape(8.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 5.dp,
                shape = actionBarShape,
                clip = false,
                ambientColor = Color(0x26000000),
                spotColor = Color(0x26000000)
            )
            .background(color = Color.White, shape = actionBarShape)
            .clip(actionBarShape)
            .padding(horizontal = 56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = onDeleteAllClick)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "전체 삭제",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray9,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(20.dp)
                .background(Color(0xFFE0E0E0))
        )
        Column(
            modifier = Modifier
                .clickable(onClick = onDeleteSelectedClick)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "선택 삭제",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray9,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview(showBackground = true, name = "액션 바")
@Composable
private fun MemorialPlaylistActionBarPreview() {
    MainPageLightTheme {
        Box(
            modifier = Modifier
                .size(width = 360.dp, height = 160.dp)
                .background(Color(0xFFB3D9FF))
        ) {
            MemorialPlaylistActionBar(
                modifier = Modifier.align(Alignment.Center),
                onDeleteAllClick = {},
                onDeleteSelectedClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MemorialPlaylistRouteScreenPreview() {
    MainPageLightTheme {
        val holder = MemorialPlaylistStateHolder().apply {
            initializeSongs(
                (1..3).map {
                    Song(id = "$it", title = "노래 제목", artist = "가수 이름")
                }
            )
        }
        MemorialPlaylistRouteScreen(
            playlistStateHolder = holder,
            onBackClick = {},
            onNavigateToAddSong = {}
        )
    }
}

@Preview(showBackground = true, name = "선택 모드")
@Composable
private fun MemorialPlaylistRouteScreenSelectionModePreview() {
    MainPageLightTheme {
        val holder = MemorialPlaylistStateHolder().apply {
            initializeSongs(
                (1..4).map {
                    Song(id = "$it", title = "노래 제목 $it", artist = "가수 이름")
                }
            )
        }
        MemorialPlaylistRouteScreen(
            playlistStateHolder = holder,
            onBackClick = {},
            onNavigateToAddSong = {},
            initialSelectedSongIds = setOf("1", "3")
        )
    }
}
