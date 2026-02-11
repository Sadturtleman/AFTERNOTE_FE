<<<<<<<< HEAD:app/src/main/java/com/kuit/afternote/core/ui/screen/AfternoteDetailState.kt
package com.kuit.afternote.core.ui.screen
========
package com.kuit.afternote.core.ui.screen.afternotedetail
>>>>>>>> 18f1a0c82c9329353ff9254b226743d587b4b33f:app/src/main/java/com/kuit/afternote/core/ui/screen/afternotedetail/AfternoteDetailState.kt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem

/**
<<<<<<<< HEAD:app/src/main/java/com/kuit/afternote/core/ui/screen/AfternoteDetailState.kt
 * AfternoteDetailScreen의 상태를 관리하는 State Holder
========
 * Detail screen UI state (dropdown, delete dialog, bottom nav).
 * Used by [SocialNetworkDetailScreen] and
 * [GalleryDetailScreen].
>>>>>>>> 18f1a0c82c9329353ff9254b226743d587b4b33f:app/src/main/java/com/kuit/afternote/core/ui/screen/afternotedetail/AfternoteDetailState.kt
 *
 * @param defaultBottomNavItem 기본 선택된 하단 네비게이션 아이템
 */
@Stable
class AfternoteDetailState(
<<<<<<<< HEAD:app/src/main/java/com/kuit/afternote/core/ui/screen/AfternoteDetailState.kt
    defaultBottomNavItem: BottomNavItem = BottomNavItem.HOME
========
    defaultBottomNavItem: BottomNavItem = BottomNavItem.AFTERNOTE
>>>>>>>> 18f1a0c82c9329353ff9254b226743d587b4b33f:app/src/main/java/com/kuit/afternote/core/ui/screen/afternotedetail/AfternoteDetailState.kt
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
<<<<<<<< HEAD:app/src/main/java/com/kuit/afternote/core/ui/screen/AfternoteDetailState.kt
 * AfternoteDetailState를 생성하는 Composable 함수
========
 * Creates [AfternoteDetailState] for detail screens (social network, gallery).
>>>>>>>> 18f1a0c82c9329353ff9254b226743d587b4b33f:app/src/main/java/com/kuit/afternote/core/ui/screen/afternotedetail/AfternoteDetailState.kt
 *
 * @param defaultBottomNavItem 기본 선택된 하단 네비게이션 아이템
 */
@Stable
@Composable
<<<<<<<< HEAD:app/src/main/java/com/kuit/afternote/core/ui/screen/AfternoteDetailState.kt
fun rememberAfternoteDetailState(defaultBottomNavItem: BottomNavItem = BottomNavItem.HOME): AfternoteDetailState =
========
fun rememberAfternoteDetailState(defaultBottomNavItem: BottomNavItem = BottomNavItem.AFTERNOTE): AfternoteDetailState =
>>>>>>>> 18f1a0c82c9329353ff9254b226743d587b4b33f:app/src/main/java/com/kuit/afternote/core/ui/screen/afternotedetail/AfternoteDetailState.kt
    remember(defaultBottomNavItem) {
        AfternoteDetailState(defaultBottomNavItem = defaultBottomNavItem)
    }
