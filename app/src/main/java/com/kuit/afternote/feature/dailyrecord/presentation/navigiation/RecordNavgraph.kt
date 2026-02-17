package com.kuit.afternote.feature.dailyrecord.presentation.navgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.toRoute
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.feature.dailyrecord.presentation.navigiation.RecordRoute
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordDailyQuestionListScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordDeepMindScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordDiaryScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordFirstDiaryListScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordMainScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordQuestionScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordWeekendReportScreen
import com.kuit.afternote.feature.dailyrecord.presentation.viewmodel.MindRecordViewModel
import com.kuit.afternote.feature.dailyrecord.presentation.viewmodel.MindRecordWriterViewModel
import com.kuit.afternote.feature.timeletter.presentation.viewmodel.TimeLetterWriterViewModel

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.recordNavGraph(
    navController: NavController,
    onBottomNavTabSelected: (BottomNavItem) -> Unit = {},

    ) {

    composable("record_main") {
        RecordMainScreen(
            onDiaryClick = { navController.navigate(RecordRoute.ListRoute) },
            onQuestionClick = { navController.navigate(RecordRoute.QuestionRouteList) },
            onDeepMindClick = { navController.navigate(RecordRoute.DeepMindRoute) },
            onWeekendReportClick = { navController.navigate(RecordRoute.WeekendReportRoute) },
            onBottomNavTabSelected = onBottomNavTabSelected
        )
    }
    composable<RecordRoute.ListRoute> {
        val viewModel: MindRecordViewModel = hiltViewModel()
        RecordFirstDiaryListScreen(
            onBackClick = { navController.popBackStack() },
            onPlusRecordClick = { navController.navigate(RecordRoute.DiaryRoute) },
            onEditClick = { recordId, recordType ->
                when (recordType) {
                    "DIARY" -> navController.navigate(RecordRoute.EditDiaryRoute(recordId))
                    "DEEP_THOUGHT" -> navController.navigate(RecordRoute.EditDeepMindRoute(recordId))
                    else -> navController.navigate(RecordRoute.EditRoute(recordId))
                }
            },
            onBottomNavTabSelected = onBottomNavTabSelected,
            viewModel = viewModel
        )
    }
    composable<RecordRoute.QuestionRouteList> {
        val viewModel: MindRecordViewModel = hiltViewModel()
        RecordDailyQuestionListScreen(
            onBackClick = { navController.popBackStack() },
            onPlusRecordClick = { navController.navigate(RecordRoute.QuestionRoute) },
            onEditClick = { recordId, _ ->
                navController.navigate(RecordRoute.EditRoute(recordId))
            },
            onBottomNavTabSelected = onBottomNavTabSelected,
            viewModel = viewModel
        )
    }
    composable<RecordRoute.EditDiaryRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<RecordRoute.EditDiaryRoute>()
        val viewModel: MindRecordWriterViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(route.recordId) {
            viewModel.loadRecordForEdit(route.recordId)
        }

        RecordDiaryScreen(
            onLeftClick = { navController.popBackStack() },
            viewModel = viewModel,
            title = uiState.title,
            content = uiState.content,
            onTitleChange = viewModel::updateTitle,
            onContentChange = viewModel::updateContent,
            onDateClick = viewModel::showDatePicker,
            sendDate = uiState.sendDate,
            showDatePicker = uiState.showDatePicker,
            onDatePickerDismiss = viewModel::hideDatePicker,
            onRegisterClick = {
                viewModel.registerWithPopUpThenSave(type = "DIARY") {
                    navController.popBackStack()
                }
            }
        )
    }
    composable<RecordRoute.DiaryRoute> {
        val viewModel: MindRecordWriterViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        RecordDiaryScreen(
            onLeftClick = { navController.popBackStack() },
            viewModel = viewModel,
            title = uiState.title,
            content = uiState.content,
            onTitleChange = viewModel::updateTitle,
            onContentChange = viewModel::updateContent,
            onDateClick = viewModel::showDatePicker,
            sendDate = uiState.sendDate,
            showDatePicker = uiState.showDatePicker,
            onDatePickerDismiss = viewModel::hideDatePicker,
            onRegisterClick = {
                viewModel.registerWithPopUpThenSave(type = "DIARY") {
                    navController.popBackStack()
                }
            }
        )
    }
    composable<RecordRoute.QuestionRoute> {
        val viewModel: MindRecordViewModel = hiltViewModel()
        val record by viewModel.selectedRecord.collectAsState()

        RecordQuestionScreen(
            onLeftClick = { navController.popBackStack() },
            onRegisterSuccess = { navController.popBackStack() },
            record = record,
            viewModel = viewModel
        )
    }
    composable<RecordRoute.EditDeepMindRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<RecordRoute.EditDeepMindRoute>()
        val viewModel: MindRecordWriterViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(route.recordId) {
            viewModel.loadRecordForEdit(route.recordId)
        }

        RecordDeepMindScreen(
            onLeftClick = { navController.popBackStack() },
            onRegisterSuccess = { navController.popBackStack() },
            viewModel = viewModel,
            recordId = null,
            onDateClick = viewModel::showDatePicker,
            sendDate = uiState.sendDate,
            showDatePicker = uiState.showDatePicker,
            onDatePickerDismiss = viewModel::hideDatePicker
        )
    }
    composable<RecordRoute.DeepMindRoute> {
        val viewModel: MindRecordWriterViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        RecordDeepMindScreen(
            onLeftClick = { navController.popBackStack() },
            onRegisterSuccess = {
                navController.navigate(RecordRoute.ListRoute) {
                    popUpTo(RecordRoute.DeepMindRoute) { inclusive = true }
                }
            },
            viewModel = viewModel,
            recordId = null,
            onDateClick = viewModel::showDatePicker,
            sendDate = uiState.sendDate,
            showDatePicker = uiState.showDatePicker,
            onDatePickerDismiss = viewModel::hideDatePicker
        )
    }

    composable<RecordRoute.WeekendReportRoute> {
        RecordWeekendReportScreen(
            onBackClick = { navController.popBackStack() },
            onBottomNavTabSelected = onBottomNavTabSelected
        )
    }
    composable<RecordRoute.EditRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<RecordRoute.EditRoute>()
        val viewModel: MindRecordViewModel = hiltViewModel()
        val record by viewModel.selectedRecord.collectAsState()

        LaunchedEffect(route.recordId) {
            viewModel.loadRecord(route.recordId)
        }

        RecordQuestionScreen(
            record = record,
            onLeftClick = { navController.popBackStack() },
            onRegisterSuccess = { navController.popBackStack() },
            viewModel = viewModel
        )

    }

}
