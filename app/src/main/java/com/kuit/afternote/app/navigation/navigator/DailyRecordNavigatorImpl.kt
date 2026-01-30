package com.kuit.afternote.app.navigation.navigator

import androidx.navigation.NavController
import com.kuit.afternote.core.navigation.DailyRecordNavigator
import com.kuit.afternote.feature.dailyrecord.presentation.navigiation.RecordRoute

class DailyRecordNavigatorImpl(
    private val navController: NavController
) : DailyRecordNavigator {
    override fun gotoDailyRecord(route: RecordRoute) {
        navController.navigate(route)
    }
}
