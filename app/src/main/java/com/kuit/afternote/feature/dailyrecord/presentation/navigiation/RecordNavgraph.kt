package com.kuit.afternote.feature.dailyrecord.presentation.navgraph

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordDeepMindScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordDiaryScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordMainScreen
import com.kuit.afternote.feature.dailyrecord.presentation.screen.RecordWeekendReportScreen

@SuppressLint("ComposableDestinationInComposeScope")

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.recordNavGraph(navController: NavController) {
    composable("recordMain") {
        RecordMainScreen(navController = navController)
    }
    composable("dailyQuestion") {
        RecordDiaryScreen()
    }
    composable("diary") {
        RecordDiaryScreen()
    }
    composable("deepThought") {
        RecordDeepMindScreen()
    }
    composable("weeklyReport") {
        RecordWeekendReportScreen()
    }
}

