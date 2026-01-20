package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.BottomNavItem
import com.kuit.afternote.core.ui.component.BottomNavigationBar
import com.kuit.afternote.core.ui.component.PlaylistSongItem
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.core.uimodel.PlaylistSongDisplay
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun MemorialPlaylistScreen() {
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.AFTERNOTE) }

    var searchText by remember { mutableStateOf("") }

    val songList = remember {
        (0..9).map { i -> PlaylistSongDisplay(id = "$i", title = "노래 제목", artist = "가수 이름") }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "추모 플레이리스트",
                onBackClick = { }
            )
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
                .padding(20.dp)
                .fillMaxSize()
        ) {
            // 1. 검색 영역
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Text(
                    text = "노래 검색",
                    fontWeight = FontWeight.Medium,
                    fontFamily = Sansneo,
                    fontSize = 16.sp,
                    color = Gray9,
                    modifier = Modifier.padding(bottom = 8.dp)
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

            // 2. 노래 리스트
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 10.dp)
            ) {
                itemsIndexed(songList) { index, display ->
                    PlaylistSongItem(
                        song = display,
                        displayIndex = index + 1,
                        onClick = null,
                        trailingContent = null
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMemorialPlaylist() {
    MaterialTheme {
        MemorialPlaylistScreen()
    }
}
