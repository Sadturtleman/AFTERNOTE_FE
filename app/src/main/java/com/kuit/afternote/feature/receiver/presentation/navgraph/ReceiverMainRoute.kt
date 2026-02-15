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
import com.kuit.afternote.core.ui.component.list.AlbumCover
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.feature.receiver.presentation.screen.ReceiverAfterNoteScreen
import com.kuit.afternote.feature.receiver.presentation.screen.afternote.ReceiverAfterNoteMainScreen
import com.kuit.afternote.feature.receiver.presentation.screen.mindrecord.MindRecordDetailScreen
import com.kuit.afternote.feature.receiver.presentation.screen.mindrecord.MindRecordScreen
import com.kuit.afternote.feature.receiver.presentation.screen.timeletter.TimeLetterScreen
import java.time.LocalDate
import com.kuit.afternote.feature.receiver.presentation.viewmodel.ReceiverTimeLetterViewModel

/**
 * 수신자 모드 메인 라우트.
 *
 * BottomNavigationBar로 4개 탭(홈, 기록, 타임레터, 애프터노트) 간 화면 전환.
 * - HOME → ReceiverAfternoteScreen
 * - RECORD → MindRecordScreen (캘린더) → MindRecordDetailScreen (선택 날짜 API 목록)
 * - TIME_LETTER → TimeLetterScreen (타임레터 목록)
 * - AFTERNOTE → ReceiverAfterNoteMainScreen
 */
@Composable
fun ReceiverMainRoute(
    receiverId: String,
    navController: NavHostController,
    receiverTitle: String,
    albumCovers: List<AlbumCover>
) {
    var selectedBottomNavItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.HOME) }
    var showMindRecordDetail by remember { mutableStateOf(false) }
    var mindRecordSelectedDate by remember { mutableStateOf(LocalDate.now()) }
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
                    if (it != BottomNavItem.RECORD) showMindRecordDetail = false
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
                    ReceiverAfterNoteScreen(
                        showBottomBar = false,
                        receiverId = receiverId,
                        onNavigateToRecord = { selectedBottomNavItem = BottomNavItem.RECORD },
                        onNavigateToTimeLetter = { selectedBottomNavItem = BottomNavItem.TIME_LETTER },
                        onNavigateToAfternote = { selectedBottomNavItem = BottomNavItem.AFTERNOTE }
                    )
                BottomNavItem.RECORD ->
                    if (showMindRecordDetail) {
                        MindRecordDetailScreen(
                            receiverId = receiverId,
                            initialSelectedDate = mindRecordSelectedDate,
                            onBackClick = { showMindRecordDetail = false }
                        )
                    } else {
                        MindRecordScreen(
                            showBottomBar = false,
                            receiverId = receiverId,
                            onBackClick = { navController.popBackStack() },
                            onNavigateToDetail = { date ->
                                mindRecordSelectedDate = date
                                showMindRecordDetail = true
                            }
                        )
                    }
                BottomNavItem.TIME_LETTER ->
                    TimeLetterScreen(
                        uiState = timeLetterUiState,
                        onBackClick = { navController.popBackStack() },
                        onLetterClick = { letter ->
                            navController.navigate(
                                "receiver_time_letter_detail/$receiverId/${letter.timeLetterId}"
                            )
                        },
                        onBottomNavSelected = {
                            selectedBottomNavItem = it
                            timeLetterViewModel.updateSelectedBottomNavItem(it)
                        },
                        onSortByDate = { timeLetterViewModel.sortByDateAscending() },
                        onSortByUnread = { timeLetterViewModel.sortByUnreadFirst() },
                        showBottomBar = false
                    )
                BottomNavItem.AFTERNOTE ->
                    ReceiverAfterNoteMainScreen(
                        title = receiverTitle,
                        albumCovers = albumCovers,
                        onNavigateToFullList = {
                            navController.navigate("receiver_afternote_list")
                        },
                        onBackClick = { navController.popBackStack() },
                        showBottomBar = false
                    )
            }
        }
    }
}
