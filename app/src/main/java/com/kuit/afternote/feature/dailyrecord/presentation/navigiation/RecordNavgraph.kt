package com.kuit.afternote.feature.dailyrecord.presentation.navgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kuit.afternote.feature.dailyrecord.presentation.navigiation.RecordRoute
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordDailyQuestionScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordDeepMindScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordDiaryScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordFirstDiaryListScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordMainScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordQuestionScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordWeekendReportScreen

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.recordNavGraph(navController: NavController) {
    composable("record_main") {
        RecordMainScreen(
            onDiaryClick = { navController.navigate(RecordRoute.DiaryRoute) },
            onQuestionClick = { navController.navigate(RecordRoute.QuestionRoute) },
            onDeepMindClick = { navController.navigate(RecordRoute.DeepMindRoute) },
            onWeekendReportClick = { navController.navigate(RecordRoute.WeekendReportRoute) }
        )
    }
    composable<RecordRoute.ListRoute> {
        RecordFirstDiaryListScreen(
            onLeftClick = { navController.popBackStack() },
            onPlusRecordClick = { navController.navigate(RecordRoute.DiaryRoute) },
        )
    }
    composable<RecordRoute.QuestionRouteList> {
        RecordDailyQuestionScreen(
            onLeftClick = { navController.popBackStack() },
            onPlusRecordClick = { navController.navigate(RecordRoute.QuestionRoute) }
        )
    }
    composable<RecordRoute.DiaryRoute> {
        RecordDiaryScreen(
            onLeftClick = { navController.popBackStack() }
        )
    }
    composable<RecordRoute.QuestionRoute> {
        RecordQuestionScreen(
            onLeftClick = { navController.popBackStack() },
            onCreateRight = { navController.popBackStack() }
        )
    }
    composable<RecordRoute.DeepMindRoute> {
        RecordDeepMindScreen(
            onLeftClick = { navController.popBackStack() }
        )
    }
    composable<RecordRoute.WeekendReportRoute> {
        RecordWeekendReportScreen(
            onLeftClick = { navController.popBackStack() }
        )
    }
}
