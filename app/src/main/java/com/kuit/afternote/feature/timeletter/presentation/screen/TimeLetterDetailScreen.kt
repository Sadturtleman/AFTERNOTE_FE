package com.kuit.afternote.feature.timeletter.presentation.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray9

private const val TAG = "TimeLetterDetailScreen"
private const val DETAIL_IMAGE_SIZE_DP = 100

/**
 * 타임레터 상세 화면에 전달할 파라미터 (S107 준수: 7개 초과 시 그룹화)
 */
data class TimeLetterDetailParams(
    val receiverName: String,
    val sendDate: String,
    val title: String,
    val content: String,
    val createdAt: String = "",
    val mediaUrls: List<String> = emptyList(),
    val audioUrls: List<String> = emptyList(),
    val linkUrls: List<String> = emptyList(),
    val onBackClick: () -> Unit = {}
)

/**
 * 타임레터 상세 화면.
 * 리스트에서 선택한 타임레터의 수신인, 제목, 작성/발송 일자, 본문을 카드 형태로 표시한다.
 * mediaList(IMAGE)가 있으면 본문 아래에 이미지를, AUDIO면 음성, DOCUMENT면 링크를 작성 화면과 동일한 스타일로 표시한다.
 *
 * @param modifier 모디파이어
 * @param params 화면 파라미터 (receiverName, sendDate, title, content, createdAt, mediaUrls, audioUrls, onBackClick)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeLetterDetailScreen(
    modifier: Modifier = Modifier,
    params: TimeLetterDetailParams
) {
    val scrollState = rememberScrollState()
    val recipientLabel = stringResource(R.string.time_letter_detail_recipient_label)
    val createdLabel = stringResource(
        R.string.time_letter_detail_created_at,
        params.createdAt.ifEmpty { "-" }
    )
    val deliveryLabel = stringResource(
        R.string.time_letter_detail_delivery_date,
        params.sendDate.ifEmpty { "-" }
    )
    val screenTitle = stringResource(R.string.nav_timeletter)

    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                TopBar(
                    title = screenTitle,
                    onBackClick = params.onBackClick
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                ) {
                    Row {
                        Text(
                            text = recipientLabel,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = " ${params.receiverName.ifEmpty { "-" }}",
                            fontSize = 14.sp,
                            color = Gray9,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = params.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight(700),
                        color = Gray9
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = createdLabel,
                        fontSize = 14.sp,
                        color = Gray5
                    )
                    Text(
                        text = deliveryLabel,
                        fontSize = 14.sp,
                        color = Gray5
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Gray4
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = params.content,
                        fontSize = 14.sp,
                        color = Gray9,
                        lineHeight = 22.sp
                    )

                    if (params.mediaUrls.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(24.dp))
                        DetailMediaRow(
                            mediaUrls = params.mediaUrls,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (params.audioUrls.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(24.dp))
                        DetailAudioSection(audioUrls = params.audioUrls)
                    }

                    if (params.linkUrls.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(24.dp))
                        DetailLinkSection(linkUrls = params.linkUrls)
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/**
 * 첨부 음성 목록을 타임레터 작성 화면과 동일한 스타일(아이콘 + 라벨)로 표시한다.
 * 상세 화면은 읽기 전용이므로 삭제 버튼은 없다.
 */
@Composable
private fun DetailAudioSection(
    audioUrls: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 20.dp)) {
        audioUrls.forEachIndexed { index, _ ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .background(
                        Color(0xFFF5F5F5),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_sound),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.time_letter_voice_file_label, index + 1),
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * 첨부 링크 목록을 타임레터 작성 화면과 동일한 스타일(아이콘 + URL)로 표시한다.
 * 상세 화면은 읽기 전용이므로 삭제 버튼은 없다.
 */
@Composable
private fun DetailLinkSection(
    linkUrls: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 20.dp)) {
        linkUrls.forEach { url ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .background(
                        Color(0xFFF5F5F5),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_plus_link),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = url,
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun DetailMediaRow(
    mediaUrls: List<String>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageSizeDp = DETAIL_IMAGE_SIZE_DP.dp
    val horizontalScrollState = rememberScrollState()

    Row(
        modifier = modifier.horizontalScroll(horizontalScrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        mediaUrls.forEach { url ->
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(url)
                    .httpHeaders(
                        NetworkHeaders.Builder().apply {
                            this["User-Agent"] = "Afternote Android App"
                        }.build()
                    )
                    .build(),
                contentDescription = null,
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
        TimeLetterDetailScreen(
            params = TimeLetterDetailParams(
                receiverName = "박채연",
                sendDate = "2026. 11. 24. 10:00",
                title = "채연아 202번째 생일을 축하해",
                content = "너가 태어난 게 엊그제 같은데 벌써 스무살이라니,, 엄마가 없어도 씩씩하게 컸을 채연이를 상상하면 너무 기특해서 안아주고 싶구나.\n\n요즘 학교는 어떻게 다니고 있니? 공부는 잘하고 있지? 친구는 많이 사귀었니? 채연이가 가고싶어했던 그 학교, 학과에 갔을지 엄마는 너무 궁금해하면서 이 편지를 쓰고 있어. 엄마랑 다시 만나면 그동안 어떻게 지냈는지 하나하나 다 알려줘야 해. 사랑한다 생일 축하해 내 딸.",
                createdAt = "2023. 11. 21. 18:24"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimeLetterDetailWithMedia() {
    MaterialTheme {
        TimeLetterDetailScreen(
            params = TimeLetterDetailParams(
                receiverName = "박채연",
                sendDate = "2026. 11. 24. 20:00",
                title = "채연아 21번째 생일을 축하해",
                content = "사랑한다 생일 축하해 내 딸2.",
                createdAt = "2023. 11. 21. 18:44",
                mediaUrls = listOf(
                    "https://example.com/sample1.jpg",
                    "https://example.com/sample2.jpg"
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimeLetterDetailWithAudio() {
    MaterialTheme {
        TimeLetterDetailScreen(
            params = TimeLetterDetailParams(
                receiverName = "박채연",
                sendDate = "2026. 11. 24. 00:10",
                title = "채연아 204번째 생일을 축하해",
                content = "사랑한다 생일 축하해 내 딸3.",
                createdAt = "2023. 11. 21. 18:34",
                audioUrls = listOf(
                    "https://example.com/voice1.m4a",
                    "https://example.com/voice2.m4a"
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimeLetterDetailWithLinks() {
    MaterialTheme {
        TimeLetterDetailScreen(
            params = TimeLetterDetailParams(
                receiverName = "박채연",
                sendDate = "2026. 11. 24. 00:10",
                title = "채연아 20번째 생일을 축하해",
                content = "사랑한다 생일 축하해 내 딸1.",
                createdAt = "2023. 11. 21. 18:34",
                linkUrls = listOf(
                    "https://example.com/page1",
                    "https://example.com/page2"
                )
            )
        )
    }
}
