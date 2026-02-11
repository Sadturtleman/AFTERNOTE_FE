package com.kuit.afternote.feature.receiver.presentation.navgraph

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.feature.receiver.presentation.screen.MindRecordScreen
import com.kuit.afternote.feature.receiver.presentation.screen.ReceiverAfterNoteMainScreen
import com.kuit.afternote.feature.receiver.presentation.screen.ReceiverAfterNoteScreen
import com.kuit.afternote.feature.receiver.presentation.screen.TimeLetterScreen
import com.kuit.afternote.feature.receiver.presentation.viewmodel.ReceiverTimeLetterViewModel

/**
 * 수신자 모드 메인 라우트.
 *
 * BottomNavigationBar로 4개 탭(홈, 기록, 타임레터, 애프터노트) 간 화면 전환.
 * - HOME → ReceiverAfternoteScreen
 * - RECORD → MindRecordScreen (마음의 기록)
 * - TIME_LETTER → TimeLetterScreen (타임레터 목록)
 * - AFTERNOTE → ReceiverAfterNoteMainScreen
 */
@Composable
fun ReceiverMainRoute(
    receiverId: String,
    navController: NavHostController,
    receiverTitle: String,
    albumCovers: List<com.kuit.afternote.core.ui.component.list.AlbumCover>
) {
    var selectedBottomNavItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.HOME) }
    val timeLetterViewModel: ReceiverTimeLetterViewModel = hiltViewModel()
    val timeLetterUiState by timeLetterViewModel.uiState.collectAsStateWithLifecycle()

    BackHandler { navController.popBackStack() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.navigationBars.only(WindowInsetsSides.Bottom),
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = {
                    selectedBottomNavItem = it
                    timeLetterViewModel.updateSelectedBottomNavItem(it)
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedBottomNavItem) {
                BottomNavItem.HOME ->
                    ReceiverAfterNoteScreen(showBottomBar = false)
                BottomNavItem.RECORD ->
                    MindRecordScreen(showBottomBar = false)
                BottomNavItem.TIME_LETTER ->
                    TimeLetterScreen(
                        uiState = timeLetterUiState,
                        onBackClick = { /* 탭 내에서는 무시 */ },
                        onLetterClick = { letter ->
                            navController.navigate(
                                "receiver_time_letter_detail/$receiverId/${letter.timeLetterId}"
                            )
                        },
                        onBottomNavSelected = {
                            selectedBottomNavItem = it
                            timeLetterViewModel.updateSelectedBottomNavItem(it)
                        },
                        showBottomBar = false
                    )
                BottomNavItem.AFTERNOTE ->
                    ReceiverAfterNoteMainScreen(
                        title = receiverTitle,
                        albumCovers = albumCovers,
                        onNavigateToFullList = {
                            navController.navigate("receiver_afternote_list")
                        },
                        onBackClick = { /* 탭 내에서는 무시 */ },
                        showBottomBar = false
                    )
            }
        }
    }
}
