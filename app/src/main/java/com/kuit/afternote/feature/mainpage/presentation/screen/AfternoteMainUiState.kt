package com.kuit.afternote.feature.mainpage.presentation.screen

import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.feature.mainpage.domain.model.AfternoteItem
import com.kuit.afternote.feature.mainpage.presentation.component.main.AfternoteTab

/**
 * 애프터노트 메인 화면 UI 상태
 * State Holder 패턴을 위한 불변 데이터 클래스
 *
 * @param selectedTab 현재 선택된 탭
 * @param selectedBottomNavItem 현재 선택된 하단 네비게이션 아이템
 * @param items 필터링된 아이템 리스트 (UI에 표시될 데이터만 포함)
 * @param isLoading 로딩 상태
 * @param canScrollRight 탭 로우 스크롤 가능 여부
 */
data class AfternoteMainUiState(
    val selectedTab: AfternoteTab = AfternoteTab.ALL,
    val selectedBottomNavItem: BottomNavItem = BottomNavItem.HOME,
    val items: List<AfternoteItem> = emptyList(),
    val isLoading: Boolean = false,
    val canScrollRight: Boolean = false
)
