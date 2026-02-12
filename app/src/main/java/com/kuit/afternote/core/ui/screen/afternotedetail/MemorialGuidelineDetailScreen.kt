package com.kuit.afternote.core.ui.screen.afternotedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import com.kuit.afternote.R
import com.kuit.afternote.core.dummy.album.AlbumDummies
import com.kuit.afternote.core.ui.component.ProfileImage
import com.kuit.afternote.core.ui.component.detail.DeleteConfirmDialog
import com.kuit.afternote.core.ui.component.detail.EditDropdownMenu
import com.kuit.afternote.core.ui.component.detail.InfoCard
import com.kuit.afternote.core.ui.component.list.AlbumCover
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.afternote.presentation.navgraph.AfternoteLightTheme
import com.kuit.afternote.ui.expand.horizontalFadingEdge
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Black
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 추모 가이드라인 상세 화면의 데이터 상태
 */
@Immutable
data class MemorialGuidelineDetailState(
    val userName: String = "서영",
    val finalWriteDate: String = "2025.11.26.",
    val profileImageUri: String? = null,
    val albumCovers: List<AlbumCover> = emptyList(),
    val songCount: Int = 0,
    val lastWish: String = ""
)

/**
 * 추모 가이드라인 상세 화면의 콜백 모음
 */
@Immutable
data class MemorialGuidelineDetailCallbacks(
    val onBackClick: () -> Unit,
    val onEditClick: () -> Unit = {},
    val onDeleteConfirm: () -> Unit = {}
)

private const val CATEGORY_NAME = "추모 가이드 라인"

/**
 * 추모 가이드라인 애프터노트 상세 화면.
 *
 * 피그마 디자인 기반:
 * - 헤더 (뒤로가기, 타이틀, 편집 버튼)
 * - 제목 ("추모 가이드 라인에 대한 {userName}님의 기록")
 * - 최종 작성일 + 프로필 사진 카드
 * - 추모 플레이리스트 카드
 * - 남기고 싶은 당부 카드
 * - 편집 드롭다운 메뉴 (수정하기/삭제하기)
 * - 삭제 확인 다이얼로그
 */
@Composable
fun MemorialGuidelineDetailScreen(
    modifier: Modifier = Modifier,
    detailState: MemorialGuidelineDetailState,
    callbacks: MemorialGuidelineDetailCallbacks,
    isEditable: Boolean = true,
    uiState: AfternoteDetailState = rememberAfternoteDetailState()
) {
    if (isEditable && uiState.showDeleteDialog) {
        DeleteConfirmDialog(
            serviceName = CATEGORY_NAME,
            onDismiss = uiState::hideDeleteDialog,
            onConfirm = {
                uiState.hideDeleteDialog()
                callbacks.onDeleteConfirm()
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (isEditable) {
                TopBar(
                    onBackClick = callbacks.onBackClick,
                    onEditClick = uiState::toggleDropdownMenu
                )
            } else {
                TopBar(
                    title = "",
                    onBackClick = callbacks.onBackClick
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = uiState.selectedBottomNavItem,
                onItemSelected = uiState::onBottomNavItemSelected
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                MemorialGuidelineDetailScrollContent(detailState = detailState)
            }
            if (isEditable) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 20.dp)
                ) {
                    EditDropdownMenu(
                        expanded = uiState.showDropdownMenu,
                        onDismissRequest = uiState::hideDropdownMenu,
                        onEditClick = callbacks.onEditClick,
                        onDeleteClick = { uiState.showDeleteDialog() }
                    )
                }
            }
        }
    }
}

@Composable
private fun MemorialGuidelineDetailScrollContent(
    detailState: MemorialGuidelineDetailState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        TitleSection(userName = detailState.userName)
        Spacer(modifier = Modifier.height(24.dp))
        CardSection(detailState = detailState)
    }
}

@Composable
private fun TitleSection(userName: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = B1)) {
                append(CATEGORY_NAME)
            }
            append("에 대한 ${userName}님의 기록")
        },
        style = TextStyle(
            fontSize = 18.sp,
            lineHeight = 24.sp,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Bold,
            color = Gray9
        )
    )
}

@Composable
private fun CardSection(detailState: MemorialGuidelineDetailState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        PhotoCard(
            finalWriteDate = detailState.finalWriteDate,
            profileImageUri = detailState.profileImageUri
        )
        PlaylistCard(
            albumCovers = detailState.albumCovers,
            songCount = detailState.songCount
        )
        LastWishCard(lastWish = detailState.lastWish)
    }
}

@Composable
private fun PhotoCard(
    finalWriteDate: String,
    profileImageUri: String?
) {
    InfoCard(
        modifier = Modifier.fillMaxWidth(),
        content = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "최종 작성일 $finalWriteDate",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 10.sp,
                        lineHeight = 16.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Normal,
                        color = Gray6
                    )
                )
                ProfileImage(
                    fallbackImageRes = R.drawable.img_default_profile_deceased,
                    profileImageSize = 144.dp,
                    isEditable = false,
                    displayImageUri = profileImageUri
                )
            }
        }
    )
}

/**
 * 추모 플레이리스트 카드 — 피그마 node 4160:9168 기준.
 *
 * 레이아웃 순서: 제목 → 앨범 커버 행 → 곡 수 텍스트.
 * InfoCard(Gray2) 안에 직접 렌더링하며, 내부 White 카드 없이 flat 구조.
 * 앨범 커버: 87dp, 간격 10dp, 오른쪽 45dp 페이드.
 */
@Composable
private fun PlaylistCard(
    albumCovers: List<AlbumCover>,
    songCount: Int
) {
    InfoCard(
        modifier = Modifier.fillMaxWidth(),
        content = {
            Column {
                Text(
                    text = "추모 플레이리스트",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Medium,
                        color = Gray9
                    )
                )
                Spacer(Modifier.height(7.dp))
                if (albumCovers.isNotEmpty()) {
                    PlaylistAlbumRow(albumCovers = albumCovers)
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "현재 ${songCount}개의 노래가 담겨 있습니다.",
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Normal,
                        color = Black
                    )
                )
            }
        }
    )
}

@Composable
private fun PlaylistAlbumRow(albumCovers: List<AlbumCover>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalFadingEdge(edgeWidth = 45.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(albumCovers) { _, album ->
            AlbumCoverItem(album = album)
        }
    }
}

@Composable
private fun AlbumCoverItem(album: AlbumCover) {
    if (!album.imageUrl.isNullOrBlank()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(album.imageUrl)
                .httpHeaders(
                    NetworkHeaders.Builder().apply {
                        this["User-Agent"] = "Afternote Android App"
                    }.build()
                )
                .build(),
            contentDescription = album.title,
            modifier = Modifier.size(87.dp),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = Modifier
                .size(87.dp)
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(8.dp)
                )
        )
    }
}

@Composable
private fun LastWishCard(lastWish: String) {
    val displayText = lastWish.ifEmpty { "남기고 싶은 당부가 없습니다." }
    val textColor = if (lastWish.isNotEmpty()) Gray9 else Gray5

    InfoCard(
        modifier = Modifier.fillMaxWidth(),
        content = {
            Column {
                Text(
                    text = "남기고 싶은 당부",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Medium,
                        color = Gray9
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = displayText,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Normal,
                        color = textColor
                    )
                )
            }
        }
    )
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun MemorialGuidelineDetailScreenPreview() {
    AfternoteLightTheme {
        MemorialGuidelineDetailScreen(
            detailState = MemorialGuidelineDetailState(
                songCount = 16,
                albumCovers = AlbumDummies.list,
                lastWish = "차분하고 조용하게 보내주세요."
            ),
            callbacks = MemorialGuidelineDetailCallbacks(
                onBackClick = {},
                onEditClick = {}
            )
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false",
    name = "Memorial Guideline Detail - Delete Dialog"
)
@Composable
private fun MemorialGuidelineDetailScreenDeleteDialogPreview() {
    AfternoteLightTheme {
        val stateWithDialog = remember {
            AfternoteDetailState().apply {
                showDeleteDialog()
            }
        }
        MemorialGuidelineDetailScreen(
            detailState = MemorialGuidelineDetailState(
                songCount = 16,
                albumCovers = AlbumDummies.list,
                lastWish = "차분하고 조용하게 보내주세요."
            ),
            callbacks = MemorialGuidelineDetailCallbacks(
                onBackClick = {},
                onEditClick = {}
            ),
            uiState = stateWithDialog
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false",
    name = "Memorial Guideline Detail - Receiver Mode"
)
@Composable
private fun MemorialGuidelineDetailScreenReceiverModePreview() {
    AfternoteLightTheme {
        MemorialGuidelineDetailScreen(
            detailState = MemorialGuidelineDetailState(
                songCount = 16,
                albumCovers = AlbumDummies.list,
                lastWish = "차분하고 조용하게 보내주세요."
            ),
            callbacks = MemorialGuidelineDetailCallbacks(
                onBackClick = {}
            ),
            isEditable = false,
            uiState = rememberAfternoteDetailState(
                defaultBottomNavItem = BottomNavItem.AFTERNOTE
            )
        )
    }
}
