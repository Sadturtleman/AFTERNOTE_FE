package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.BottomNavItem
import com.kuit.afternote.core.ui.component.BottomNavigationBar
import com.kuit.afternote.core.ui.component.CustomRadioButton
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.feature.mainpage.presentation.component.edit.PlaylistSongItem
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song
import com.kuit.afternote.feature.mainpage.presentation.navgraph.MainPageLightTheme
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

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
 * - 노래 검색창, 검색 결과 선택 목록
 * - 하단 네비게이션 바
 */
@Composable
fun AddSongScreen(
    modifier: Modifier = Modifier,
    availableSongs: List<Song> = rememberSearchResultSongs(),
    callbacks: AddSongCallbacks
) {
    var selectedSongIds by remember { mutableStateOf<Set<String>>(emptySet()) }

    AddSongScaffold(
        modifier = modifier,
        availableSongs = availableSongs,
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

/**
 * 검색 결과용 목업. 플레이리스트 곡과 구분되도록 "검색 곡 N", "검색 가수 X" 형식.
 */
@Composable
private fun rememberSearchResultSongs(): List<Song> {
    return remember {
        listOf(
            Song(id = "s1", title = "검색 곡 1", artist = "검색 가수 A"),
            Song(id = "s2", title = "검색 곡 2", artist = "검색 가수 B"),
            Song(id = "s3", title = "검색 곡 3", artist = "검색 가수 C"),
            Song(id = "s4", title = "검색 곡 4", artist = "검색 가수 D"),
            Song(id = "s5", title = "검색 곡 5", artist = "검색 가수 E"),
            Song(id = "s6", title = "검색 곡 6", artist = "검색 가수 F"),
            Song(id = "s7", title = "검색 곡 7", artist = "검색 가수 G"),
            Song(id = "s8", title = "검색 곡 8", artist = "검색 가수 H"),
            Song(id = "s9", title = "검색 곡 9", artist = "검색 가수 I")
        )
    }
}

@Composable
private fun AddSongScaffold(
    modifier: Modifier,
    availableSongs: List<Song>,
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
        Box(modifier = Modifier.padding(paddingValues)) {
            AddSongContent(
                modifier = Modifier.fillMaxSize(),
                availableSongs = availableSongs,
                selectedSongIds = selectedSongIds,
                onSongToggle = onSongToggle,
                extraBottomPadding = if (selectedSongIds.isNotEmpty()) 72.dp else 0.dp
            )
            if (selectedSongIds.isNotEmpty()) {
                AddButton(
                    count = selectedSongIds.size,
                    onClick = onAddClick,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}

/**
 * 선택 시에만 노출되는 추가하기 버튼 (피그마 1518-11274).
 * - 연한 회색 배경, 왼쪽에 파란 원형 뱃지(선택 개수), 오른쪽 "추가하기"
 */
@Composable
private fun AddButton(
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(color = Gray2, shape = RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(color = B2, shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$count",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    color = White
                )
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.add_button),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray9
            )
        )
    }
}

@Composable
private fun AddSongContent(
    modifier: Modifier,
    availableSongs: List<Song>,
    selectedSongIds: Set<String>,
    onSongToggle: (String) -> Unit,
    extraBottomPadding: Dp = 0.dp
) {
    AddSongList(
        modifier = modifier,
        songs = availableSongs,
        selectedSongIds = selectedSongIds,
        onSongClick = { onSongToggle(it) },
        extraBottomPadding = extraBottomPadding
    )
}

@Composable
private fun AddSongList(
    modifier: Modifier = Modifier,
    songs: List<Song>,
    selectedSongIds: Set<String>,
    onSongClick: (String) -> Unit,
    extraBottomPadding: Dp = 0.dp
) {
    val searchQueryState = rememberTextFieldState()

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 0.dp, bottom = extraBottomPadding)
    ) {
        // 검색창 (피그마 1518-11257: 노래 검색 라벨 + 입력창만, 총 곡 수·노래 추가하기 버튼 없음)
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.song_search_label),
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Medium,
                        color = Gray9
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    state = searchQueryState,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.song_search_placeholder),
                            fontSize = 16.sp,
                            fontFamily = Sansneo,
                            color = Gray4
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.song_search_label),
                            tint = Gray4,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    lineLimits = TextFieldLineLimits.SingleLine,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Gray2,
                        focusedContainerColor = Gray2,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    textStyle = TextStyle(fontSize = 16.sp, fontFamily = Sansneo, color = Gray9)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        // 노래 목록 (1부터 차례대로 숫자 표시)
        itemsIndexed(songs) { index, song ->
            PlaylistSongItem(
                song = song,
                displayIndex = index + 1,
                onClick = { onSongClick(song.id) },
                trailingContent = {
                    CustomRadioButton(
                        selected = selectedSongIds.contains(song.id),
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
