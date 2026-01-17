package com.kuit.afternote.app.navigation.navigator

import androidx.navigation.NavController
import com.kuit.afternote.core.navigation.DailyRecordNavigator
import com.kuit.afternote.core.navigation.MainPageNavigator
import com.kuit.afternote.feature.dailyrecord.presentation.navigiation.RecordRoute
import com.kuit.afternote.feature.onboarding.presentation.navgraph.OnboardingRoute

class DailyRecordNavigatorImpl(
    private val navController: NavController
) : DailyRecordNavigator {
    override fun gotoDailyRecord(route: RecordRoute) {
        navController.navigate(route)
    }
}
