package com.kuit.afternote.feature.afternote.presentation.screen

import androidx.lifecycle.ViewModel
import com.kuit.afternote.core.ui.component.list.AfternoteTab
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem
import com.kuit.afternote.feature.afternote.domain.model.ServiceType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * 애프터노트 목록 화면 ViewModel.
 * List is supplied by the caller (e.g. NavGraph) via [setItems]; no API call in this layer.
 */
@HiltViewModel
class AfternoteListViewModel
    @Inject
    constructor() : ViewModel() {

        private val _uiState = MutableStateFlow(AfternoteListUiState())
        val uiState: StateFlow<AfternoteListUiState> = _uiState.asStateFlow()

        private var allItems: List<AfternoteItem> = emptyList()

        /**
         * 초기 데이터 설정 (NavGraph에서 더미/캐시로 주입)
         */
        fun setItems(items: List<AfternoteItem>) {
            allItems = items
            updateFilteredItems(_uiState.value.selectedTab)
        }

        /**
         * UI 이벤트 처리
         */
        fun onEvent(event: AfternoteListEvent) {
            when (event) {
                is AfternoteListEvent.SelectTab -> updateTab(event.tab)
                is AfternoteListEvent.SelectBottomNav -> updateBottomNav(event.navItem)
                is AfternoteListEvent.ClickItem -> handleItemClick(event.itemId)
                is AfternoteListEvent.ClickAdd -> {
                    // 네비게이션은 Route에서 처리
                }
            }
        }

        /**
         * 탭 변경 및 필터링 로직
         */
        private fun updateTab(tab: AfternoteTab) {
            _uiState.update { it.copy(selectedTab = tab) }
            updateFilteredItems(tab)
        }

        /**
         * 선택된 탭에 따라 아이템 필터링
         */
        private fun updateFilteredItems(tab: AfternoteTab) {
            val filtered = when (tab) {
                AfternoteTab.ALL -> allItems
                AfternoteTab.SOCIAL_NETWORK -> allItems.filter { it.type == ServiceType.SOCIAL_NETWORK }
                AfternoteTab.BUSINESS -> allItems.filter { it.type == ServiceType.BUSINESS }
                AfternoteTab.GALLERY_AND_FILES -> allItems.filter { it.type == ServiceType.GALLERY_AND_FILES }
                AfternoteTab.ASSET_MANAGEMENT -> allItems.filter { it.type == ServiceType.ASSET_MANAGEMENT }
                AfternoteTab.MEMORIAL -> allItems.filter { it.type == ServiceType.MEMORIAL }
            }

            _uiState.update { it.copy(items = filtered) }
        }

        /**
         * 하단 네비게이션 아이템 변경 처리
         */
        private fun updateBottomNav(navItem: BottomNavItem) {
            _uiState.update { it.copy(selectedBottomNavItem = navItem) }
            // 필요시 네비게이션 로직 추가
        }

        /**
         * 아이템 클릭 처리
         */
        private fun handleItemClick(
            @Suppress("UNUSED_PARAMETER") itemId: String
        ) {
            // 네비게이션은 Route에서 처리
        }
    }
