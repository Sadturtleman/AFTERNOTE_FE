package com.kuit.afternote.presentation.navigation.navigator

import androidx.navigation.NavController
import com.kuit.afternote.feature.dailyrecord.presentation.navigiation.RecordRoute
import com.kuit.afternote.presentation.navigation.DailyRecordNavigator

class DailyRecordNavigatorImpl(
    private val navController: NavController,
) : DailyRecordNavigator {
    override fun gotoDailyRecord(route: RecordRoute) {
        navController.navigate(route)
    }
}
