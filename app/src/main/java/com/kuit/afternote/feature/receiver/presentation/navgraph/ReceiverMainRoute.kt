package com.kuit.afternote.feature.receiver.presentation.navgraph

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.list.AlbumCover
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter
import com.kuit.afternote.feature.receiver.presentation.screen.ReceiverAfterNoteScreen
import com.kuit.afternote.feature.receiver.presentation.screen.afternote.ReceiverAfterNoteMainScreen
import com.kuit.afternote.feature.receiver.presentation.screen.mindrecord.MindRecordDetailScreen
import com.kuit.afternote.feature.receiver.presentation.screen.mindrecord.MindRecordScreen
import com.kuit.afternote.feature.receiver.presentation.screen.timeletter.ReceiverTimeLetterListScreen
import com.kuit.afternote.feature.receiver.presentation.screen.timeletter.TimeLetterScreen
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceivedTimeLetterListItemUi
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverTimeLettersListUiState
import com.kuit.afternote.feature.receiver.presentation.viewmodel.ReceiverAfternoteTriggerViewModel
import com.kuit.afternote.feature.receiver.presentation.viewmodel.ReceiverTimeLetterViewModel
import com.kuit.afternote.feature.receiverauth.session.ReceiverAuthSessionHolder
import java.time.LocalDate

/**
 * 타임레터 목록 화면 표시 모드.
 * - SortByDate: 날짜 순 (과거 위, 미래 아래)
 * - UnreadOnly: 읽지 않은 것만
 */
private enum class TimeLetterListMode {
    SortByDate,
    UnreadOnly
}

private fun ReceivedTimeLetter.toListItemUi(): ReceivedTimeLetterListItemUi =
    ReceivedTimeLetterListItemUi(
        timeLetterId = timeLetterId,
        timeLetterReceiverId = timeLetterReceiverId,
        senderName = senderName.orEmpty(),
        sendAt = sendAt.orEmpty(),
        title = title.orEmpty(),
        content = content.orEmpty(),
        isRead = isRead
    )

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
    albumCovers: List<AlbumCover>,
    receiverAuthSessionHolder: ReceiverAuthSessionHolder
) {
    DisposableEffect(receiverAuthSessionHolder) {
        onDispose { receiverAuthSessionHolder.clearAuthCode() }
    }
    var selectedBottomNavItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.HOME) }
    var showMindRecordDetail by remember { mutableStateOf(false) }
    var mindRecordSelectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showTimeLetterList by remember { mutableStateOf(false) }
    var timeLetterListMode by remember { mutableStateOf(TimeLetterListMode.SortByDate) }
    val timeLetterViewModel: ReceiverTimeLetterViewModel = hiltViewModel()
    val timeLetterUiState by timeLetterViewModel.uiState.collectAsStateWithLifecycle()
    val afternoteTriggerViewModel: ReceiverAfternoteTriggerViewModel = hiltViewModel()
    val leaveMessage by afternoteTriggerViewModel.leaveMessage.collectAsStateWithLifecycle()
    val mindRecordTotalCount by afternoteTriggerViewModel.mindRecordTotalCount.collectAsStateWithLifecycle()
    val timeLetterTotalCount by afternoteTriggerViewModel.timeLetterTotalCount.collectAsStateWithLifecycle()
    val afternoteTotalCount by afternoteTriggerViewModel.afternoteTotalCount.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        receiverAuthSessionHolder.getAuthCode()
            ?.let { afternoteTriggerViewModel.loadHomeSummary(it) }
    }

    BackHandler {
        when {
            selectedBottomNavItem == BottomNavItem.TIME_LETTER && showTimeLetterList -> {
                showTimeLetterList = false
            }
            selectedBottomNavItem == BottomNavItem.HOME -> {
                navController.popBackStack()
            }
            else -> {
                selectedBottomNavItem = BottomNavItem.HOME
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                        senderName = receiverAuthSessionHolder.getSenderName().orEmpty(),
                        leaveMessage = leaveMessage,
                        mindRecordTotalCount = mindRecordTotalCount,
                        timeLetterTotalCount = timeLetterTotalCount,
                        afternoteTotalCount = afternoteTotalCount,
                        onNavigateToRecord = { selectedBottomNavItem = BottomNavItem.RECORD },
                        onNavigateToTimeLetter = {
                            receiverAuthSessionHolder.getAuthCode()
                                ?.let { timeLetterViewModel.loadTimeLetters(it) }
                            selectedBottomNavItem = BottomNavItem.TIME_LETTER
                        },
                        onNavigateToAfternote = {
                            receiverAuthSessionHolder.getAuthCode()
                                ?.let { afternoteTriggerViewModel.loadAfterNotes(it) }
                            selectedBottomNavItem = BottomNavItem.AFTERNOTE
                        }
                    )
                BottomNavItem.RECORD ->
                    if (showMindRecordDetail) {
                        MindRecordDetailScreen(
                            receiverId = receiverId,
                            initialSelectedDate = mindRecordSelectedDate,
                            onBackClick = {
                                showMindRecordDetail = false
                                selectedBottomNavItem = BottomNavItem.HOME
                            }
                        )
                    } else {
                        MindRecordScreen(
                            showBottomBar = false,
                            receiverId = receiverId,
                            onBackClick = { selectedBottomNavItem = BottomNavItem.HOME },
                            onNavigateToDetail = { date ->
                                mindRecordSelectedDate = date
                                showMindRecordDetail = true
                            }
                        )
                    }
                BottomNavItem.TIME_LETTER ->
                    if (showTimeLetterList) {
                        val listItems = when (timeLetterListMode) {
                            TimeLetterListMode.SortByDate ->
                                timeLetterUiState.timeLetters
                                    .map(ReceivedTimeLetter::toListItemUi)
                                    .sortedBy { it.sendAt }
                            TimeLetterListMode.UnreadOnly ->
                                timeLetterUiState.timeLetters
                                    .map(ReceivedTimeLetter::toListItemUi)
                                    .filter { !it.isRead }
                        }
                        ReceiverTimeLetterListScreen(
                            uiState = ReceiverTimeLettersListUiState(
                                items = listItems,
                                isLoading = timeLetterUiState.isLoading,
                                errorMessage = timeLetterUiState.errorMessage
                            ),
                            onBackClick = { showTimeLetterList = false },
                            sortModeLabelResId = when (timeLetterListMode) {
                                TimeLetterListMode.SortByDate -> R.string.receiver_timeletter_sort_by_date
                                TimeLetterListMode.UnreadOnly -> R.string.receiver_timeletter_sort_unread_first
                            },
                            onLetterClick = { item ->
                                val letter = timeLetterUiState.timeLetters.find {
                                    it.timeLetterReceiverId == item.timeLetterReceiverId
                                }
                                if (letter != null) {
                                    receiverAuthSessionHolder.setSelectedTimeLetter(letter)
                                    navController.navigate(
                                        "receiver_time_letter_detail/$receiverId/${item.timeLetterReceiverId}"
                                    )
                                }
                            }
                        )
                    } else {
                        TimeLetterScreen(
                            uiState = timeLetterUiState,
                            onBackClick = { selectedBottomNavItem = BottomNavItem.HOME },
                            onLetterClick = { letter ->
                                receiverAuthSessionHolder.setSelectedTimeLetter(letter)
                                navController.navigate(
                                    "receiver_time_letter_detail/$receiverId/${letter.timeLetterReceiverId}"
                                )
                            },
                            onBottomNavSelected = {
                                selectedBottomNavItem = it
                                timeLetterViewModel.updateSelectedBottomNavItem(it)
                            },
                            onSortByDate = {
                                timeLetterListMode = TimeLetterListMode.SortByDate
                                showTimeLetterList = true
                            },
                            onSortByUnread = {
                                timeLetterListMode = TimeLetterListMode.UnreadOnly
                                showTimeLetterList = true
                            },
                            showBottomBar = false
                        )
                    }
                BottomNavItem.AFTERNOTE -> {
                    val afternoteSenderName = receiverAuthSessionHolder.getSenderName().orEmpty()
                    Log.d(TAG_RECEIVER_MAIN, "AFTERNOTE tab getSenderName=${receiverAuthSessionHolder.getSenderName()}, orEmpty='$afternoteSenderName'")
                    ReceiverAfterNoteMainScreen(
                        senderName = afternoteSenderName,
                        albumCovers = albumCovers,
                        onNavigateToFullList = {
                            navController.navigate("receiver_afternote_list")
                        },
                        onBackClick = { selectedBottomNavItem = BottomNavItem.HOME },
                        showBottomBar = false
                    )
                }
            }
        }
    }
}

private const val TAG_RECEIVER_MAIN = "ReceiverMain"
