package com.kuit.afternote.feature.timeletter.presentation.navgraph

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kuit.afternote.core.navigation.ReceiverRoute
import com.kuit.afternote.core.navigation.SELECTED_RECEIVER_ID_KEY
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.feature.timeletter.presentation.screen.DraftLetterScreen
import com.kuit.afternote.feature.timeletter.presentation.screen.ReceiveListScreen
import com.kuit.afternote.feature.timeletter.presentation.screen.TimeLetterDetailParams
import com.kuit.afternote.feature.timeletter.presentation.screen.TimeLetterDetailScreen
import com.kuit.afternote.feature.timeletter.presentation.screen.TimeLetterScreen
import com.kuit.afternote.feature.timeletter.presentation.screen.TimeLetterWriterScreen
import com.kuit.afternote.feature.timeletter.presentation.screen.TimeLetterWriterScreenEvents
import com.kuit.afternote.feature.timeletter.presentation.screen.TimeLetterWriterScreenState
import com.kuit.afternote.feature.timeletter.presentation.viewmodel.ReceiveListViewModel
import com.kuit.afternote.feature.timeletter.presentation.viewmodel.TimeLetterWriterViewModel

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
            onShowAllClick = {
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.remove<Long>(SELECTED_RECEIVER_ID_KEY)
                navController.navigate(ReceiverRoute.ReceiverListRoute)
            },

            onLetterClick = { letter ->
                navController.navigate(
                    TimeLetterRoute.TimeLetterDetailRoute(
                        id = letter.id,
                        receiverName = letter.receivername,
                        sendDate = letter.sendDate,
                        title = letter.title,
                        content = letter.content,
                        createDate = letter.createDate,
                        mediaUrls = letter.mediaUrls,
                        audioUrls = letter.audioUrls,
                        linkUrls = letter.linkUrls
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
            val receiverId = navController.currentBackStackEntry
                ?.savedStateHandle
                ?.get<Long>(SELECTED_RECEIVER_ID_KEY)
            if (receiverId != null) {
                viewModel.updateSelectedReceiverIds(listOf(receiverId))
            }
        }

        TimeLetterWriterScreen(
            state = TimeLetterWriterScreenState(
                receiverIds = uiState.receiverIds,
                title = uiState.title,
                content = uiState.content,
                sendDate = uiState.sendDate,
                sendTime = uiState.sendTime,
                showDatePicker = uiState.showDatePicker,
                showTimePicker = uiState.showTimePicker,
                draftCount = uiState.draftCount,
                receivers = uiState.receivers,
                showWritingPlusMenu = uiState.showWritingPlusMenu,
                showRegisteredPopUp = uiState.showRegisteredPopUp,
                showDraftSavePopUp = uiState.showDraftSavePopUp,
                showWaitingAgainPopUp = uiState.showWaitingAgainPopUp,
                selectedImageUriStrings = uiState.selectedImageUriStrings,
                selectedVoiceUriStrings = uiState.selectedVoiceUriStrings,
                addedLinks = uiState.addedLinks
            ),
            events = TimeLetterWriterScreenEvents(
                onTitleChange = viewModel::updateTitle,
                onContentChange = viewModel::updateContent,
                onNavigateBack = { navController.popBackStack() },
                onRecipientClick = { navController.navigate(ReceiverRoute.ReceiverListRoute) },
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
                onDraftCountClick = { navController.navigate(TimeLetterRoute.DraftLetterRoute) },
                onDateClick = viewModel::showDatePicker,
                onTimeClick = viewModel::showTimePicker,
                onBackClick = { navController.popBackStack() },
                onDatePickerDismiss = viewModel::hideDatePicker,
                onDateSelected = { year, month, day ->
                    val formattedDate =
                        "$year. ${month.toString().padStart(2, '0')}. ${day.toString().padStart(2, '0')}"
                    viewModel.updateSendDate(formattedDate)
                },
                onTimePickerDismiss = viewModel::hideTimePicker,
                onTimeSelected = { hour, minute ->
                    viewModel.updateSendTime("%02d:%02d".format(hour, minute))
                },
                onMoreClick = viewModel::showPlusMenu,
                onDismissPlusMenu = viewModel::hidePlusMenu,
                onAddImages = viewModel::addImageUris,
                onRemoveImage = viewModel::removeImageUri,
                onAddVoiceUris = viewModel::addVoiceUris,
                onRemoveVoiceUri = viewModel::removeVoiceUri,
                onAddedLinksChange = viewModel::updateAddedLinks
            )
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

    composable<ReceiverRoute.ReceiverListRoute> {
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
                    ?.set(SELECTED_RECEIVER_ID_KEY, receiver.id)
                navController.popBackStack()
            }
        )
    }

    composable<TimeLetterRoute.TimeLetterDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<TimeLetterRoute.TimeLetterDetailRoute>()
        TimeLetterDetailScreen(
            params = TimeLetterDetailParams(
                receiverName = route.receiverName,
                sendDate = route.sendDate,
                title = route.title,
                content = route.content,
                createdAt = route.createDate,
                mediaUrls = route.mediaUrls,
                audioUrls = route.audioUrls,
                linkUrls = route.linkUrls,
                onBackClick = { navController.popBackStack() }
            )
        )
    }
}
