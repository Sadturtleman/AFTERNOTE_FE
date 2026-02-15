package com.kuit.afternote.feature.receiver.presentation.screen.timeletter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverTimeLetterDetailUiState
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun TimeLetterDetailScreen(
    uiState: ReceiverTimeLetterDetailUiState,
    onBackClick: () -> Unit,
    onBottomNavSelected: (BottomNavItem) -> Unit
) {
    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                TopBar(
                    title = "타임레터",
                    onBackClick = { onBackClick() }
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = uiState.selectedBottomNavItem,
                onItemSelected = onBottomNavSelected
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading && uiState.letter == null -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.errorMessage != null && uiState.letter == null -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.errorMessage,
                        color = Gray9,
                        fontFamily = Sansneo
                    )
                }
            }
            uiState.letter != null -> {
                TimeLetterDetailContent(
                    modifier = Modifier.padding(innerPadding),
                    letter = uiState.letter!!
                )
            }
        }
    }
}

@Composable
private fun TimeLetterDetailContent(
    modifier: Modifier = Modifier,
    letter: ReceivedTimeLetter
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(all = 20.dp)
    ) {
        Text(
            text = letter.sendAt.orEmpty().ifEmpty { "-" },
            color = Gray4,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 8.dp),
            fontFamily = Sansneo,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = letter.title.orEmpty().ifEmpty { "제목 없음" },
            color = Gray9,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 16.dp),
            fontFamily = Sansneo
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.6f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.LightGray)
        ) {
            Text(
                text = "가족 사진 영역",
                color = Color.DarkGray,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = letter.content.orEmpty().ifEmpty { " " },
            color = Gray9,
            fontSize = 14.sp,
            lineHeight = 26.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = Sansneo
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimeLetterDetail() {
    MaterialTheme {
        val sampleLetter = ReceivedTimeLetter(
            timeLetterId = 1L,
            title = "채연아 20번째 생일을 축하해",
            content = "너가 태어난 게 엊그제같은데 벌써 스무살이라니..\n" +
                "엄마가 없어도 씩씩하게 컸을 채연이를 상상하면\n" +
                "너무 기특해서 안아주고 싶구나.\n" +
                "20번째 생일을 축하해.",
            sendAt = "2027년 11월 24일",
            status = "DRAFT",
            senderName = "박채연",
            deliveredAt = null,
            createdAt = null
        )
        TimeLetterDetailScreen(
            uiState = ReceiverTimeLetterDetailUiState(letter = sampleLetter),
            onBackClick = { },
            onBottomNavSelected = { }
        )
    }
}
