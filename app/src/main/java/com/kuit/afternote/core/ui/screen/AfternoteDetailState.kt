package com.kuit.afternote.core.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem

/**
 * AfternoteDetailScreen의 상태를 관리하는 State Holder
 *
 * @param defaultBottomNavItem 기본 선택된 하단 네비게이션 아이템
 */
@Stable
class AfternoteDetailState(
    defaultBottomNavItem: BottomNavItem = BottomNavItem.HOME
) {
    var selectedBottomNavItem by mutableStateOf(defaultBottomNavItem)
        private set

    var showDropdownMenu by mutableStateOf(false)
        private set

    var showDeleteDialog by mutableStateOf(false)
        private set

    /**
     * 하단 네비게이션 아이템 선택
     */
    fun onBottomNavItemSelected(navItem: BottomNavItem) {
        selectedBottomNavItem = navItem
    }

    /**
     * 드롭다운 메뉴 표시/숨김 토글
     */
    fun toggleDropdownMenu() {
        showDropdownMenu = !showDropdownMenu
    }

    /**
     * 드롭다운 메뉴 숨김
     */
    fun hideDropdownMenu() {
        showDropdownMenu = false
    }

    /**
     * 삭제 다이얼로그 표시
     */
    fun showDeleteDialog() {
        showDeleteDialog = true
    }

    /**
     * 삭제 다이얼로그 숨김
     */
    fun hideDeleteDialog() {
        showDeleteDialog = false
    }
}

/**
 * AfternoteDetailState를 생성하는 Composable 함수
 *
 * @param defaultBottomNavItem 기본 선택된 하단 네비게이션 아이템
 */
@Stable
@Composable
fun rememberAfternoteDetailState(defaultBottomNavItem: BottomNavItem = BottomNavItem.HOME): AfternoteDetailState =
    remember(defaultBottomNavItem) {
        AfternoteDetailState(defaultBottomNavItem = defaultBottomNavItem)
    }
