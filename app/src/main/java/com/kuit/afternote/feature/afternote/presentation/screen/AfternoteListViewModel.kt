package com.kuit.afternote.feature.afternote.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.core.ui.component.list.AfternoteTab
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem
import com.kuit.afternote.feature.afternote.domain.model.ServiceType
import com.kuit.afternote.feature.afternote.domain.usecase.GetAfternotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 애프터노트 목록 화면 ViewModel.
 *
 * 초기에는 NavGraph에서 전달된 더미/캐시 데이터를 사용할 수 있지만,
 * 실제 API 로딩이 실패했을 때는 더미 데이터를 유지하지 않고
 * 오류 상태만 표시하도록 설계합니다.
 */
@HiltViewModel
class AfternoteListViewModel
    @Inject
    constructor(
        private val getAfternotesUseCase: GetAfternotesUseCase
    ) : ViewModel() {

        private val _uiState = MutableStateFlow(AfternoteListUiState())
        val uiState: StateFlow<AfternoteListUiState> = _uiState.asStateFlow()

        private var allItems: List<AfternoteItem> = emptyList()

        init {
            loadAfternotes()
        }

        /**
         * API에서 애프터노트 목록을 로드합니다.
         * category가 null이면 전체, 그 외에는 카테고리별 필터링을 서버에서 수행합니다.
         *
         * 최초 로드는 init에서 자동 호출되며,
         * 편집/저장 후 명시적으로 새로고침이 필요한 경우에만 외부에서 호출합니다.
         */
        fun loadAfternotes(
            category: String? = null,
            page: Int = 0,
            size: Int = 10
        ) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, loadError = null) }
                getAfternotesUseCase(category = category, page = page, size = size)
                    .onSuccess { items ->
                        allItems = items
                        _uiState.update { it.copy(isLoading = false, loadError = null) }
                        updateFilteredItems(_uiState.value.selectedTab)
                    }
                    .onFailure { e ->
                        // 서버 로딩 실패 시 더미/기존 데이터 대신 오류 상태만 표시
                        allItems = emptyList()
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                items = emptyList(),
                                loadError = e.message ?: "애프터노트 목록을 불러오지 못했습니다."
                            )
                        }
                    }
            }
        }

        /**
         * 초기 데이터 설정 (NavGraph에서 더미/캐시로 주입)
         */
        fun setItems(items: List<AfternoteItem>) {
            allItems = items
            _uiState.update { it.copy(loadError = null, isLoading = false) }
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
                AfternoteTab.GALLERY_AND_FILES -> allItems.filter { it.type == ServiceType.GALLERY_AND_FILES }
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
