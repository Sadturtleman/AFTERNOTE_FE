package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.feature.mainpage.domain.model.AfternoteItem

/**
 * 애프터노트 메인 화면 Route
 * ViewModel 주입 및 네비게이션 처리 담당
 */
@Composable
fun AfternoteMainRoute(
    viewModel: AfternoteMainViewModel = hiltViewModel(),
    onNavigateToDetail: () -> Unit = {},
    onNavigateToGalleryDetail: () -> Unit = {},
    onNavigateToAdd: () -> Unit = {},
    initialItems: List<AfternoteItem> = emptyList()
) {
    LaunchedEffect(initialItems) {
        if (initialItems.isNotEmpty()) {
            viewModel.setItems(initialItems)
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AfternoteMainScreen(
        uiState = uiState,
        onEvent = { event ->
            when (event) {
                is AfternoteMainEvent.ClickItem -> {
                    val item = uiState.items.find { it.id == event.itemId }
                    if (item?.serviceName == "갤러리") {
                        onNavigateToGalleryDetail()
                    } else {
                        onNavigateToDetail()
                    }
                }
                is AfternoteMainEvent.ClickAdd -> onNavigateToAdd()
                else -> viewModel.onEvent(event)
            }
        }
    )
}
