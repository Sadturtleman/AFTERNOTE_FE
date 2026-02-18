package com.kuit.afternote.feature.receiver.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.kuit.afternote.app.compositionlocal.DataProviderLocals
import com.kuit.afternote.core.dummy.receiver.AfternoteListItemSeed
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.screen.afternotedetail.GalleryDetailCallbacks
import com.kuit.afternote.core.ui.screen.afternotedetail.GalleryDetailScreen
import com.kuit.afternote.core.ui.screen.afternotedetail.GalleryDetailState
import com.kuit.afternote.core.ui.screen.afternotedetail.MemorialGuidelineDetailCallbacks
import com.kuit.afternote.core.ui.screen.afternotedetail.MemorialGuidelineDetailScreen
import com.kuit.afternote.core.ui.screen.afternotedetail.MemorialGuidelineDetailState
import com.kuit.afternote.core.ui.screen.afternotedetail.SocialNetworkDetailContent
import com.kuit.afternote.core.ui.screen.afternotedetail.SocialNetworkDetailScreen
import com.kuit.afternote.core.ui.screen.afternotedetail.rememberAfternoteDetailState

/**
 * 수신자 애프터노트 상세 라우트.
 * ReceiverDataProvider에서 seed를 조회해 카테고리별 상세 스크린(Gallery/MemorialGuideline/Social)을 표시합니다.
 */
@Composable
fun ReceiverAfternoteDetailRoute(
    navHostController: NavHostController,
    itemId: String?
) {
    val receiverProvider = DataProviderLocals.LocalReceiverDataProvider.current
    val seed =
        remember(receiverProvider, itemId) {
            receiverProvider
                .getAfternoteListSeedsForReceiverList()
                .firstOrNull { it.id == itemId }
                ?: receiverProvider.getAfternoteListSeedsForReceiverList().firstOrNull()
        }
    val category = receiverDetailCategoryFromSeed(seed)
    val serviceName = seed?.serviceNameLiteral ?: ""
    val userName = receiverProvider.getDefaultReceiverTitle()
    val defaultState = rememberAfternoteDetailState(
        defaultBottomNavItem = BottomNavItem.AFTERNOTE
    )
    when (category) {
        ReceiverDetailCategory.GALLERY -> GalleryDetailScreen(
            detailState = GalleryDetailState(
                serviceName = serviceName.ifEmpty { "갤러리" },
                userName = userName,
                finalWriteDate = seed?.date ?: ""
            ),
            callbacks = GalleryDetailCallbacks(
                onBackClick = { navHostController.popBackStack() },
                onEditClick = {}
            ),
            isEditable = false,
            uiState = defaultState
        )
        ReceiverDetailCategory.MEMORIAL_GUIDELINE -> MemorialGuidelineDetailScreen(
            detailState = MemorialGuidelineDetailState(
                userName = userName,
                finalWriteDate = seed?.date ?: ""
            ),
            callbacks = MemorialGuidelineDetailCallbacks(
                onBackClick = { navHostController.popBackStack() }
            ),
            isEditable = false,
            uiState = defaultState
        )
        ReceiverDetailCategory.SOCIAL -> SocialNetworkDetailScreen(
            content = SocialNetworkDetailContent(
                serviceName = serviceName,
                userName = userName
            ),
            isEditable = false,
            onBackClick = { navHostController.popBackStack() },
            state = defaultState
        )
    }
}

private enum class ReceiverDetailCategory { GALLERY, MEMORIAL_GUIDELINE, SOCIAL }

private fun receiverDetailCategoryFromSeed(seed: AfternoteListItemSeed?): ReceiverDetailCategory {
    return when (seed?.serviceNameLiteral) {
        "갤러리" -> ReceiverDetailCategory.GALLERY
        "추모 가이드라인" -> ReceiverDetailCategory.MEMORIAL_GUIDELINE
        else -> ReceiverDetailCategory.SOCIAL
    }
}
