package com.kuit.afternote.core.presentation.navigation.ui.navigator
import androidx.navigation.NavController
import com.kuit.afternote.feature.dailyrecord.presentation.navigiation.RecordRoute
import com.kuit.afternote.core.presentation.navigation.model.DailyRecordNavigator

class DailyRecordNavigatorImpl(
    private val navController: NavController,
) : DailyRecordNavigator {
    override fun gotoDailyRecord(route: RecordRoute) {
        navController.navigate(route)
    }
}
