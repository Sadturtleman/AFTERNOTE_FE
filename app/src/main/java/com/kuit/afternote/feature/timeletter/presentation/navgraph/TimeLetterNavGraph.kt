package com.kuit.afternote.feature.timeletter.presentation.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.feature.timeletter.presentation.screen.DraftLetterScreen
import com.kuit.afternote.feature.timeletter.presentation.screen.ReceiveListScreen
import com.kuit.afternote.feature.timeletter.presentation.screen.TimeLetterScreen
import com.kuit.afternote.feature.timeletter.presentation.screen.TimeLetterWriterScreen

fun NavGraphBuilder.timeLetterNavGraph(
    navController: NavController,
    onNavItemSelected: (BottomNavItem) -> Unit = {}
) {
    composable<TimeLetterRoute.TimeLetterMainRoute> {
        TimeLetterScreen(
            onBackClick = { navController.popBackStack() },
            onNavItemSelected = onNavItemSelected
        )
    }

    composable<TimeLetterRoute.TimeLetterWriterRoute> {
        TimeLetterWriterScreen()
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
}

