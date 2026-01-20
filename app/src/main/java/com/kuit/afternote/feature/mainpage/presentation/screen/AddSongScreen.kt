package com.kuit.afternote.feature.mainpage.presentation.screen

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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.BottomNavItem
import com.kuit.afternote.core.ui.component.BottomNavigationBar
import com.kuit.afternote.core.ui.component.CustomRadioButton
import com.kuit.afternote.core.ui.component.PlaylistSongItem
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.core.uimodel.PlaylistSongDisplay
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Song
import com.kuit.afternote.feature.mainpage.presentation.navgraph.MainPageLightTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray1
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
 *
 * @param initialSelectedSongIds Preview용. 넣으면 해당 ID가 선택된 상태로 시작해 추가하기 버튼이 노출됨 (기본 null)
 */
@Composable
fun AddSongScreen(
    modifier: Modifier = Modifier,
    availableSongs: List<Song> = rememberSearchResultSongs(),
    callbacks: AddSongCallbacks,
    initialSelectedSongIds: Set<String>? = null
) {
    var selectedSongIds by remember { mutableStateOf<Set<String>>(initialSelectedSongIds ?: emptySet()) }

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
                modifier = Modifier
                    .fillMaxSize(),
                availableSongs = availableSongs,
                selectedSongIds = selectedSongIds,
                onSongToggle = onSongToggle,
                extraBottomPadding = if (selectedSongIds.isNotEmpty()) 72.dp else 0.dp
            )
            if (selectedSongIds.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 20.dp, end = 20.dp, bottom = 24.dp)
                ) {
                    AddButton(
                        count = selectedSongIds.size,
                        onClick = onAddClick
                    )
                }
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
    val addButtonShape = RoundedCornerShape(8.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(
                elevation = 5.dp,
                shape = addButtonShape,
                clip = false,
                ambientColor = Color(0x26000000),
                spotColor = Color(0x26000000)
            )
            .background(color = Gray1, shape = addButtonShape)
            .clip(addButtonShape)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row {

            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(color = B1, shape = RoundedCornerShape(40.dp)),
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
            Spacer(Modifier.width(16.dp))
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
    var searchQuery by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 0.dp, bottom = extraBottomPadding)
    ) {
        // 검색 영역 (receiver MemorialPlaylistScreen 스타일)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.song_search_label),
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Medium,
                        color = Gray9
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.song_search_placeholder),
                            color = Gray4,
                            fontSize = 14.sp
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.song_search_label),
                            tint = Gray9
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = White,
                        unfocusedContainerColor = White,
                        focusedBorderColor = B1,
                        unfocusedBorderColor = Gray2,
                        cursorColor = B1
                    ),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 14.sp, fontFamily = Sansneo, color = Gray9),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                )
            }
        }
        // 노래 목록 (receiver 스타일 + 라디오, 구분선은 PlaylistSongItem 내부)
        itemsIndexed(songs) { index, song ->
            val display = PlaylistSongDisplay(id = song.id, title = song.title, artist = song.artist)
            PlaylistSongItem(
                song = display,
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

@Preview(showBackground = true, name = "추가하기 버튼 노출")
@Composable
private fun AddSongScreenAddButtonPreview() {
    MainPageLightTheme {
        AddSongScreen(
            callbacks = AddSongCallbacks(
                onBackClick = {},
                onSongsAdded = {}
            ),
            initialSelectedSongIds = setOf("s1", "s3")
        )
    }
}
