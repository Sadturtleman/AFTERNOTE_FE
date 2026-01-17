package com.kuit.afternote.feature.dailyrecord.presentation.navgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kuit.afternote.feature.dailyrecord.presentation.navigiation.RecordRoute
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordDeepMindScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordDiaryScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordMainScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordWeekendReportScreen


@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.recordNavGraph(navController: NavController) {
    composable("record_main") {
        RecordMainScreen(
            onDiaryClick = {navController.navigate(RecordRoute.DiaryRoute) },
            onQuestionClick = {navController.navigate(RecordRoute.QuestionRoute)},
            onDeepMindClick = {navController.navigate(RecordRoute.DeepMindRoute)},
            onWeekendReportClick = {navController.navigate(RecordRoute.WeekendReportRoute)}
        )
    }
    composable<RecordRoute.DiaryRoute>{
        RecordDiaryScreen()
    }
    composable<RecordRoute.QuestionRoute> {
        RecordDiaryScreen()
    }
    composable<RecordRoute.DeepMindRoute> {
        RecordDeepMindScreen()
    }
    composable<RecordRoute.WeekendReportRoute> {
        RecordWeekendReportScreen()
    }
}

