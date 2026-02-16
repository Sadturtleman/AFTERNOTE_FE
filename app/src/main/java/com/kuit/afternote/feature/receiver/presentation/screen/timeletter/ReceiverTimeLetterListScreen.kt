package com.kuit.afternote.feature.receiver.presentation.screen.timeletter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceivedTimeLetterListItemUi
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverTimeLettersListUiState

@Composable
fun ReceiverTimeLetterListScreen(
    uiState: ReceiverTimeLettersListUiState,
    selectedBottomNavItem: BottomNavItem,
    onBackClick: () -> Unit,
    onBottomNavSelected: (BottomNavItem) -> Unit,
    onLetterClick: (ReceivedTimeLetterListItemUi) -> Unit,
    onSortByDate: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .statusBarsPadding()
            ) {
                TopBar(
                    title = stringResource(R.string.nav_timeletter),
                    onBackClick = onBackClick
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = onBottomNavSelected
            )
        }
    ) { innerPadding ->
        ReceiverTimeLetterContent(
            uiState = uiState,
            onLetterClick = onLetterClick,
            onSortByDate = onSortByDate,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ReceiverTimeLetterContent(
    uiState: ReceiverTimeLettersListUiState,
    onLetterClick: (ReceivedTimeLetterListItemUi) -> Unit,
    onSortByDate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .width(86.dp)
                .height(34.dp)
                .background(color = Color.White, shape = RoundedCornerShape(20.dp))
                .border(
                    width = 1.dp,
                    color = Color(0xFF328BFF),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 12.dp)
                .clickable { onSortByDate() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "날짜순",
                fontSize = 12.sp,
                color = Color(0xFF328BFF)
            )
            Image(
                painter = painterResource(R.drawable.ic_down_vector),
                contentDescription = stringResource(R.string.content_description_expand_down),
                modifier = Modifier
                    .width(12.dp)
                    .height(6.dp),
                colorFilter = ColorFilter.tint(Color(0xFF89C2FF))
            )
        }

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.errorMessage,
                        fontSize = 14.sp,
                        color = Color(0xFFBCBCBC)
                    )
                }
            }
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        items = uiState.items,
                        key = { it.timeLetterReceiverId }
                    ) { letter ->
                        TimeLetterRow(
                            letter = letter,
                            onClick = { onLetterClick(letter) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TimeLetterRow(
    letter: ReceivedTimeLetterListItemUi,
    onClick: () -> Unit
) {
    val contentColor = if (letter.isRead) Color.Black else Color(0xFFBCBCBC)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = letter.sendAt,
            fontSize = 13.sp,
            color = Color(0xFFBCBCBC)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = letter.title,
            fontSize = 16.sp,
            fontFamily = Sansneo,
            color = contentColor
        )
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
    }
}

@Preview(showBackground = true)
@Composable
private fun ReceiverTimeLetterListScreenPreview() {
    ReceiverTimeLetterListScreen(
        uiState = ReceiverTimeLettersListUiState(
            items = listOf(
                ReceivedTimeLetterListItemUi(
                    timeLetterId = 1L,
                    timeLetterReceiverId = 10L,
                    senderName = "보낸이",
                    sendAt = "2025.02.17",
                    title = "첫 번째 타임레터",
                    content = "내용",
                    isRead = false
                ),
                ReceivedTimeLetterListItemUi(
                    timeLetterId = 2L,
                    timeLetterReceiverId = 11L,
                    senderName = "보낸이2",
                    sendAt = "2025.02.16",
                    title = "두 번째 타임레터",
                    content = "내용",
                    isRead = true
                )
            )
        ),
        selectedBottomNavItem = BottomNavItem.TIME_LETTER,
        onBackClick = {},
        onBottomNavSelected = {},
        onLetterClick = {}
    )
}
