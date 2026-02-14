package com.kuit.afternote.feature.dailyrecord.presentation.navgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.recordNavGraph(
    navController: NavController,
    onBottomNavTabSelected: (BottomNavItem) -> Unit = {}
    ) {

    composable("record_main") {
        RecordMainScreen(
            onDiaryClick = { navController.navigate(RecordRoute.DiaryRoute) },
            onQuestionClick = { navController.navigate(RecordRoute.QuestionRoute) },
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
            onEditClick = { recordId ->
                navController.navigate(RecordRoute.EditRoute(recordId))
            },
            onBottomNavTabSelected = onBottomNavTabSelected,
            viewModel = viewModel
        )
        LaunchedEffect(Unit) {
            viewModel.loadRecords()
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
            viewModel.loadRecords()
        }

    }
    composable<RecordRoute.DiaryRoute> {
        RecordDiaryScreen(
            onLeftClick = { navController.popBackStack() }
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
        val viewModel: MindRecordViewModel = hiltViewModel()
        RecordDeepMindScreen(
            onLeftClick = { navController.popBackStack() },
            viewModel = viewModel,
            recordId = null
        )
    }

    composable<RecordRoute.WeekendReportRoute> {
        RecordWeekendReportScreen(
            onBackClick = { navController.popBackStack() },
            onBottomNavTabSelected = onBottomNavTabSelected
        )
    }
    composable<RecordRoute.EditRoute> { _ ->
        val viewModel: MindRecordViewModel = hiltViewModel()
        val record by viewModel.selectedRecord.collectAsState()

        RecordQuestionScreen(
            record = record, // 수정 시 기존 데이터 전달
            onLeftClick = { navController.popBackStack() },
            viewModel = viewModel
        )

    }

}
