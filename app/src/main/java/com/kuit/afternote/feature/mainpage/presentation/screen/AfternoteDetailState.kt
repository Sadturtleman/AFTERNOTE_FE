package com.kuit.afternote.feature.mainpage.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kuit.afternote.core.BottomNavItem

/**
 * AfternoteDetailScreen의 상태를 관리하는 State Holder
 */
@Stable
class AfternoteDetailState {
    var selectedBottomNavItem by mutableStateOf(BottomNavItem.HOME)
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
 */
@Stable
@Composable
fun rememberAfternoteDetailState(): AfternoteDetailState = remember { AfternoteDetailState() }
