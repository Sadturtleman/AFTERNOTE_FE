package com.kuit.afternote.feature.dailyrecord.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kuit.afternote.feature.dailyrecord.presentation.navigiation.RecordRoute
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



        //주간 리포트
        composable("weeklyReport") {
            RecordWeekendReportScreen()
        }
    }
}
