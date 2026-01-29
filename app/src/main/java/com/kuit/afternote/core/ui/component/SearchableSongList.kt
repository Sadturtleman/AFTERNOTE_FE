package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.core.uimodel.PlaylistSongDisplay
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

/**
 * Slots for [SearchableSongList]: optional trailing (per row) and leading (header) content.
 *
 * @param trailingContent Optional composable for each row (e.g. radio button).
 * @param leadingContent Optional composable for the first item (e.g. custom header).
 */
data class SearchableSongListSlots(
    val trailingContent: (@Composable RowScope.(PlaylistSongDisplay) -> Unit)? = null,
    val leadingContent: (@Composable () -> Unit)? = null
)

/**
 * Content slots for the management-mode [SongPlaylistScreen]: leading header and selection bottom bar.
 *
 * @param leadingContent Header composable receiving selectedIds.
 * @param selectionBottomBar Bottom bar composable when selection is non-empty.
 */
data class SongPlaylistScreenManagementContent(
    val leadingContent: @Composable (selectedIds: Set<String>) -> Unit,
    val selectionBottomBar: @Composable (selectedIds: Set<String>, onClearSelection: () -> Unit) -> Unit
)

// region ── SongPlaylistScreen (full screen composable) ──

/**
 * 노래 검색 + 목록 전체 화면 (view-only).
 * Scaffold(TopBar + BottomNavigationBar) + SearchableSongList.
 *
 * @param title TopBar에 표시할 타이틀
 * @param onBackClick 뒤로가기 콜백
 * @param songs 표시할 노래 목록
 * @param defaultBottomNavItem 초기 선택 BottomNavItem
 */
@Composable
@Suppress("AssignedValueIsNeverRead")
fun SongPlaylistScreen(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit,
    songs: List<PlaylistSongDisplay>,
    defaultBottomNavItem: BottomNavItem = BottomNavItem.HOME
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedBottomNavItem by remember { mutableStateOf(defaultBottomNavItem) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = title,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { selectedBottomNavItem = it }
            )
        }
    ) { paddingValues ->
        SearchableSongList(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            songs = songs,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            contentPadding = PaddingValues(horizontal = 20.dp)
        )
    }
}

/**
 * 노래 검색 + 선택 가능 목록 전체 화면 (selectable).
 * 라디오 버튼 + 하단 추가하기 버튼 포함.
 *
 * @param title TopBar에 표시할 타이틀
 * @param onBackClick 뒤로가기 콜백
 * @param songs 표시할 노래 목록
 * @param onSongsSelected 추가하기 버튼 클릭 시 선택된 노래 목록 전달
 * @param defaultBottomNavItem 초기 선택 BottomNavItem
 * @param initialSelectedSongIds Preview용 초기 선택 ID (기본 null)
 */
@Composable
@Suppress("AssignedValueIsNeverRead")
fun SongPlaylistScreen(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit,
    songs: List<PlaylistSongDisplay>,
    onSongsSelected: (List<PlaylistSongDisplay>) -> Unit,
    defaultBottomNavItem: BottomNavItem = BottomNavItem.HOME,
    initialSelectedSongIds: Set<String>? = null
) {
    var selectedSongIds by remember { mutableStateOf(initialSelectedSongIds ?: emptySet<String>()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedBottomNavItem by remember { mutableStateOf(defaultBottomNavItem) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = title,
                onBackClick = onBackClick
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
            SearchableSongList(
                modifier = Modifier.fillMaxSize(),
                songs = songs,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onSongClick = { song ->
                    selectedSongIds = if (song.id in selectedSongIds) {
                        selectedSongIds - song.id
                    } else {
                        selectedSongIds + song.id
                    }
                },
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = if (selectedSongIds.isNotEmpty()) 72.dp else 0.dp
                ),
                slots = SearchableSongListSlots(
                    trailingContent = { song ->
                        CustomRadioButton(
                            selected = selectedSongIds.contains(song.id),
                            onClick = null,
                            buttonSize = 24.dp,
                            selectedColor = B2,
                            unselectedColor = Gray4
                        )
                    }
                )
            )
            if (selectedSongIds.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 20.dp, end = 20.dp, bottom = 24.dp)
                ) {
                    SongAddButton(
                        count = selectedSongIds.size,
                        onClick = {
                            val selected = songs.filter { it.id in selectedSongIds }
                            onSongsSelected(selected)
                        }
                    )
                }
            }
        }
    }
}

/**
 * 노래 선택 + 커스텀 하단 액션 바가 있는 플레이리스트 화면 (관리 모드).
 * managementContent.leadingContent로 "총 N곡" + 노래 추가하기 등 헤더를 넣고,
 * managementContent.selectionBottomBar로 선택 시 표시할 액션 바(예: 전체 삭제/선택 삭제)를 넣을 수 있음.
 *
 * @param title TopBar 타이틀
 * @param onBackClick 뒤로가기 콜백
 * @param songs 표시할 노래 목록
 * @param managementContent leadingContent + selectionBottomBar (헤더 및 하단 액션 바)
 * @param defaultBottomNavItem 초기 BottomNavItem
 * @param initialSelectedSongIds Preview용 초기 선택 ID
 */
@Composable
@Suppress("AssignedValueIsNeverRead")
fun SongPlaylistScreen(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit,
    songs: List<PlaylistSongDisplay>,
    managementContent: SongPlaylistScreenManagementContent,
    defaultBottomNavItem: BottomNavItem = BottomNavItem.HOME,
    initialSelectedSongIds: Set<String>? = null
) {
    var selectedSongIds by remember {
        mutableStateOf(initialSelectedSongIds ?: emptySet<String>())
    }
    var selectedBottomNavItem by remember { mutableStateOf(defaultBottomNavItem) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = title,
                onBackClick = onBackClick
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
            SearchableSongList(
                modifier = Modifier.fillMaxSize(),
                songs = songs,
                searchQuery = "",
                onSearchQueryChange = {},
                onSongClick = { song ->
                    selectedSongIds = if (song.id in selectedSongIds) {
                        selectedSongIds - song.id
                    } else {
                        selectedSongIds + song.id
                    }
                },
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = if (selectedSongIds.isNotEmpty()) 72.dp else 0.dp
                ),
                slots = SearchableSongListSlots(
                    trailingContent = { song ->
                        CustomRadioButton(
                            selected = selectedSongIds.contains(song.id),
                            onClick = null,
                            buttonSize = 24.dp,
                            selectedColor = B2,
                            unselectedColor = Gray4
                        )
                    },
                    leadingContent = { managementContent.leadingContent(selectedSongIds) }
                )
            )
            if (selectedSongIds.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 20.dp, end = 20.dp, bottom = 24.dp)
                ) {
                    managementContent.selectionBottomBar(selectedSongIds) {
                        selectedSongIds = emptySet()
                    }
                }
            }
        }
    }
}

// endregion

// region ── SearchableSongList (list-level composable) ──

/**
 * 검색창 + 노래 목록 패턴.
 * SongPlaylistScreen 내부에서 사용하거나, 커스텀 Scaffold가 필요한 경우 직접 사용.
 *
 * @param songs 표시할 노래 목록
 * @param searchQuery 현재 검색 텍스트
 * @param onSearchQueryChange 검색 텍스트 변경 콜백
 * @param onSongClick 노래 행 클릭 콜백 (null이면 비클릭)
 * @param contentPadding LazyColumn contentPadding
 * @param slots Optional trailing (per row) and leading (header) content; nulls use defaults.
 */
@Composable
fun SearchableSongList(
    modifier: Modifier = Modifier,
    songs: List<PlaylistSongDisplay>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSongClick: ((PlaylistSongDisplay) -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    slots: SearchableSongListSlots = SearchableSongListSlots()
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding
    ) {
        item {
            if (slots.leadingContent != null) {
                slots.leadingContent()
            } else {
                SongSearchSection(
                    searchQuery = searchQuery,
                    onSearchQueryChange = onSearchQueryChange
                )
            }
        }
        itemsIndexed(songs) { _, song ->
            val trailing = slots.trailingContent
            PlaylistSongItem(
                song = song,
                onClick = if (onSongClick != null) {
                    { onSongClick(song) }
                } else {
                    null
                },
                trailingContent = if (trailing != null) {
                    { trailing(song) }
                } else {
                    null
                }
            )
        }
    }
}

// endregion

// region ── Private sub-components ──

@Composable
private fun SongSearchSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
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
            onValueChange = onSearchQueryChange,
            placeholder = {
                Text(
                    text = "Text Field",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Normal,
                        color = Gray4
                    )
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
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = Sansneo,
                color = Gray9
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
        )
    }
}

/**
 * 선택 시에만 노출되는 추가하기 버튼.
 * - 연한 회색 배경, 왼쪽에 파란 원형 뱃지(선택 개수), 오른쪽 "추가하기"
 */
@Composable
private fun SongAddButton(
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(8.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(
                elevation = 5.dp,
                shape = shape,
                clip = false,
                ambientColor = Color(0x26000000),
                spotColor = Color(0x26000000)
            ).background(color = Gray1, shape = shape)
            .clip(shape)
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

// endregion

// region ── Previews ──

@Preview(showBackground = true, name = "View-only 모드")
@Composable
private fun SongPlaylistScreenPreview() {
    val songs = (1..5).map { i ->
        PlaylistSongDisplay(id = "$i", title = "노래 제목 $i", artist = "가수 이름")
    }
    SongPlaylistScreen(
        title = "추모 플레이리스트",
        onBackClick = {},
        songs = songs,
        defaultBottomNavItem = BottomNavItem.AFTERNOTE
    )
}

@Preview(showBackground = true, name = "선택 모드")
@Composable
private fun SongPlaylistScreenSelectablePreview() {
    val songs = (1..5).map { i ->
        PlaylistSongDisplay(id = "$i", title = "노래 제목 $i", artist = "가수 이름")
    }
    SongPlaylistScreen(
        title = "추모 플레이리스트 추가",
        onBackClick = {},
        songs = songs,
        onSongsSelected = {},
        initialSelectedSongIds = setOf("1", "3")
    )
}

@Preview(showBackground = true, name = "SearchableSongList 단독")
@Composable
@Suppress("AssignedValueIsNeverRead")
private fun SearchableSongListPreview() {
    val songs = (1..5).map { i ->
        PlaylistSongDisplay(id = "$i", title = "노래 제목 $i", artist = "가수 이름")
    }
    var query by remember { mutableStateOf("") }
    SearchableSongList(
        songs = songs,
        searchQuery = query,
        onSearchQueryChange = { query = it },
        contentPadding = PaddingValues(horizontal = 20.dp)
    )
}

// endregion
