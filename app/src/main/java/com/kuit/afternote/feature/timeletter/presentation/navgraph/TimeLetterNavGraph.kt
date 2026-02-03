package com.kuit.afternote.feature.timeletter.presentation.navgraph

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.feature.timeletter.presentation.screen.DraftLetterScreen
import com.kuit.afternote.feature.timeletter.presentation.screen.LetterEmptyScreen
import com.kuit.afternote.feature.timeletter.presentation.screen.ReceiveListScreen
import com.kuit.afternote.feature.timeletter.presentation.screen.TimeLetterScreen
import com.kuit.afternote.feature.timeletter.presentation.screen.TimeLetterWriterScreen
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
            onAddClick = { navController.navigate(TimeLetterRoute.TimeLetterWriterRoute) }
        )
    }

    composable<TimeLetterRoute.TimeLetterWriterRoute> {
        val viewModel: TimeLetterWriterViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            onRecipientClick = {
                // TODO: 수신자 선택 화면으로 이동
            },
            onRegisterClick = {
                viewModel.saveTimeLetter {
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
            onDismissPlusMenu = viewModel::hidePlusMenu
        )
    }

    composable<TimeLetterRoute.DraftLetterRoute> {
        DraftLetterScreen(
            onCloseClick = { navController.popBackStack() }
        )
    }

    composable<TimeLetterRoute.ReceiveListRoute> {
        ReceiveListScreen(
            receivers = emptyList(), // TODO: ViewModel에서 데이터 가져오기
            onBackClick = { navController.popBackStack() },
            onNavItemSelected = onNavItemSelected
        )
    }

    composable<TimeLetterRoute.LetterEmptyRoute> {
        LetterEmptyScreen(
            onNavigateBack = { navController.popBackStack() },
            onAddClick = { navController.navigate(TimeLetterRoute.TimeLetterWriterRoute) }
        )
    }
}
