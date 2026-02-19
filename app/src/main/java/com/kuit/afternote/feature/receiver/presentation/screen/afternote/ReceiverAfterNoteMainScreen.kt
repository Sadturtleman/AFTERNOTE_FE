package com.kuit.afternote.feature.receiver.presentation.screen.afternote

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.LastWishesRadioGroup
import com.kuit.afternote.core.ui.component.ProfileImage
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.core.ui.component.content.MemorialGuidelineContent
import com.kuit.afternote.core.ui.component.content.MemorialGuidelineSlots
import com.kuit.afternote.core.ui.component.detail.InfoCard
import com.kuit.afternote.core.ui.component.list.AlbumCover
import com.kuit.afternote.core.ui.component.list.MemorialPlaylist
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Suppress("AssignedValueIsNeverRead")
@Composable
fun ReceiverAfterNoteMainScreen(
    senderName: String,
    onNavigateToFullList: () -> Unit = {},
    onNavigateToPlaylist: () -> Unit = {},
    onBackClick: () -> Unit = {},
    profileImageResId: Int? = null,
    albumCovers: List<AlbumCover>,
    songCount: Int = 16,
    memorialVideoUrl: String? = null,
    memorialThumbnailUrl: String? = null,
    showBottomBar: Boolean = true
) {
    Log.d(TAG_RECEIVER_AFTERNOTE_MAIN, "ReceiverAfterNoteMainScreen received senderName='$senderName'")
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.TIME_LETTER) }
    val profileResId = profileImageResId ?: R.drawable.img_default_profile_deceased

    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                TopBar(
                    title = "故${senderName}님의 애프터노트",
                    onBackClick = { onBackClick() }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    selectedItem = selectedBottomNavItem,
                    onItemSelected = { selectedBottomNavItem = it }
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            item {
                MemorialGuidelineContent(
                    slots = MemorialGuidelineSlots(
                        introContent = {
                            Text(
                                text = "故 ${senderName}님의 애프터노트입니다.",
                                color = Gray9,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = Sansneo,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        photoContent = {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ProfileImage(
                                    fallbackImageRes = profileResId,
                                    profileImageSize = 140.dp,
                                    isEditable = false
                                )
                            }
                        },
                        playlistContent = {
                            MemorialPlaylist(
                                label = "추모 플레이리스트",
                                songCount = songCount,
                                albumCovers = albumCovers,
                                onAddSongClick = null,
                                onPlaylistClick = onNavigateToPlaylist
                            )
                        },
                        lastWishContent = {
                            LastWishesRadioGroup(
                                displayTextOnly = "끼니 거르지 말고 건강 챙기고 지내."
                            )
                        },
                        videoContent = {
                            ReceiverVideoSection(
                                memorialVideoUrl = memorialVideoUrl,
                                memorialThumbnailUrl = memorialThumbnailUrl
                            )
                        }
                    ),
                    sectionSpacing = 32.dp
                )
            }
            item {
                Spacer(modifier = Modifier.height(70.dp))

                ClickButton(
                    color = B3,
                    title = "애프터노트 확인하기",
                    onButtonClick = onNavigateToFullList
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

private const val TAG_RECEIVER_AFTERNOTE_MAIN = "ReceiverAfterNoteMain"
private const val LABEL_VIDEO_SECTION = "장례식에 남길 영상"

@Composable
private fun ReceiverVideoSection(
    memorialVideoUrl: String? = null,
    memorialThumbnailUrl: String? = null
) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth()) {
        ReceiverSectionHeader()
        Spacer(modifier = Modifier.height(12.dp))
        if (!memorialVideoUrl.isNullOrBlank()) {
            val videoUrl = memorialVideoUrl
            InfoCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                        if (context.packageManager.resolveActivity(
                                intent,
                                PackageManager.MATCH_DEFAULT_ONLY
                            ) != null
                        ) {
                            context.startActivity(intent)
                        } else {
                            Toast
                                .makeText(
                                    context,
                                    context.getString(R.string.receiver_memorial_video_no_app),
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    }
            ) {
                ReceiverMemorialVideoThumbnail(thumbnailUrl = memorialThumbnailUrl)
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
private fun ReceiverMemorialVideoThumbnail(thumbnailUrl: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(183.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        if (!thumbnailUrl.isNullOrBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(thumbnailUrl)
                    .httpHeaders(
                        NetworkHeaders.Builder()
                            .set("User-Agent", "Afternote Android App")
                            .build()
                    )
                    .build(),
                contentDescription = "장례식에 남길 영상 썸네일",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x99757575),
                            Color(0x99222222)
                        )
                    )
                )
        )
        Image(
            painter = painterResource(R.drawable.ic_playback),
            contentDescription = "영상 재생",
            modifier = Modifier
                .align(Alignment.Center)
                .size(32.dp)
        )
    }
}

@Composable
private fun ReceiverSectionHeader(title: String = LABEL_VIDEO_SECTION) {
    Text(
        text = title,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        color = Gray9,
        fontFamily = Sansneo,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewReceiverAfterNoteMain() {
    AfternoteTheme {
        ReceiverAfterNoteMainScreen(senderName = "박서연", albumCovers = emptyList())
    }
}
