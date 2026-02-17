package com.kuit.afternote.feature.afternote.presentation.screen

import com.kuit.afternote.core.ui.component.list.AfternoteTab
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem

/**
 * 애프터노트 목록 화면 UI 상태
 * State Holder 패턴을 위한 불변 데이터 클래스
 *
 * @param selectedTab 현재 선택된 탭
 * @param selectedBottomNavItem 현재 선택된 하단 네비게이션 아이템
 * @param items 필터링된 아이템 리스트 (UI에 표시될 데이터만 포함)
 * @param isLoading 로딩 상태
 * @param hasNext 더 불러올 페이지 존재 여부
 * @param isLoadingMore 다음 페이지 로딩 중 여부
 */
data class AfternoteListUiState(
    val selectedTab: AfternoteTab = AfternoteTab.ALL,
    val selectedBottomNavItem: BottomNavItem = BottomNavItem.AFTERNOTE,
    val items: List<AfternoteItem> = emptyList(),
    val isLoading: Boolean = false,
    val loadError: String? = null,
    val hasNext: Boolean = false,
    val isLoadingMore: Boolean = false
)
