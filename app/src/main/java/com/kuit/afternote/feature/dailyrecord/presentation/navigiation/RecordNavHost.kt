package com.kuit.afternote.feature.dailyrecord.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kuit.afternote.feature.dailyrecord.presentation.navigiation.RecordRoute
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordFirstDiaryListScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordMainScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordWeekendReportScreen

@Composable
fun RecordNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "recordMain",
        modifier = modifier
    ) {
        composable("recordMain") {
            RecordMainScreen(
                onDiaryClick = {navController.navigate(RecordRoute.DiaryRoute) },
                onQuestionClick = {navController.navigate(RecordRoute.QuestionRoute)},
                onDeepMindClick = {navController.navigate(RecordRoute.DeepMindRoute)},
                onWeekendReportClick = {navController.navigate(RecordRoute.WeekendReportRoute)}
            )
        }
        //데일리 질문 답변
        composable("dailyQuestion") { backStackEntry ->
            //val recordId = backStackEntry.arguments?.getString("recordId")
            RecordFirstDiaryListScreen()
        }
        //일기
        composable("diary") {
            RecordFirstDiaryListScreen()
        }
        //깊은 생각
        composable("deepThought") {
            RecordFirstDiaryListScreen()
        }
        //주간 리포트
        composable("weeklyReport") {
            RecordWeekendReportScreen()
        }
    }
}
