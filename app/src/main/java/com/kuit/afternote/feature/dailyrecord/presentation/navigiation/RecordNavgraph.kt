package com.kuit.afternote.feature.dailyrecord.presentation.navgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.recordNavGraph(
    navController: NavController,
    onBottomNavTabSelected: (BottomNavItem) -> Unit = {},

    ) {

    composable("record_main") {
        RecordMainScreen(
            onDiaryClick = { navController.navigate(RecordRoute.ListRoute("DIARY")) },
            onQuestionClick = { navController.navigate(RecordRoute.QuestionRouteList) },
            onDeepMindClick = { navController.navigate(RecordRoute.ListRoute("DEEP_THOUGHT")) },
            onWeekendReportClick = { navController.navigate(RecordRoute.WeekendReportRoute) },
            onBottomNavTabSelected = onBottomNavTabSelected
        )
    }
    composable<RecordRoute.ListRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<RecordRoute.ListRoute>()
        val listMode = route.listMode
        val viewModel: MindRecordViewModel = hiltViewModel()
        RecordFirstDiaryListScreen(
            listMode = listMode,
            onBackClick = { navController.popBackStack() },
            onPlusRecordClick = {
                when (listMode) {
                    "DEEP_THOUGHT" -> navController.navigate(RecordRoute.DeepMindRoute)
                    else -> navController.navigate(RecordRoute.DiaryRoute)
                }
            },
            onEditClick = { recordId ->
                navController.navigate(RecordRoute.EditRoute(recordId))
            },
            onBottomNavTabSelected = onBottomNavTabSelected,
            viewModel = viewModel
        )
        LaunchedEffect(listMode) {
            if (listMode == "DEEP_THOUGHT") {
                viewModel.loadRecords("DEEP_THOUGHT")
            } else {
                viewModel.loadRecordsForDiaryList()
            }
        }
    }
    composable<RecordRoute.QuestionRouteList> {
        val viewModel: MindRecordViewModel = hiltViewModel()
        RecordDailyQuestionListScreen(
            onBackClick = { navController.popBackStack() },
            onPlusRecordClick = { navController.navigate(RecordRoute.QuestionRoute) },
            onEditClick = { recordId ->
                navController.navigate(RecordRoute.EditRoute(recordId))
            },
            onBottomNavTabSelected = onBottomNavTabSelected,
            viewModel = viewModel
        )
        LaunchedEffect(Unit) {
            viewModel.loadRecords("DAILY_QUESTION")
        }
    }
    composable<RecordRoute.DiaryRoute> {
        val viewModel: MindRecordWriterViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        RecordDiaryScreen(
            onLeftClick = { navController.popBackStack() },
            title = uiState.title,
            content = uiState.content,
            onTitleChange = viewModel::updateTitle,
            onContentChange = viewModel::updateContent,
            sendDate = uiState.sendDate,
            showDatePicker = uiState.showDatePicker,
            onDateClick = viewModel::showDatePicker,
            onDatePickerDismiss = viewModel::hideDatePicker,
            onDateSelected = { year, month, day ->
                val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
                viewModel.updateSendDate(LocalDate.of(year, month, day).format(formatter))
            },
            onRegisterClick = {
                viewModel.registerWithPopUpThenSave(onSuccess = { navController.popBackStack() })
            }
        )
    }
    composable<RecordRoute.QuestionRoute> {
        val viewModel: MindRecordViewModel = hiltViewModel()
        val record by viewModel.selectedRecord.collectAsState()

        RecordQuestionScreen(
            onLeftClick = { navController.popBackStack() },
            record = record,
            viewModel = viewModel
        )
    }
    composable<RecordRoute.DeepMindRoute> {
        val viewModel: MindRecordWriterViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        RecordDeepMindScreen(
            onLeftClick = { navController.popBackStack() },
            recordId = null,
            title = uiState.title,
            content = uiState.content,
            onTitleChange = viewModel::updateTitle,
            onContentChange = viewModel::updateContent,
            sendDate = uiState.sendDate,
            showDatePicker = uiState.showDatePicker,
            onDateClick = viewModel::showDatePicker,
            onDatePickerDismiss = viewModel::hideDatePicker,
            selectedCategory = uiState.selectedCategory,
            onCategoryChange = viewModel::updateCategory,
            showCategoryDropdown = uiState.showCategoryDropdown,
            onCategoryClick = viewModel::showCategoryDropdown,
            onCategoryDropdownDismiss = viewModel::hideCategoryDropdown,
            onRegisterClick = {
                viewModel.registerWithPopUpThenSave(
                    recordType = "DEEP_THOUGHT",
                    category = uiState.selectedCategory,
                    onSuccess = { navController.popBackStack() }
                )
            }
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
            viewModel = viewModel
        )
    }

}
