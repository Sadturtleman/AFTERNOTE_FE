package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.ArrowIconSpec
import com.kuit.afternote.core.ui.component.BottomNavItem
import com.kuit.afternote.core.ui.component.SongPlaylistScreenManagementContent
import com.kuit.afternote.core.ui.component.RightArrowIcon
import com.kuit.afternote.core.ui.component.SongPlaylistScreen
import com.kuit.afternote.core.uimodel.PlaylistSongDisplay
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song
import com.kuit.afternote.feature.mainpage.presentation.navgraph.MainPageLightTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 추모 플레이리스트 전체 화면 (MemorialPlaylistList).
 * 공통 [SongPlaylistScreen]을 사용하며, "노래 추가하기" 클릭 시 [onNavigateToAddSongScreen]으로 AddSongScreen으로 이동.
 *
 * @param onNavigateToAddSongScreen MemorialPlaylistList 내 "노래 추가하기" 클릭 시 AddSongScreen으로 이동하는 콜백
 * @param initialSelectedSongIds Preview용. 넣으면 해당 ID가 선택된 상태로 시작 (기본 null)
 */
@Composable
fun MemorialPlaylistRouteScreen(
    modifier: Modifier = Modifier,
    playlistStateHolder: MemorialPlaylistStateHolder,
    onBackClick: () -> Unit,
    onNavigateToAddSongScreen: () -> Unit,
    initialSelectedSongIds: Set<String>? = null
) {
    val songs = playlistStateHolder.songs.map { s ->
        PlaylistSongDisplay(id = s.id, title = s.title, artist = s.artist)
    }
    SongPlaylistScreen(
        modifier = modifier,
        title = "추모 플레이리스트",
        onBackClick = onBackClick,
        songs = songs,
        managementContent = SongPlaylistScreenManagementContent(
            leadingContent = { selectedIds ->
                MemorialPlaylistListHeader(
                    songCount = songs.size,
                    isSelectionMode = selectedIds.isNotEmpty(),
                    onAddSongClick = onNavigateToAddSongScreen
                )
            },
            selectionBottomBar = { selectedIds, onClearSelection ->
                MemorialPlaylistActionBar(
                    onDeleteAllClick = {
                        playlistStateHolder.clearAllSongs()
                        onClearSelection()
                    },
                    onDeleteSelectedClick = {
                        playlistStateHolder.removeSongs(selectedIds)
                        onClearSelection()
                    }
                )
            }
        ),
        defaultBottomNavItem = BottomNavItem.HOME,
        initialSelectedSongIds = initialSelectedSongIds
    )
}

/**
 * MemorialPlaylistList 화면 상단 헤더: "총 N곡" 및 "노래 추가하기" 버튼 (선택 모드가 아닐 때만 버튼 표시).
 */
@Composable
private fun MemorialPlaylistListHeader(
    modifier: Modifier = Modifier,
    songCount: Int,
    isSelectionMode: Boolean,
    onAddSongClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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
                    text = "총 ${songCount}곡",
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
                    text = "총 ${songCount}곡",
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
                        ).clickable(onClick = onAddSongClick),
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
            ).background(color = Color.White, shape = actionBarShape)
            .clip(actionBarShape)
            .padding(horizontal = 56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.clickable(onClick = onDeleteAllClick)
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
            modifier = Modifier.clickable(onClick = onDeleteSelectedClick)
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
            onNavigateToAddSongScreen = {}
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
            onNavigateToAddSongScreen = {},
            initialSelectedSongIds = setOf("1", "3")
        )
    }
}

