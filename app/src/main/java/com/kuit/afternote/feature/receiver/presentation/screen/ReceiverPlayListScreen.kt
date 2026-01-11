package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.core.ui.component.TopBar

// --- Data Model ---
data class Song(
    val id: Int,
    val title: String,
    val artist: String
)

@Composable
fun MemorialPlaylistScreen() {
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.TIME_LETTER) }

    var searchText by remember { mutableStateOf("") }

    // 더미 데이터 생성
    val songList = List(10) { index ->
        Song(index, "노래 제목", "가수 이름")
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "추모 플레이리스트"
            ) { }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { selectedBottomNavItem = it }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // 1. 검색 영역
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "노래 검색",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextDark,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // 검색 입력창
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = {
                        Text("Text Field", color = Color.LightGray, fontSize = 14.sp)
                    },
                    trailingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = TextDark)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = Color(0xFFEEEEEE),
                        cursorColor = PrimaryBlue
                    ),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 14.sp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                )
            }

            Divider(thickness = 1.dp, color = Color(0xFFF5F5F5))

            // 2. 노래 리스트
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(songList) { song ->
                    SongListItem(song = song)
                    Divider(thickness = 1.dp, color = Color(0xFFF8F8F8), modifier = Modifier.padding(horizontal = 20.dp))
                }
            }
        }
    }
}

@Composable
fun SongListItem(song: Song) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 앨범 아트 (Placeholder)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.DarkGray) // 이미지 대신 회색 배경
        ) {
            // 실제 이미지 사용 시:
            // Image(painter = painterResource(id = R.drawable.album_art), contentScale = ContentScale.Crop)
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 노래 정보
        Column {
            Text(
                text = song.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = song.artist,
                fontSize = 12.sp,
                color = TextGray
            )
        }
    }
}

// --- Bottom Navigation (재사용) ---


@Preview(showBackground = true)
@Composable
fun PreviewMemorialPlaylist() {
    MaterialTheme {
        MemorialPlaylistScreen()
    }
}
