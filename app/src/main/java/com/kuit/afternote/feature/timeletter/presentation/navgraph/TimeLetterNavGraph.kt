package com.kuit.afternote.feature.timeletter.presentation.navgraph

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.feature.timeletter.presentation.screen.DraftLetterScreen
import com.kuit.afternote.feature.timeletter.presentation.screen.ReceiveListScreen
import com.kuit.afternote.feature.timeletter.presentation.screen.TimeLetterDetailScreen
import com.kuit.afternote.feature.timeletter.presentation.screen.TimeLetterScreen
import com.kuit.afternote.feature.timeletter.presentation.screen.TimeLetterWriterScreen
import com.kuit.afternote.feature.timeletter.presentation.viewmodel.ReceiveListViewModel
import com.kuit.afternote.feature.timeletter.presentation.viewmodel.TimeLetterWriterViewModel

private const val SELECTED_RECEIVER_NAME_KEY = "selected_receiver_name"

/**
 * 타임레터 기능의 네비게이션 그래프
 *
 * @param navController 네비게이션 컨트롤러
 * @param onNavItemSelected 하단 네비게이션 아이템 선택 콜백
 */
fun NavGraphBuilder.timeLetterNavGraph(
    navController: NavController,
    onNavItemSelected: (BottomNavItem) -> Unit = {}
) {
    composable<TimeLetterRoute.TimeLetterMainRoute> {
        TimeLetterScreen(
            onBackClick = { navController.popBackStack() },
            onNavItemSelected = onNavItemSelected,
            onAddClick = { navController.navigate(TimeLetterRoute.TimeLetterWriterRoute()) },
            onShowAllClick = { navController.navigate(TimeLetterRoute.ReceiveListRoute) },
            onLetterClick = { letter ->
                navController.navigate(
                    TimeLetterRoute.TimeLetterDetailRoute(
                        id = letter.id,
                        receiverName = letter.receivername,
                        sendDate = letter.sendDate,
                        title = letter.title,
                        content = letter.content
                    )
                )
            },
            onEditLetter = { letter ->
                letter.id.toLongOrNull()?.let { draftId ->
                    navController.navigate(TimeLetterRoute.TimeLetterWriterRoute(draftId = draftId))
                }
            }
        )
    }

    composable<TimeLetterRoute.TimeLetterWriterRoute> {
        val viewModel: TimeLetterWriterViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            val name = navController.currentBackStackEntry
                ?.savedStateHandle
                ?.get<String>(SELECTED_RECEIVER_NAME_KEY)
            if (name != null) {
                viewModel.updateRecipientName(name)
            }
        }

        TimeLetterWriterScreen(
            recipientName = uiState.recipientName.ifEmpty { "수신자 선택" },
            title = uiState.title,
            content = uiState.content,
            sendDate = uiState.sendDate,
            sendTime = uiState.sendTime,
            showDatePicker = uiState.showDatePicker,
            showTimePicker = uiState.showTimePicker,
            draftCount = uiState.draftCount,
            onTitleChange = viewModel::updateTitle,
            onContentChange = viewModel::updateContent,
            onNavigateBack = { navController.popBackStack() },
            onRecipientClick = { navController.navigate(TimeLetterRoute.ReceiveListRoute) },
            receivers = uiState.receivers,
            showRecipientDropdown = uiState.showRecipientDropdown,
            onRecipientDropdownDismiss = { viewModel.hideRecipientDropdown() },
            onReceiverSelected = { receiver ->
                viewModel.updateRecipientName(receiver.receiver_name)
                viewModel.hideRecipientDropdown()
            },
            onRegisterClick = {
                viewModel.registerWithPopUpThenSave {
                    navController.popBackStack()
                }
            },
            onSaveDraftClick = {
                viewModel.saveDraft {
                    navController.navigate(TimeLetterRoute.DraftLetterRoute)
                }
            },
            onDraftCountClick = {
                navController.navigate(TimeLetterRoute.DraftLetterRoute)
            },
            onDateClick = viewModel::showDatePicker,
            onTimeClick = viewModel::showTimePicker,
            onDatePickerDismiss = viewModel::hideDatePicker,
            onDateSelected = { year, month, day ->
                val formattedDate = "$year. ${month.toString().padStart(2, '0')}. ${day.toString().padStart(2, '0')}"
                viewModel.updateSendDate(formattedDate)
            },
            onTimePickerDismiss = viewModel::hideTimePicker,
            onTimeSelected = { hour, minute ->
                viewModel.updateSendTime("%02d:%02d".format(hour, minute))
            },
            showWritingPlusMenu = uiState.showWritingPlusMenu,
            onMoreClick = viewModel::showPlusMenu,
            onDismissPlusMenu = viewModel::hidePlusMenu,
            showRegisteredPopUp = uiState.showRegisteredPopUp,
            showDraftSavePopUp = uiState.showDraftSavePopUp,
            showWaitingAgainPopUp = uiState.showWaitingAgainPopUp
        )
    }

    composable<TimeLetterRoute.DraftLetterRoute> {
        DraftLetterScreen(
            onCloseClick = { navController.popBackStack() },
            onLetterClick = { letter ->
                letter.id.toLongOrNull()?.let { draftId ->
                    navController.navigate(TimeLetterRoute.TimeLetterWriterRoute(draftId = draftId))
                }
            }
        )
    }

    composable<TimeLetterRoute.ReceiveListRoute> {
        val receiveListViewModel: ReceiveListViewModel = hiltViewModel()
        val receiveListState by receiveListViewModel.uiState.collectAsStateWithLifecycle()
        LaunchedEffect(Unit) { receiveListViewModel.loadReceivers() }
        ReceiveListScreen(
            receivers = receiveListState.receivers,
            onBackClick = { navController.popBackStack() },
            onNavItemSelected = onNavItemSelected,
            onReceiverClick = { receiver ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set(SELECTED_RECEIVER_NAME_KEY, receiver.receiver_name)
                navController.popBackStack()
            }
        )
    }

    composable<TimeLetterRoute.TimeLetterDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<TimeLetterRoute.TimeLetterDetailRoute>()
        TimeLetterDetailScreen(
            receiverName = route.receiverName,
            sendDate = route.sendDate,
            title = route.title,
            content = route.content,
            onBackClick = { navController.popBackStack() }
        )
    }
}
