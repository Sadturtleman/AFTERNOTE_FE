package com.kuit.afternote.feature.receiver.presentation.navgraph

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.list.AlbumCover
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.core.ui.screen.afternotedetail.GalleryDetailCallbacks
import com.kuit.afternote.core.ui.screen.afternotedetail.GalleryDetailScreen
import com.kuit.afternote.core.ui.screen.afternotedetail.GalleryDetailState
import com.kuit.afternote.core.ui.screen.afternotedetail.MemorialGuidelineDetailCallbacks
import com.kuit.afternote.core.ui.screen.afternotedetail.MemorialGuidelineDetailScreen
import com.kuit.afternote.core.ui.screen.afternotedetail.MemorialGuidelineDetailState
import com.kuit.afternote.core.ui.screen.afternotedetail.SocialNetworkDetailContent
import com.kuit.afternote.core.ui.screen.afternotedetail.SocialNetworkDetailScreen
import com.kuit.afternote.core.ui.screen.afternotedetail.rememberAfternoteDetailState
import com.kuit.afternote.feature.receiver.presentation.viewmodel.ReceiverAfternoteDetailViewModel

/**
 * 수신자 애프터노트 상세 라우트.
 *
 * GET /api/receiver-auth/after-notes/{afternoteId}로 상세를 조회하고,
 * 발신자 상세와 동일한 화면(Social/Gallery/MemorialGuideline)에 title→serviceName, senderName→userName으로 표시합니다.
 */
private const val TAG = "ReceiverAfternoteDetail"

@Composable
fun ReceiverAfternoteDetailRoute(
    navHostController: NavHostController,
    itemId: String?,
    viewModel: ReceiverAfternoteDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Log.d(TAG, "ReceiverAfternoteDetailRoute composed: itemId=$itemId")
    LaunchedEffect(itemId) {
        Log.d(TAG, "LaunchedEffect(itemId) running: itemId=$itemId")
        viewModel.loadDetail(itemId)
    }

    val defaultState = rememberAfternoteDetailState(
        defaultBottomNavItem = BottomNavItem.AFTERNOTE
    )
    val onBackClick: () -> Unit = { navHostController.popBackStack(); Unit }

    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        uiState.errorMessage != null || uiState.detail == null -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopBar(title = "", onBackClick = onBackClick)
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.errorMessage ?: stringResource(R.string.design_pending)
                    )
                }
            }
        }
        else -> {
            val detail = uiState.detail!!
            val serviceName = detail.title.orEmpty().ifEmpty { "애프터노트" }
            val userName = detail.senderName.orEmpty().ifEmpty { "" }
            val finalWriteDate = detail.createdAt.orEmpty()

            when (receiverDetailCategoryFromApi(detail.category)) {
                ReceiverDetailCategory.GALLERY -> GalleryDetailScreen(
                    detailState = GalleryDetailState(
                        serviceName = serviceName,
                        userName = userName,
                        finalWriteDate = finalWriteDate,
                        afternoteEditReceivers = emptyList(),
                        informationProcessingMethod = detail.processMethod.orEmpty(),
                        processingMethods = detail.actions,
                        message = detail.leaveMessage.orEmpty()
                    ),
                    callbacks = GalleryDetailCallbacks(
                        onBackClick = onBackClick,
                        onEditClick = {}
                    ),
                    isEditable = false,
                    uiState = defaultState
                )
                ReceiverDetailCategory.MEMORIAL_GUIDELINE -> MemorialGuidelineDetailScreen(
                    detailState = MemorialGuidelineDetailState(
                        userName = userName,
                        finalWriteDate = finalWriteDate,
                        profileImageUri = null,
                        albumCovers = detail.playlist?.songs?.mapIndexed { index, s ->
                            AlbumCover(
                                id = index.toString(),
                                imageUrl = s.coverUrl,
                                title = s.title
                            )
                        } ?: emptyList(),
                        songCount = detail.playlist?.songs?.size ?: 0,
                        lastWish = detail.playlist?.atmosphere.orEmpty(),
                        afternoteEditReceivers = emptyList(),
                        memorialVideoUrl = detail.playlist?.memorialVideoUrl,
                        memorialThumbnailUrl = detail.playlist?.memorialThumbnailUrl
                    ),
                    callbacks = MemorialGuidelineDetailCallbacks(
                        onBackClick = onBackClick,
                        onEditClick = {}
                    ),
                    isEditable = false,
                    uiState = defaultState
                )
                ReceiverDetailCategory.SOCIAL -> SocialNetworkDetailScreen(
                    content = SocialNetworkDetailContent(
                        serviceName = serviceName,
                        userName = userName,
                        accountId = "",
                        password = "",
                        accountProcessingMethod = detail.processMethod.orEmpty(),
                        processingMethods = detail.actions,
                        message = detail.leaveMessage.orEmpty(),
                        finalWriteDate = finalWriteDate,
                        afternoteEditReceivers = emptyList()
                    ),
                    isEditable = false,
                    onBackClick = onBackClick,
                    state = defaultState
                )
            }
        }
    }
}

private enum class ReceiverDetailCategory {
    GALLERY,
    MEMORIAL_GUIDELINE,
    SOCIAL
}

private fun receiverDetailCategoryFromApi(category: String?): ReceiverDetailCategory =
    when (category?.uppercase()) {
        "GALLERY" -> ReceiverDetailCategory.GALLERY
        "PLAYLIST" -> ReceiverDetailCategory.MEMORIAL_GUIDELINE
        else -> ReceiverDetailCategory.SOCIAL
    }
