package com.kuit.afternote.feature.receiver.presentation.screen.timeletter

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetterMedia
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverTimeLetterDetailUiState
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

private const val TAG = "ReceiverTimeLetterDetailScreen"
private const val DETAIL_IMAGE_SIZE_DP = 100

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

        val imageUrls = letter.mediaList
            .filter { it.mediaType == "IMAGE" }
            .map { it.mediaUrl }
        if (imageUrls.isNotEmpty()) {
            ReceiverDetailMediaRow(
                mediaUrls = imageUrls,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

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

@Composable
private fun ReceiverDetailMediaRow(
    mediaUrls: List<String>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageSizeDp = DETAIL_IMAGE_SIZE_DP.dp
    val contentDesc = stringResource(R.string.content_description_time_letter_media)

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        mediaUrls.forEach { url ->
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(url)
                    .httpHeaders(
                        NetworkHeaders.Builder()
                            .set("User-Agent", "Afternote Android App")
                            .build()
                    )
                    .build(),
                contentDescription = contentDesc,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(imageSizeDp)
                    .clip(RoundedCornerShape(8.dp)),
                error = painterResource(R.drawable.img_time_letter_placeholder),
                onError = { state: AsyncImagePainter.State.Error ->
                    Log.e(TAG, "Coil load failed: url=$url", state.result.throwable)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimeLetterDetail() {
    MaterialTheme {
        val sampleLetter = ReceivedTimeLetter(
            timeLetterId = 1L,
            timeLetterReceiverId = 100L,
            title = "채연아 20번째 생일을 축하해",
            content = "너가 태어난 게 엊그제같은데 벌써 스무살이라니..\n" +
                "엄마가 없어도 씩씩하게 컸을 채연이를 상상하면\n" +
                "너무 기특해서 안아주고 싶구나.\n" +
                "20번째 생일을 축하해.",
            sendAt = "2027년 11월 24일",
            status = "DRAFT",
            senderName = "박채연",
            deliveredAt = null,
            createdAt = null,
            mediaList = emptyList(),
            isRead = false
        )
        TimeLetterDetailScreen(
            uiState = ReceiverTimeLetterDetailUiState(letter = sampleLetter),
            onBackClick = { },
            onBottomNavSelected = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimeLetterDetailWithMedia() {
    MaterialTheme {
        val sampleLetter = ReceivedTimeLetter(
            timeLetterId = 1L,
            timeLetterReceiverId = 100L,
            title = "채연아 20번째 생일을 축하해",
            content = "사랑한다 생일 축하해 내 딸.",
            sendAt = "2027년 11월 24일",
            status = "DRAFT",
            senderName = "박채연",
            deliveredAt = null,
            createdAt = null,
            mediaList = listOf(
                ReceivedTimeLetterMedia(
                    id = 1L,
                    mediaType = "IMAGE",
                    mediaUrl = "https://example.com/sample1.jpg"
                ),
                ReceivedTimeLetterMedia(
                    id = 2L,
                    mediaType = "IMAGE",
                    mediaUrl = "https://example.com/sample2.jpg"
                )
            ),
            isRead = false
        )
        TimeLetterDetailScreen(
            uiState = ReceiverTimeLetterDetailUiState(letter = sampleLetter),
            onBackClick = { },
            onBottomNavSelected = { }
        )
    }
}
